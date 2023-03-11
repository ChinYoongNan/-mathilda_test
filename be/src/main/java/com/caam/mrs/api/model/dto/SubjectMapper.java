package com.caam.mrs.api.model.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.caam.mrs.api.model.Subject;

@Mapper
public interface SubjectMapper {
	@Mappings({
		@Mapping(target="id", source="subject.id"),
		@Mapping(target="name", source="subject.name"),
//		@Mapping(target="createdBy", source="company.createdByRef.name"),
//		@Mapping(target="updatedBy", source="subject.updatedByRef.name"),
//		@Mapping(target="createdAt", source="company.createdAt", dateFormat="MMMM dd, yyyy hh:mm:ss"),
//		@Mapping(target="updatedAt", source="company.updatedAt", dateFormat="MMMM dd, yyyy hh:mm:ss")
	})
	SubjectDto toSubjectDto(Subject subject);
		
	Iterable<SubjectDto> toSubjectDtos(Iterable<Subject> subjects);
	
	Subject toSubject (SubjectDto SubjectDto);
	
	Iterable<Subject> toSubjects(Iterable<SubjectDto> SubjectDtos);
}
