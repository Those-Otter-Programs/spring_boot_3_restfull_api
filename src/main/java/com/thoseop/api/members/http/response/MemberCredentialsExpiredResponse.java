package com.thoseop.api.members.http.response;

import java.io.Serializable;

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
public class MemberCredentialsExpiredResponse extends RepresentationModel<MemberCredentialsExpiredResponse> implements Serializable {

    private static final long serialVersionUID = -2237225941192188251L;

    @Schema(description = "The member's username", example = "johnwart@corp.org")
    private String memberUsername;
    
    @Schema(description = "If member's credentials are not expired", example = "false|true")
    private Boolean memberCredentialsNotExpired;
}
