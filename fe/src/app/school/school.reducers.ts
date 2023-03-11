import * as actions from "./school.actions";
import { ACTIONS_SUBJECT_PROVIDERS } from "@ngrx/store/src/actions_subject";

export interface State {
  subjectList: any[];
  classesList: any[];
  studentList: any[];
  teacherList: any[];
  responseBody: any;
  status: {
    loadSuccess: any;
    errMessage: string;
  };
  deleteStatus: {
    success: any;
    errMessage: string;
  };
  updStatus: {
    success: any;
    errMessage: string;
  };
  pageOptions: {
    currentPage: number;
    limit: number;
    total: number;
    keyword: string;
  };
}

export const initState: State = {
  subjectList: [],
  classesList: [],
  studentList: [],
  teacherList: [],
  responseBody: undefined,
  status: {
    loadSuccess: undefined,
    errMessage: "",
  },
  deleteStatus: {
    success: undefined,
    errMessage: "",
  },
  updStatus: {
    success: undefined,
    errMessage: "",
  },
  pageOptions: {
    currentPage: 0,
    limit: 5,
    total: null,
    keyword: "",
  }
};

export function SchoolReducers(state = initState, action: any): State {
  switch (action.type) {
    case actions.GET_CLASSES: {
      return {
        ...state,
        status: {
          loadSuccess: undefined,
          errMessage: '',
        },
      };
    }
    case actions.GET_DETAIL_SUCCESS: {
      return {
        ...state,
        responseBody: action.payload.list,
        status: {
          loadSuccess: true,
          errMessage: '',
        },
      };
    }
    case actions.GET_DETAIL_FAILED:{
      return {
        ...state,
        status: {
          loadSuccess: false,
          errMessage: action.payload.message,
        },
      };
    }  
    case actions.GET_SUBJECT_TEACHER_LIST:
    case actions.GET_TEACHER_LIST: 
    case actions.GET_STUDENT_LIST: 
    case actions.GET_SUBJECT_LIST: 
    case actions.GET_CLASSES_LIST: {
      return {
        ...state,
        responseBody: undefined,
        pageOptions: {
          currentPage: action.payload.pageOptions.currentPage,
          limit: action.payload.pageOptions.limit,
          total: action.payload.pageOptions.total,
          keyword: action.payload.pageOptions.keyword,
        },
        status: {
          loadSuccess: undefined,
          errMessage: "",
        },
      };
    }     
    case actions.GET_LIST_SUCCESS: {
      switch (action.payload.method){
        case actions.GET_TEACHER_LIST: {
          return {
            ...state,
            teacherList: action.payload.list,
            // pageOptions: {
            //   currentPage: action.payload.pageOptions.currentPage,
            //   limit: action.payload.pageOptions.limit,
            //   total: action.payload.pageOptions.total,
            //   keyword: action.payload.pageOptions.keyword,
            // },
            status: {
              loadSuccess: true,
              errMessage: "",
            },
          };
        }
        case actions.GET_STUDENT_LIST:{
          return {
            ...state,
            studentList: action.payload.list,
            // pageOptions: {
            //   currentPage: action.payload.pageOptions.currentPage,
            //   limit: action.payload.pageOptions.limit,
            //   total: action.payload.pageOptions.total,
            //   keyword: action.payload.pageOptions.keyword,
            // },
            status: {
              loadSuccess: true,
              errMessage: "",
            },
          };
        } 
        case actions.GET_SUBJECT_TEACHER_LIST:
        case actions.GET_SUBJECT_LIST: {
          return {
            ...state,
            subjectList: action.payload.list,
            // pageOptions: {
            //   currentPage: action.payload.pageOptions.currentPage,
            //   limit: action.payload.pageOptions.limit,
            //   total: action.payload.pageOptions.total,
            //   keyword: action.payload.pageOptions.keyword,
            // },
            status: {
              loadSuccess: true,
              errMessage: "",
            },
          };
        }
        case actions.GET_CLASSES_LIST: {
          return {
            ...state,
            classesList: action.payload.list,
            // pageOptions: {
            //   currentPage: action.payload.pageOptions.currentPage,
            //   limit: action.payload.pageOptions.limit,
            //   total: action.payload.pageOptions.total,
            //   keyword: action.payload.pageOptions.keyword,
            // },
            status: {
              loadSuccess: true,
              errMessage: "",
            },
          };
        }
      }
      break;
    }
    case actions.GET_LIST_FAILED: {
      return {
        ...state,
        responseBody: undefined,
        subjectList: [],
        classesList: [],
        studentList: [],
        teacherList: [],
        pageOptions: {
          currentPage: action.payload.pageOptions.currentPage,
          limit: action.payload.pageOptions.limit,
          total: action.payload.pageOptions.total,
          keyword: action.payload.pageOptions.keyword,
        },
        status: {
          loadSuccess: false,
          errMessage: action.payload,
        },
      };
    }
    case actions.ADD_SUBJECT:
    case actions.ADD_TEACHER:
    case actions.ADD_STUDENT:
    case actions.ADD_CLASSES: {
      return {
        ...state,
        updStatus: {
          success: undefined,
          errMessage: '',
        },
      };
    }
    case actions.ADD_SUCCESS: {
      return {
        ...state,
        responseBody: action.payload.list,
        updStatus: {
          success: true,
          errMessage: '',
        },
      };
    }
    case actions.ADD_FAILED:{
      return {
        ...state,
        updStatus: {
          success: false,
          errMessage: action.payload.message,
        },
      };
    }  

    
    case actions.DELETE_SUBJECT: {
      return {
        ...state,
        deleteStatus: {
          success: state.deleteStatus.success,
          errMessage: "",
        },
      };
    }
    case actions.DELETE_SUCCESS: {
      return {
        ...state,
        deleteStatus: {
          success: true,
          errMessage: "",
        },
      };
    }
    case actions.DELETE_FAILED: {
      return {
        ...state,        
        deleteStatus: {
          success: false,
          errMessage: "",
        },
      };
    }  
    default: {
      return state;
    }
  }
}
