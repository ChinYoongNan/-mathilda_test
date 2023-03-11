package com.caam.mrs.api.service;

public interface ISecurityUserService {
	String validatePasswordResetToken(String token);
}
