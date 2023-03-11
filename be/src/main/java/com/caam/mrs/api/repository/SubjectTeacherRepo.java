package com.caam.mrs.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.caam.mrs.api.model.Student;
import com.caam.mrs.api.model.Subject;
import com.caam.mrs.api.model.SubjectTeacher;

@RepositoryRestResource(exported=false)
public interface SubjectTeacherRepo extends CrudRepository<SubjectTeacher, Long> {

	@Query("select j from SubjectTeacher j where j.subjects.id = :subjects and j.teachers.id = :teacher")
	Optional<SubjectTeacher> findBySubjectTeacher(@Param("subjects") Long subject, @Param("teacher") Long teacher);
	

	@Query("select j from SubjectTeacher j where j.subjects.id is not null and j.teachers.id is not null")
	Iterable<SubjectTeacher> findAllSubjectTeacher();
}
