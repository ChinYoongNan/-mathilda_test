import { Injectable } from '@angular/core';

const versionPersistKey = Symbol('Version');

@Injectable({
  providedIn: 'root'
})
export class VersionService {

  constructor() {}

  public load(): Promise<boolean> {
    return new Promise<boolean>((resolve) => {
      const currentVersion = localStorage.getItem(versionPersistKey.toString());
      resolve(true);
    });
  }
}
