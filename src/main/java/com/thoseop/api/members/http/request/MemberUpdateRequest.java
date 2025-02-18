package com.thoseop.api.members.http.request;

import java.io.Serializable;
import java.util.Set;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
@EqualsAndHashCode
@ToString
public class MemberUpdateRequest implements Serializable {

    private static final long serialVersionUID = -1377811706321506680L;

    @Schema( description = "The member's ID", example = "15")
    private Long memberId;
    
    @Schema(description = "The member's username", example = "John Wart")
    @NotNull
    @Size(min = 2, max = 100)
    private String memberName;
    
    @Schema(description = "The member's email", example = "johnwart@corp.org")
    @NotNull
    @Size(min = 4, max = 45)
    private String memberEmail;
    
    @Schema(description = "The member's mobile number", example = "(11) 91234-5678")
    @NotNull
    @Size(min = 4, max = 45)
    private String memberMobileNumber;
    
    @Schema(description = "The member's authorities", example = "ROLE_ASSOCIATE, VIEWINFOCORP")
    @NotNull
    @Size(min = 2, max = 200)
    private Set<String> memberAuthorities;
}
