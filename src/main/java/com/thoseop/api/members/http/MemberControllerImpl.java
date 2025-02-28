package com.thoseop.api.members.http;

import static com.thoseop.config.OtterWebMvcConfig._APPLICATION_YAML_VALUE;
import static com.thoseop.utils.http.HttpPaginatedUtil.filteringSortBy;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.thoseop.api.members.entity.MemberEntity;
import com.thoseop.api.members.entity.enums.MemberEnabledStatus;
import com.thoseop.api.members.entity.enums.MemberLockedStatus;
import com.thoseop.api.members.http.request.MemberCreateRequest;
import com.thoseop.api.members.http.request.MemberManagePasswordRequest;
import com.thoseop.api.members.http.request.MemberUpdatePasswordRequest;
import com.thoseop.api.members.http.request.MemberUpdateRequest;
import com.thoseop.api.members.http.response.MemberAccountLockedResponse;
import com.thoseop.api.members.http.response.MemberCreatedResponse;
import com.thoseop.api.members.http.response.MemberDetailsResponse;
import com.thoseop.api.members.http.response.MemberEnabledResponse;
import com.thoseop.api.members.http.response.MemberResponse;
import com.thoseop.api.members.service.MemberService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/member/v1")
@RequiredArgsConstructor
public class MemberControllerImpl implements MemberController {

    private final MemberService memberService;
    private final PagedResourcesAssembler<MemberResponse> membersPage_Assembler;

    /*  ============= cURL ==============
      	# BASH:

        # get the JWT token and stores it in a bash variable:
        myJWTToken=`curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' -L -X GET 'http://localhost:8080/api/member/v1/token' | jq -r '.token'`
        
        # run cURL using the variable as the authorization token:
        
        # ------------- JSON --------------
        curl -s -u 'ayrton.senna@bravo.com:ayrton_pass'
        	-L -X GET 'http://localhost:8080/api/member/v1/token' | jq
        # Base64 encoded credentials:
        curl -s -u 'YXlydG9uLnNlbm5hQGJyYXZvLmNvbTpheXJ0b25fcGFzcw=='
        	-L -X GET 'http://localhost:8080/api/member/v1/token' | jq

        # ------------- XML --------------
        curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' -H 'Accept: application/xml'
        	-L -X GET 'http://localhost:8080/api/member/v1/token' | xmllint --format -
        
        # ------------- YAML --------------
        curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' -H 'Accept: application/x-yaml'\
		-L -X GET 'http://localhost:8080/api/member/v1/token' | yq
        
        # ------------- CORS --------------
        curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' -H 'Origin: http://localhost:3000'
        	-L -X GET 'http://localhost:8080/api/member/v1/token' | jq
    */
    @Override
    @GetMapping(value = "/token",
	    produces = { _APPLICATION_YAML_VALUE,
		    MediaType.APPLICATION_JSON_VALUE, 
		    MediaType.APPLICATION_XML_VALUE })
    public @ResponseBody ResponseEntity<Model> memberToken(HttpServletResponse response, Model model) {
	
	model.addAttribute("token", response.getHeader("Authorization"));
	
	return ResponseEntity.ok(model);
    }

