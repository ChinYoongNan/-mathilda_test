import { ActionReducerMap } from '@ngrx/store'
import { routerReducer, RouterReducerState } from '@ngrx/router-store'
import {
  ROOT_GET_PROFILE_FAILED,
  ROOT_GET_PROFILE_SUCCESS,
  ROOT_GET_USER_SUCCESS,
  ROOT_REFRESH_TOKEN_SUCCESS,
  ROOT_TOKEN_WILL_EXPIRE,
  ROOT_USER_LOGOUT,
  ROOT_USER_CHANGE_COVER,
  ROOT_GET_USER_ACCESS_SUCCESS,
  ROOT_USER_UPD_PROFILE,
} from './app.actions'
import { TOKEN_COOKIE_NAME } from './middlewares/auth-guard'
import User from './models/User'

import { State as JobState } from './school/school.reducers'
import { State as SchoolState } from './school/school.reducers'

export interface State {
  rootAuthenticate: AuthenticateState
  router: RouterReducerState
  job?: JobState
  school?: SchoolState
}

export interface AuthenticateState {
  isAuthenticated: boolean
  token: any
  tokenExpired: number
  tokenWillExpire: boolean
  profile: any
  authRemember: boolean
  authUser: User
  access: any
}

export const initState: AuthenticateState = {
  isAuthenticated: false,
  token: {},
  tokenExpired: 0,
  tokenWillExpire: false,
  profile: {},
  authRemember: false,
  authUser: undefined,
  access: [],
}

const setTokenWithCookie = (remember: boolean, token: string): void => {
  let cookieExpire: any = '0' // Default as session cookie
  if (remember) {
    const expire = new Date()
    expire.setMonth(expire.getMonth() + 1)
    cookieExpire = expire
  }
  document.cookie = `${TOKEN_COOKIE_NAME}=${token};expires=${cookieExpire};path=/`
}

export const AppRootReducer = (
  state: AuthenticateState = initState,
  { type, payload }: any
): AuthenticateState => {
  switch (type) {
    case ROOT_USER_CHANGE_COVER:
      const authUser = { ...state.authUser }
      authUser.cover_image = payload
      return {
        ...state,
        authUser,
      }
    // case AUTH_GET_TOKEN_FAIL:
    //   return {
    //     ...state,
    //     token: {},
    //     isAuthenticated: false,
    //     tokenWillExpire: false,
    //   }
    // case AUTH_GET_TOKEN_SUCCESS: {
    //   const token = payload.token
    //   const tokenExpired = new Date().getTime() + token.expires_in * 1000
    //   return {
    //     ...state,
    //     token,
    //     tokenExpired,
    //     tokenWillExpire: false,
    //     authRemember: payload.remember,
    //   }
    // }
    case ROOT_GET_PROFILE_SUCCESS: {
      setTokenWithCookie(payload.remember, state.token.access_token)
      return {
        ...state,
        profile: payload.profile,
        authUser: payload.profile,
      }
    }
    case ROOT_GET_USER_ACCESS_SUCCESS: {
      return {
        ...state,
        access: payload.access.map((a) => ({
          method_name:
            a.method_name && a.method_name.length > 0 ? a.method_name[0] : null,
          rule_id: a.rule_id,
          name: a.name,
          rule_name: a.rule_name,
        })),
        isAuthenticated: true,
      }
    }
    case ROOT_GET_PROFILE_FAILED:
      return {
        ...initState,
      }
    case ROOT_REFRESH_TOKEN_SUCCESS: {
      const tokenExpired = new Date().getTime() + payload.expires_in * 1000
      setTokenWithCookie(state.authRemember, payload.access_token)
      return {
        ...state,
        token: {
          ...state.token,
          access_token: payload.access_token,
        },
        tokenExpired,
        tokenWillExpire: false,
      }
    }
    case ROOT_TOKEN_WILL_EXPIRE:
      return {
        ...state,
        tokenWillExpire: true,
      }
    case ROOT_USER_LOGOUT:
      return {
        ...initState,
      }
    case ROOT_USER_UPD_PROFILE:
      return {
        ...state,
        profile: {
          ...state.profile,
          first_name: payload.first_name,
          last_name: payload.last_name,
          phone_number: payload.phone_number,
        },
        authUser: {
          ...state.authUser,
          first_name: payload.first_name,
          last_name: payload.last_name,
          phone_number: payload.phone_number,
        },
      }
    default:
      return state
  }
}

export const reducers: ActionReducerMap<State> = {
  rootAuthenticate: AppRootReducer,
  router: routerReducer
}
