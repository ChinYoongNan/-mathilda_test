package com.caam.mrs.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.caam.mrs.api.model.Student;

@RepositoryRestResource(exported=false)
public interface StudentRepo extends CrudRepository<Student, Long> {

	@Query("select j from Student j where j.email = :email")
	Optional<Student> findByEmail(@Param("email") String email);
}
