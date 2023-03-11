import { createAction } from '@ngrx/store';

export const ROOT_GET_PROFILE = '[Root] Get profile';
export const ROOT_GET_PROFILE_SUCCESS = '[Root] Get profile success';
export const ROOT_GET_PROFILE_FAILED = '[Root] Get profile failed';
export const ROOT_TOKEN_WILL_EXPIRE = '[Root] Token will expire';
export const ROOT_REFRESH_TOKEN = '[Root] Refresh token';
export const ROOT_REFRESH_TOKEN_SUCCESS = '[Root] Refresh token success';
export const ROOT_REFRESH_TOKEN_FAIL = '[Root] Refresh token failed';
export const ROOT_USER_LOGOUT = '[Root] User logout';
export const ROOT_GET_USER_SUCCESS = '[Root] Get user success';
export const ROOT_GET_USER_FAILED = '[Root] Get user failed';
export const EMPTY_ACTION = '[Root] Empty';
export const ROOT_USER_TO_USER_PROFILE = '[Root] User to User Profile';
export const ROOT_USER_CHANGE_COVER = '[Root] Change user cover image';
export const ROOT_USER_UPD_PROFILE = '[Root] Update User Profile';
export const ROOT_GET_USER_ACCESS = '[Root] Get User Access';
export const ROOT_GET_USER_ACCESS_SUCCESS = '[Root] Get User Access Success';
export const ROOT_GET_USER_ACCESS_FAILED = '[Root] Get User Access Failed';

export const nope = createAction(EMPTY_ACTION);

export const userLogout = createAction(ROOT_USER_LOGOUT);
