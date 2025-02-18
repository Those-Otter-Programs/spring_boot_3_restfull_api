package com.thoseop.api.members.http.request;

import java.io.Serializable;

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
public class MemberManagePasswordRequest implements Serializable {

    private static final long serialVersionUID = -6153353822460684876L;

    @Schema( description = "The member's username", example = "johnwart@corp.com")
    private String memberUsername;
    
    @Schema(description = "The member's new password", example = "mynewpassword")
    @NotNull
    @Size(min = 8, max = 200)
    private String memberNewPassword;
}
