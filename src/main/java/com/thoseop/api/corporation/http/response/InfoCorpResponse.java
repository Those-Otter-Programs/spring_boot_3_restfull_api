package com.thoseop.api.corporation.http.response;

import java.io.Serializable;

import org.springframework.hateoas.RepresentationModel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
public class InfoCorpResponse extends RepresentationModel<InfoCorpResponse>
	implements Serializable {

    private static final long serialVersionUID = -6329551738003411831L;

    @Schema(description = "Message", example = "All facilies operating - no alerts...")
    private final String message;

    @Schema(description = "Local time", example = "25/01/2025 02:23:00") //
    private final String dateTime;

    @Schema(description = "Location", example = "Toronto - Canada") //
    private final String location;
}
