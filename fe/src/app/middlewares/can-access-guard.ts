import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthService } from '../services/auth.service';


@Injectable()
export class CanAccessGuard implements CanActivate {
    constructor(private auth: AuthService, private router: Router) {}

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean | UrlTree | Observable<boolean | UrlTree> | Promise<boolean | UrlTree> {
        const canActivate = this.auth.hasAccessTo(route.data['auth']);

        if (!canActivate) {
            this.router.navigate(['404']);
        }
        return canActivate;
    }

    // canActivate(route: Route): boolean | Observable<boolean> | Promise<boolean> {
    //     return this.auth.hasAccessTo(route.data["auth"]);
    // }

}