    /*  ============= cURL ==============
	# BASH:
	myJWTToken=`curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' -L -X GET 'http://localhost:8080/api/member/v1/token' | jq -r '.token'`

	# ------------- JSON (request and response) --------------
       	curl -s -H "Authorization: $myJWTToken" -H 'Content-Type: application/json' \
		-L -X POST 'http://localhost:8080/api/member/v1/member-create' \
		-d '{
			"memberName":"Rubens Barrichello", 
			"memberEmail":"rubens.barrichello@bravo.com",
		     	"memberMobileNumber":"(11) 98765-4321", 
		     	"memberPassword": "barrichello_pass",
		     	"memberAuthorities": [
		     		"ROLE_ADMIN"
		     	]
		     }' | jq

       	# -------------- XML (request and response) --------------
       	curl -s -H "Authorization: $myJWTToken" -H 'Accept: application/xml' -H 'Content-Type: application/xml' \
       		-L -X POST 'http://localhost:8080/api/member/v1/member-create' \
       		-d '<MemberCreateRequest>
                        <memberName>Emerson Fittipaldi</memberName>
                        <memberEmail>emerson.fittipaldi@bravo.com</memberEmail>
                        <memberMobileNumber>(11) 98765-4321</memberMobileNumber>
                        <memberPassword>fittipaldi_pass</memberPassword>
                        <memberAuthorities>
                                <memberAuthorities>ROLE_ADMIN</memberAuthorities>
                        </memberAuthorities>
                   </MemberCreateRequest>' | xmllint --format - 
       
        # ------------- YAML (request and response) --------------
	curl -s -H "Authorization: $myJWTToken" -H 'Accept: application/x-yaml' -H 'Content-Type: application/x-yaml' \
       		-L -X POST 'http://localhost:8080/api/member/v1/member-create' \
       		-d '---
                    memberName: "Nelson Piquet"
                    memberEmail: "nelson.piquet@bravo.com"
                    memberMobileNumber: "(11) 98765-4321"
                    memberPassword: "piquet_pass"
                    memberAuthorities:
                        - "ROLE_ADMIN"' | yq

        # ------------- CORS - origin filter and JSON request / response -------------
        curl -s -H "Authorization: $myJWTToken" -H 'Origin: http://localhost:3000' \
        	-H 'Content-Type: application/json' \
        	-L -X POST 'http://localhost:8080/api/member/v1/member-create' \
        	-d '{
        		"memberName":"Felipe Massa", 
        		"memberEmail":"felipe.massa@bravo.com",
	     		"memberMobileNumber":"(11) 98765-4321", 
	     		"memberPassword": "massa_pass",
	     		"memberAuthorities": [
	     			"ROLE_ADMIN"
	     		]
	     	    }' | jq
     */
    @Override
    @PostMapping(value = "/member-create",
	    consumes = { _APPLICATION_YAML_VALUE,
		    MediaType.APPLICATION_JSON_VALUE, 
		    MediaType.APPLICATION_XML_VALUE }, 
	    produces = { _APPLICATION_YAML_VALUE,
		    MediaType.APPLICATION_JSON_VALUE, 
		    MediaType.APPLICATION_XML_VALUE })
    public @ResponseBody ResponseEntity<MemberCreatedResponse> createMember(@RequestBody @Valid MemberCreateRequest request) 
	    throws Exception {

        log.info("MemberController - creating member");

	MemberCreatedResponse savedMember;
        try {
            savedMember = this.memberService.saveMember(request);
            savedMember.add(linkTo(methodOn(MemberControllerImpl.class)
        	    .getMemberByUsername(savedMember.getMemberEmail())).withSelfRel());
        } catch (Exception ex) {
            throw new Exception(ex);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMember);
    }

