package com.thoseop.api.members.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
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

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "mobile_number")
    private String mobileNumber;
    
    @Column(name = "password")
    private String password;

    @Column(name = "created_at")
    @JsonIgnore
    private Date createdAt;

    @Column(name = "updated_at")
    @JsonIgnore
    private Date updatedAt;

    @OneToMany(mappedBy = "member", fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<AuthorityEntity> authorities;
}
