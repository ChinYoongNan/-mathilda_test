import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router'
import { Observable, of } from 'rxjs'
import { Store } from '@ngrx/store'
import { AuthenticateState, State } from '../app.reducers'
import { mergeMap } from 'rxjs/operators'
import { Injectable } from '@angular/core'
import { getCookieByName } from '../helpers'
import { ROOT_USER_LOGOUT } from '../app.actions'

export const TOKEN_COOKIE_NAME = Symbol('KAI-Token').toString()

@Injectable()
export class AuthGuard implements CanActivate {

  constructor(
    private router: Router,
    private store: Store<State>,
  ) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean | UrlTree> {
    return this.store.select(x => x.rootAuthenticate)
      .pipe(
        mergeMap((auth: AuthenticateState): Observable<boolean | UrlTree> => {
          const now = (new Date()).getTime()
          const tokenFromCookie = getCookieByName(TOKEN_COOKIE_NAME)
          if (!auth.isAuthenticated) {
            window.location.href = '/authentication/login'
            return of(false)
          }

          if (!tokenFromCookie || auth.token.access_token !== tokenFromCookie || now >= auth.tokenExpired) {
            this.store.dispatch({ type: ROOT_USER_LOGOUT })
            return of(false)
          }

          return of(true)
        })
      )
  }
}
