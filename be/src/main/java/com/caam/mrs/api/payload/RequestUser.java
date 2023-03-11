package com.caam.mrs.api.payload;

import java.util.List;

public class RequestUser {

    private String name;

    private String email;
    private String type;
    private Long role;
    private Long company;
    private String remarks;
    private List<String> userRegions;
    private List<String> userSites;
    
    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Long getRole() {
		return role;
	}
	public void setRole(Long role) {
		this.role = role;
	}
	public Long getCompany() {
		return company;
	}
	public void setCompany(Long company) {
		this.company = company;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public List<String> getUserRegions() {
		return userRegions;
	}
	public void setUserRegions(List<String> userRegions) {
		this.userRegions = userRegions;
	}
	public List<String> getUserSites() {
		return userSites;
	}
	public void setUserSites(List<String> userSites) {
		this.userSites = userSites;
	}
    
    
}
