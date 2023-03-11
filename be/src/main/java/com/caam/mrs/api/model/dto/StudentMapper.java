package com.caam.mrs.api.model.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.caam.mrs.api.model.Student;



@Mapper
public interface StudentMapper {
	@Mappings({
		@Mapping(target="id", source="student.id"),
		@Mapping(target="name", source="student.name"),
		@Mapping(target="classes", source="student.classes")
	})
	StudentDto toStudentDto(Student student);
		
	Iterable<StudentDto> toStudentDtos(Iterable<Student> students);
	
	Student toStudent (StudentDto StudentDto);
	
	Iterable<Student> toStudents(Iterable<StudentDto> StudentDtos);
}