    /*  # ============= cURL ==============
      	# BASH:
	myJWTToken=`curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' -L -X GET 'http://localhost:8080/api/member/v1/token' | jq -r '.token'`

       	# ------------- JSON - PAGINATED --------------
       	curl -s -H "Authorization: $myJWTToken" -L -X GET 'http://localhost:8080/api/member/v1/list?page=0&size=8&sortDir=desc&sortBy=memberId' | jq
       	curl -s -H "Authorization: $myJWTToken" -L -X GET 'http://localhost:8080/api/member/v1/list?page=0&size=8&sortDir=desc' | jq
       	curl -s -H "Authorization: $myJWTToken" -L -X GET 'http://localhost:8080/api/member/v1/list?page=0&size=8' | jq
       	curl -s -H "Authorization: $myJWTToken" -L -X GET 'http://localhost:8080/api/member/v1/list?page=0' | jq
       	curl -s -H "Authorization: $myJWTToken" -L -X GET 'http://localhost:8080/api/member/v1/list' | jq

       	# -------------- XML - PAGINATED --------------
       	curl -s -H "Authorization: $myJWTToken" -H 'Accept: application/xml' \
       		-L -X GET 'http://localhost:8080/api/member/v1/list?page=0&size=8&sortDir=desc&sortBy=memberId' | xmllint --format -
       
       	# ------------- YAML - PAGINATED --------------
       	curl -s -H "Authorization: $myJWTToken" -H 'Accept: application/x-yaml' \
       		-L -X GET 'http://localhost:8080/api/member/v1/list?page=0&size=8&sortDir=desc&sortBy=memberId' | yq

       	# ------------- CORS - PAGINATED --------------
       	curl -s -H "Authorization: $myJWTToken" -H 'Origin: http://localhost:3000' \
       		-L -X GET 'http://localhost:8080/api/member/v1/list?page=0&size=8&sortDir=desc&sortBy=memberId' | jq
     */
    @Override
    @GetMapping(value = "/list", 
	    produces = { _APPLICATION_YAML_VALUE, 
		    MediaType.APPLICATION_JSON_VALUE,
		    MediaType.APPLICATION_XML_VALUE })
    public @ResponseBody ResponseEntity<PagedModel<EntityModel<MemberResponse>>> getMembers(
	    @RequestParam(defaultValue = "0") Integer page, 
	    @RequestParam(defaultValue = "8") Integer size,
	    @RequestParam(defaultValue = "asc") String sortDir,
	    @RequestParam(defaultValue = "memberEmail") String sortBy
	    ) throws Exception { 	
	
        log.info("MemberController - reading all members");
        
        sortBy = filteringSortBy(sortBy, "member", new MemberEntity());
        
	Direction sortDirection = "desc".equalsIgnoreCase(sortDir) ? Direction.DESC : Direction.ASC;
	Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
	Page<MemberResponse> members = memberService.readMembers(pageable);
	
	members.map(m -> m.add(linkTo(methodOn(MemberControllerImpl.class)
		.getMemberByUsername(m.getMemberEmail())).withSelfRel()));
	
	Link link = linkTo(methodOn(MemberControllerImpl.class)
		.getMembers(pageable.getPageNumber(), 
			pageable.getPageSize(), 
			(sortDirection == Direction.DESC)? "desc": "asc",
			sortBy
			)).withSelfRel();

	return ResponseEntity.ok(membersPage_Assembler.toModel(members, link));
    }

    /*  # ============= cURL ==============
      	# BASH:
	myJWTToken=`curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' -L -X GET 'http://localhost:8080/api/member/v1/token' | jq -r '.token'`
      
       	# ------------- JSON --------------
       	curl -s -H "Authorization: $myJWTToken" \
       		-L -X GET 'http://localhost:8080/api/member/v1/member-details/ayrton.senna@bravo.com' | jq

       	# -------------- XML --------------
       	curl -s -H "Authorization: $myJWTToken" -H 'Accept: application/xml' \
       		-L -X GET 'http://localhost:8080/api/member/v1/member-details/ayrton.senna@bravo.com' | xmllint --format -
       
       	# ------------- YAML --------------
       	curl -s -H "Authorization: $myJWTToken" -H 'Accept: application/x-yaml' \
       		-L -X GET 'http://localhost:8080/api/member/v1/member-details/ayrton.senna@bravo.com' | yq

       	# ------------- CORS --------------
       	curl -s -H "Authorization: $myJWTToken" -H 'Origin: http://localhost:3000' \
       		-L -X GET 'http://localhost:8080/api/member/v1/member-details/ayrton.senna@bravo.com' | jq
     */
    @Override
    @GetMapping(value = "/member-details/{username}", 
	    produces = { _APPLICATION_YAML_VALUE, 
		    MediaType.APPLICATION_JSON_VALUE, 
		    MediaType.APPLICATION_XML_VALUE })
    public @ResponseBody ResponseEntity<MemberResponse> getMemberByUsername(@PathVariable String username) {

        log.info("MemberController - reading member by username: {}", username);

	MemberResponse member = memberService.readMemberByEmail(username);
	member.add(linkTo(methodOn(MemberControllerImpl.class)
		.getMemberByUsername(member.getMemberEmail())).withSelfRel());

//	return new ResponseEntity<MemberResponse>(member, HttpStatus.OK);
	return ResponseEntity.ok(member);
    }

