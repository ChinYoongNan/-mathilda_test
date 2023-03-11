export class ResetPasswordRequest {
  password: string;
  secretKey: string;

  constructor(password, secretKey) {
    this.password = password;
    this.secretKey = secretKey;
  }
}
