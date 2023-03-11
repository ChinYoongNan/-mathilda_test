import { Routes } from '@angular/router';
import { ClassDetailComponent } from './classesDetail/classes-detail.component';
import { ListComponent } from "./list/list.component";

export const SchoolRoutes: Routes = [
  {
    path: '',
    component: ListComponent,
  },
  {
    path: 'class/:id',
    component: ClassDetailComponent,
  },
];
