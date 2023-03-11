package com.caam.mrs.api.model.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.caam.mrs.api.model.SubjectTeacher;

@Mapper
public interface SubjectTeacherMapper {
	@Mappings({
		@Mapping(target="id", source="id"),
		@Mapping(target="name", source="subjects.name"),
		@Mapping(target="teachername", source="teachers.name"),
	})
	SubjectTeacherDto toSubjectTeacherDto(SubjectTeacher subject);
		
	Iterable<SubjectTeacherDto> toSubjectTeacherDtos(Iterable<SubjectTeacher> subjects);
	
	SubjectTeacher toSubjectTeacher (SubjectTeacherDto SubjectDto);
	
	Iterable<SubjectTeacher> toSubjectTeachers(Iterable<SubjectTeacherDto> SubjectDtos);
}
