import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatProgressButtonOptions } from 'mat-progress-buttons';
import { BarButtonOptions } from '../../models/BarButtonOptions';
import swal from 'sweetalert2';
import { Store } from "@ngrx/store";
import * as root from "../../app.reducers";
import * as actions from "../school.actions";
import { Observable, Subscription } from "rxjs";

@Component({
  selector: 'app-add-student',
  templateUrl: './add-student.component.html',
  styleUrls: ['./add-student.component.scss']
})
export class AddStudentComponent implements OnInit {

  private subscription: Subscription = new Subscription();
  private updStatus$: Observable<any>;
  private responseBody$: Observable<any>;
  public form: FormGroup;
  public submitButtonOptions: MatProgressButtonOptions = new BarButtonOptions(
    this.dialogData.type
  );

  columnList:any;
  constructor(
    private fb: FormBuilder,
    public dialogRef: MatDialogRef<AddStudentComponent>,
    @Inject(MAT_DIALOG_DATA) public dialogData: any,
    public dialog: MatDialog,
    private store: Store<root.State>
  ) {
    this.updStatus$ = store.select("school", "updStatus");
    this.responseBody$ = store.select("school", "classesList");
  }

  ngOnInit(): void {
    
    this.form = this.fb.group({
      name: ['', null],
      email: ['', null],
      classes: [{}, null],
    });
    
    this.store.dispatch({
      type: actions.GET_CLASSES_LIST,
      payload: {
        pageOptions: {
          currentPage: 1,
          limit: 100,
          keyword: [],
        },
      },
    });
    this.subscription.add(
      this.responseBody$.subscribe((res: any) => {
        console.log(res);
        this.columnList = res;
      })
    )
    
    this.form.statusChanges.subscribe((x) => {
      this.submitButtonOptions.disabled = x !== 'VALID';
    });
  }

  public submit() {
    const executeRequest = {
      name: this.form.controls.name.value,
      email: this.form.controls.email.value,
      classes: this.form.controls.classes.value,
    };
    this.store.dispatch({
      type: actions.ADD_STUDENT,
      payload: executeRequest
    });


    this.subscription.add(
      this.updStatus$.subscribe((res: any) => {
        if (res.success === undefined) {
          return;
        }
        if (res.success) {
          swal.fire('Success!', 'Subject have been added', 'success').then(res => {
          });
        } else {
          swal.fire('Oops!', res.errMessage ? res.errMessage : 'Something went wrong. Please try again', 'error');
        }
      })
    )
    this.dialogRef.close();
  }

  // submit() {
  //   this.submitButtonOptions.active = true;

  //   swal.fire(
  //     'Success!',
  //     `${this.dialogData.type} user successfully`,
  //     'success'
  //   );

  //   this.submitButtonOptions.active = false;
  // }

}
