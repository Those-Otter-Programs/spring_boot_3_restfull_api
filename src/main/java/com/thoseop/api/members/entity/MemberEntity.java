package com.thoseop.api.members.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Accessors(chain = true)
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "members")
public class MemberEntity implements Serializable {

    private static final long serialVersionUID = -6215517080051710605L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", 
	    nullable = false)
    private String name;

    @Column(name = "email", 
	    nullable = false, 
	    unique = true)
    private String email;

    @Column(name = "mobile_number", 
	    nullable = true)
    private String mobileNumber;
    
    @Column(name = "password", 
	    nullable = false)
    private String password;
    
    @Column(name = "account_not_expired", 
	    columnDefinition = "Boolean default false")  
    private Boolean accountNotExpired;
    
    @Column(name = "credentials_not_expired", 
	    columnDefinition = "Boolean default false")  
    private Boolean credentialsNotExpired;
    
    @Column(name = "account_not_locked", 
	    columnDefinition = "Boolean default false")  
    private Boolean accountNotLocked;
    
    @Column(name = "enabled", 
	    columnDefinition = "Boolean default false")  
    private Boolean enabled;

    @Column(name = "created_at",
	    columnDefinition = "Date default CURDATE()")
    @JsonIgnore
    private Date createdAt;

    @Column(name = "updated_at",
	    columnDefinition = "Date default CURDATE()")
    @JsonIgnore
    private Date updatedAt;

    @OneToMany(mappedBy = "member", 
	    cascade = CascadeType.ALL, 
//	    fetch = FetchType.LAZY)
	    fetch = FetchType.EAGER)
    private Set<AuthorityEntity> authorities;

    /**
     * 
     * @param authority
     * @return
     */
    public MemberEntity addAuthority(AuthorityEntity authority) {
	if (this.authorities == null)
	    this.authorities = new HashSet<>();

	authority.setMember(this);
	this.authorities.add(authority);

	return this;
    }

    /**
     * 
     * @param memberAuthorities
     * @return
     */
    public MemberEntity addAuthorities(AuthorityEntity... memberAuthorities) {
	if (this.authorities == null)
	    this.authorities = new HashSet<>();

	for (AuthorityEntity authority : memberAuthorities) {
	    authority.setMember(this);
	    this.authorities.add(authority);
	}
	return this;
    }

    /**
     * 
     * @param memberAuthorities
     * @return
     */
    public MemberEntity addAuthorities(Set<AuthorityEntity> memberAuthorities) {
	if (this.authorities == null)
	    this.authorities = new HashSet<>();

	for (AuthorityEntity authority : memberAuthorities) {
	    authority.setMember(this);
	    this.authorities.add(authority);
	}
	return this;
    }
}
