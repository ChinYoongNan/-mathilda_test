package com.caam.mrs.api.model.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.caam.mrs.api.model.SecRole;

@Mapper
public interface SecRoleMapper {
	
	@Mappings({
		@Mapping(target="roleId", source="secRole.id"),
		@Mapping(target="roleName", source="secRole.name")
	})
	SecRoleDto toSecRoleDto(SecRole secRole);
	
	Iterable<SecRoleDto> toSecRoleDtos(Iterable<SecRole> secRoles);
	
	SecRole toSecRole (SecRoleDto secRoleDto);
}
