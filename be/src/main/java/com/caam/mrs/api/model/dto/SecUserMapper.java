package com.caam.mrs.api.model.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.caam.mrs.api.model.SecUser;

@Mapper
public interface SecUserMapper {
	
	@Mappings({
		@Mapping(target="uid", source="secUser.id"),
		@Mapping(target="createdByName", source="secUser.createdByRef.name"),
		@Mapping(target="updatedByName", source="secUser.updatedByRef.name"),
		@Mapping(target="createdAt", source="secUser.createdAt", dateFormat="MMMM dd, yyyy hh:mm:ss"),
		@Mapping(target="updatedAt", source="secUser.updatedAt", dateFormat="MMMM dd, yyyy hh:mm:ss")
	})
	SecUserDto toSecUserDto(SecUser secUser);
	
	Iterable<SecUserDto> toSecUserDtos(Iterable<SecUser> secUsers);
	
	SecUser toSecUser (SecUserDto secUserDto);
}
