import { Injectable } from '@angular/core';
import { CanLoad, ActivatedRouteSnapshot, RouterStateSnapshot, Route, UrlSegment, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthService } from '../services/auth.service';


@Injectable()
export class CanLoadRouteGuard implements CanLoad {
    constructor(private auth: AuthService, private router: Router) {}

    canLoad(route: Route): boolean | Observable<boolean> | Promise<boolean> {
        const canActivate = this.auth.hasAccessTo(route.data['auth']);

        if (!canActivate) {
            this.router.navigate(['404']);
        }
        return canActivate;
    }

}
