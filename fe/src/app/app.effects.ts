import { Injectable } from "@angular/core";
import { Actions, Effect, ofType } from "@ngrx/effects";
import { catchError, first, map, mergeMap } from "rxjs/operators";
import { from, interval, Observable, of } from "rxjs";
import {
  nope,
  ROOT_GET_PROFILE_FAILED,
  ROOT_GET_PROFILE_SUCCESS,
  ROOT_GET_USER_ACCESS_FAILED,
  ROOT_GET_USER_ACCESS_SUCCESS,
  ROOT_REFRESH_TOKEN,
  ROOT_REFRESH_TOKEN_FAIL,
  ROOT_REFRESH_TOKEN_SUCCESS,
  ROOT_TOKEN_WILL_EXPIRE,
  ROOT_USER_LOGOUT,
  ROOT_USER_TO_USER_PROFILE,
} from "./app.actions";
import { AuthService } from "./services/auth.service";
import { Store } from "@ngrx/store";
import { AuthenticateState, State } from "./app.reducers";
import { APP_CONFIG } from "./cores/config";
import { Router } from "@angular/router";
import swal from "sweetalert2";
// import {
//   NOTIFICATION_GET_LAST_10,
//   NOTIFICATION_GET_LAST_10_FAILED,
//   NOTIFICATION_GET_LAST_10_SUCCESS,
// } from "./notifications/notification.actions";

@Injectable()
export class AppEffects {
  constructor(
    private actions$: Actions,
    private authService: AuthService,
    private store: Store<State>,
    private router: Router
  ) {}

  

  @Effect()
  heartbeat$ = interval(APP_CONFIG.secrets.auth.heartbeat).pipe(
    mergeMap(() =>
      this.store
        .select((x) => x.rootAuthenticate)
        .pipe(
          map((res: AuthenticateState) => {
            if (res.isAuthenticated) {
              const now = new Date().getTime();
              const tokenThreshold =
                res.tokenExpired - APP_CONFIG.secrets.auth.threshold;
              if (now >= tokenThreshold && !res.tokenWillExpire) {
                return { type: ROOT_TOKEN_WILL_EXPIRE };
              }
              if (now >= res.tokenExpired && !res.authRemember) {
                return { type: ROOT_USER_LOGOUT };
              }
            }
            return nope();
          })
        )
    )
  );

  @Effect()
  refreshToken$ = this.actions$.pipe(
    ofType(ROOT_REFRESH_TOKEN),
    mergeMap(() =>
      this.store
        .select((x) => x.rootAuthenticate)
        .pipe(
          first(),
          mergeMap((auth: AuthenticateState) =>
            this.authService.refreshToken(auth.token.refresh_token).pipe(
              map((tokenInfo: any) => ({
                type: ROOT_REFRESH_TOKEN_SUCCESS,
                payload: tokenInfo,
              })),
              catchError((err) =>
                of({ type: ROOT_USER_LOGOUT, payload: err.message })
              )
            )
          )
        )
    )
  );

  @Effect({ dispatch: false })
  userLogout$ = this.actions$.pipe(
    ofType(ROOT_USER_LOGOUT),
    mergeMap(() => {
      swal.close();
      window.location.href = "/authentication/login";
      return of(1);
    })
  );

  @Effect({ dispatch: false })
  navigateToUserProfile$ = this.actions$.pipe(
    ofType(ROOT_USER_TO_USER_PROFILE),
    mergeMap(() => {
      return from(this.router.navigate(["user-management/user-profile"]));
    })
  );


}
