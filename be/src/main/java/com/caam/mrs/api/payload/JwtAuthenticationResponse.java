package com.caam.mrs.api.payload;

import java.util.List;

/**
 * Created by Suhaimi AK on 27/03/2019.
 */
public class JwtAuthenticationResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    //private List<String> accessRoles;

    //public JwtAuthenticationResponse(String accessToken, List<String> roles) {
    public JwtAuthenticationResponse(String accessToken) {
        this.accessToken = accessToken;
        //this.accessRoles = roles;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

	/*
	 * public List<String> getAccessRoles() { return accessRoles; }
	 * 
	 * public void setAccessRoles(List<String> accessRoles) { this.accessRoles =
	 * accessRoles; }
	 */
    
}