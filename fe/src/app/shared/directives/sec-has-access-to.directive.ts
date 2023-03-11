import { Directive, Input, TemplateRef, ViewContainerRef } from '@angular/core';
import { AuthService } from '../../services/auth.service';

@Directive({
  selector: '[appSecHasAccessTo]',
})
export class SecHasAccessToDirective {
  constructor(
    private templateRef: TemplateRef<any>,
    private viewContainer: ViewContainerRef,
    private authService: AuthService
  ) {}

  @Input()
  set appSecHasAccessTo(access: string) {
    const allowed: boolean = this.authService.hasAccessTo(access);

    if (access == '' || allowed) {
      this.viewContainer.createEmbeddedView(this.templateRef);
    } else {
      this.viewContainer.clear();
    }
  }
}
