import * as $ from "jquery";
import { MediaMatcher } from "@angular/cdk/layout";
import { Router } from "@angular/router";
import {
  ChangeDetectorRef,
  Component,
  NgZone,
  OnDestroy,
  ViewChild,
  HostListener,
  Directive,
  AfterViewInit,
} from "@angular/core";
import { MenuItems } from "../../shared/menu-items/menu-items";
import { AppHeaderComponent } from "./header/header.component";
import { AppSidebarComponent } from "./sidebar/sidebar.component";

import { PerfectScrollbarConfigInterface } from "ngx-perfect-scrollbar";
/** @title Responsive sidenav */
@Component({
  selector: "app-full-layout",
  templateUrl: "full.component.html",
  styleUrls: ["full.component.scss"],
})
export class FullComponent implements OnDestroy, AfterViewInit {
  mobileQuery: MediaQueryList;
  dir = "ltr";
  green: boolean;
  blue: boolean;
  dark: boolean;
  minisidebar: boolean = true;
  boxed: boolean;
  danger: boolean;
  showHide: boolean;
  sidebarOpened;
  contentMargin = 240;

  public config: PerfectScrollbarConfigInterface = {};
  private _mobileQueryListener: () => void;

  constructor(
    changeDetectorRef: ChangeDetectorRef,
    media: MediaMatcher,
    public menuItems: MenuItems
  ) {
    this.mobileQuery = media.matchMedia("(min-width: 768px)");
    this._mobileQueryListener = () => changeDetectorRef.detectChanges();
    this.mobileQuery.addListener(this._mobileQueryListener);
  }

  ngOnDestroy(): void {
    this.mobileQuery.removeListener(this._mobileQueryListener);
  }
  ngAfterViewInit() {}

  // Mini sidebar
  toggleMini() {
    this.minisidebar = !this.minisidebar;
    if (!this.minisidebar) {
      this.contentMargin = 240;
    } else {
      this.contentMargin = 80;
    }
  }
}
