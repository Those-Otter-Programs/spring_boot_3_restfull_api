package com.thoseop.api.members.http;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;

import com.thoseop.api.members.http.request.MemberRequest;
import com.thoseop.api.members.http.response.MemberResponse;
import com.thoseop.exception.response.OtterAPIErrorResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "MemberController", description = "Endpoints for InfoController actions requests")
public interface MemberController {

    /**
     * 
     * @param request
     * @return
     */
    @Operation(summary = "Creates a member", 
	    description = "Creates a member", 
	    tags = { "MemberControler" }, 
	    responses = { 
		    @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = MemberResponse.class))),
		    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))),
		    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))),
		    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))) 
    		})
    public ResponseEntity<MemberResponse> createMember(MemberRequest request) throws Exception;

    /**
     * 
     * @param page
     * @param size
     * @param sort
     * @return
     */
    @Operation(summary = "Returns paginated list of members", 
	    description = "Returns paginated list of members", 
	    tags = { "MemberController" }, 
	    responses = { 
		    @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = MemberResponse.class))),
		    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))),
		    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))),
		    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))),
		    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))), 
    		})
    public ResponseEntity<PagedModel<EntityModel<MemberResponse>>> getMembers(
	    Integer page, Integer size, String sort);

    /**
     * 
     * @param memberUsername
     * @return
     */
    @Operation(summary = "Finds a member by its username", 
	    description = "Finds a member by its username", 
	    tags = { "MemberController" }, 
	    responses = {
		    @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = MemberResponse.class))),
		    @ApiResponse(description = "No Content", responseCode = "204", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))),
		    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))),
		    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))),
		    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))),
		    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))), 
    		})
    public ResponseEntity<MemberResponse> getMemberByUsername(String memberUsername);

    /**
     * 
     * @param id
     * @return
     */
    @Operation(summary = "Activates a member", 
	    description = "Activates a member", 
	    tags = { "MemberController" }, 
	    responses = {
		    @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = MemberResponse.class))),
		    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))),
		    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))),
		    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))),
		    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))), 
    		})
    public ResponseEntity<MemberResponse> activateMember(Long id);
    
    /**
     * 
     * @param id
     * @return
     */
    @Operation(summary = "Inactivates a member", 
	    description = "Inactivates a member", 
	    tags = { "MemberController" }, 
	    responses = {
		    @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = MemberResponse.class))),
		    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))),
		    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))),
		    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))),
		    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))), 
    		})
    public ResponseEntity<MemberResponse> inactivateMember(Long id);
}
