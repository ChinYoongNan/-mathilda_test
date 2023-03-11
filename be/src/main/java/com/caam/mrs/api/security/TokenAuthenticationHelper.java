package com.caam.mrs.api.security;

import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class TokenAuthenticationHelper {
    private static final String COOKIE_BEARER = "COOKIE-BEARER";

    private TokenAuthenticationHelper() {
        throw new IllegalStateException("Utility class");
    }

    public static String getJwtFromRequest(HttpServletRequest request) {

        Cookie cookie = WebUtils.getCookie(request, COOKIE_BEARER);
        String token = cookie != null ? cookie.getValue() : null;

        if (token != null) 
        	return token;
        else
        	return null;
    }
}