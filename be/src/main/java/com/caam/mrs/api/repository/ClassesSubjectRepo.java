package com.caam.mrs.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.caam.mrs.api.model.ClassesSubject;
import com.caam.mrs.api.model.Subject;
import com.caam.mrs.api.model.SubjectTeacher;

@RepositoryRestResource(exported=false)
public interface ClassesSubjectRepo extends CrudRepository<ClassesSubject, Long> {
	@Query("select j from ClassesSubject j where j.subjects.id = :subjects and j.classes.id = :classes")
	Optional<ClassesSubject> findByClassesSubject(@Param("subjects") Long subject, @Param("classes") Long classes);
}
