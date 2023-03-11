package com.caam.mrs.api.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.caam.mrs.api.model.Subject;
import com.caam.mrs.api.repository.SubjectRepo;
import com.caam.mrs.api.security.UserPrincipal;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SubjectService {
	@Autowired
	private SubjectRepo subjectRepo;

	public Iterable<Subject> findAll() {
		return subjectRepo.findAll();
	}
	
	
	public Optional<Subject> findById(Long id) {
        return subjectRepo.findById(id);
    }

    public Subject save(Subject subject,  UserPrincipal currUser) {
    	if (subject.getId() != null) {
    		Optional<Subject> subOpt = findById(subject.getId());
    		if (subOpt.isPresent()) {
    			Subject subOri = subOpt.get();
    			
    			subOri.setName(subject.getName());
    			
    			subject = subjectRepo.save(subOri);
    		}
    		subOpt = null;
    	} else {
    		subject = subjectRepo.save(subject);
    	}
    	return subject;
    }

    public void deleteById(Long id) {
    	subjectRepo.deleteById(id);
    }

	public void deleteByList(Iterable<Subject> subjects) {
		subjectRepo.deleteAll(subjects);
	}

}
