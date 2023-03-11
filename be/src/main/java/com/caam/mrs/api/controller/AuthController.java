package com.caam.mrs.api.controller;

import com.caam.mrs.api.model.SecUser;
import com.caam.mrs.api.payload.ApiResponse;
import com.caam.mrs.api.payload.ForgotRequest;
import com.caam.mrs.api.payload.LoginRequest;
import com.caam.mrs.api.payload.PasswordReset;
import com.caam.mrs.api.payload.RequestUser;
import com.caam.mrs.api.repository.SecUserRepo;
import com.caam.mrs.api.security.CustomUserDetailsService;
import com.caam.mrs.api.security.JwtTokenProvider;
import com.caam.mrs.api.security.TokenAuthenticationHelper;
import com.caam.mrs.api.service.UserSecurityService;
import com.caam.mrs.api.service.UserService;
import com.caam.mrs.api.validator.EmailValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.AbstractMap;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * Created by Suhaimi AK on 27/03/2019.
 */
@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {
	private static final String COOKIE_BEARER = "COOKIE-BEARER";
	
	@Value("${com.caam.mrs.email.checkduplicate}")
	Boolean emailCheck;
	
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SecUserRepo userRepository;

    @Autowired
    private JwtTokenProvider tokenProvider;
    
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    
    @Autowired
	private UserService userService;
    
    @Autowired
	private UserSecurityService securityService;
    
    private EmailValidator emailValidator;
    
//    @Value(("${com.caam.mrs.domain}"))
//    String domainName;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest,
    		HttpServletResponse response) {
    	Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        
		String jwt = tokenProvider.generateToken(authentication);
//		System.out.println("token:" + jwt);
        Cookie cookie = new Cookie(COOKIE_BEARER, jwt);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
//        cookie.setDomain(domainName);
        response.addCookie(cookie);
        
        Long userId = tokenProvider.getUserIdFromJWT(jwt);
        Optional<SecUser> secUserOpt = userRepository.findById(userId);
        if (secUserOpt.isPresent()) {
        	SecUser secUser = secUserOpt.get();
        	secUser.setLastLogin(new Timestamp((new Date()).getTime()));
        	
        	userRepository.save(secUser);
        }
        
        return ResponseEntity.ok("Login success");
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping("/resetpwd")
    public ResponseEntity<?> resetPwdRequest(@Valid @RequestBody ForgotRequest forgotReq) {
    	emailValidator = new EmailValidator();
        if(!emailValidator.validate(forgotReq.getEmail())) {
            return new ResponseEntity(new ApiResponse(false, "Email is not valid!"),
                    HttpStatus.BAD_REQUEST);
        }
        
    	try {
//			Optional<SecUser> secUserOpt = userService.findByEmail(forgotReq.getEmail());
			Optional<SecUser> secUserOpt = userService.findUsernameAndEmail(forgotReq.getUsername(), forgotReq.getEmail());
			if (secUserOpt.isPresent()) {
				String token = UUID.randomUUID().toString();
			    userService.createPasswordResetTokenForUser(secUserOpt.get(), token);
			    
			    return new ResponseEntity(new ApiResponse(true, "Password reset request success. Email has been sent!"),
		                HttpStatus.OK);
		    } else {
		    	return new ResponseEntity(new ApiResponse(false, "User not found!"),
	                    HttpStatus.BAD_REQUEST);
//		    	return new ResponseEntity(new ApiResponse(true, "Password reset request success. Email has been sent!"),
//		    			HttpStatus.OK);
		    }
		    			
		} catch (MailException | InterruptedException e) {
			e.printStackTrace();
			return new ResponseEntity(new ApiResponse(false, "Could not reset password!"),
                    HttpStatus.BAD_REQUEST);
		}
		
    }
    
    @GetMapping("/validateresettoken/{token}")
    public AbstractMap.SimpleEntry<String, Boolean> validateResetPwdToken(@PathVariable("token") String token) {
    	String result = securityService.validatePasswordResetToken(token);
    	return new AbstractMap.SimpleEntry<String, Boolean>("valid", result != null ? false : true);
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping("/changeresetpwd")
    public ResponseEntity<?> resetPwd(@Valid @RequestBody PasswordReset passwordReset) {
		try {
			if(!passwordReset.getNewPassword().equals(passwordReset.getConfirmPassword())) {
				return new ResponseEntity(new ApiResponse(false, "New Password and Confirm Password does not match!"),
	                    HttpStatus.BAD_REQUEST);				
			}
			
			String result = securityService.validatePasswordResetToken(passwordReset.getToken());
			if(result != null) {
				return new ResponseEntity(new ApiResponse(false, "User not found!"),
	                    HttpStatus.BAD_REQUEST);				
			}
			
			Optional<SecUser> user = userService.getUserByPasswordResetToken(passwordReset.getToken());
			
			if(user.isPresent()) {
				userService.saveResetPwd(user.get(), passwordReset.getNewPassword()); 
				return new ResponseEntity(new ApiResponse(true, "Password has been reset!"),
		                HttpStatus.OK);
			} else {
				return new ResponseEntity(new ApiResponse(false, "Invalid user token!"),
	                    HttpStatus.BAD_REQUEST);	
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(new ApiResponse(false, "Could not reset password!"),
                    HttpStatus.BAD_REQUEST);
		}
    }
    
    @GetMapping("/validate")
    public AbstractMap.SimpleEntry<String, String> currentAuthentication(HttpServletRequest request) {
    	String token = TokenAuthenticationHelper.getJwtFromRequest(request);
    	
    	return new AbstractMap.SimpleEntry<String, String>("username", tokenProvider.getUserNameFromJWT(token));	
    		
    }
    
    @GetMapping("/token")
    public AbstractMap.SimpleEntry<String, String> currentAuthenticationToken(HttpServletRequest request) {
    	String token = TokenAuthenticationHelper.getJwtFromRequest(request);
    	
    	return new AbstractMap.SimpleEntry<String, String>("token", token);	
    		
    }
    
    @GetMapping("/refreshtoken")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
    	Long userId = customUserDetailsService.getUserId();
    	
    	if (userId != null) {
    		UserDetails userDetails = customUserDetailsService.loadUserById(userId);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    		
    		SecurityContextHolder.getContext().setAuthentication(authentication);
    		
    		String jwt = tokenProvider.generateToken(authentication);
    		Cookie cookie = new Cookie(COOKIE_BEARER, jwt);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
//            cookie.setDomain(domainName);
            response.addCookie(cookie);
    		
    	} 
    }
    
    @RequestMapping(value = "/roles", method = RequestMethod.GET)
	@ResponseBody
	public AbstractMap.SimpleEntry<String, ?> getUserRoles(HttpServletRequest request) {
    	String token = TokenAuthenticationHelper.getJwtFromRequest(request);
    	
    	System.out.println("token:" + token);
    	if (token !=null)
	    	return new AbstractMap.SimpleEntry<String, List<String>>("roles", tokenProvider.getUserRolesFromJWT(token));
    	else
    		return new AbstractMap.SimpleEntry<String, List<String>>("roles", null);
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RequestUser requestUser) throws MailException, InterruptedException {
        if(emailCheck && ((userService.findByEmail(requestUser.getEmail()).isPresent()) || (!userService.findByEmail(requestUser.getEmail()).isPresent() && userService.reqUserexistsByEmailNew(requestUser.getEmail())))) {
            return new ResponseEntity(new ApiResponse(false, "Email Address has been used to apply before!"),
                    HttpStatus.BAD_REQUEST);
        } 
        
        emailValidator = new EmailValidator();
        if(!emailValidator.validate(requestUser.getEmail())) {
            return new ResponseEntity(new ApiResponse(false, "Email is not valid!"),
                    HttpStatus.BAD_REQUEST);
        }
        
        userService.registerNewUser(requestUser);
        
        return new ResponseEntity(new ApiResponse(true, "New User request has been registered successfully!"),
                HttpStatus.OK);

    }
    
	/*
	 * @SuppressWarnings({ "unchecked", "rawtypes" })
	 * 
	 * @PostMapping("/signup") public ResponseEntity<?>
	 * registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
	 * if(userRepository.existsByUsername(signUpRequest.getUsername())) { return new
	 * ResponseEntity(new ApiResponse(false, "Username is already taken!"),
	 * HttpStatus.BAD_REQUEST); }
	 * 
	 * if(userRepository.existsByEmail(signUpRequest.getEmail())) { return new
	 * ResponseEntity(new ApiResponse(false, "Email Address already in use!"),
	 * HttpStatus.BAD_REQUEST); }
	 * 
	 * // Creating user's account SecUser user = new
	 * SecUser(signUpRequest.getName(), signUpRequest.getUsername(),
	 * signUpRequest.getEmail(), signUpRequest.getPassword(),
	 * signUpRequest.getType());
	 * 
	 * user.setPassword(passwordEncoder.encode(user.getPassword()));
	 * 
	 * SecRole userRole = roleRepository.findByName(RoleName.ROLE_USER)
	 * .orElseThrow(() -> new AppException("User Role not set."));
	 * 
	 * user.setRoles(Collections.singleton(userRole));
	 * 
	 * SecUser result = userRepository.save(user);
	 * 
	 * URI location = ServletUriComponentsBuilder
	 * .fromCurrentContextPath().path("/users/{username}")
	 * .buildAndExpand(result.getUsername()).toUri();
	 * 
	 * return ResponseEntity.created(location).body(new ApiResponse(true,
	 * "User registered successfully")); }
	 */
}
