package com.caam.mrs.api.model.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.caam.mrs.api.model.Classes;

@Mapper
public interface ClassesMapper {
	@Mappings({
		@Mapping(target="id", source="classes.id"),
		@Mapping(target="name", source="classes.name"),
		@Mapping(target="subjects", source="classes.subjects"),
//		@Mapping(target="students", source="classes.students")
	})
	ClassesDto toClassesDto(Classes classes);
		
	Iterable<ClassesDto> toClassesDtos(Iterable<Classes> classes);
	
	Classes toClasses (ClassesDto ClassesDto);
	
	Iterable<Classes> toClassess(Iterable<ClassesDto> ClassesDtos);
}
