import { Component, OnInit, OnDestroy } from "@angular/core";
import { PerfectScrollbarConfigInterface } from "ngx-perfect-scrollbar";
import { select, Store } from "@ngrx/store";
import { State } from "../../../app.reducers";
import { ROOT_REFRESH_TOKEN, ROOT_USER_LOGOUT } from "../../../app.actions";
import { Observable, Subscription } from "rxjs";
import swal from "sweetalert2";
import { MatDialog } from "@angular/material/dialog";
import { APP_CONFIG } from "../../../cores/config";
import Notification from "../../../models/Notification";

@Component({
  selector: "app-header",
  templateUrl: "./header.component.html",
  styleUrls: [],
})
export class AppHeaderComponent implements OnInit, OnDestroy {
  shortUsername = "";
  public config: PerfectScrollbarConfigInterface = {};
  public user$: Observable<any>;
  public tokenWillExpired$: Observable<any>;
  public refreshToken$: Observable<any>;
  private isAuth$: Observable<boolean>;
  private subscription: Subscription = new Subscription();
  private lastActionTime: number = new Date().getTime();
  private interval;
  private timeout = APP_CONFIG.secrets.auth.activeTimeout;
  private isActive = true;
  public receivedMessages: string[] = [];
  public unreadNotiCount = 0;
  private userId = "";

  public notifications: Notification[] = [];
  constructor(private store: Store<State>, private dialogRef: MatDialog) {
    this.user$ = store.pipe(select("rootAuthenticate", "authUser"));
    this.tokenWillExpired$ = store.pipe(
      select("rootAuthenticate", "tokenWillExpire")
    );
    this.refreshToken$ = store.pipe(
      select("rootAuthenticate", "token", "refresh_token")
    );
    this.isAuth$ = store.select("rootAuthenticate", "isAuthenticated");
  }

  ngOnInit(): void {
    this.subscription.add(
      this.isAuth$.subscribe((isAuth: boolean) => {
        if (!isAuth) {
          this.dialogRef.closeAll();
        }
      })
    );

    this.tokenWillExpired$.subscribe((tokenWillExpire: any) => {
      if (tokenWillExpire && this.isActive) {
        this.store.dispatch({ type: ROOT_REFRESH_TOKEN });
      }
    });

    this.user$.subscribe((authUser: any) => {
      if (
        !authUser ||
        (authUser.logo &&
          authUser.logo !==
            "https://image.flaticon.com/icons/svg/149/149452.svg")
      ) {
        return;
      }

      this.shortUsername = "";
      if (authUser.first_name) {
        this.shortUsername += authUser.first_name.charAt(0);
      }
      if (authUser.last_name) {
        this.shortUsername += authUser.last_name.charAt(0);
      }

      if (this.userId) {
        return;
      }

      this.userId = authUser.user_id;
    });

    window.addEventListener("touchstart", () => {
      this.resetTimer();
    });
    window.addEventListener("click", () => {
      this.resetTimer();
    });
    this.interval = setInterval(() => {
      this.handleActiveTimeoutHandler();
    }, APP_CONFIG.secrets.auth.activeTimeout);
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
    // clearInterval(this.interval);
    // window.removeEventListener('touchstart', () => {
    //   this.resetTimer();
    // });
    // window.removeEventListener('click', () => {
    //   this.resetTimer();
    // });
  }

  private resetTimer() {
    this.isActive = true;
    this.lastActionTime = new Date().getTime();
    clearInterval(this.interval);
    this.interval = setInterval(() => {
      this.handleActiveTimeoutHandler();
    }, this.timeout);
  }

  private handleActiveTimeoutHandler() {
    const now = new Date().getTime();
    const activeTime = now - this.lastActionTime;
    if (activeTime >= this.timeout) {
      this.isActive = false;
      clearInterval(this.interval);
      swal
        .fire({
          icon: "info",
          title: "Oops!",
          text: "You're idling. Continue ?",
          showCancelButton: true,
          reverseButtons: true,
          confirmButtonText: "Continue",
          cancelButtonText: "Dismiss",
          cancelButtonColor: "#fc4b6c",
          allowOutsideClick: false,
        })
        .then((res) => {
          if (res.dismiss) {
            this.store.dispatch({ type: ROOT_USER_LOGOUT });
          }
          if (res.value) {
            this.store.dispatch({ type: ROOT_REFRESH_TOKEN });
            this.resetTimer();
          }
        });
    }
  }
}
