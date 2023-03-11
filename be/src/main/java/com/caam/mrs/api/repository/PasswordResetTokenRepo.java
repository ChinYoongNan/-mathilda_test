package com.caam.mrs.api.repository;

import java.util.Date;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.caam.mrs.api.model.PasswordResetToken;
import com.caam.mrs.api.model.SecUser;

public interface PasswordResetTokenRepo extends JpaRepository<PasswordResetToken, Long> {
	PasswordResetToken findByToken(String token);

    PasswordResetToken findByUser(SecUser user);

    Stream<PasswordResetToken> findAllByExpiryDateLessThan(Date now);

    void deleteByExpiryDateLessThan(Date now);

    @Modifying
    @Query("delete from PasswordResetToken t where t.expiryDate <= ?1")
    void deleteAllExpiredSince(Date now);
}
