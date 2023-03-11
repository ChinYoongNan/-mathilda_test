
export default class User {
  user_id!: string;
  first_name: string;
  last_name: string;
  phone_number: string;
  logo: string;
  role_ids: Array<string>;
  _status: string;
  email: string;
  password: string;
  cover_image: any;
  role_name: string;

  constructor(firstName = '', lastName = '', logo = '', roleIds = []) {
    this.first_name = firstName;
    this.last_name = lastName;
    this.logo = logo;
    this.role_ids = roleIds;
  }
}
