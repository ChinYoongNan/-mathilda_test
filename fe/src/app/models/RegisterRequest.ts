export class RegisterRequest {
  email: string;
  first_name: string;
  last_name: string;
  password: string;
  logo: string;
  role_ids: string[];

  constructor(
    {
      email: email,
      first_name: first_name,
      last_name: last_name,
      password: password,
      logo: logo,
      role_ids: role_ids}
    ) {
    this.email = email || '';
    this.first_name = first_name || '';
    this.last_name = last_name || '';
    this.password = password || '';
    this.logo = logo || '';
    this.role_ids = role_ids || [];
  }
}
