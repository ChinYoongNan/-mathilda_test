package com.caam.mrs.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.caam.mrs.api.model.Subject;
import com.caam.mrs.api.model.SubjectTeacher;
import com.caam.mrs.api.model.Teacher;
import com.caam.mrs.api.repository.SubjectRepo;
import com.caam.mrs.api.repository.SubjectTeacherRepo;
import com.caam.mrs.api.repository.TeacherRepo;
import com.caam.mrs.api.security.UserPrincipal;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TeacherService {
	@Autowired
	private TeacherRepo teachRepo;
	
	@Autowired
	private SubjectRepo subjectRepo;

	@Autowired
	private SubjectTeacherRepo subjectTeacherRepo;
	
	public Iterable<Teacher> findAll() {
		return teachRepo.findAll();
	}
	
	public Optional<Teacher> findById(Long id) {
        return teachRepo.findById(id);
    }
	
	public Optional<Teacher> findByEmail(String email) {
        return teachRepo.findByEmail(email);
    }

    public Teacher save(Teacher teacher,  UserPrincipal currUser) {
    	if (teacher.getId() != null) {
    		Optional<Teacher> teachOpt = findById(teacher.getId());
    		if (teachOpt.isPresent()) {
        		Optional<Teacher> emailcheckOpt = findByEmail(teacher.getEmail());
        		if(!emailcheckOpt.isPresent()) {
        			Teacher teachOri = teachOpt.get();    			
        			teachOri.setName(teacher.getName());
        			teachOri.setEmail(teacher.getEmail());    
        			teacher = teachRepo.save(teachOri);        			
        		}else {
        			teacher = emailcheckOpt.get();
        		}
    		}
    	} else {
    		Optional<Teacher> emailcheckOpt = findByEmail(teacher.getEmail());
    		if (emailcheckOpt.isPresent()) {
    			teacher = null;
    		}else {
        		teacher = teachRepo.save(teacher);    			
    		}
    	}
    	return teacher;
    }
    
    public Teacher assignSubject(Teacher teacher,  UserPrincipal currUser) {
    	if (teacher.getId() != null) {
    		Optional<Teacher> teachOpt = findById(teacher.getId());
    		if (teachOpt.isPresent()) {		
    			for(Subject subject: teacher.getSubjects()){
    	    		Optional<Subject> subjectOpt = subjectRepo.findById(subject.getId());
    	    		if (!subjectOpt.isPresent()) {
    	    			return null;
    	    		}else {
    	        		Optional<SubjectTeacher> subjectteachOpt = subjectTeacherRepo.findBySubjectTeacher(subject.getId(),teacher.getId());
    	    			if(!subjectteachOpt.isPresent()) {
        	    			SubjectTeacher subjectTeacher = new SubjectTeacher();
        	    			subjectTeacher.setSubjects(subjectOpt.get());
        	    			subjectTeacher.setTeachers(teacher);
        	    			subjectTeacherRepo.save(subjectTeacher);    	    				
    	    			}
    	    		}
    			}
    		}
    	}else {
    		teacher = null;
    	}
    	return teacher;
    }

    public void deleteById(Long id) {
    	teachRepo.deleteById(id);
    }

	public void deleteByList(Iterable<Teacher> teachers) {
		teachRepo.deleteAll(teachers);
	}

}
