package com.caam.mrs.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.mail.MailException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.caam.mrs.api.exception.AppException;
import com.caam.mrs.api.model.PasswordResetToken;
import com.caam.mrs.api.model.RoleName;
import com.caam.mrs.api.model.SecRole;
import com.caam.mrs.api.model.SecUser;
import com.caam.mrs.api.model.dto.SecUserDto;
import com.caam.mrs.api.payload.RequestUser;
import com.caam.mrs.api.repository.PasswordResetTokenRepo;
import com.caam.mrs.api.repository.SecRoleRepo;
import com.caam.mrs.api.repository.SecUserRepo;
import com.caam.mrs.api.security.UserPrincipal;
import com.caam.mrs.api.util.RandomPasswordGenerator;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
	@Autowired
	private SecUserRepo secUserRepo;
	
	@Autowired
	private SecRoleRepo secRoleRepo;
	
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	@Autowired
	private PasswordResetTokenRepo passwordTokenRepo;
	
	
    String domainUrl;

	String emailFrom;
	
	public Iterable<SecUser> findAll() {
		return secUserRepo.findAll();
	}
	
	public Page<SecUser> findAllInternal(Integer page, Integer size, String sortDir, String sortField, List<Long> roles, String filter) {
		Page<SecUser> secUsers = null;
		
		if (size.equals(0)) {
			size = Integer.MAX_VALUE;
		}
		
		Direction dir = Sort.Direction.ASC;
		
		if (sortDir!=null && !sortDir.equalsIgnoreCase("false"))
			dir = Sort.Direction.DESC;	
		
		if (sortField == null)
			sortField = "id";
			
		
		if (roles != null && roles.size() > 0)
			return secUserRepo.findAllByTypeRole("I", roles, filter, PageRequest.of(page, size, dir, sortField));
		else
			return secUserRepo.findAllByType("I", filter, PageRequest.of(page, size, dir, sortField));
	}
	
	public Iterable<SecUser> findAllAdmin() {
		SecRole role = secRoleRepo.findByName(RoleName.ROLE_ADM)   
                .orElseThrow(() -> new AppException("User Role not set."));

		return secUserRepo.findAllByRole(role);
	}
	
	public Iterable<SecRole> findAllRoles() {
		return secRoleRepo.findAllRoles(); 
	}
	
	public Iterable<SecRole> findAllRolesInternal() {
		return secRoleRepo.findAllRolesInternal(); 
	}
	
	public Iterable<SecRole> findAllRolesExternal() {
		return secRoleRepo.findAllRolesExternal(); 
	}
	
	public Optional<SecUser> findById(Long id) {
        return secUserRepo.findById(id);
    }
	
	public Optional<SecUser> findByEmail(String userEmail) {
		return secUserRepo.findByEmail(userEmail);
	}
	
	public Optional<SecUser> findUsernameAndEmail(String username, String email) {
		return secUserRepo.findUsernameAndEmail(username, email);
	}
	
	public Optional<SecUser> getUserByPasswordResetToken(String token) {
		return Optional.ofNullable(passwordTokenRepo.findByToken(token).getUser());
	}
	
	public boolean existsByUsername(String username) {
		long count = secUserRepo.existsByUsername(username);
		if (count > 0)
			return true;
		else
			return false;
	}

	public boolean existsByEmail(String email) {
		long count = secUserRepo.existsByEmail(email);
		if (count > 0)
			return true;
		else
			return false;
	}
	
	public boolean checkUsernameExist(String username, Long userId) {
		long count = secUserRepo.checkUsernameExist(username, userId);
		if (count > 0)
			return true;
		else
			return false;
	}

	public boolean checkEmailExist(String email, Long userId) {
		long count =  secUserRepo.checkEmailExist(email, userId);
		if (count > 0)
			return true;
		else
			return false;
	}
	
