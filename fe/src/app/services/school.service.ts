import { Injectable } from "@angular/core";
import { APP_CONFIG } from "../cores/config";
import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import PageOptions from "../models/PageOptions";
import Classess from "../models/Classess";

@Injectable()
export class SchoolService {
  private readonly ksEndpoint: string;
  private readonly kaiEndpoint: string;
  private readonly data: string;

  constructor(private httpService: HttpClient) {
    const {
      kdEndpoint: ksApiEndpoint,
      prefix: { authorization: authorizationPrefix,
        ingestion: ingestionPrefix },
    } = APP_CONFIG.secrets.api;
    this.ksEndpoint = `http://localhost:8082/api/`;
  }

  getClassesList(pageOptions: PageOptions) {
    return this.httpService.get<Classess>(
        `${this.ksEndpoint}classes/list?page=${pageOptions.currentPage}&size=${pageOptions.limit}&keyword=${pageOptions.keyword}`
    );
  }
  
  getClassesById(id: string) {
    return this.httpService.get<PageOptions>(
        `${this.ksEndpoint}classes/${id}`
    );
  }
  
  deleteClassesById(payload) {
    return this.httpService.delete(
      `${this.ksEndpoint}classes/delete/${payload.id}`
    );
  }

  createClasses(payload) {    
    return this.httpService.post(
      `${this.ksEndpoint}classes/new`,
      payload)
  }    

  getSubjectTeacherList(pageOptions: PageOptions) {
    return this.httpService.get<Classess>(
        `${this.ksEndpoint}classes/subject/list?page=${pageOptions.currentPage}&size=${pageOptions.limit}&keyword=${pageOptions.keyword}`
    );
  }

  getSubjectList(pageOptions: PageOptions) {
    return this.httpService.get<Classess>(
        `${this.ksEndpoint}subject/list?page=${pageOptions.currentPage}&size=${pageOptions.limit}&keyword=${pageOptions.keyword}`
    );
  }

  deleteSubjectById(payload) {
    return this.httpService.delete(
      `${this.ksEndpoint}subject/delete/${payload.id}`
    );
  }

  createSubject(payload) {
    return this.httpService.post(
      `${this.ksEndpoint}subject/new`,
      payload)
  }    

  getStudentList(pageOptions: PageOptions) {
    return this.httpService.get<Classess>(
        `${this.ksEndpoint}onboarding/student/list?page=${pageOptions.currentPage}&size=${pageOptions.limit}&keyword=${pageOptions.keyword}`
    );
  }

  deleteStudentById(payload) {
    return this.httpService.delete(
      `${this.ksEndpoint}onboarding/student/delete/${payload.id}`
    );
  }

  createStudent(payload) {    
    return this.httpService.post(
      `${this.ksEndpoint}onboarding/student/new`,
      payload)
  }    

  getTeacherList(pageOptions: PageOptions) {
    return this.httpService.get<Classess>(
        `${this.ksEndpoint}onboarding/teacher/list?page=${pageOptions.currentPage}&size=${pageOptions.limit}&keyword=${pageOptions.keyword}`
    );
  }

  deleteTeacherById(payload) {
    return this.httpService.delete(
      `${this.ksEndpoint}onboarding/teacher/delete/${payload.id}`
    );
  }

  createTeacher(payload) {
    return this.httpService.post(
      `${this.ksEndpoint}onboarding/teacher/new`,
      payload)
  }

  assignTeachertoSubject(payload) {
    const formData = new FormData();
    formData.append("payload", JSON.stringify(payload))
    return this.httpService.post(
      `${this.ksEndpoint}onboarding/teachersubject/${payload.tableName}`,
    formData)
  }
  
}
