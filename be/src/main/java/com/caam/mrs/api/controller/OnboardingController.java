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

import com.caam.mrs.api.model.Student;
import com.caam.mrs.api.model.Teacher;
import com.caam.mrs.api.model.dto.StudentDto;
import com.caam.mrs.api.model.dto.TeacherDto;
import com.caam.mrs.api.model.dto.TeacherMapper;
import com.caam.mrs.api.model.dto.StudentMapper;
import com.caam.mrs.api.security.CurrentUser;
import com.caam.mrs.api.security.UserPrincipal;
import com.caam.mrs.api.service.TeacherService;
import com.caam.mrs.api.service.StudentService;

import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;

/**
 * Created by Suhaimi AK on 17/04/2019.
 */

//@Slf4j
@RequiredArgsConstructor

@RestController
@RequestMapping("/api/onboarding")
@ComponentScan(basePackageClasses=TeacherMapper.class)
public class OnboardingController {
	
	@Autowired
	private final TeacherService TeacherService;
	
	@Autowired
	private final TeacherMapper teacherMapper;
	

	@Autowired
	private final StudentService StudentService;
	
	@Autowired
	private final StudentMapper studentMapper;
	
	@GetMapping("/teacher/list")
    public ResponseEntity<Iterable<TeacherDto>> findAllTeacher() {
        return ResponseEntity.ok(teacherMapper.toTeacherDtos(TeacherService.findAll()));
    }
	
	@GetMapping("/student/list")
    public ResponseEntity<Iterable<StudentDto>> findAllStudent() {
        return ResponseEntity.ok(studentMapper.toStudentDtos(StudentService.findAll()));
    }
	
	@GetMapping("/teacher/{id}")
    public ResponseEntity<TeacherDto> getTeacher(@PathVariable("id") Long id) {
		Optional<Teacher> teacherOpt = TeacherService.findById(id);
		if (teacherOpt.isPresent())      
 			return ResponseEntity.ok(teacherMapper.toTeacherDto(teacherOpt.get()));
		else
			return null;
    }
	
	@PostMapping("/student/new")
    public ResponseEntity<StudentDto> createStudent(@RequestBody StudentDto StudentDto, @CurrentUser UserPrincipal currUser) {
        Student student = studentMapper.toStudent(StudentDto);
		return ResponseEntity.ok(studentMapper.toStudentDto(StudentService.save(student, currUser)));
    }	
	
//	@PreAuthorize("hasAuthority('ROLE_ADM')")
	@PutMapping("/student")
    public ResponseEntity<StudentDto> updateStudent(@RequestBody StudentDto StudentDto, @CurrentUser UserPrincipal currUser) {
		Student student = studentMapper.toStudent(StudentDto);
		return ResponseEntity.ok(studentMapper.toStudentDto(StudentService.save(student, currUser)));
    }
	
//	@PreAuthorize("hasAuthority('ROLE_ADM')")
	@DeleteMapping("/student/delete/{id}") 
	public void deleteStudent(@PathVariable(value = "id") Long id, @CurrentUser UserPrincipal currUser) {
		try {
			StudentService.deleteById(id);
		} catch (Exception e)
		{ 
			//throw new RuntimeException("Could not delete category! Record is being referred by other records.");
			System.out.println(e.getMessage());
		} 	
    }
	
//	@PreAuthorize("hasAuthority('ROLE_ADM')")
	@PostMapping("/teacher/new")
    public ResponseEntity<TeacherDto> createTeacher(@RequestBody TeacherDto TeacherDto, @CurrentUser UserPrincipal currUser) {
        Teacher teacher = teacherMapper.toTeacher(TeacherDto);
        Teacher newteacher = teacherMapper.toTeacher(TeacherDto);
        teacher.setSubjects(null);
        teacher = TeacherService.save(teacher, currUser);
        newteacher.setId(teacher.getId());
		return ResponseEntity.ok(teacherMapper.toTeacherDto(TeacherService.assignSubject(newteacher, currUser)));
    }
	
	
//	@PreAuthorize("hasAuthority('ROLE_ADM')")
	@PutMapping("/teacher")
    public ResponseEntity<TeacherDto> updateTeacher(@RequestBody TeacherDto TeacherDto, @CurrentUser UserPrincipal currUser) {
        
		Teacher teacher = teacherMapper.toTeacher(TeacherDto);
		return ResponseEntity.ok(teacherMapper.toTeacherDto(TeacherService.save(teacher, currUser)));
    }
	
//	@PreAuthorize("hasAuthority('ROLE_ADM')")
	@DeleteMapping("/teacher/delete/{id}") 
	public void deleteTeacher(@PathVariable(value = "id") Long id, @CurrentUser UserPrincipal currUser) {
		try {
			TeacherService.deleteById(id);
		} catch (Exception e)
		{ 
			//throw new RuntimeException("Could not delete category! Record is being referred by other records.");
			System.out.println(e.getMessage());
		} 	
    }

}
