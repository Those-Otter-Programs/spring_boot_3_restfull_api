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
public class MemberUpdatePasswordRequest implements Serializable {

    private static final long serialVersionUID = 8045254557335516777L;

    @Schema(description = "The member's new password", example = "mynewpassword")
    @NotNull
    @Size(min = 8, max = 200)
    private String newPassword;
}
