package com.thoseop.api.members_logs.http.response;

import java.io.Serializable;
import java.util.Date;

import org.springframework.hateoas.RepresentationModel;

import io.swagger.v3.oas.annotations.media.Schema;
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
@EqualsAndHashCode(callSuper = false)
@ToString
public class AuthenticationFailureLogResponse extends RepresentationModel<AuthenticationFailureLogResponse>
	implements Serializable {
    
    private static final long serialVersionUID = 5374414034278764314L;

    @Schema( description = "The log's id on the database", example = "1")
    private Long logId;
    
    @Schema( description = "The member's username", example = "johnwart")
    private String logUsername;
    
    @Schema( description = "The result of authentication", example = "SUCCESS/FAILURE")
    private String logEventResult;
    
    @Schema( description = "The remote ip address of the member", example = "0.0.0.0")
    private String logRemoteIpAddress;
    
    @Schema( description = "The log's error message", example = "User account is locked")
    private String logMessage;
    
    @Schema( description = "The time when the authentication was triggered", example = "2025-02-25 20:46:23.932")
    private Date logAuthTime;

    @Schema( description = "The time when the database registry was created", example = "2025-02-25 20:46:23.932")
    private Date logCreatedAt;
}