//	public Iterable<UserRegion> findUserRegions(Long id) {
//		return userRegionRepo.findAllByUser(id);
//	}
//	
//	public Iterable<UserSite> findUserSites(Long id) {
//		return userSiteRepo.findAllByUser(id);
//	}
//	
	@Transactional
    public SecUser save(SecUser secUser, SecUserDto secUserDto, UserPrincipal currUser) {
    	try {
    		if (secUser.getId() != null) {
	    		Optional<SecUser> secUserOpt = secUserRepo.findById(secUser.getId());
	    		if (secUserOpt.isPresent()) {
	    			String pwd = "";
	    			SecUser secUserOri = secUserOpt.get();
	    			secUserOri.setUsername(secUser.getUsername().trim());
	    			secUserOri.setName(secUser.getName().trim());
	    			secUserOri.setEmail(secUser.getEmail().trim());
	    			secUserOri.setEnabled(secUser.getEnabled());
	                
//	    			if (secUserDto.getType() != null && secUserDto.getType().equals("C"))
//	    				secUserOri.setCompany(new Company(Long.valueOf(secUserDto.getComId())));
	    			
	    			secUserOri.setUpdatedByRef(new SecUser(currUser.getId()));
	    			
	    			if (secUserDto.getAutoPwd() != null && !secUserDto.getAutoPwd() && secUserDto.getTempval() != null && secUserDto.getTempval().trim() != "") {
	    				pwd = secUserDto.getTempval();
	    				secUserOri.setPassword(passwordEncoder.encode(pwd));
	    			}
	    			
	    			if (secUserDto.getUserRoles() != null) {
	    				try {
	    					secUserRepo.deleteUserRole(secUserOri.getId());
	    				} catch (Exception e) {
	    					System.out.println("Error: " + e.getMessage());
	    				}
	    				
	                	List<SecRole> secRoles = new ArrayList<SecRole>();
	                	for (String roleId : secUserDto.getUserRoles()) {
	                		if (!Long.valueOf(roleId).equals(2))
	                			secRoles.add(secRoleRepo.findById(Long.valueOf(roleId)).get());
	                	}
	                	secRoles.add(secRoleRepo.findById(Long.valueOf("2")).get()); //Add Normal Role, ROLE_USER
	                	secUserOri.setRoles(secRoles.stream().collect(Collectors.toSet()));
	                }
	               
	    			secUserOri = secUserRepo.save(secUserOri);

	                //insert regions
//	                if (secUserDto.getUserRegions() != null) {
//	                	insertRegions(secUserOri, secUserDto.getUserRegions(), currUser);
//	                } else {
//	                	deleteRegions(secUserOri, secUserDto.getUserRegions(), currUser);
//	                }
//	                
//	                //insert sites
//	                if (secUserDto.getUserSites() != null) {
//	                	insertSites(secUserOri, secUserDto.getUserSites(), currUser);
//	                }else {
//	                	deleteSites(secUserOri, secUserDto.getUserSites(), currUser);
//	                }
	                
	                if (secUserDto.getAutoPwd() != null && !secUserDto.getAutoPwd() && secUserDto.getTempval() != null && secUserDto.getTempval().trim() != "") {
		                String body = "Dear <b>" + secUser.getName() + "</b>,"
		            			+ "<br><br>"
		            			+ "Your password has been reset by MMRS Administrator. The following is your new password:<br>"
		            			+ "New Password: "+ pwd
		            			+ "<br><br>"
		            			+ "You may login directly to MMRS, <a href=\""+ domainUrl +"\">"+ domainUrl + "</a>. "
		            			+ "<br><br>"
		            			+ "Please contact us should you have further enquiries."
		            			+ "<br><br>"
		            			+ "Thank you."
		            			+ "<br><br>"
		            			+ "MMRS Administrator";
		            	
	                }
	    		}
	    	} else {
	    		// Creating user's account
	            String pwd = "";
	    		SecUser user = new SecUser();
	            user.setUsername(secUser.getUsername().trim());
	            user.setName(secUser.getName().trim());
	            user.setEmail(secUser.getEmail().trim());
	            user.setEnabled(secUser.getEnabled());
	            user.setType(secUser.getType());
	            
//	            if (secUserDto.getType() != null && secUserDto.getType().equals("C"))
//	            	user.setCompany(new Company(Long.valueOf(secUserDto.getComId())));
	            
	            if (secUserDto.getAutoPwd() !=null && !secUserDto.getAutoPwd() && secUserDto.getTempval() != null && secUserDto.getTempval().trim() != "") {
	            	pwd = secUserDto.getTempval();
	            	user.setPassword(passwordEncoder.encode(pwd));
	            } else {
	            	// auto generate password
	            	pwd = RandomPasswordGenerator.generatePassayPassword();
	//            	System.out.println("Pwd: " + pwd);
	            	user.setPassword(passwordEncoder.encode(pwd));
	            }
	            
	            if (secUserDto.getUserRoles() != null) {
	            	List<SecRole> secRoles = new ArrayList<SecRole>();
	            	for (String roleId : secUserDto.getUserRoles()) {
	            		if (!Long.valueOf(roleId).equals(2))
	            			secRoles.add(secRoleRepo.findById(Long.valueOf(roleId)).get());
	            	}
	            	secRoles.add(secRoleRepo.findById(Long.valueOf("2")).get()); //Add Normal Role, ROLE_USER
	            	user.setRoles(secRoles.stream().collect(Collectors.toSet()));
	            }
	            user.setCreatedByRef(new SecUser(currUser.getId()));
	            
	            user = secUserRepo.save(user);
	            
	            //insert regions
//	            if (secUserDto.getUserRegions() != null) {
//	            	insertRegions(user, secUserDto.getUserRegions(), currUser);
//	            }
//	            
//	            //insert sites
//	            if (secUserDto.getUserSites() != null) {
//	            	insertSites(user, secUserDto.getUserSites(), currUser);
//	            }
	            
	            String body = "Dear <b>" + secUser.getName() + "</b>,"
	        			+ "<br><br>"
	        			+ "Welcome to MMRS system. New account has been created for you.<br>"
	        			+ "You may login to this url " + domainUrl + ", or download the MMRS mobile application from AppStore or Google Play Store.<br><br>"
	        			+ "The following are your login details: <br>"
	        			+ "Username: "+ secUser.getUsername() + "<br>"
	        			+ "Password: "+ pwd + "<br>"
	        			+ "<br><br>"
	        			+ "Please contact us should you have further enquiries."
	        			+ "<br><br>"
	        			+ "Thank you."
	        			+ "<br><br>"
	        			+ "MMRS Administrator";
	        	
	    	}
	    	return secUser;
    	} catch (Exception e)
		{ 
			throw new RuntimeException("Could not update user!");
		} 	
    }
    
    public SecUser saveProfile(SecUser secUser, SecUserDto secUserDto, UserPrincipal currUser) {
    	Optional<SecUser> secUserOpt = secUserRepo.findById(currUser.getId());
		if (secUserOpt.isPresent()) {
			SecUser secUserOri = secUserOpt.get();
			secUserOri.setName(secUserDto.getName());
			secUserOri.setEmail(secUserDto.getEmail());
            
			secUserOri.setUpdatedByRef(new SecUser(currUser.getId()));
			
			secUserOri = secUserRepo.save(secUserOri);
			
			//insert regions
//            if (secUserDto.getUserRegions() != null) {
//            	insertRegions(secUserOri, secUserDto.getUserRegions(), currUser);
//            }
//            
//            //insert sites
//            if (secUserDto.getUserSites() != null) {
//            	insertSites(secUserOri, secUserDto.getUserSites(), currUser);
//            }
			
			return secUserOri;
		}
		else 
			return secUser;
	}

	public void resetPwd(Long userId, UserPrincipal currUser) throws MailException, InterruptedException {
    	Optional<SecUser> secUserOpt = secUserRepo.findById(userId);
		if (secUserOpt.isPresent()) {
			SecUser secUser = secUserOpt.get();
			
			// auto generate password
        	String pwd = RandomPasswordGenerator.generatePassayPassword();
        	
        	secUser.setPassword(passwordEncoder.encode(pwd));
        	secUser.setUpdatedByRef(new SecUser(currUser.getId()));
        	
        	secUserRepo.save(secUser);
        	
        	String body = "Dear <b>" + secUser.getName() + "</b>,"
        			+ "<br><br>"
        			+ "Your password has been reset by the system. The following is your new password:<br>"
        			+ "New Password: "+ pwd
        			+ "<br><br>"
        			+ "You may login directly to MMRS, <a href=\""+ domainUrl +"\">"+ domainUrl + "</a>. "
        			+ "<br><br>"
        			+ "Please contact us should you have further enquiries."
        			+ "<br><br>"
        			+ "Thank you."
        			+ "<br><br>"
        			+ "MMRS Administrator";
        	
		}
	}
	
	public void saveResetPwd(SecUser user, String password) throws MailException, InterruptedException {
		user.setPassword(passwordEncoder.encode(password));
		user.setUpdatedByRef(user);
		secUserRepo.save(user);
	}
	
	public void createPasswordResetTokenForUser(SecUser secUser, String token) throws MailException, InterruptedException {
		final PasswordResetToken myToken = new PasswordResetToken(token, secUser);
		passwordTokenRepo.save(myToken);
		
		String body = "Dear <b>" + secUser.getName() + "</b>,"
    			+ "<br><br>"
    			+ "Your have requested to reset password of your MMRS account.<br>"
    			+ "<br><br>"
    			+ "Click on the following link to change your password, <a href=\""+ domainUrl +"/resetpwd/"+ token +"\">"+ domainUrl + "/resetpwd/"+ token + "</a>. "  
    			+ "<br><br>"
    			+ "Please contact us should you have further enquiries."
    			+ "<br><br>"
    			+ "Thank you."
    			+ "<br><br>"
    			+ "MMRS Administrator";
    	
	}
	
	public SecUser changePwd(SecUser secUser, SecUserDto secUserDto, UserPrincipal currUser) {
		Optional<SecUser> secUserOpt = secUserRepo.findById(currUser.getId());
		if (secUserOpt.isPresent()) {
			SecUser secUserOri = secUserOpt.get();
			
			// auto generate password
        	String pwd = secUserDto.getTempval();
        	secUserOri.setPassword(passwordEncoder.encode(pwd));
        	secUserOri.setUpdatedByRef(new SecUser(currUser.getId()));
        	
        	secUserRepo.save(secUserOri);
		}
		
		return secUser;
	}

    @Transactional
    public void deleteById(Long userId) {
    	try {
	    	secUserRepo.deleteUserRegion(userId);
	    	secUserRepo.deleteUserSite(userId);
	    	secUserRepo.deleteUserRole(userId);
	    	secUserRepo.deleteById(userId);
	    } catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
    }

	public void deleteByList(Iterable<SecUser> companies) {
		secUserRepo.deleteAll(companies);
	}
	
