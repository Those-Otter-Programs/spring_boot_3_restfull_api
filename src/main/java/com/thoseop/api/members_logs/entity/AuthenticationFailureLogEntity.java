package com.thoseop.api.members_logs.entity;

import java.io.Serializable;
import java.util.Date;

import com.thoseop.api.members_logs.entity.enums.AuthStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    @Enumerated(EnumType.STRING)
    private AuthStatus eventResult;
    
    @Column(name = "remote_address")
    private String remoteIpAddress;
    
    @Column(name = "log_message")
    private String logMessage;
    
    @Column(name = "log_time")
    private Date logTime;

    // useful when using threads - log_time and created_at wont be the same
    @Column(name = "created_at")
    private Date createdAt;

    public AuthenticationFailureLogEntity(String username, AuthStatus eventResult, String remoteIpAddress,
	    String logMessage, Date logTime) {
	this.username = username;
	this.eventResult = eventResult;
	this.remoteIpAddress = remoteIpAddress;
	this.logMessage = logMessage;
	this.logTime = logTime;
	this.createdAt = new Date();
    }
}
