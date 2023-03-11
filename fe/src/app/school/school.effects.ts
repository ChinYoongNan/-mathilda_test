import { Actions, Effect, ofType } from "@ngrx/effects";
import { catchError, map, mergeMap, take, debounce } from "rxjs/operators";
import { of, timer } from "rxjs";
import { Injectable } from "@angular/core";
import * as actions from "./school.actions";
import { State } from "../app.reducers";
import { Store } from "@ngrx/store";
import PageOptions from "../models/PageOptions";
import { SchoolService } from "../services/school.service";

@Injectable()
export class SchoolEffects {
  constructor(
    private ss: SchoolService,
    private actions$: Actions,
    private store: Store<State>
  ) {    
  }

  
  @Effect()
  getClassDetailData$ = this.actions$.pipe(
    ofType(actions.GET_CLASSES),
    debounce(() => timer(1000)),
    mergeMap(({ payload }: any): any => {
      console.log(payload);
      return this.ss.getClassesById(payload.id).pipe(
        map((res: any) => {
            return {
              type: actions.GET_DETAIL_SUCCESS,
              payload: {
                // method: actions.GET_CLASSES_LIST,
                // list: res.entity,
                list: res,
                // pageOptions: {
                //   ...payload.pageOptions,
                //   total: res.entity.total,
                // },
              },
            };
        }),
        catchError((error: any) => {
          return of({ type: actions.GET_DETAIL_FAILED, payload: error });
        })
      );
    })
  ); 

  @Effect()
  getClassesListData$ = this.actions$.pipe(
    ofType(actions.GET_CLASSES_LIST),
    debounce(() => timer(1000)),
    mergeMap(({ payload }: any): any => {
      return this.ss.getClassesList(payload.pageOptions).pipe(
        map((res: any) => {
            return {
              type: actions.GET_LIST_SUCCESS,
              payload: {
                method: actions.GET_CLASSES_LIST,
                // list: res.entity,
                list: res,
                // pageOptions: {
                //   ...payload.pageOptions,
                //   total: res.entity.total,
                // },
              },
            };
        }),
        catchError((error: any) => {
          return of({ type: actions.GET_LIST_FAILED, payload: error });
        })
      );
    })
  ); 
  
  @Effect()
  getSubjectTeacherListData$ = this.actions$.pipe(
    ofType(actions.GET_SUBJECT_TEACHER_LIST),
    debounce(() => timer(1000)),
    mergeMap(({ payload }: any): any => {
      return this.ss.getSubjectTeacherList(payload.pageOptions).pipe(
        map((res: any) => {
            return {
              type: actions.GET_LIST_SUCCESS,
              payload: {
                method: actions.GET_SUBJECT_TEACHER_LIST,
                // list: res.entity,
                list: res,
                // pageOptions: {
                //   ...payload.pageOptions,
                //   total: res.entity.total,
                // },
              },
            };
        }),
        catchError((error: any) => {
          return of({ type: actions.GET_LIST_FAILED, payload: error });
        })
      );
    })
  ); 

