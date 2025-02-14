package com.thoseop.exception.response;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OtterAPIErrorResponse implements Serializable {

    private static final long serialVersionUID = -7047378989332913063L;

    @Schema(description = "Time that the error happened") //
    private String timestamp;

    @Schema(description = "Error status") //
    private int status;

    @Schema(description = "Type of Error") //
    private String error;

    @Schema(description = "Error message") //
    private String message;

    @Schema(description = "The url which triggered the error") //
    private String path;
}