    /*  # ============= cURL ==============
      	# BASH:
	myJWTToken=`curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' -L -X GET 'http://localhost:8080/api/member/v1/token' | jq -r '.token'`
      
       	# ------------- JSON --------------
       	curl -s -H "Authorization: $myJWTToken" \
       		-L -X GET 'http://localhost:8080/api/member/v1/member-full-details/ayrton.senna@bravo.com' | jq

       	# -------------- XML --------------
       	curl -s -H "Authorization: $myJWTToken" -H 'Accept: application/xml' \
       		-L -X GET 'http://localhost:8080/api/member/v1/member-full-details/ayrton.senna@bravo.com' | xmllint --format -
       
       	# ------------- YAML --------------
       	curl -s -H "Authorization: $myJWTToken" -H 'Accept: application/x-yaml' \
       		-L -X GET 'http://localhost:8080/api/member/v1/member-full-details/ayrton.senna@bravo.com' | yq

       	# ------------- CORS --------------
       	curl -s -H "Authorization: $myJWTToken" -H 'Origin: http://localhost:3000' \
       		-L -X GET 'http://localhost:8080/api/member/v1/member-full-details/ayrton.senna@bravo.com' | jq
     */
    @Override
    @GetMapping(value = "/member-full-details/{username}", 
	    produces = { _APPLICATION_YAML_VALUE, 
		    MediaType.APPLICATION_JSON_VALUE, 
		    MediaType.APPLICATION_XML_VALUE })
    public @ResponseBody ResponseEntity<MemberDetailsResponse> getMemberFullDetailsByUsername(@PathVariable String username) {

        log.info("MemberController - reading member by username: {}", username);

	MemberDetailsResponse member = memberService.readMemberDetailsByEmail(username);
	member.add(linkTo(methodOn(MemberControllerImpl.class)
		.getMemberByUsername(member.getMemberEmail())).withSelfRel());

//	return new ResponseEntity<MemberResponse>(member, HttpStatus.OK);
	return ResponseEntity.ok(member);
    }

    /*  # ============= cURL ==============
      	# BASH:
	myJWTToken=`curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' -L -X GET 'http://localhost:8080/api/member/v1/token' | jq -r '.token'`
      
	# ------------- JSON --------------
        curl -s -H "Authorization: $myJWTToken" -L -X GET 'http://localhost:8080/api/member/v1/me' | jq

        # -------------- XML --------------
        curl -s -H "Authorization: $myJWTToken" -H 'Accept: application/xml' \
        	-L -X GET 'http://localhost:8080/api/member/v1/me' | xmllint --format -
        
        # ------------- YAML --------------
        curl -s -H "Authorization: $myJWTToken" -H 'Accept: application/x-yaml' \
        	-L -X GET 'http://localhost:8080/api/member/v1/me' | yq

        # ------------- CORS --------------
        curl -s -H "Authorization: $myJWTToken" -H 'Origin: http://localhost:3000' \
        	-L -X GET 'http://localhost:8080/api/member/v1/me' | jq
     */
    @Override
    @GetMapping(value = "/me", 
	    produces = { _APPLICATION_YAML_VALUE, 
		    MediaType.APPLICATION_JSON_VALUE, 
		    MediaType.APPLICATION_XML_VALUE })
    public @ResponseBody ResponseEntity<MemberResponse> getMineDetails() {
	
	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	MemberResponse member = new MemberResponse();

	if (authentication != null) {
            member = memberService.readMemberByEmail(authentication.getName());
            member.add(linkTo(methodOn(MemberControllerImpl.class)
                    .getMemberByUsername(member.getMemberEmail())).withSelfRel());
	}

	return ResponseEntity.ok(member);
    }

