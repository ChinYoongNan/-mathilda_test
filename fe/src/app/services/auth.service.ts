import { Injectable } from "@angular/core";
import { APP_CONFIG } from "../cores/config";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { LoginRequest } from "../models/LoginRequest";
import { RegisterRequest } from "../models/RegisterRequest";
import { ResetPasswordRequest } from "../models/ResetPasswordRequest";
import { hasAccess } from "../helpers/index";
@Injectable()
export class AuthService {
  private readonly clientId: string;
  private readonly clientSecret: string;
  private readonly ssoEndpoint: string;
  private readonly kaiEndpoint: string;
  private httpOptions: any;

  constructor(private httpService: HttpClient) {
    const {
      endpoint: apiEndpoint,
      prefix: { user: userPrefix },
    } = APP_CONFIG.secrets.api;
    const {
      endpoint: ssoEndpoint,
      clientId,
      clientSecret,
    } = APP_CONFIG.secrets.sso;

    this.clientId = clientId;
    this.clientSecret = clientSecret;
    this.ssoEndpoint = ssoEndpoint;
    this.kaiEndpoint = `${apiEndpoint}${userPrefix}`;
    this.httpOptions = {
      headers: new HttpHeaders({
        "Content-Type": "application/json",
      }),
    };
  }

  login(loginInfo: LoginRequest) {
    const formData = new FormData();
    formData.set("grant_type", "password");
    formData.set("client_id", this.clientId);
    formData.set("client_secret", this.clientSecret);
    formData.set("username", loginInfo.email);
    formData.set("password", loginInfo.password);
    return this.httpService.post(`${this.ssoEndpoint}accessToken`, formData);
  }

  refreshToken(refreshToken: string) {
    const params = new URLSearchParams({
      grant_type: "refresh_token",
      client_id: this.clientId,
      client_secret: this.clientSecret,
      refresh_token: refreshToken,
    });
    return this.httpService.post(
      `${this.ssoEndpoint}accessToken?${params.toString()}`,
      {},
      this.httpOptions
    );
  }

  register(registerInfo: RegisterRequest) {
    return this.httpService.post(
      `${this.kaiEndpoint}user/registration`,
      registerInfo,
      this.httpOptions
    );
  }

  getUserInfo(token: string) {
    return this.httpService.get(
      `${this.ssoEndpoint}profile?access_token=${token}`
    );
  }

  forgotPassword(email: string) {
    return this.httpService.post(
      `${this.kaiEndpoint}user/password/recovery?email=${email}`,
      {},
      {
        headers: new HttpHeaders({
          "X-APP": "Kew Score",
          "X-APP-DOMAIN": window.location.origin,
        }),
      }
    );
  }

  resetPassword(payload: ResetPasswordRequest) {
    return this.httpService.post(
      `${this.kaiEndpoint}user/password/reset`,
      payload
    );
  }

  validateSecret(reset_password_code: string) {
    return this.httpService.post(
      `${this.kaiEndpoint}user/password/token/validate`,
      { reset_password_code }
    );
  }

  hasAccessTo(access: string) {
    return hasAccess(access);
  }
}
