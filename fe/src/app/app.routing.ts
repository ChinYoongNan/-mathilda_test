import { Routes } from '@angular/router'
import { FullComponent } from './layouts/full/full.component'
import { AuthGuard } from './middlewares/auth-guard'
import { ErrorComponent } from './static-pages/error/error.component'
import { ResetPasswordExpiredComponent } from './static-pages/reset-password-expired/reset-password-expired.component'

export const AppRoutes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    // redirectTo: 'dashboard',
    redirectTo : 'school',
  },
  {
    path: '',
    component: FullComponent,
    // canActivate: [AuthGuard],
    children: [
      {
        path: 'school',
        loadChildren: () =>
          import('./school/school.module').then(
            (m) => m.SchoolModule
          ),
        data: { breadcrumb: 'School' },
      }
    ],
  },
  {
    path: 'reset-password-expired',
    component: ResetPasswordExpiredComponent,
  },
  {
    path: '404',
    component: ErrorComponent,
  },
  {
    path: '**',
    redirectTo: '404',
  },
]
