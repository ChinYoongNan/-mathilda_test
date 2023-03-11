package com.caam.mrs.api.model.dto;

import java.sql.Timestamp;
import java.util.List;

import lombok.Data;

@Data
public class SecUserDto {
	
	private Long uid;
	private String name;
    private String username;
    private String email;
    private String type;
    private Timestamp lastLogin;
    private Boolean enabled;
    
    private String comId;
    private String comName;
    private String comRegno;
    private String comShortName;
    
    private Long siteId;
    private String siteName;

    private Long regionId;
    private String regionName;
    
    private String createdAt;
    private String updatedAt;
    private Long createdBy;
    private String createdByName;
    private Long updatedBy;
    private String updatedByName;
    
    private String tempval;
    private Boolean autoPwd;
    
    private List<String> userRoles;
    private List<String> userRegions;
    private List<String> userSites;
    
    private String roleList;

}
