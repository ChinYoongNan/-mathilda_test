import Classes from './Classess';
export default class Student {
  id: string;
  name: string;
  email: string;
  type: string;
  classes: Classes;
  constructor(
    id?: string,
    name?: string,
    email?: string,
    type?: string,
    classes?: Classes
  ) {
    this.id = id || '';
    this.name = name;
    this.email = email;
    this.type = type;
    this.classes = classes;
  }
}