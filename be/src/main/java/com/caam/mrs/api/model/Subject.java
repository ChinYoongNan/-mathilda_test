// Generated with g9.

package com.caam.mrs.api.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.caam.mrs.api.model.audit.DateAudit;

@Entity
@Table(name="subject")
public class Subject  implements Serializable {

    private static final long serialVersionUID = 1L;

	/** Primary key. */
    protected static final String PK = "comId";

    @GenericGenerator(
            name = "companySequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "company_id_seq"),
                    @Parameter(name = "initial_value", value = "1"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    
    @Id
    @GeneratedValue(generator="companySequenceGenerator")
    @Column(name="id", unique=true, nullable=false, precision=19)
    private Long id;
    
    @Column(name="name", length=100)
    private String name;
    
    
//    @OneToMany(mappedBy="teacher")
//    private Set<Contractor> contractor;
//    
//    @OneToMany(mappedBy="company")
//    private Set<Supplier> supplier;

    /** Default constructor. */
    public Subject() {
        super();
    }


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	/**
     * Compares the key for this instance with another Company.
     *
     * @param other The object to compare to
     * @return True if other object is instance of class Company and the key objects are equal
     */
    private boolean equalKeys(Object other) {
        if (this==other) {
            return true;
        }
        if (!(other instanceof Subject)) {
            return false;
        }
        Subject that = (Subject) other;
        if (this.getId() != that.getId()) {
            return false;
        }
        return true;
    }

    /**
     * Compares this instance with another Company.
     *
     * @param other The object to compare to
     * @return True if the objects are the same
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Subject)) return false;
        return this.equalKeys(other) && ((Subject)other).equalKeys(this);
    }

    /**
     * Returns a hash code for this instance.
     *
     * @return Hash code
     */
    @Override
    public int hashCode() {
        int i;
        int result = 17;
        i = (int)(getId() ^ (getId()>>>32));
        result = 37*result + i;
        return result;
    }

    /**
     * Returns a debug-friendly String representation of this instance.
     *
     * @return String representation of this instance
     */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("[Subject |");
        sb.append(" subId=").append(getId());
        sb.append("]");
        return sb.toString();
    }

    /**
     * Return all elements of the primary key.
     *
     * @return Map of key names to values
     */
    public Map<String, Object> getPrimaryKey() {
        Map<String, Object> ret = new LinkedHashMap<String, Object>(6);
        ret.put("comId", Long.valueOf(getId()));
        return ret;
    }

}
