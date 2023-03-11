package com.caam.mrs.api.model.dto;

import java.sql.Timestamp;
import java.util.List;

import com.caam.mrs.api.model.Classes;

import lombok.Data;

@Data
public class StudentDto {

	private Long id;
    private String name;
    private String email;
    private Classes classes;
    
}
