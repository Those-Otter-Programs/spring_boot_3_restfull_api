package com.thoseop.api.members_logs.entity.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/*
 * Indeed, no Record, no final properties - my project, to me it is nonsense...
 */
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class MemberDetailsAuthenticationDTO implements Serializable  {

    private static final long serialVersionUID = -2091571719430151408L;

    // Member Data
    private Long memberId;
    private String memberUsername;
    private Boolean memberAccountNotExpired;
    private Boolean memberCredentialsNotExpired;
    private Boolean memberAccountNotLocked;
    private Boolean memberEnabled;
    // Authentication Failure data
    private Long logId;
    private String logEventResult;
    private String logRemoteIpAddress;
    private String logMessage;
    private Date logTime;
}
