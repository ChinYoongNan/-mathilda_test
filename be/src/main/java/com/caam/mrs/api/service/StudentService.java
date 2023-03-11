package com.caam.mrs.api.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.caam.mrs.api.model.Student;
import com.caam.mrs.api.model.Teacher;
import com.caam.mrs.api.repository.StudentRepo;
import com.caam.mrs.api.security.UserPrincipal;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class StudentService {
	@Autowired
	private StudentRepo studentRepo;

	public Iterable<Student> findAll() {
		return studentRepo.findAll();
	}
	
	public Optional<Student> findById(Long id) {
        return studentRepo.findById(id);
    }

	
	public Optional<Student> findByEmail(String email) {
        return studentRepo.findByEmail(email);
    }
    public Student save(Student student,  UserPrincipal currUser) {

    	if (student.getId() != null) {
    		Optional<Student> studentOpt = findById(student.getId());
    		if (studentOpt.isPresent()) {
        		Optional<Student> emailcheckOpt = findByEmail(student.getEmail());
        		if(!emailcheckOpt.isPresent()) {
        			Student studentOri = studentOpt.get();
        			
        			studentOri.setName(student.getName());
        			studentOri.setEmail(student.getEmail());
        			studentOri.setClasses(student.getClasses());    
        			
        			student = studentRepo.save(studentOri);   			
        		}else {
        			student = null;
        		}
    		}
    	} else {
    		Optional<Student> emailcheckOpt = findByEmail(student.getEmail());
    		if(emailcheckOpt.isPresent()) {
    			student = null;
    		}else {
    			student.setType("Internal"); 
    			student = studentRepo.save(student); 			
    		}
    	}
    	return student;
    }

    public void deleteById(Long id) {
    	studentRepo.deleteById(id);
    }

	public void deleteByList(Iterable<Student> students) {
		studentRepo.deleteAll(students);
	}

}