//	public Boolean reqUserexistsByEmail(String email) {
//		return requestNewUserRepo.existsByEmail(email);
//	}
//	
	public boolean reqUserexistsByEmailNew(String email) {
//		long count = requestNewUserRepo.reqUserexistsByEmailNew(email);
//		if (count > 0)
//			return true;
//		else
			return false;
	}
//	
//	public boolean reqUserexistsByEmailApproved(String email) {
//		long count = requestNewUserRepo.reqUserexistsByEmailApproved(email);
//		if (count > 0)
//			return true;
//		else
//			return false;
//	}
	
	public void registerNewUser(@Valid RequestUser requestUser) throws MailException, InterruptedException {
//		RequestNewUser newRequest = new RequestNewUser();
//		newRequest.setName(requestUser.getName());
//		newRequest.setEmail(requestUser.getEmail());
//		newRequest.setRemarks(requestUser.getRemarks());
//		newRequest.setStatus("N");
//		newRequest.setType(requestUser.getType());
//		if (requestUser.getType() != null && requestUser.getType().equals("C"))
//			newRequest.setCompanyRef(new Company(requestUser.getCompany()));
//		newRequest.setRoleRef(new SecRole(requestUser.getRole()));
//		
//		newRequest.setCreatedAt(new Date());
//		
//		newRequest = requestNewUserRepo.save(newRequest);
//		
//		//insert regions
//        if (requestUser.getUserRegions() != null) {
//        	insertNewUserRegions(newRequest, requestUser.getUserRegions());
//        }
//        
//        //insert sites
//        if (requestUser.getUserSites() != null) {
//        	insertNewUserSites(newRequest, requestUser.getUserSites());
//        }
//		
//		Iterable<SecUser> userList = findAllAdmin();
//		for (SecUser user : userList) {
//		
//			String body = "Dear <b>" + user.getName() + "</b>,"
//	    			+ "<br><br>"
//	    			+ "A new user request has been submitted for your approval:<br>"
//	    			+ "<br><br>"
//	    			+ "Name: " + requestUser.getName() +".<br>" 
//	    			+ "email: " + requestUser.getEmail() +". "  
//	    			+ "<br><br>"
//	    			+ "Please login via MMRS mobile or "+ domainUrl +" for more details.";
//	    	
//	    	
//	    	this.mailGunEmailService.sendHTML(emailFrom,
//	    			user.getEmail(),
//	    			"New User Request",
//	    			body);
//	    	
//	    	NotificationDto noticeDto = notifyMessageService.notifyMessage(
//					new Timestamp((new Date()).getTime()),
//					"New user request has been submitted for your approval. Name: " + requestUser.getName() + ", " + requestUser.getEmail() +".",
//					user.getId());
//
////			if (noticeDto != null) 
////				notifyMessageService.sendToUser(user.getUsername(), "/queue/notifications", noticeDto);
//		}
	}
