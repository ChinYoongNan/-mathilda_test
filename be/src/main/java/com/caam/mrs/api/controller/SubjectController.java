package com.caam.mrs.api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.caam.mrs.api.model.Subject;
import com.caam.mrs.api.model.dto.SubjectDto;
import com.caam.mrs.api.model.dto.SubjectMapper;
import com.caam.mrs.api.security.CurrentUser;
import com.caam.mrs.api.security.UserPrincipal;
import com.caam.mrs.api.service.SubjectService;

import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;

/**
 * Created by Suhaimi AK on 17/04/2019.
 */

//@Slf4j
@RequiredArgsConstructor

@RestController
@RequestMapping("/api/subject")
@ComponentScan(basePackageClasses=SubjectMapper.class)
public class SubjectController {
	
	@Autowired
	private final SubjectService SubjectService;
	
	@Autowired
	private final SubjectMapper subjectMapper;
	
	@GetMapping("/list")
    public ResponseEntity<Iterable<SubjectDto>> findAll() {
        return ResponseEntity.ok(subjectMapper.toSubjectDtos(SubjectService.findAll()));
    }
	
	
	@GetMapping("/{id}")
    public ResponseEntity<SubjectDto> getSubject(@PathVariable("id") Long id) {
		Optional<Subject> supplierOpt = SubjectService.findById(id);
		if (supplierOpt.isPresent())      
 			return ResponseEntity.ok(subjectMapper.toSubjectDto(supplierOpt.get()));
		else
			return null;
    }
	
//	@PreAuthorize("hasAuthority('ROLE_ADM')")
	@PostMapping("/new")
    public ResponseEntity<SubjectDto> createSubject(@RequestBody SubjectDto SubjectDto, @CurrentUser UserPrincipal currUser) {
        Subject supplier = subjectMapper.toSubject(SubjectDto);
		return ResponseEntity.ok(subjectMapper.toSubjectDto(SubjectService.save(supplier, currUser)));
    }
	
	
//	@PreAuthorize("hasAuthority('ROLE_ADM')")
	@PutMapping("/{id}")
    public ResponseEntity<SubjectDto> updateSubject(@RequestBody SubjectDto SubjectDto, @CurrentUser UserPrincipal currUser) {
        
		Subject supplier = subjectMapper.toSubject(SubjectDto);
		return ResponseEntity.ok(subjectMapper.toSubjectDto(SubjectService.save(supplier, currUser)));
    }
	
//	@PreAuthorize("hasAuthority('ROLE_ADM')")
	@DeleteMapping("/delete/{id}") 
	public void deleteSubject(@PathVariable(value = "id") Long id, @CurrentUser UserPrincipal currUser) {
		try {
			SubjectService.deleteById(id);
		} catch (Exception e)
		{ 
			//throw new RuntimeException("Could not delete category! Record is being referred by other records.");
			System.out.println(e.getMessage());
		} 	
    }


}
