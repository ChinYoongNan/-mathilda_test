package com.caam.mrs.api.model;

import com.caam.mrs.api.model.audit.DateAudit;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.GenericGenerator;
//import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Suhaimi AK on 27/03/2019.
 */

@Entity
@Table(name = "sec_users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
            "username"
        }),
        @UniqueConstraint(columnNames = {
            "email"
        })
})
public class SecUser extends DateAudit {
	   
    private static final long serialVersionUID = 1L;

	@GenericGenerator(
            name = "secUserSequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "sec_user_id_seq"),
                    @Parameter(name = "initial_value", value = "1"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    
    @Id
    @GeneratedValue(generator="secUserSequenceGenerator")
    private Long id;

    @NotBlank
    @Size(max = 50)
    private String name;

    @NotBlank
    @Size(max = 20)
    private String username;

    //@NaturalId
    @NotBlank
    @Size(max = 50)
    private String email;

    @NotBlank
    @Size(max = 100)
    private String password;
    
    @Column(name="type")
    private String type;
    
    @Column(name="last_login")
    private Timestamp lastLogin;
    
    @Column(name="enabled")
    private Boolean enabled;
    
//    @ManyToOne
//    @JoinColumn(name="com_id")
//    private Company company;
//    
//    @ManyToOne
//    @JoinColumn(name="site_id")
//    private Site site;
//    
//    @ManyToOne
//    @JoinColumn(name="region_id")
//    private Region region;
    
    @Formula("(select string_agg(sr.region_name, ', ')  " + 
    		"from user_region sur " + 
    		"left join region sr on sr.region_id = sur.region_id " + 
    		"where sur.user_id = id)")
    private String regionList;
    
    @ManyToOne
    @JoinColumn(name="created_by")
    private SecUser createdByRef;
    
    @ManyToOne
    @JoinColumn(name="updated_by")
    private SecUser updatedByRef;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "sec_user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<SecRole> roles = new HashSet<>();
    
    
    @Formula("(select string_agg(sr.desc, ', ')  " + 
    		"from sec_user_roles sur " + 
    		"left join sec_roles sr on sr.id = sur.role_id " + 
    		"where sur.user_id = id and sr.name <> 'ROLE_USER')")
    private String roleList;
//    
//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(name = "user_region",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "region_id"))
//    private Set<Region> regions = new HashSet<>();
//    
//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(name = "user_site",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "site_id"))
//    private Set<Site> sites = new HashSet<>();

    public SecUser(Long id) {
    	this.id = id;
    }
    
    public SecUser() {

    }

    public SecUser(String name, String username, String email, String password, String type) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Timestamp getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Timestamp lastLogin) {
		this.lastLogin = lastLogin;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

//	public Company getCompany() {
//		return company;
//	}
//
//	public void setCompany(Company company) {
//		this.company = company;
//	}
//
//	public Site getSite() {
//		return site;
//	}
//
//	public void setSite(Site site) {
//		this.site = site;
//	}
//
//	public Region getRegion() {
//		return region;
//	}
//
//	public void setRegion(Region region) {
//		this.region = region;
//	}

	public SecUser getCreatedByRef() {
		return createdByRef;
	}

	public void setCreatedByRef(SecUser createdByRef) {
		this.createdByRef = createdByRef;
	}

	public SecUser getUpdatedByRef() {
		return updatedByRef;
	}

	public void setUpdatedByRef(SecUser updatedByRef) {
		this.updatedByRef = updatedByRef;
	}

	public Set<SecRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<SecRole> roles) {
        this.roles = roles;
    }

	public String getRoleList() {
		return roleList;
	}

	public void setRoleList(String roleList) {
		this.roleList = roleList;
	}
	
	public String getRegionList() {
		return regionList;
	}

	public void setRegionList(String regionList) {
		this.regionList = regionList;
	}
}
