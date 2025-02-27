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
public class MemberAccountExpiredResponse extends RepresentationModel<MemberAccountExpiredResponse> implements Serializable {

    private static final long serialVersionUID = -6924788852184894782L;

    @Schema(description = "The member's username", example = "johnwart@corp.org")
    private String memberUsername;
    
    @Schema(description = "The member's account is not expired", example = "false|true")
    private Boolean memberAccountNotExpired;
}
