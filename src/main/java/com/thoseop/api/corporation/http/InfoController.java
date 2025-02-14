package com.thoseop.api.corporation.http;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

import com.thoseop.api.corporation.http.response.InfoCorpResponse;
import com.thoseop.exception.response.OtterAPIErrorResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "InfoController", description = "Endpoints for InfoController actions requests")
public interface InfoController {

    @Operation(summary = "Info page - it displays public information data.", 
	    description = "Info page - it displays public information data.", 
	    tags = {"InfoController"},
	    responses = {
		    @ApiResponse(description = "Success", responseCode = "200"),
                    @ApiResponse(description = "Bad Credentials", responseCode = "401", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))),
		    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class)))
	    }) 
    public ResponseEntity<Model> info(Model model);

    @Operation(summary = "Info page - it displays corporative information data.", 
	    description = "Info page - it displays corporative information data.", 
	    tags = {"InfoController"},
	    responses = {
		    @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = InfoCorpResponse.class))),
                    @ApiResponse(description = "Bad Credentials", responseCode = "401", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))),
		    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class)))
	    }) 
    public ResponseEntity<InfoCorpResponse> infoCorp();
}
