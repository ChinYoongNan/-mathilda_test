import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatProgressButtonOptions } from 'mat-progress-buttons';
import { BarButtonOptions } from '../../models/BarButtonOptions';
import ConfigurationMenu from '../../models/ConfigurationMenu';
import swal from 'sweetalert2';
import { Store } from "@ngrx/store";
import * as root from "../../app.reducers";
import * as actions from "../school.actions";
import { Observable, Subscription } from "rxjs";

@Component({
  selector: 'app-add-subject',
  templateUrl: './add-subject.component.html',
  styleUrls: ['./add-subject.component.scss']
})
export class AddSubjectComponent implements OnInit {

  private subscription: Subscription = new Subscription();
  private updStatus$: Observable<any>;
  public form: FormGroup;
  public submitButtonOptions: MatProgressButtonOptions = new BarButtonOptions(
    this.dialogData.type
  );
  public data: ConfigurationMenu;
  public validTypes: Map<string, string> = new Map<string, string>([
    ['atm', 'ATM'],
    ['debit', 'Debit Card'],
    ['credit', 'Credit Card'],
  ]);

  updStatus
  constructor(
    private fb: FormBuilder,
    public dialogRef: MatDialogRef<AddSubjectComponent>,
    @Inject(MAT_DIALOG_DATA) public dialogData: any,
    public dialog: MatDialog,
    private store: Store<root.State>
  ) {
    this.data = dialogData.data;
    this.updStatus$ = store.select("school", "updStatus");
  }

  ngOnInit(): void {
    
    this.form = this.fb.group({
      name: ['', null],
    });

    this.form.statusChanges.subscribe((x) => {
      this.submitButtonOptions.disabled = x !== 'VALID';
    });
  }

  public submit() {
    const executeRequest = {
      name: this.form.controls.name.value,
    };
    this.store.dispatch({
      type: actions.ADD_SUBJECT,
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
