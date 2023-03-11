import { ChangeDetectorRef, Component, OnDestroy } from '@angular/core';
import { PerfectScrollbarConfigInterface } from 'ngx-perfect-scrollbar';
import { MediaMatcher } from '@angular/cdk/layout';
import { MenuItems } from '../../../shared/menu-items/menu-items';
import { select, Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { State } from '../../../app.reducers';
import { DomSanitizer } from '@angular/platform-browser';
@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: [],
})
export class AppSidebarComponent implements OnDestroy {
  public config: PerfectScrollbarConfigInterface = {};
  mobileQuery: MediaQueryList;

  private _mobileQueryListener: () => void;

  status = true;
  shortUsername = '';
  coverImage = '';
  background: any;

  itemSelect: number[] = [];
  public user$: Observable<any>;

  subclickEvent() {
    this.status = true;
  }
  constructor(
    changeDetectorRef: ChangeDetectorRef,
    media: MediaMatcher,
    public menuItems: MenuItems,
    private store: Store<State>,
    private sanitization: DomSanitizer
  ) {
    this.mobileQuery = media.matchMedia('(min-width: 768px)');
    this._mobileQueryListener = () => changeDetectorRef.detectChanges();
    this.mobileQuery.addListener(this._mobileQueryListener);
    this.user$ = store.pipe(select('rootAuthenticate', 'authUser'));
    this.user$.subscribe((authUser: any) => {
      if (!authUser) {
        return;
      }

      this.coverImage = '';
      if (authUser.cover_image !== undefined) {
          this.coverImage = authUser.cover_image;
          this.background = this.sanitization.bypassSecurityTrustStyle(`url(${this.coverImage})`);
      }

      if (
        authUser.logo &&
        authUser.logo !== 'https://image.flaticon.com/icons/svg/149/149452.svg'
      ) {
        return;
      }

      this.shortUsername = '';
      if (authUser.first_name) {
        this.shortUsername += authUser.first_name.charAt(0);
      }
      if (authUser.last_name) {
        this.shortUsername += authUser.last_name.charAt(0);
      }
    });
  }

  ngOnDestroy(): void {
    this.mobileQuery.removeListener(this._mobileQueryListener);
  }
}
