package com.caam.mrs.api.service;

import java.util.Calendar;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.caam.mrs.api.model.PasswordResetToken;
import com.caam.mrs.api.repository.PasswordResetTokenRepo;

@Service
@Transactional
public class UserSecurityService implements ISecurityUserService{
	@Autowired
    private PasswordResetTokenRepo passwordTokenRepo;
	
	@Override
	public String validatePasswordResetToken(String token) {
		final PasswordResetToken passToken = passwordTokenRepo.findByToken(token);

        return !isTokenFound(passToken) ? "invalidToken"
                : isTokenExpired(passToken) ? "expired"
                : null;
	}
	
	private boolean isTokenFound(PasswordResetToken passToken) {
        return passToken != null;
    }

    private boolean isTokenExpired(PasswordResetToken passToken) {
        final Calendar cal = Calendar.getInstance();
        return passToken.getExpiryDate().before(cal.getTime());
    }

}
