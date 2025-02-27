package com.thoseop.api.members_logs.http;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;

import com.thoseop.api.members_logs.http.response.AuthenticationFailureLogResponse;
import com.thoseop.exception.response.OtterAPIErrorResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "AuthenticationFailureLogController", description = "Endpoints for AuthenticationFailureLogController actions requests")
public interface AuthenticationFailureLogController {

    /**
     * 
     * @param username
     * @param page
     * @param size
     * @param sort
     * @return
     */
    @Operation(summary = "Returns paginated list of member's authentication failure logs", 
	    description = "Returns paginated list of membera's authentication failure logs", 
	    tags = { "AuthenticationFailureLogController" }, 
	    responses = {
		    @ApiResponse(description = "Success", responseCode = "200", content = 
			{@Content(array = @ArraySchema(schema = @Schema(implementation = AuthenticationFailureLogResponse.class)))}),
		    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))),
		    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))),
		    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))),
		    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))), 
    		})
    public ResponseEntity<PagedModel<EntityModel<AuthenticationFailureLogResponse>>> getMemberAuthenticationFailures(
	    String username, Integer page, Integer size, String sort); 
    
    /**
     * 
     * @param id
     * @return
     */
    @Operation(summary = "Finds log by its id", 
	    description = "Finds log by its id", 
	    tags = { "AuthenticationFailureLogController" }, 
	    responses = {
		    @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = AuthenticationFailureLogResponse.class))),
		    @ApiResponse(description = "No Content", responseCode = "204", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))),
		    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))),
		    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))),
		    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))),
		    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))), 
    		})
    public ResponseEntity<AuthenticationFailureLogResponse> getLogById(Long id);
}
