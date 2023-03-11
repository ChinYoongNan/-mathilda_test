package com.caam.mrs.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.caam.mrs.api.model.RoleName;
import com.caam.mrs.api.model.SecRole;

@RepositoryRestResource(exported=false)
public interface SecRoleRepo extends CrudRepository<SecRole, Long>{
	Optional<SecRole> findByName(RoleName roleName);
	
	@Query("select r from SecRole r where r.name <> 'ROLE_USER'")
	Iterable<SecRole> findAllRoles();

	@Query("select r from SecRole r where r.name not in ('ROLE_USER', 'ROLE_SPVCON', 'ROLE_TCHCON', 'ROLE_HQCON', 'ROLE_ADMCON')")
	Iterable<SecRole> findAllRolesInternal();

	@Query("select r from SecRole r where r.name in ('ROLE_SPVCON', 'ROLE_TCHCON', 'ROLE_HQCON','ROLE_ADMCON')")
	Iterable<SecRole> findAllRolesExternal();
}
