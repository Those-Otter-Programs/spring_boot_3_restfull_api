package com.thoseop.api.members.http;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;

import com.thoseop.api.members.http.request.MemberCreateRequest;
import com.thoseop.api.members.http.request.MemberManagePasswordRequest;
import com.thoseop.api.members.http.request.MemberUpdatePasswordRequest;
import com.thoseop.api.members.http.request.MemberUpdateRequest;
import com.thoseop.api.members.http.response.MemberAccountLockedResponse;
import com.thoseop.api.members.http.response.MemberCreatedResponse;
import com.thoseop.api.members.http.response.MemberDetailsResponse;
import com.thoseop.api.members.http.response.MemberEnabledResponse;
import com.thoseop.api.members.http.response.MemberResponse;
import com.thoseop.exception.response.OtterAPIErrorResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

@Tag(name = "MemberController", description = "Endpoints for InfoController actions requests")
public interface MemberController {

    /**
     * 
     * @param request
     * @return
     */
    @Operation(summary = "Returns a member's JWT Token", 
	    description = "Returns a member's JWT Token", 
	    tags = { "MemberController" }, 
	    responses = { 
		    @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = Model.class))),
		    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))),
		    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))),
		    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))) 
    		})
    public ResponseEntity<Model> memberToken(HttpServletResponse response, Model model);

    /**
     * 
     * @param request
     * @return
     */
    @Operation(summary = "Creates a member", 
	    description = "Creates a member", 
	    tags = { "MemberController" }, 
	    responses = { 
		    @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = MemberCreatedResponse.class))),
		    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))),
		    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))),
		    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))) 
    		})
    public ResponseEntity<MemberCreatedResponse> createMember(MemberCreateRequest memberRequest) throws Exception;

    /**
     * 
     * @param memberRequest
     * @return
     */
    @Operation(summary = "Update a member", 
	    description = "Update a member", 
	    tags = { "MemberController" }, 
	    responses = { 
		    @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = MemberResponse.class))),
		    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))),
		    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))),
		    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))),
		    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))) 
    		})
    public ResponseEntity<MemberResponse> updateMember(MemberUpdateRequest memberRequest) throws Exception;

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
		    @ApiResponse(description = "Success", responseCode = "200", content = 
			{@Content(array = @ArraySchema(schema = @Schema(implementation = MemberResponse.class)))}),
		    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))),
		    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))),
		    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))),
		    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))), 
    		})
    public ResponseEntity<PagedModel<EntityModel<MemberResponse>>> getMembers(
	    Integer page, Integer size, String sortDir, String sortBy) throws Exception;

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
     * @param memberUsername
     * @return
     */
    @Operation(summary = "Finds a member full details by its username", 
	    description = "Finds a member full details by its username", 
	    tags = { "MemberController" }, 
	    responses = {
		    @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = MemberDetailsResponse.class))),
		    @ApiResponse(description = "No Content", responseCode = "204", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))),
		    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))),
		    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))),
		    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))),
		    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))), 
    		})
    public ResponseEntity<MemberDetailsResponse> getMemberFullDetailsByUsername(String memberUsername);

    /**
     * 
     * @param memberUsername
     * @return
     */
    @Operation(summary = "Shows the authenticated member details", 
	    description = "Shows the authenticated member details", 
	    tags = { "MemberController" }, 
	    responses = {
		    @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = MemberResponse.class))),
		    @ApiResponse(description = "No Content", responseCode = "204", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))),
		    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))),
		    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))),
		    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))),
		    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))), 
    		})
    public ResponseEntity<MemberResponse> getMineDetails();

    /**
     * 
     * @param request
     * @return
     */
    @Operation(summary = "Updates an authenticated member's own password", 
	    description = "Updates an authenticated member's own password", 
	    tags = { "MemberController" }, 
	    responses = {
		    @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = MemberResponse.class))),
		    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))),
		    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))),
		    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))),
		    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))), 
    		})
    public ResponseEntity<MemberResponse> updateMemberPassword(MemberUpdatePasswordRequest request,Authentication userAuth);


    /**
     * 
     * @param request
     * @return
     */
    @Operation(summary = "Used by admin users to manage a member's password", 
	    description = "Used by admin users to manage a member's password", 
	    tags = { "MemberController" }, 
	    responses = {
		    @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = MemberResponse.class))),
		    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))),
		    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))),
		    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))),
		    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))), 
    		})
    public ResponseEntity<MemberResponse> manageMemberPassword(MemberManagePasswordRequest request);

    /**
     * 
     * @param id
     * @return
     */
    @Operation(summary = "Activates a member", 
	    description = "Activates a member - it has to be run after member creation", 
	    tags = { "MemberController" }, 
	    responses = {
		    @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = MemberEnabledResponse.class))),
		    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))),
		    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))),
		    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))),
		    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))), 
    		})
    public ResponseEntity<MemberEnabledResponse> activateMember(Long id) throws Exception;
    
    /**
     * 
     * @param id
     * @return
     */
    @Operation(summary = "Inactivates a member", 
	    description = "Inactivates a member", 
	    tags = { "MemberController" }, 
	    responses = {
		    @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = MemberEnabledResponse.class))),
		    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))),
		    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))),
		    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))),
		    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))), 
    		})
    public ResponseEntity<MemberEnabledResponse> inactivateMember(Long id) throws Exception;

    /**
     * 
     * @param id
     * @return
     */
    @Operation(summary = "Lock member's account", 
	    description = "Lock member's account", 
	    tags = { "MemberController" }, 
	    responses = {
		    @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = MemberAccountLockedResponse.class))),
		    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))),
		    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))),
		    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))),
		    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))), 
    		})
    public ResponseEntity<MemberAccountLockedResponse> lockMemberAccount(Long id) throws Exception;
    
    /**
     * 
     * @param id
     * @return
     */
    @Operation(summary = "Unlock member's account", 
	    description = "Unlock member's account", 
	    tags = { "MemberController" }, 
	    responses = {
		    @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = MemberAccountLockedResponse.class))),
		    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))),
		    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))),
		    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))),
		    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content(schema = @Schema(implementation = OtterAPIErrorResponse.class))), 
    		})
    public ResponseEntity<MemberAccountLockedResponse> unlockMemberAccount(Long id) throws Exception;
}
