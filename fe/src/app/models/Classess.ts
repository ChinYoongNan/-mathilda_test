import Subject from './Subject';
import Teacher from './Teacher';
export default class Classes {
  id: string;
  name: string;
  subjects: Subject[];
  teachers: Teacher[];
  constructor(
    id?: string,
    name?: string,
    subjects?: Subject[],
    teachers?: Teacher[]
  ) {
    this.id = id || '';
    this.name = name;
    this.subjects = subjects;
    this.teachers = teachers;
  }
}