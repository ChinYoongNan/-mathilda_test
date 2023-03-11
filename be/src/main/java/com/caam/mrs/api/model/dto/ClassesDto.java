package com.caam.mrs.api.model.dto;

import java.util.List;

import com.caam.mrs.api.model.Subject;
import com.caam.mrs.api.model.SubjectTeacher;
import com.caam.mrs.api.model.Teacher;
import com.caam.mrs.api.model.Student;

import lombok.Data;

@Data
public class ClassesDto {

	private Long id;
    private String name;
    private List<SubjectTeacher> subjects;
    private List<Student> students;
    
}
