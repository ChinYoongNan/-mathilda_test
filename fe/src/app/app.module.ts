import { BrowserModule } from '@angular/platform-browser'
import { APP_INITIALIZER, NgModule } from '@angular/core'
import { RouterModule } from '@angular/router'
import { FormsModule } from '@angular/forms'
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http'
import { AppRoutes } from './app.routing'
import { AppComponent } from './app.component'

import { FlexLayoutModule } from '@angular/flex-layout'
import { FullComponent } from './layouts/full/full.component'
import { AppBlankComponent } from './layouts/blank/blank.component'
import { AppHeaderComponent } from './layouts/full/header/header.component'
import { AppSidebarComponent } from './layouts/full/sidebar/sidebar.component'
import { BrowserAnimationsModule } from '@angular/platform-browser/animations'
import { CommonMaterialModule } from './common-material.module'

import {
  PERFECT_SCROLLBAR_CONFIG,
  PerfectScrollbarConfigInterface,
  PerfectScrollbarModule,
} from 'ngx-perfect-scrollbar'

import { SharedModule } from './shared/shared.module'
import { SpinnerComponent } from './shared/spinner.component'
import { AppConfigService } from './cores/config.service'

import { StoreDevtoolsModule } from '@ngrx/store-devtools'
import { environment } from '../environments/environment'
import { ActionReducer, INIT, MetaReducer, StoreModule } from '@ngrx/store'
import { EffectsModule } from '@ngrx/effects'
import { localStorageSync } from 'ngrx-store-localstorage'
import { AppEffects } from './app.effects'
import { reducers } from './app.reducers'
import { RouterState, StoreRouterConnectingModule } from '@ngrx/router-store'
import { AuthService } from './services/auth.service'
import {
  CustomRouterStateSerializer,
  provideBootstrapEffects,
} from './app.utils'
import { AuthGuard } from './middlewares/auth-guard'
import { VersionService } from './cores/version.service'
import { NonAuthGuard } from './middlewares/non-auth-guard'
import { ErrorComponent } from './static-pages/error/error.component'
import { ProfileMenuComponent } from './shared/profile-menu/profile-menu.component'
import { TokenInterceptor } from './middlewares/http-interceptor'
import { NgxWidgetGridModule } from 'ngx-widget-grid'
import { ResetPasswordExpiredComponent } from './static-pages/reset-password-expired/reset-password-expired.component'
import { BreadcrumbModule } from 'xng-breadcrumb'
import { SecurityModule } from './shared/security.module'
import { ToastrModule } from 'ngx-toastr'
import { ROOT_USER_LOGOUT } from './app.actions'
import { CanLoadRouteGuard } from './middlewares/can-load-guard'
import { CanAccessGuard } from './middlewares/can-access-guard'
import { NgxDatatableModule } from '@swimlane/ngx-datatable'
import { NgxEchartsModule } from 'ngx-echarts'
import { Ng2SearchPipeModule } from 'ng2-search-filter'

const DEFAULT_PERFECT_SCROLLBAR_CONFIG: PerfectScrollbarConfigInterface = {
  suppressScrollX: true,
  wheelSpeed: 2,
  wheelPropagation: true,
}

export const localStorageSyncReducer = (
  reducer: ActionReducer<any>
): ActionReducer<any> => localStorageSync({
  keys: ['rootAuthenticate', 'evidence'],
  rehydrate: true,
})(reducer)

export const logout = (reducer: ActionReducer<any>): ActionReducer<any> => (state, action) => {
  if (action != null && action.type === ROOT_USER_LOGOUT) {
    return reducer(undefined, { type: INIT })
  }
  return reducer(state, action)
}

const metaReducers: Array<MetaReducer<any, any>> = [
  localStorageSyncReducer,
  logout,
]

// @ts-ignore
@NgModule({
  declarations: [
    AppComponent,
    FullComponent,
    AppHeaderComponent,
    SpinnerComponent,
    AppBlankComponent,
    AppSidebarComponent,
    ErrorComponent,
    ProfileMenuComponent,
    ResetPasswordExpiredComponent,
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    CommonMaterialModule,
    FormsModule,
    FlexLayoutModule,
    HttpClientModule,
    PerfectScrollbarModule,
    NgxDatatableModule,
    SharedModule,
    RouterModule.forRoot(AppRoutes, { relativeLinkResolution: 'legacy' }),
    StoreModule.forRoot(reducers, { metaReducers }),
    EffectsModule.forRoot([]),
    StoreDevtoolsModule.instrument({
      maxAge: 25,
      logOnly: environment.production,
    }),
    StoreRouterConnectingModule.forRoot({
      routerState: RouterState.Minimal,
      serializer: CustomRouterStateSerializer,
    }),
    NgxWidgetGridModule,
    BreadcrumbModule,
    SecurityModule,
    ToastrModule.forRoot({
      progressBar: true,
      timeOut: 5000,
      maxOpened: 10,
      positionClass: 'toast-bottom-left',
    }),
    NgxEchartsModule.forRoot({
      echarts: () => import('echarts'),
    }),
    Ng2SearchPipeModule
  ],
  providers: [
    AppConfigService,
    VersionService,
    {
      provide: APP_INITIALIZER,
      useFactory: (
        configSvr: AppConfigService,
        versionSvr: VersionService
      ): () => void => () => versionSvr.load().then(() => configSvr.load()),
      deps: [AppConfigService, VersionService],
      multi: true,
    },
    provideBootstrapEffects([AppEffects]),
    {
      provide: PERFECT_SCROLLBAR_CONFIG,
      useValue: DEFAULT_PERFECT_SCROLLBAR_CONFIG,
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true,
    },
    AuthService,
    AuthGuard,
    NonAuthGuard,
    CanLoadRouteGuard,
    CanAccessGuard,
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
