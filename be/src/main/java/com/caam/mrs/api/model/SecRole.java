package com.caam.mrs.api.model;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

/**
 * Created by Suhaimi AK on 27/03/2019.
 */
@Entity
@Table(name = "sec_roles")
public class SecRole {
	@GenericGenerator(
            name = "secRoleSequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "sec_role_id_seq"),
                    @Parameter(name = "initial_value", value = "1"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    
    @Id
    @GeneratedValue(generator="secRoleSequenceGenerator")
    private Long id;

    @Enumerated(EnumType.STRING)
    @NaturalId
    @Column(length = 60)
    private RoleName name;

    @Column(length = 50)
    private String desc;
    
    public SecRole() {

    }
    
    public SecRole(Long id) {
    	this.id = id;
    }

    public SecRole(RoleName name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoleName getName() {
        return name;
    }

    public void setName(RoleName name) {
        this.name = name;
    }

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}