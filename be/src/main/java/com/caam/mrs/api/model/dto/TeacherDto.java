package com.caam.mrs.api.model.dto;

import java.sql.Timestamp;
import java.util.List;

import com.caam.mrs.api.model.Subject;

import lombok.Data;

@Data
public class TeacherDto {

	private Long id;
    private String name;
    private String email;
    private String teachingsubject;
    private List<Subject> subjects;
}
