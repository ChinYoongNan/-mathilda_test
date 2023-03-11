import { Observable, of } from 'rxjs'

const persistStorageKey = Symbol('Config')

export class AppConfig {
  readonly vaultEndpoint: string = ''
  readonly vaultRole: string = ''
  readonly vaultSecret: string = ''
  readonly vaultPath: string = ''
  readonly vaultToken: string = ''

  readonly secrets: any = {
    auth: {
      heartbeat: 300000, // 5 mins
      threshold: 360000, // 6 mins
      activeTimeout: 120000, // 2 mins
    },
    api: {
      endpoint: '',
      ksEndpoint: '',
      socketEndpoint: '',
    },
    sso: {
      clientId: '',
      clientSecret: '',
      endpoint: '',
    },
    dashboardUrl: '',
  }

  /**
   * Fill configuration items from raw data into object
   *
   * @param data
   * @param isTransform
   */
  public fill(data: any, isTransform: boolean = true): AppConfig {
    for (const key in data) {
      if (!data.hasOwnProperty(key)) {
        continue
      }

      if (isTransform) {
        const propKey = key.toLocaleLowerCase()
          .replace(/[-_]+/g, ' ')
          .replace(/[^\w\s]/g, '')
          .replace(/ (.)/g, x => x.toUpperCase())
          .replace(/ /g, '')
        if (propKey in this) {
          // @ts-ignore
          this[propKey] = data[key]
        }
      } else {
        if (key in this) {
          // @ts-ignore
          this[key] = data[key]
        }
      }
    }
    return this
  }

  /**
   * Persist config into local storage
   */
  public persist(): AppConfig {
    const persistData: any = {}
    // eslint-disable-next-line guard-for-in
    for (const key in this) {
      if (!this.hasOwnProperty(key)) {
        break
      }

      persistData[key] = this[key]
    }

    localStorage.setItem(persistStorageKey.toString(), JSON.stringify(persistData))
    return this
  }

  /**
   * Load configurations from localstorage
   *
   * @return Observable
   */
  public loadFromPersists(): Observable<boolean> {
    const persistData = localStorage.getItem(persistStorageKey.toString())
    if (!persistData) {
      return of(false)
    }
    
    this.fill(JSON.parse(persistData), false)
    return of(true)
  }

}

export const APP_CONFIG: AppConfig = new AppConfig()
