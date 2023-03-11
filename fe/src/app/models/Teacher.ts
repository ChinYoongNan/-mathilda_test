import Subject from './Subject';
export default class Teacher {
  id: string;
  name: string;
  email: string;
  subjects: Subject[];
  constructor(
    id?: string,
    name?: string,
    email?: string,
    subjects?: Subject[]
  ) {
    this.id = id || '';
    this.name = name;
    this.email = email;
    this.subjects = subjects;
  }
}