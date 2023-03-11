package com.caam.mrs.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.caam.mrs.api.model.Subject;
import com.caam.mrs.api.model.Teacher;

@RepositoryRestResource(exported=false)
public interface TeacherRepo extends CrudRepository<Teacher, Long> {

	@Query("select j from Teacher j where j.email = :email")
	Optional<Teacher> findByEmail(@Param("email") String email);
}
