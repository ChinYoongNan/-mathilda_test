package com.caam.mrs.api.payload;

import javax.validation.constraints.NotBlank;

/**
 * Created by Suhaimi AK on 06/06/2020.
 */
public class PasswordReset {
    @NotBlank
    private String newPassword;

    @NotBlank
    private String confirmPassword;
    
    private String token;

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

    
}
