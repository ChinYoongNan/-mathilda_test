package com.caam.mrs.api.model.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.caam.mrs.api.model.Teacher;

@Mapper
public interface TeacherMapper {
	@Mappings({
		@Mapping(target="id", source="teacher.id"),
		@Mapping(target="name", source="teacher.name"),		
		@Mapping(target="teachingsubject", source="teacher.subject"),
		@Mapping(target="subjects", source="teacher.subjects")
	})
	TeacherDto toTeacherDto(Teacher teacher);
		
	Iterable<TeacherDto> toTeacherDtos(Iterable<Teacher> teachers);
	
	Teacher toTeacher (TeacherDto TeacherDto);
	
	Iterable<Teacher> toTeachers(Iterable<TeacherDto> TeacherDtos);
}
