import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import swal from 'sweetalert2';
import { Router } from '@angular/router';
import { MatSlideToggleChange } from '@angular/material/slide-toggle'
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ADD_FORM, EDIT_FORM } from '../../app.constants';
import { Store } from "@ngrx/store";
import * as root from "../../app.reducers";
import * as actions from "../school.actions";
import PageOptions from "../../models/PageOptions";
import { Observable, Subscription } from "rxjs";
import { MatPaginator } from "@angular/material/paginator";
import { AddTeacherComponent } from "../addTeacher/add-teacher.component";

@Component({
  selector: 'app-teacher',
  templateUrl: './teacher.component.html',
  styleUrls: ['./teacher.component.scss']
})
export class TeacherComponent implements OnInit {
  @ViewChild('tableActionMenu') tableActionMenu;
  @ViewChild("paginator") paginator2: MatPaginator;
  public responseBody$: Observable<any>;
  public deleteStatus$: Observable<any>;
  private subscription: Subscription = new Subscription();

  public columnID = [
    'id',
    'name',
    'email',
    'subjects',
    'actions'
  ];

  public listData: Array<any>;
  public searchQuery = '';
  public pageOption: PageOptions;
  public pageOptions$: Observable<PageOptions>;
  public globalMessage: string = null;
  private webActivateStatus$: Observable<any>;
  private webInactivateStatus$: Observable<any>;

  private isChecked = true;
  private isUnchecked = false;
  constructor(private router: Router,
    public dialog: MatDialog,
    public dialogRef: MatDialogRef<TeacherComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private store: Store<root.State>) {
    this.responseBody$ = store.select("school", "teacherList");
    this.pageOptions$ = store.select("school", "pageOptions");
    this.deleteStatus$ = store.select("school", "deleteStatus");
  }

  public delete(id: string) {
    const deleteRequest = {
      id: id
    };

    this.store.dispatch({
      type: actions.DELETE_TEACHER,
      payload: deleteRequest
    });

  }

  ngOnInit(): void {
    const that = this;

    this.loadListing();
    this.subscription.add(
      this.responseBody$.subscribe((api: any) => {
        this.listData = api;
      })
    )    
    this.subscription.add(
      this.pageOptions$.subscribe((options: any) => {
        this.pageOption = options
  
      })
    )    

    this.subscription.add(
      this.deleteStatus$.subscribe((api: any) => {
        this.loadListing();
      })
    )
  }

  loadListing(searchKey:any = [],currentPage:any=1,limit:any=10) {
    this.store.dispatch({
      type: actions.GET_TEACHER_LIST,
      payload: {
        pageOptions: {
          currentPage: currentPage,
          limit: limit,
          keyword: searchKey,
        },
      },
    });
  }
  slideToggleChange(event: MatSlideToggleChange,tablename: string) {
   console.log(event.source.checked);
   if(event.source.checked){
    // inactive
   }else{
    //active
   }
  }  

  search(filterValue: string) {
    this.loadListing(filterValue);
  }
  changeListPaging(pageOption: any) {
    this.loadListing(this.searchQuery,pageOption.pageIndex + 1,pageOption.pageSize);
  }
  
  trackList(idx: number, item: any): any {
    return item.id;
  }

  
  addTeacherDialog(type: string = ADD_FORM, row: any = {}): void {
    const dialogRef = this.dialog.open(AddTeacherComponent, {
      width: "950px",
      height: '550px',
      data: { row },
    });
    dialogRef.afterClosed().subscribe(() => {  
      this.loadListing();
    });
  }


}
