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

import com.caam.mrs.api.model.Classes;
import com.caam.mrs.api.model.Teacher;
import com.caam.mrs.api.model.dto.ClassesMapper;
import com.caam.mrs.api.model.dto.SubjectTeacherDto;
import com.caam.mrs.api.model.dto.SubjectTeacherMapper;
import com.caam.mrs.api.model.dto.ClassesDto;
import com.caam.mrs.api.model.dto.ClassesMapper;
import com.caam.mrs.api.security.CurrentUser;
import com.caam.mrs.api.security.UserPrincipal;
import com.caam.mrs.api.service.ClassesService;
import com.caam.mrs.api.service.ClassesService;

import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;

/**
 * Created by Suhaimi AK on 17/04/2019.
 */

//@Slf4j
@RequiredArgsConstructor

@RestController
@RequestMapping("/api/classes/")
@ComponentScan(basePackageClasses=ClassesMapper.class)
public class CommonController {
	
	@Autowired
	private final ClassesService ClassesService;
	
	@Autowired
	private final ClassesMapper classesMapper;
	

	@Autowired
	private final SubjectTeacherMapper subjectTeacherMapper;
	
	
	@GetMapping("list")
    public ResponseEntity<Iterable<ClassesDto>> findAll() {
        return ResponseEntity.ok(classesMapper.toClassesDtos(ClassesService.findAll()));
    }
	
	@GetMapping("subject/list")
    public ResponseEntity<Iterable<SubjectTeacherDto>> findAllSubeject() {
        return ResponseEntity.ok(subjectTeacherMapper.toSubjectTeacherDtos(ClassesService.findAllSubject()));
    }
	
	
	@GetMapping("{id}")
    public ResponseEntity<ClassesDto> getClasses(@PathVariable("id") Long id) {
		Optional<Classes> classesOpt = ClassesService.findById(id);
		if (classesOpt.isPresent())      
 			return ResponseEntity.ok(classesMapper.toClassesDto(classesOpt.get()));
		else
			return null;
    }
	
//	@PreAuthorize("hasAuthority('ROLE_ADM')")
	@PostMapping("new")
    public ResponseEntity<ClassesDto> createClasses(@RequestBody ClassesDto ClassesDto, @CurrentUser UserPrincipal currUser) {
		Classes classes = classesMapper.toClasses(ClassesDto);
		Classes newclasses = classesMapper.toClasses(ClassesDto);
		classes.setSubjects(null);
		classes = ClassesService.save(classes, currUser);
		newclasses.setId(classes.getId());
		return ResponseEntity.ok(classesMapper.toClassesDto(ClassesService.assignSubject(newclasses, currUser)));
    }
	
}
