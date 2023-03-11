import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { ListComponent } from "./list/list.component";
import { StudentComponent } from "./student/student.component";
import { SubjectComponent } from "./subject/subject.component";
import { TeacherComponent } from "./teacher/teacher.component";
import { ClassesComponent } from "./classes/classes.component";
import { AddSubjectComponent } from "./addSubject/add-subject.component";
import { AddStudentComponent } from "./addStudent/add-student.component";
import { AddTeacherComponent } from "./addTeacher/add-teacher.component";
import { AddClassComponent } from "./addClass/add-class.component";
import { RouterModule } from "@angular/router";
import { SchoolRoutes } from "./school.routing";
import { MatCardModule } from "@angular/material/card";
import { MatInputModule } from "@angular/material/input";
import { NgxDatatableModule } from "@swimlane/ngx-datatable";
import { CommonMaterialModule } from "../common-material.module";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { FlexLayoutModule } from "@angular/flex-layout";
import { MatIconModule } from "@angular/material/icon";
import { MatProgressButtonsModule } from "mat-progress-buttons";
import { StoreModule } from '@ngrx/store';
import { ClassDetailComponent } from './classesDetail/classes-detail.component';
import { SchoolReducers } from './school.reducers';
import { EffectsModule } from '@ngrx/effects';
import { SchoolEffects } from './school.effects';
import { SchoolService } from '../services/school.service';
import { VerticalTabComponent } from './vertical_tabs/vertical_tabs.component';
import { MatStepperModule } from '@angular/material/stepper';
import { MatDialogModule, MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";

@NgModule({
  declarations: [
    TeacherComponent,
    StudentComponent,
    SubjectComponent,
    ClassesComponent,
    VerticalTabComponent,
    AddSubjectComponent,
    AddStudentComponent,
    AddTeacherComponent,
    AddClassComponent,
    ClassDetailComponent,
    ListComponent,
  ],
  entryComponents: [
    AddSubjectComponent,
    AddStudentComponent,
    AddTeacherComponent,
    AddClassComponent,
  ],
  imports: [
    CommonModule,
    MatStepperModule,
    MatCardModule,
    MatInputModule,
    MatDialogModule,
    NgxDatatableModule,
    CommonMaterialModule,
    FormsModule,
    ReactiveFormsModule,
    FlexLayoutModule,
    MatIconModule,
    MatProgressButtonsModule,
    RouterModule.forChild(SchoolRoutes),
    StoreModule.forFeature('school', SchoolReducers),
    EffectsModule.forFeature([SchoolEffects]),
  ],
  providers: [ SchoolService,
    { 
      provide: MatDialogRef,
      useValue: []
       }, 
      { 
      provide: MAT_DIALOG_DATA, 
      useValue: [] 
      }
  ],
})
export class SchoolModule {}
