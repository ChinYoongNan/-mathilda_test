import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { EMPTY, Observable, of, throwError } from 'rxjs';
import { every, map, mergeMap } from 'rxjs/operators';
import { APP_CONFIG } from './config';

@Injectable({
  providedIn: 'root'
})
export class AppConfigService {

  constructor(private http: HttpClient) {}

  public load(): Promise<boolean> {
    return new Promise((resolve, reject) => {
        APP_CONFIG.loadFromPersists()
          .pipe(
            mergeMap(x => {
              if (x) {
                return of(true);
              }
              return this.fetchConfig();
            })
          )
        .subscribe(resolve.bind(this, true), reject);
    });
  }

  private fetchConfig(): Observable<any> {
    return this.http.get('/env')
      .pipe(
        mergeMap((res) => {
          APP_CONFIG.fill(res);
          return this.checkVaultConfig();
        }),
        mergeMap(() => this.getVaultToken())
      );
  }

  private checkVaultConfig(): Observable<any> {
    return of('vaultEndpoint', 'vaultRole', 'vaultSecret', 'vaultPath')
      .pipe(
        every(x => APP_CONFIG[x].length !== 0),
        map((x) => x ? of(true) : throwError(new Error('Vault config error')))
      );
  }

  private getVaultToken(): Observable<any> {
    return this.http
      .post(
        APP_CONFIG.vaultEndpoint + 'auth/approle/login',
        {
          'role_id': APP_CONFIG.vaultRole,
          'secret_id': APP_CONFIG.vaultSecret
        }
      )
      .pipe(
        mergeMap(({auth}: any) => {
          if (!auth.client_token) {
            return throwError(new Error('Can not get token from Vault'));
          }

          APP_CONFIG.fill({vault_token: auth.client_token}).persist();
          return this.getVaultSecrets();
        })
      );
  }

  private getVaultSecrets(): Observable<any> {
    return this.http
      .get(
        APP_CONFIG.vaultEndpoint + APP_CONFIG.vaultPath,
        {
          headers: {
            'X-Vault-Token': APP_CONFIG.vaultToken
          }
        }
      )
      .pipe(
        map(({data: secrets}: any) => {
          if (Object.keys(secrets).length === 0) {
            return EMPTY;
          }

          APP_CONFIG.fill({secrets}, false).persist();
          return EMPTY;
        })
      );
  }
}
