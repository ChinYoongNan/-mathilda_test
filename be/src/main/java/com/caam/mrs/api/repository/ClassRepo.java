package com.caam.mrs.api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.caam.mrs.api.model.Classes;
import com.caam.mrs.api.model.Student;

@RepositoryRestResource(exported=false)
public interface ClassRepo extends CrudRepository<Classes, Long> {
//
//	@Query("select j from Subject j where j.subId = :id")
//	Iterable<Subject> findJobsByType(@Param("id") Long jobType);
}