    /*  ============= cURL ==============
      	# BASH:
	myJWTToken=`curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' -L -X GET 'http://localhost:8080/api/member/v1/token' | jq -r '.token'`
     
	# ------------- JSON (request and response) --------------
       	curl -s -H "Authorization: $myJWTToken" -H 'Content-Type: application/json' \
		-L -X PUT 'http://localhost:8080/api/member/v1/member-update' \
		-d '{
			"membrerId": 51,
			"memberName":"Rubens Barrichello", 
			"memberEmail":"rubens.barrichello@bravo.com",
		     	"memberMobileNumber":"(11) 98765-4321", 
		     	"memberPassword": "barrichello_pass",
		     	"memberAuthorities": [
		     		"ROLE_ADMIN"
		     	]
		     }' | jq

       	# -------------- XML (request and response) --------------
       	curl -s -H "Authorization: $myJWTToken" -H 'Accept: application/xml' -H 'Content-Type: application/xml' \
       		-L -X PUT 'http://localhost:8080/api/member/v1/member-update' \
       		-d '<MemberCreateRequest>
       			<memberId>52</memberId>
                        <memberName>Emerson Fittipaldi</memberName>
                        <memberEmail>emerson.fittipaldi@bravo.com</memberEmail>
                        <memberMobileNumber>(11) 98765-4321</memberMobileNumber>
                        <memberPassword>fittipaldi_pass</memberPassword>
                        <memberAuthorities>
                                <memberAuthorities>ROLE_ADMIN</memberAuthorities>
                        </memberAuthorities>
                   </MemberCreateRequest>' | xmllint --format - 
       
        # ------------- YAML (request and response) --------------
	curl -s -H "Authorization: $myJWTToken" -H 'Accept: application/x-yaml' -H 'Content-Type: application/x-yaml' \
		-L -X PUT 'http://localhost:8080/api/member/v1/member-update' \
       		-d '---
       		    memberId: 53
                    memberName: "Nelson Piquet"
                    memberEmail: "nelson.piquet@bravo.com"
                    memberMobileNumber: "(11) 98765-4321"
                    memberPassword: "piquet_pass"
                    memberAuthorities:
                        - "ROLE_ADMIN"' | yq

        # ------------- CORS - origin filter and JSON request / response -------------
        curl -s -H "Authorization: $myJWTToken" -H 'Origin: http://localhost:3000' \
        	-H 'Content-Type: application/json' \
        	-L -X PUT 'http://localhost:8080/api/member/v1/member-update' \
        	-d '{
        		"memberId": 54,
        		"memberName":"Felipe Massa", 
        		"memberEmail":"felipe.massa@bravo.com",
	     		"memberMobileNumber":"(11) 98765-4321", 
	     		"memberPassword": "massa_pass",
	     		"memberAuthorities": [
	     			"ROLE_ADMIN"
	     		]
	     	    }' | jq
     */
    @Override
    @PutMapping(value = "/member-update", 
	    consumes = { _APPLICATION_YAML_VALUE,
		    MediaType.APPLICATION_JSON_VALUE, 
		    MediaType.APPLICATION_XML_VALUE}, 
	    produces = { _APPLICATION_YAML_VALUE, 
		    MediaType.APPLICATION_JSON_VALUE, 
		    MediaType.APPLICATION_XML_VALUE })
    public @ResponseBody ResponseEntity<MemberResponse> updateMember(@RequestBody @Valid MemberUpdateRequest memberRequest) throws Exception {

        log.info("MemberController - updating member");
        
        MemberResponse updatedMember = null;
        try { 
            updatedMember = memberService.modifyMember(memberRequest);
            updatedMember.add(linkTo(methodOn(MemberControllerImpl.class)
                    .getMemberByUsername(memberRequest.getMemberEmail())).withSelfRel());
        } catch (Exception ex) {
	    throw new Exception(ex);
	}
	return ResponseEntity.ok(updatedMember);
    }

