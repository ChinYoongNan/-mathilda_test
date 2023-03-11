package com.caam.mrs.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.caam.mrs.api.model.RoleName;
import com.caam.mrs.api.model.SecRole;
import com.caam.mrs.api.model.SecUser;

@RepositoryRestResource(exported=false)
public interface SecUserRepo extends CrudRepository<SecUser, Long>{
	Optional<SecUser> findByEmail(String email);

    Optional<SecUser> findByUsernameOrEmail(String username, String email);
	
	@Query("select s from SecUser s where (s.username = :username or s.email = :email) and s.enabled = true ")
    Optional<SecUser> findEnableByUsernameOrEmail(@Param("username") String username, @Param("email") String email);

	@Query("select s from SecUser s where (s.username = :username and s.email = :email) and s.enabled = true ")
    Optional<SecUser> findUsernameAndEmail(@Param("username") String username, @Param("email") String email);
	
    List<SecUser> findByIdIn(List<Long> userIds);

    Optional<SecUser> findByUsername(String username);

    @Query("select count(*) from SecUser s where s.username = :username")
    long existsByUsername(@Param("username") String username);

    @Query("select count(*) from SecUser s where s.email = :email")
    long existsByEmail(@Param("email") String email);
    
    @Query("select count(*) from SecUser s where s.username = :username and s.id <> :userId")
    long checkUsernameExist(@Param("username") String username, @Param("userId") Long userId);

    @Query("select count(*) from SecUser s where s.email = :email and s.id <> :userId")
    long checkEmailExist(@Param("email") String email, @Param("userId") Long userId);

    @Query("select s from SecUser s where s.type = :type and (:filter is null or s.username like :filter or s.email like :filter or s.name like :filter) order by s.username")
    Page<SecUser> findAllByType(@Param("type") String type, String filter, Pageable pageable);    
    
    @Query("select s from SecUser s where s.type = :type order by s.username")
	Iterable<SecUser> findAllByType(@Param("type") String type);
    
    @Query("select s from SecUser s inner join s.roles r where s.type = :type and r.id  IN :roles and (:filter is null or s.username like :filter or s.email like :filter or s.name like :filter) order by s.username")
    Page<SecUser> findAllByTypeRole(@Param("type") String type, List<Long> roles, String filter, Pageable pageable);
    
    @Query("select s from SecUser s inner join s.roles r where s.type = :type and r.id IN :roles order by s.username")
    Iterable<SecUser> findAllByTypeRole(@Param("type") String type, List<Long> roles);
    
//    @Query("select s from SecUser s inner join s.roles r where s.type = :type and s.company.comId = :comId and r.id IN :roles and (:filter is null or s.username like :filter or s.email like :filter or s.name like :filter)  order by s.username")
//    Page<SecUser> findAllByTypeRoleCompany(@Param("type") String type, List<Long> roles, @Param("comId") Long comId, String filter, Pageable pageable);
//    
//    @Query("select s from SecUser s inner join s.roles r where s.type = :type and s.company.comId = :comId and r.id IN :roles order by s.username")
//    Iterable<SecUser> findAllByTypeRoleCompany(@Param("type") String type, List<Long> roles, @Param("comId") Long comId);
//
//    @Query("select s from SecUser s where s.type = :type and s.company.comId = :comId  and (:filter is null or s.username like :filter or s.email like :filter or s.name like :filter)  order by s.username")
//    Page<SecUser> findAllByCompany(@Param("type") String type, @Param("comId") Long comId, String filter, Pageable pageable);	
//    
//    @Query("select s from SecUser s where s.type = :type and s.company.comId = :comId order by s.username")
//	Iterable<SecUser> findAllByCompany(@Param("type") String type, @Param("comId") Long comId);	
//
//    @Query("select s from SecUser s where s.type = :type and s.company.comId = :comId and s.enabled = true order by s.username")
//    Iterable<SecUser> findAllByTypeCompany(@Param("type") String type, @Param("comId") Long comId);
//    
//    @Query("select s from SecUser s inner join s.roles r where s.type = :type and s.company.comId = :comId and s.enabled = true and r.id IN :roles order by s.username")
//    Iterable<SecUser> findAllTechnicalRoleIdByCompany(@Param("type") String type, @Param("comId") Long comId, List<Long> roles);
//    
//    @Query("select s from SecUser s inner join s.roles r where s.type = :type and s.company.comId = :comId and s.enabled = true and r.name IN :roles "
//    		+ "and exists (select 1 from UserSite us where us.userRef.id = s.id and us.siteRef.siteId = :siteId) order by s.username")
//    Iterable<SecUser> findAllTechnicalRoleNameByCompany(@Param("type") String type, @Param("comId") Long comId, List<RoleName> roles, @Param("siteId") Long siteId);
//    
//    @Query("select s from SecUser s inner join s.roles r where s.type = :type and s.company.comId = :comId and s.enabled = true and r.name IN :roles "
//    		+ "and exists (select 1 from UserSite us where us.userRef.id = s.id "
//    		+ "and us.siteRef.siteId in (:siteList)) "
//    		+ "order by s.username")
//    Iterable<SecUser> findAllContractorTechnicalSitesByCompany(@Param("type") String type, @Param("comId") Long comId, List<RoleName> roles, List<Long> siteList);
        
//    @Query("select s from SecUser s where s.type = :type and s.company.comId = :comId and :theRole MEMBER OF s.roles and s.enabled = true order by s.username")
//    Iterable<SecUser> findAllByTypeCompanyandRole(@Param("type") String type, @Param("comId") Long comId, @Param("theRole") SecRole theRole);
//    
//    @Query("select s from SecUser s where :theRole MEMBER OF s.roles and exists (select 1 from UserSite us where us.userRef.id = s.id and us.siteRef.siteId = :siteId) and s.enabled = true")
//    Iterable<SecUser> findUserByRoleSite(@Param("theRole") SecRole theRole, @Param("siteId") Long siteId);
//    
//    @Query("select s from SecUser s where :theRole MEMBER OF s.roles and exists (select 1 from UserSite us where us.userRef.id = s.id and us.siteRef.siteId in (:siteList)) and s.enabled = true")
//    Iterable<SecUser> findUserByRoleSites(@Param("theRole") SecRole role, List<Long> siteList);
//    
//    @Query("select s from SecUser s where :theRole MEMBER OF s.roles and exists (select 1 from UserRegion ur where ur.userRef.id = s.id and ur.regionRef.regionId = :regionId) and s.enabled = true")
//    Iterable<SecUser> findUserByRoleRegion(@Param("theRole") SecRole theRole, @Param("regionId") Long regionId);
//    
    @Query("select s from SecUser s where :theRole MEMBER OF s.roles and s.enabled = true")
    Iterable<SecUser> findAllByRole(@Param("theRole") SecRole role);
    
    @Modifying
    @Query(value = "delete from sec_user_roles where user_id = :userId", nativeQuery = true)
	void deleteUserRole(@Param("userId") Long userId);
    
    @Modifying
    @Query(value = "delete from user_region where user_id = :userId", nativeQuery = true)
	void deleteUserRegion(@Param("userId") Long userId);
    
    @Modifying
    @Query(value = "delete from user_site where user_id = :userId", nativeQuery = true)
	void deleteUserSite(@Param("userId") Long userId);

}
