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
import { MatTableDataSource } from '@angular/material/table';


@Component({
  selector: 'app-add-teacher',
  templateUrl: './add-teacher.component.html',
  styleUrls: ['./add-teacher.component.scss']
})
export class AddTeacherComponent implements OnInit {

  private subscription: Subscription = new Subscription();
  private updStatus$: Observable<any>;
  private responseBody$: Observable<any>;
  public form: FormGroup;
  public formSubject: FormGroup;
  
  public submitButtonOptions: MatProgressButtonOptions = new BarButtonOptions(
    this.dialogData.type
  );

  public columnID = [
    'id',
    'name',
    'actions'
  ];


  listData = [];
  dataSource = new MatTableDataSource<any>();

  columnList:any;
  constructor(
    private fb: FormBuilder,
    public dialogRef: MatDialogRef<AddTeacherComponent>,
    @Inject(MAT_DIALOG_DATA) public dialogData: any,
    public dialog: MatDialog,
    private store: Store<root.State>
  ) {
    this.updStatus$ = store.select("school", "updStatus");
    this.responseBody$ = store.select("school", "subjectList");
  }

  ngOnInit(): void {
    this.formSubject = this.fb.group({
      subject: [{}, null],
    });
    this.form = this.fb.group({
      name: ['', null],
      email: ['', null],
      // subjects: [[], null],
    });
    
    this.store.dispatch({
      type: actions.GET_SUBJECT_LIST,
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
      subjects: this.listData,
    };
    this.store.dispatch({
      type: actions.ADD_TEACHER,
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

  
  trackList(idx: number, item: any): any {
    return item.id;
  }

  assignSubject(){
    console.log("assignSubject");
    this.listData.push(this.formSubject.controls.subject.value);
    this.dataSource.data = this.listData;
  }

  delete(index){    
    // this.OrderItems.splice(index,1);
    // if(this.OrderItems.length==0){
    //   this.OrderItems=null;  
    // }    
    // this.order_items.order_item.splice(index,1);
  }

}

