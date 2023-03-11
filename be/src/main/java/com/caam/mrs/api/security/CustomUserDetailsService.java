package com.caam.mrs.api.security;

import com.caam.mrs.api.exception.ResourceNotFoundException;
import com.caam.mrs.api.model.SecUser;
import com.caam.mrs.api.repository.SecUserRepo;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Suhaimi AK on 27/03/2019.
 */

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    SecUserRepo userRepository;
    
    Long userId = null;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String usernameOrEmail)
            throws UsernameNotFoundException {
        // Let people login with either username or email
        SecUser user = userRepository.findEnableByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username or email : " + usernameOrEmail)
        );

        return UserPrincipal.create(user);
    }

    @Transactional
    public UserDetails loadUserById(Long id) {
        SecUser user = userRepository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("User", "id", id)
        );

        return UserPrincipal.create(user);
    }

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	@SuppressWarnings("unchecked")
	public boolean hasRole(String role) {
	  Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>)
	  SecurityContextHolder.getContext().getAuthentication().getAuthorities();
	  boolean hasRole = false;
	  for (GrantedAuthority authority : authorities) {
	     hasRole = authority.getAuthority().equals(role);
	     if (hasRole) {
		  break;
	     }
	  }
	  return hasRole;
	}  
}