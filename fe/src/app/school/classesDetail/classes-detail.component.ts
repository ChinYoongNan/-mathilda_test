import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import swal from 'sweetalert2';
import { MatSlideToggleChange } from '@angular/material/slide-toggle'
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ADD_FORM, EDIT_FORM } from '../../app.constants';
import { ActivatedRoute, Router } from '@angular/router';
import { Store } from "@ngrx/store";
import * as root from "../../app.reducers";
import * as actions from "../school.actions";
import PageOptions from "../../models/PageOptions";
import { Observable, Subscription } from "rxjs";
import { MatPaginator } from "@angular/material/paginator";
import { AddClassComponent } from "../addClass/add-class.component";

@Component({
  selector: 'app-classes-detail',
  templateUrl: './classes-detail.component.html',
  styleUrls: ['./classes-detail.component.scss']
})
export class ClassDetailComponent implements OnInit {
  @ViewChild('tableActionMenu') tableActionMenu;
  @ViewChild("paginator") paginator2: MatPaginator;
  public responseBody$: Observable<any>;
  public deleteStatus$: Observable<any>;
  private subscription: Subscription = new Subscription();

  public columnID = [
    'id',
    'name',
  ];

  
  public subjectcolumnID = [
    'id',
    'subjectname',
    'teachername',
  ];

  public classDetail: any;
  public listData: Array<any>;
  public subjectListData: Array<any>;
  public searchQuery = '';
  public pageOption: PageOptions;
  public pageOptions$: Observable<PageOptions>;
  public globalMessage: string = null;
  id:any;
  constructor(private router: Router,
    public dialog: MatDialog,
    public dialogRef: MatDialogRef<ClassDetailComponent>,
    private route: ActivatedRoute,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private store: Store<root.State>) {
      this.responseBody$ = store.select("school", "responseBody");
      this.pageOptions$ = store.select("school", "pageOptions");
      this.deleteStatus$ = store.select("school", "deleteStatus");
    }
  
    public delete(id: string) {
      const deleteRequest = {
        id: id
      };
  
      this.store.dispatch({
        type: actions.DELETE_CLASSES,
        payload: deleteRequest
      });
  
    }
  
    ngOnInit(): void {
      const that = this;
      
      this.id = this.route.snapshot.paramMap.get('id');
      this.loadDetail(this.id);
      this.subscription.add(
        this.responseBody$.subscribe((api: any) => {
          console.log(api);
          if(api){

            this.classDetail = api;
            try{
              this.listData = this.classDetail.students;
              this.subjectListData = this.classDetail.subjects;

            }catch(e){

            }
          }
        })
      )    
    }
  
    loadDetail(searchKey:any = [],currentPage:any=1,limit:any=10) {
      this.store.dispatch({
        type: actions.GET_CLASSES,
        payload: {
          id: this.id,
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
      // this.loadListing(filterValue);
    }
    changeListPaging(pageOption: any) {
      // this.loadListing(this.searchQuery,pageOption.pageIndex + 1,pageOption.pageSize);
    }
    
    trackList(idx: number, item: any): any {
      return item.id;
    }
  
    addClassDialog(){

    }
      
  
  
  }
