import { APP_BOOTSTRAP_LISTENER, Inject, InjectionToken, Type } from '@angular/core';
import { Data, Params, RouterStateSnapshot } from '@angular/router';
import { EffectSources } from '@ngrx/effects';
import { RouterStateSerializer } from '@ngrx/router-store';

export const BOOTSTRAP_EFFECTS  = new InjectionToken('Bootstrap Effects');

export function bootstrapEffects(effects: Type<any>[], sources: EffectSources) {
  return () => {
    effects.forEach(effect => sources.addEffects(effect));
  };
}

export function createInstances(...instances: any[]) {
  return instances;
}

export function provideBootstrapEffects(effects: Type<any>[]) {
  return [
    effects,
    { provide: BOOTSTRAP_EFFECTS, deps: effects, useFactory: createInstances },
    {
      provide: APP_BOOTSTRAP_LISTENER,
      multi: true,
      useFactory: bootstrapEffects,
      deps: [[new Inject(BOOTSTRAP_EFFECTS)], EffectSources]
    }
  ];
}

/**
 * Create custom router state serializer to fix issue about browser hangout after navigate
 */
export interface CustomRouterState {
  url: string;
  params: Params;
  queryParams: Params;
  data: Data;
}

export class CustomRouterStateSerializer implements RouterStateSerializer<CustomRouterState> {
  serialize(routerState: RouterStateSnapshot): CustomRouterState {
    let route = routerState.root;

    while (route.firstChild) {
      route = route.firstChild;
    }

    return {
      url: routerState.url || '/',
      params: route.params,
      queryParams: routerState.root.queryParams,
      data: route.data
    };
  }
}
