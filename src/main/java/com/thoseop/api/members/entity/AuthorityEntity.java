package com.thoseop.api.members.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="authorities")
public class AuthorityEntity implements Serializable {

    private static final long serialVersionUID = 3354226995598969830L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name",
	    nullable = false)
    private String name;

    @ManyToOne(
	    fetch = FetchType.LAZY,
            cascade = { 
		CascadeType.DETACH, 
		CascadeType.MERGE, 
		CascadeType.PERSIST,
		CascadeType.REFRESH })
    @JoinColumn(name = "member_id")
    private MemberEntity member;
    
    @Column(name = "created_at")
    private Date createdAt;
}