    /*  # ============= cURL ==============
      	# BASH:
	myJWTToken=`curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' -L -X GET 'http://localhost:8080/api/member/v1/token' | jq -r '.token'`
      
       	# ------------- JSON --------------
       	curl -s -H "Authorization: $myJWTToken" \
       		-L -X PATCH 'http://localhost:8080/api/member/v1/member-password' \
       		-d '{"newPassword": "mynewpassword"}' | jq
       
       	# -------------- XML --------------
       	curl -s -H "Authorization: $myJWTToken" -H 'Accept: application/xml' -H 'Content-Type: application/xml' \
       		-L -X PATCH 'http://localhost:8080/api/member/v1/member-password' \
       		-d '<MemberUpdatePasswordRequest>
       			<newPassword>mynewpassword</newPassword>
       		    </MemberUpdatePasswordRequest>' | xmllint --format -
       
       	# ------------- YAML --------------
       	curl -s -H "Authorization: $myJWTToken" -H 'Accept: application/x-yaml' -H 'Content-Type: application/x-yaml' \
       		-L -X PATCH 'http://localhost:8080/api/member/v1/member-password' \
       		-d '--- newPassword: "mynewpassword"' | yq
    
       	# ------------- CORS --------------
       	curl -s -H "Authorization: $myJWTToken" -H 'Origin: http://localhost:3000' \
       		-L -X PATCH 'http://localhost:8080/api/member/v1/member-password' \
       		-d '{"newPassword": "mynewpassword"}' | jq
    */
    @Override
    @PatchMapping(value = "/member-password",
	    consumes = { _APPLICATION_YAML_VALUE,
		    MediaType.APPLICATION_JSON_VALUE, 
		    MediaType.APPLICATION_XML_VALUE}, 
	    produces = { _APPLICATION_YAML_VALUE, 
		    MediaType.APPLICATION_JSON_VALUE, 
		    MediaType.APPLICATION_XML_VALUE })
    public @ResponseBody ResponseEntity<MemberResponse> updateMemberPassword(@RequestBody @Valid MemberUpdatePasswordRequest request, 
	    Authentication userAuth) {

        log.info("MemberController - updating member {} password", userAuth.getName());

        MemberResponse member = new MemberResponse();
        if (userAuth != null) {
            member = memberService.changeMemberPassword(userAuth.getName(), request);
            member.add(linkTo(methodOn(MemberControllerImpl.class)
                    .getMemberByUsername(member.getMemberEmail())).withSelfRel());
        }
        
	return ResponseEntity.ok(member);
    }

    /*  # ============= cURL ==============
      	# BASH:
	myJWTToken=`curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' -L -X GET 'http://localhost:8080/api/member/v1/token' | jq -r '.token'`
      
       	# ------------- JSON --------------       	
       	curl -s -H "Authorization: $myJWTToken" \
       		-L -X PATCH 'http://localhost:8080/api/member/v1/manage-member-password' \
            	-d '{"memberUsername": "mfredson2@amazon.com", "memberPassword": "newpassword"}' | jq
    
       	# -------------- XML --------------
       	curl -s -H "Authorization: $myJWTToken" -H 'Accept: application/xml' -H 'Content-Type: application/xml' \
       	    	-L -X PATCH 'http://localhost:8080/api/member/v1/manage-member-password' \
       	    	-d '<MemberManagePasswordRequest>
       		    	<memberUsername>mfredson2@amazon.com</memberUsername>
       		    	<memberPassword>newpassword</memberPassword>
       		    </MemberManagePasswordRequest>' | xmllint --format -
       
       	# ------------- YAML --------------
	curl -s -H "Authorization: $myJWTToken" -H 'Accept: application/x-yaml' -H 'Content-Type: application/x-yaml' \
		-L -X PATCH 'http://localhost:8080/api/member/v1/manage-member-password' \
       		-d '---
                    memberUsername: "Nelson Piquet"
                    memberPassword: "newpassword"' | yq
    
       	# ------------- CORS --------------
       	curl -s -H "Authorization: $myJWTToken" -H 'Origin: http://localhost:3000' \
            -L -X PATCH 'http://localhost:8080/api/member/v1/manage-member-password' \
            -d '{"memberUsername": "mfredson2@amazon.com", "memberPassword": "newpassword"}' \
            | jq
    */
    @Override
    @PatchMapping(value = "/manage-member-password",
	    consumes = { _APPLICATION_YAML_VALUE,
		    MediaType.APPLICATION_JSON_VALUE, 
		    MediaType.APPLICATION_XML_VALUE}, 
	    produces = { _APPLICATION_YAML_VALUE, 
		    MediaType.APPLICATION_JSON_VALUE, 
		    MediaType.APPLICATION_XML_VALUE })
    public @ResponseBody ResponseEntity<MemberResponse> manageMemberPassword(@RequestBody @Valid MemberManagePasswordRequest request) {

        log.info("MemberController - updating member {} password", request.getMemberUsername());

	MemberResponse member = memberService.manageMemberPassword(request);
	member.add(linkTo(methodOn(MemberControllerImpl.class)
		.getMemberByUsername(member.getMemberEmail())).withSelfRel());

	return ResponseEntity.ok(member);
    }

