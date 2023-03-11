import { NgModule } from "@angular/core";
import { MenuItems } from "./menu-items/menu-items";
import {
  AccordionAnchorDirective,
  AccordionLinkDirective,
  AccordionDirective,
} from "./accordion";
import { MatIconModule } from "@angular/material/icon";
import { BrowserModule } from "@angular/platform-browser";
import { DialogConfirmComponent } from "./dialog-confirm/dialog-confirm.component";
import { CommonMaterialModule } from "../common-material.module";

@NgModule({
  imports: [BrowserModule, MatIconModule, CommonMaterialModule],
  declarations: [
    AccordionAnchorDirective,
    AccordionLinkDirective,
    AccordionDirective,
    DialogConfirmComponent,
  ],
  exports: [
    AccordionAnchorDirective,
    AccordionLinkDirective,
    AccordionDirective,
  ],
  providers: [MenuItems],
  entryComponents: [DialogConfirmComponent],
})
export class SharedModule {}
