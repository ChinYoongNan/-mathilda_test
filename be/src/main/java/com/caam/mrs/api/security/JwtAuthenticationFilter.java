package com.caam.mrs.api.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Suhaimi AK on 27/03/2019.
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String excludeFilter="/refreshtoken";
            String path = ((HttpServletRequest) request).getServletPath();
            
        	String jwt = TokenAuthenticationHelper.getJwtFromRequest(request);
            if (!path.contains(excludeFilter) && StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                Long userId = tokenProvider.getUserIdFromJWT(jwt);

                SecurityContextHolder.getContext().setAuthentication(authenticate(userId, request));
            } 
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException |
                SignatureException | IllegalArgumentException e) {
        	SecurityContextHolder.clearContext();
        	logger.error("Could not set user authentication in security context", e);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expired");
        }
        filterChain.doFilter(request, response);
    }

	private Authentication authenticate (Long userId, HttpServletRequest request) {
		UserDetails userDetails = customUserDetailsService.loadUserById(userId);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        
        return authentication;
	}
    
    /*
	 * private String getJwtFromRequest(HttpServletRequest request) { String
	 * bearerToken = request.getHeader("Authorization"); if
	 * (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
	 * return bearerToken.substring(7, bearerToken.length()); } return null; }
	 */
    
}