    /*  # ============= cURL ==============
      	# BASH:
	myJWTToken=`curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' -L -X GET 'http://localhost:8080/api/member/v1/token' | jq -r '.token'`
      
       	# ------------- JSON --------------
       	curl -s -H "Authorization: $myJWTToken" \
       		-L -X PATCH 'http://localhost:8080/api/member/v1/member-disable/3' | jq
    
       	# -------------- XML --------------
       	curl -s -H "Authorization: $myJWTToken" -H 'Accept: application/xml' \
       		-L -X PATCH 'http://localhost:8080/api/member/v1/member-disable/3' | xmllint --format -
       
       	# ------------- YAML --------------
       	curl -s -H "Authorization: $myJWTToken" -H 'Accept: application/x-yaml' \
       		-L -X PATCH 'http://localhost:8080/api/member/v1/member-disable/3' | yq
    
       	# ------------- CORS --------------
       	curl -s -H "Authorization: $myJWTToken" -H 'Origin: http://localhost:3000' \
       		-L -X PATCH 'http://localhost:8080/api/member/v1/member-disable/3' | jq
    */
    @Override
    @PatchMapping(value = "/member-disable/{id}",
	    produces = { _APPLICATION_YAML_VALUE, 
		    MediaType.APPLICATION_JSON_VALUE, 
		    MediaType.APPLICATION_XML_VALUE })
    public @ResponseBody ResponseEntity<MemberEnabledResponse> inactivateMember(@PathVariable Long id) throws Exception {

        log.info("MemberController - disabling member id: {}", id);

	MemberEnabledResponse member = memberService.changeMemberEnabledStatus(id, MemberEnabledStatus.DISABLED);
	member.add(linkTo(methodOn(MemberControllerImpl.class)
		.getMemberByUsername(member.getMemberUsername())).withSelfRel());

	return ResponseEntity.ok(member);
    }

    /*  # ============= cURL ==============
      	# BASH:
	myJWTToken=`curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' -L -X GET 'http://localhost:8080/api/member/v1/token' | jq -r '.token'`
      
       	# ------------- JSON --------------
       	curl -s -H "Authorization: $myJWTToken" \
       		-L -X PATCH 'http://localhost:8080/api/member/v1/member-enable/3' | jq

       	# -------------- XML --------------
       	curl -s -H "Authorization: $myJWTToken" -H 'Accept: application/xml' \
       		-L -X PATCH 'http://localhost:8080/api/member/v1/member-enable/3' | xmllint --format -
       
       	# ------------- YAML --------------
       	curl -s -H "Authorization: $myJWTToken" -H 'Accept: application/x-yaml' \
       		-L -X PATCH 'http://localhost:8080/api/member/v1/member-enable/3' | yq

       	# ------------- CORS --------------
       	curl -s -H "Authorization: $myJWTToken" -H 'Origin: http://localhost:3000' \
       		-L -X PATCH 'http://localhost:8080/api/member/v1/member-enable/3' | jq
     */
    @Override
    @PatchMapping(value = "/member-enable/{id}",
	    produces = { _APPLICATION_YAML_VALUE, 
		    MediaType.APPLICATION_JSON_VALUE, 
		    MediaType.APPLICATION_XML_VALUE })
    public @ResponseBody ResponseEntity<MemberEnabledResponse> activateMember(@PathVariable Long id) throws Exception {

        log.info("MemberController - enabling member id: {}", id);

	MemberEnabledResponse member = memberService.changeMemberEnabledStatus(id, MemberEnabledStatus.ENABLED);
	member.add(linkTo(methodOn(MemberControllerImpl.class)
		.getMemberByUsername(member.getMemberUsername())).withSelfRel());

	return ResponseEntity.ok(member);
    }