  @Effect()
  getSubjectListData$ = this.actions$.pipe(
    ofType(actions.GET_SUBJECT_LIST),
    debounce(() => timer(1000)),
    mergeMap(({ payload }: any): any => {
      return this.ss.getSubjectList(payload.pageOptions).pipe(
        map((res: any) => {
            return {
              type: actions.GET_LIST_SUCCESS,
              payload: {
                method: actions.GET_SUBJECT_LIST,
                // list: res.entity,
                list: res,
                // pageOptions: {
                //   ...payload.pageOptions,
                //   total: res.entity.total,
                // },
              },
            };
        }),
        catchError((error: any) => {
          return of({ type: actions.GET_LIST_FAILED, payload: error });
        })
      );
    })
  ); 

  
  @Effect()
  getStudentListData$ = this.actions$.pipe(
    ofType(actions.GET_STUDENT_LIST),
    debounce(() => timer(1000)),
    mergeMap(({ payload }: any): any => {
      return this.ss.getStudentList(payload.pageOptions).pipe(
        map((res: any) => {
            return {
              type: actions.GET_LIST_SUCCESS,
              payload: {
                method: actions.GET_STUDENT_LIST,
                // list: res.entity,
                list: res,
                // pageOptions: {
                //   ...payload.pageOptions,
                //   total: res.entity.total,
                // },
              },
            };
        }),
        catchError((error: any) => {
          return of({ type: actions.GET_LIST_FAILED, payload: error });
        })
      );
    })
  ); 
  
  
  @Effect()
  getTeacherListData$ = this.actions$.pipe(
    ofType(actions.GET_TEACHER_LIST),
    debounce(() => timer(1000)),
    mergeMap(({ payload }: any): any => {
      return this.ss.getTeacherList(payload.pageOptions).pipe(
        map((res: any) => {
            return {
              type: actions.GET_LIST_SUCCESS,
              payload: {
                method: actions.GET_TEACHER_LIST,
                // list: res.entity,
                list: res,
                // pageOptions: {
                //   ...payload.pageOptions,
                //   total: res.entity.total,
                // },
              },
            };
        }),
        catchError((error: any) => {
          return of({ type: actions.GET_LIST_FAILED, payload: error });
        })
      );
    })
  ); 
  @Effect()
  uploadSubjectData$ = this.actions$.pipe(
    ofType(actions.ADD_SUBJECT),
    mergeMap(({ payload }: any): any => {
      return this.ss.createSubject(payload).pipe(
        map((res: any) => {
          return { 
            type: actions.ADD_SUCCESS,
            payload: {
              list: res.entity,
            }
          };
        }),
        catchError((res: any) => {
          return of({
            type: actions.ADD_FAILED,
            payload: res.error,
          });
        })
      );
      
    })
  );
  @Effect()
  uploadStudentData$ = this.actions$.pipe(
    ofType(actions.ADD_STUDENT),
    mergeMap(({ payload }: any): any => {
      return this.ss.createStudent(payload).pipe(
        map((res: any) => {
          return { 
            type: actions.ADD_SUCCESS,
            payload: {
              list: res.entity,
            }
          };
        }),
        catchError((res: any) => {
          return of({
            type: actions.ADD_FAILED,
            payload: res.error,
          });
        })
      );
      
    })
  );

  @Effect()
  uploadTeacherData$ = this.actions$.pipe(
    ofType(actions.ADD_TEACHER),
    mergeMap(({ payload }: any): any => {
      return this.ss.createTeacher(payload).pipe(
        map((res: any) => {
          return { 
            type: actions.ADD_SUCCESS,
            payload: {
              list: res.entity,
            }
          };
        }),
        catchError((res: any) => {
          return of({
            type: actions.ADD_FAILED,
            payload: res.error,
          });
        })
      );
      
    })
  );

  
  @Effect()
  uploadClassesData$ = this.actions$.pipe(
    ofType(actions.ADD_CLASSES),
    mergeMap(({ payload }: any): any => {
      return this.ss.createClasses(payload).pipe(
        map((res: any) => {
          return { 
            type: actions.ADD_SUCCESS,
            payload: {
              list: res.entity,
            }
          };
        }),
        catchError((res: any) => {
          return of({
            type: actions.ADD_FAILED,
            payload: res.error,
          });
        })
      );
      
    })
  );


  @Effect()
  deleteSubjectData$ = this.actions$
    .pipe(
      ofType(actions.DELETE_SUBJECT),
      mergeMap(({ payload }: any): any => (this.ss.deleteSubjectById(payload)
        .pipe(
          map((res: any) => ({ type: actions.DELETE_SUCCESS })),
          catchError((resp: any) => of({ type: actions.DELETE_FAILED, payload: resp }))
        ))
      )
  );

  @Effect()
  deleteStudentData$ = this.actions$
    .pipe(
      ofType(actions.DELETE_STUDENT),
      mergeMap(({ payload }: any): any => (this.ss.deleteStudentById(payload)
        .pipe(
          map((res: any) => ({ type: actions.DELETE_SUCCESS })),
          catchError((resp: any) => of({ type: actions.DELETE_FAILED, payload: resp }))
        ))
      )
    );

  @Effect()
  deleteTeacherData$ = this.actions$
    .pipe(
      ofType(actions.DELETE_TEACHER),
      mergeMap(({ payload }: any): any => (this.ss.deleteTeacherById(payload)
        .pipe(
          map((res: any) => ({ type: actions.DELETE_SUCCESS })),
          catchError((resp: any) => of({ type: actions.DELETE_FAILED, payload: resp }))
        ))
      )
    );

  @Effect()
  deleteClassData$ = this.actions$
    .pipe(
      ofType(actions.DELETE_CLASSES),
      mergeMap(({ payload }: any): any => (this.ss.deleteClassesById(payload)
        .pipe(
          map((res: any) => ({ type: actions.DELETE_SUCCESS })),
          catchError((resp: any) => of({ type: actions.DELETE_FAILED, payload: resp }))
        ))
      )
    );


}
