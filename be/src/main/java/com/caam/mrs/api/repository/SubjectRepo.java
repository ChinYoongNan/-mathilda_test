package com.caam.mrs.api.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.caam.mrs.api.model.Subject;

@RepositoryRestResource(exported=false)
public interface SubjectRepo extends CrudRepository<Subject, Long> {
//
//	@Query("select j from Subject j where j.subId = :id")
//	Iterable<Subject> findJobsByType(@Param("id") Long jobType);
}