    /*  # ============= cURL ==============
      	# BASH:
	myJWTToken=`curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' -L -X GET 'http://localhost:8080/api/member/v1/token' | jq -r '.token'`
      
       	# ------------- JSON --------------
       	curl -s -H "Authorization: $myJWTToken" \
       		-L -X PATCH 'http://localhost:8080/api/member/v1/member-lock/3' | jq
    
       	# -------------- XML --------------
       	curl -s -H "Authorization: $myJWTToken" -H 'Accept: application/xml' \
       		-L -X PATCH 'http://localhost:8080/api/member/v1/member-lock/3' | xmllint --format -
       
       	# ------------- YAML --------------
       	curl -s -H "Authorization: $myJWTToken" -H 'Accept: application/x-yaml' \
       		-L -X PATCH 'http://localhost:8080/api/member/v1/member-lock/3' | yq
    
       	# ------------- CORS --------------
       	curl -s -H "Authorization: $myJWTToken" -H 'Origin: http://localhost:3000' \
       		-L -X PATCH 'http://localhost:8080/api/member/v1/member-lock/3' | jq
    */
    @Override
    @PatchMapping(value = "/member-lock/{id}",
	    produces = { _APPLICATION_YAML_VALUE, 
		    MediaType.APPLICATION_JSON_VALUE, 
		    MediaType.APPLICATION_XML_VALUE })
    public @ResponseBody ResponseEntity<MemberAccountLockedResponse> lockMemberAccount(@PathVariable Long id) throws Exception {

        log.info("MemberController - locking member account id: {}", id);

        MemberAccountLockedResponse member = memberService.changeMemberAccountLockedStatus(id, MemberLockedStatus.LOCKED);
	member.add(linkTo(methodOn(MemberControllerImpl.class)
		.getMemberByUsername(member.getMemberUsername())).withSelfRel());

	return ResponseEntity.ok(member);
    }

    /*  # ============= cURL ==============
      	# BASH:
	myJWTToken=`curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' -L -X GET 'http://localhost:8080/api/member/v1/token' | jq -r '.token'`
      
       	# ------------- JSON --------------
       	curl -s -H "Authorization: $myJWTToken" \
       		-L -X PATCH 'http://localhost:8080/api/member/v1/member-unlock/3' | jq

       	# -------------- XML --------------
       	curl -s -H "Authorization: $myJWTToken" -H 'Accept: application/xml' \
       		-L -X PATCH 'http://localhost:8080/api/member/v1/member-unlock/3' | xmllint --format -
       
       	# ------------- YAML --------------
       	curl -s -H "Authorization: $myJWTToken" -H 'Accept: application/x-yaml' \
       		-L -X PATCH 'http://localhost:8080/api/member/v1/member-unlock/3' | yq

       	# ------------- CORS --------------
       	curl -s -H "Authorization: $myJWTToken" -H 'Origin: http://localhost:3000' \
       		-L -X PATCH 'http://localhost:8080/api/member/v1/member-unlock/3' | jq
     */
    @Override
    @PatchMapping(value = "/member-unlock/{id}",
	    produces = { _APPLICATION_YAML_VALUE, 
		    MediaType.APPLICATION_JSON_VALUE, 
		    MediaType.APPLICATION_XML_VALUE })
    public @ResponseBody ResponseEntity<MemberAccountLockedResponse> unlockMemberAccount(@PathVariable Long id) throws Exception {

        log.info("MemberController - unlocking member id: {}", id);

        MemberAccountLockedResponse member = memberService.changeMemberAccountLockedStatus(id, MemberLockedStatus.UNLOCKED);
	member.add(linkTo(methodOn(MemberControllerImpl.class)
		.getMemberByUsername(member.getMemberUsername())).withSelfRel());

	return ResponseEntity.ok(member);
    }
}
