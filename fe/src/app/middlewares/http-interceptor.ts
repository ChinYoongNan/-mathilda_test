import { Injectable } from "@angular/core";
import {
  HttpErrorResponse,
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
  HttpResponse,
} from "@angular/common/http";
import { Store } from "@ngrx/store";
import { State } from "../app.reducers";
import { first, flatMap, map, tap } from "rxjs/operators";
import { APP_CONFIG } from "../cores/config";
import { ROOT_USER_LOGOUT } from "../app.actions";
import { of } from "rxjs";

@Injectable()
export class TokenInterceptor implements HttpInterceptor {
  constructor(private store: Store<State>) {}

  intercept(req: HttpRequest<any>, next: HttpHandler) {
    const appId = "";
    req = req.clone({
      setHeaders: {
        "X-APP-ID": appId,
      },
    });

    if (
      !!APP_CONFIG &&
      !!APP_CONFIG.secrets.api &&
      (req.url.includes(APP_CONFIG.secrets.api.endpoint) ||
        req.url.includes(APP_CONFIG.secrets.api.ksEndpoint) ||
        // req.url.includes("localhost") ||
        req.url.includes(APP_CONFIG.secrets.api.kdEndpoint))
    ) {
      return this.store
        .select((state) => state.rootAuthenticate.token.access_token)
        .pipe(
          first(),
          flatMap((token) => {
            const authReq = !!token
              ? req.clone({
                  setHeaders: {
                    Authorization: "Bearer " + token,
                    "X-APP-ID": appId,
                  },
                })
              : req;
            return next.handle(authReq).pipe(
              tap(
                () => {},
                (error) => {
                  if (error instanceof HttpErrorResponse) {
                    const {
                      status,
                      error: { message },
                    } = error;
                    if (status == 401 && message === "Unauthorized") {
                      this.store.dispatch({
                        type: ROOT_USER_LOGOUT,
                      });
                      return;
                    }
                  }
                }
              )
            );
          })
        );
    }
    return next.handle(req);
  }
}
