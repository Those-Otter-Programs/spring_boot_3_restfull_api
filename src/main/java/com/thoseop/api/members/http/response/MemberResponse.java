package com.thoseop.api.members.http.response;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
public class MemberResponse extends RepresentationModel<MemberResponse> implements Serializable {

    private static final long serialVersionUID = 7638859550206253144L;

    @Schema( description = "The member's ID", example = "15")
    private Long memberId;
    
    @Schema(description = "The member's username", example = "John Wart")
    private String memberName;
    
    @Schema(description = "The member's email", example = "johnwart@corp.org")
    private String memberEmail;
    
    @Schema(description = "The member's mobile number", example = "(11) 91234-5678")
    private String memberMobileNumber;
    
    @Schema(description = "The member's creation date", example = "1945-09-02")
    private Date memberCreatedAt;
    
    @Schema(description = "The member's authorities", example = "ROLE_ASSOCIATE, VIEWINFOCORP")
    private List<String> memberAuthorities;
    
    @Schema(description = "Message to member", example = "You need to confirm your account")
    private String message;
}
