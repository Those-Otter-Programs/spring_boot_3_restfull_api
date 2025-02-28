package com.thoseop.api.members_logs.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "authentication_failure_logs")
public class AuthenticationFailureLogEntity implements Serializable {

    private static final long serialVersionUID = -1330390085923329841L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "username")
    private String username;
    
    @Column(name = "event_result")
    private String eventResult;
    
    @Column(name = "remote_address")
    private String remoteIpAddress;
    
    @Column(name = "message")
    private String message;
    
    @Column(name = "auth_time")
    private Date authTime;

    // useful when using threads - log_time and created_at wont be the same
    @Column(name = "created_at")
    private Date createdAt;

    public AuthenticationFailureLogEntity(String username, String eventResult, String remoteIpAddress,
	    String message, Date authTime) {
	this.username = username;
	this.eventResult = eventResult;
	this.remoteIpAddress = remoteIpAddress;
	this.message = message;
	this.authTime = authTime;
	this.createdAt = new Date();
    }
}