//	
//	public void insertRegions(SecUser secUser, List<String> regions, UserPrincipal currUser) {
//		Iterable<UserRegion> userRegions = userRegionRepo.findAllByUser(secUser.getId());
//		if (userRegions.iterator().hasNext())
//			userRegionRepo.deleteAll(userRegions);
//		
//		for (String regionId : regions) {
//			UserRegion userRegion = new UserRegion();
//			userRegion.setRegionRef(new Region(Long.valueOf(regionId)));
//			userRegion.setUserRef(secUser);
//			userRegion.setCreatedByRef(new SecUser(currUser.getId()));
//			userRegionRepo.save(userRegion);
//    	}
//	}
//	
//	public void deleteRegions(SecUser secUser, List<String> regions, UserPrincipal currUser) {
//		Iterable<UserRegion> userRegions = userRegionRepo.findAllByUser(secUser.getId());
//		if (userRegions.iterator().hasNext())
//			userRegionRepo.deleteAll(userRegions);
//	}
//	
//	public void insertNewUserRegions(RequestNewUser requestNewUser, List<String> regions) {
//		for (String regionId : regions) {
//			if(!regionId.trim().equals("")) {
//				RequestUserRegion userRegion = new RequestUserRegion();
//				userRegion.setRegionRef(new Region(Long.valueOf(regionId)));
//				userRegion.setRequestRef(requestNewUser);
//				reqUserRegionRepo.save(userRegion);				
//			}
//    	}
//	}
//	
//	public void insertSites(SecUser secUser, List<String> sites, UserPrincipal currUser) {
//		Iterable<UserSite> userSites = userSiteRepo.findAllByUser(secUser.getId());
//		if (userSites.iterator().hasNext())
//			userSiteRepo.deleteAll(userSites);
//		
//		for (String siteId : sites) {
//			UserSite userSite = new UserSite();
//			userSite.setSiteRef(new Site(Long.valueOf(siteId)));
//			userSite.setUserRef(secUser);
//			userSite.setCreatedByRef(new SecUser(currUser.getId()));
//			userSiteRepo.save(userSite);
//    	}
//	}
//	
//	public void deleteSites(SecUser secUser, List<String> sites, UserPrincipal currUser) {
//		Iterable<UserSite> userSites = userSiteRepo.findAllByUser(secUser.getId());
//		if (userSites.iterator().hasNext())
//			userSiteRepo.deleteAll(userSites);
//	}
//	
//	public void insertNewUserSites(RequestNewUser requestNewUser, List<String> sites) {
//		for (String siteId : sites) {
//			RequestUserSite userSite = new RequestUserSite();
//			userSite.setSiteRef(new Site(Long.valueOf(siteId)));
//			userSite.setRequestRef(requestNewUser);
//			reqUserSiteRepo.save(userSite);
//    	}
//	}

}
