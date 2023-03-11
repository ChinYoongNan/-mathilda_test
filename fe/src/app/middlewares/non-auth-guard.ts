import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable, of } from 'rxjs';
import { Store } from '@ngrx/store';
import { AuthenticateState, State } from '../app.reducers';
import { mergeMap } from 'rxjs/operators';
import { Injectable } from '@angular/core';

@Injectable()
export class NonAuthGuard implements CanActivate {

  constructor(
    private router: Router,
    private store: Store<State>,
  ) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean | UrlTree> {
    return this.store.select(x => x.rootAuthenticate)
      .pipe(
        mergeMap((auth: AuthenticateState): Observable<boolean | UrlTree> => {
          const now = (new Date()).getTime();
          if (auth.isAuthenticated && now < auth.tokenExpired) {
            return of(this.router.createUrlTree(['/']));
          }

          return of(true);
        })
      );
  }

}
