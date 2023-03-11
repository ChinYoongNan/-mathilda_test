package com.caam.mrs.api.security;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Suhaimi AK on 27/03/2019.
 */
@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
    
    private static final String AUTHORITIES_KEY = "authorities";
    private static final String USERNAME_KEY = "username";

    @Value("${com.caam.mrs.app.jwtSecret}")
    private String jwtSecret;

    @Value("${com.caam.mrs.app.jwtExpirationInMs}")
    private long jwtExpirationInMs;
    
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    public String generateToken(Authentication authentication) {

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);
        
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(authentication.getAuthorities()); 
        List<String> roles = new ArrayList<String>();
     	for (GrantedAuthority auth : authorities) { 
     		roles.add(auth.getAuthority()); 
     	}
        
        return Jwts.builder()
                .setSubject(Long.toString(userPrincipal.getId()))
                .claim("username", userPrincipal.getUsername())
                .claim("authorities", roles)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public Authentication getAuthentication(String token) {
	    UserDetails userDetails = customUserDetailsService.loadUserById(getUserIdFromJWT(token));
	    return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
    
    public Long getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        
        return Long.parseLong(claims.getSubject());
    }
    
    public String getUserNameFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        
        return claims.get(USERNAME_KEY, String.class);
    }
    
    @SuppressWarnings("unchecked")
	public List<String> getUserRolesFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
                
        return claims.get(AUTHORITIES_KEY, List.class);
    }

    public boolean validateToken(String authToken) {
    	try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
//            System.out.println("Id:" + ex.getClaims().getSubject());
            customUserDetailsService.setUserId(Long.parseLong(ex.getClaims().getSubject()));
            throw ex;
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return false;
    }
}
