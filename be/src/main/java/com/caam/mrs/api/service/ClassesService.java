package com.caam.mrs.api.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.caam.mrs.api.model.Classes;
import com.caam.mrs.api.model.ClassesSubject;
import com.caam.mrs.api.model.Subject;
import com.caam.mrs.api.model.SubjectTeacher;
import com.caam.mrs.api.model.Teacher;
import com.caam.mrs.api.repository.ClassRepo;
import com.caam.mrs.api.repository.ClassesSubjectRepo;
import com.caam.mrs.api.repository.SubjectTeacherRepo;
import com.caam.mrs.api.security.UserPrincipal;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ClassesService {
	@Autowired
	private ClassRepo classRepo;
	

	@Autowired
	private ClassesSubjectRepo classesSubjectRepo;
	

	@Autowired
	private SubjectTeacherRepo subjectTeacherRepo;

	public Iterable<Classes> findAll() {
		return classRepo.findAll();
	}
	
	public Iterable<SubjectTeacher> findAllSubject() {
		return subjectTeacherRepo.findAllSubjectTeacher();
	}
	
	
	public Optional<Classes> findById(Long id) {
        return classRepo.findById(id);
    }    
    
    public Classes save(Classes classes,  UserPrincipal currUser) {
    	if (classes.getId() != null) {
    		Optional<Classes> classOpt = findById(classes.getId());
    		if (classOpt.isPresent()) {
    			Classes subOri = classOpt.get();
    			
    			subOri.setName(classes.getName());
    			
    			classes = classRepo.save(subOri);
    		}
    	} else {
    		
    		classes = classRepo.save(classes);
    	}
    	return classes;
    }
    
    public Classes assignSubject(Classes classes, UserPrincipal currUser) {
    	if (classes.getId() != null) {
    		Optional<Classes> classOpt = findById(classes.getId());
    		if (classOpt.isPresent()) {			
    			for(SubjectTeacher subject: classes.getSubjects()){
    	    		Optional<SubjectTeacher> subjectOpt = subjectTeacherRepo.findById(subject.getId());
    	    		if (!subjectOpt.isPresent()) {
    	    			return null;
    	    		}else {
    	        		Optional<ClassesSubject> classessubjectOpt = classesSubjectRepo.findByClassesSubject(subject.getId(),classes.getId());
    	    			if(!classessubjectOpt.isPresent()) {
        	    			ClassesSubject classesSubject = new ClassesSubject();
        	    			classesSubject.setSubjects(subjectOpt.get());
        	    			classesSubject.setClasses(classes);
        	    			classesSubjectRepo.save(classesSubject);  	    				
    	    			}
    	    		}
    			}
    		}
    	}
    	return classes;
    }

    public void deleteById(Long id) {
    	classRepo.deleteById(id);
    }

	public void deleteByList(Iterable<Classes> classes) {
		classRepo.deleteAll(classes);
	}

}
