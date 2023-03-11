package com.caam.mrs.api.payload;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * Created by Suhaimi AK on 06/06/2020.
 */
public class ForgotRequest {
	@NotBlank
    private String email;
	
	@NotBlank
    private String username;
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
