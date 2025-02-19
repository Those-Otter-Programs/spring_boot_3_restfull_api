package com.thoseop.api.members.http;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static com.thoseop.config.OtterWebMvcConfig._APPLICATION_YAML_VALUE;

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

import com.thoseop.api.members.entity.enums.MemberStatus;
import com.thoseop.api.members.http.request.MemberCreateRequest;
import com.thoseop.api.members.http.request.MemberManagePasswordRequest;
import com.thoseop.api.members.http.request.MemberUpdatePasswordRequest;
import com.thoseop.api.members.http.request.MemberUpdateRequest;
import com.thoseop.api.members.http.response.MemberResponse;
import com.thoseop.api.members.service.MemberService;

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

    /* ============= cURL ==============
     
       ------------- JSON --------------
       curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' -H 'Content-Type: application/json' \ 
       -L -X POST 'http://localhost:8080/api/member/v1/member-create' \
       -d '{"memberName":"Ayrton Senna","memberEmail":"ayrton.senna@bravo.com","memberMobileNumber":"(11) 98765-4321","memberAuthorities":null}' \
       | jq

       -------------- XML --------------
       curl -s -u 'ayrton.senna@bravo.com' -H 'Accept: application/xml' -H 'Content-Type: application/xml' \
       -L -X POST 'http://localhost:8080/api/member/v1/member-create' \
       -d '<MemberCreateRequest><memberId>1</memberId><memberName>Ayrton Senna</memberName><memberEmail>ayrton.senna@bravo.com</memberEmail><memberMobileNumber>(11) 98765-4321</memberMobileNumber></MemberCreateRequest>' \
       | xmllint --format - 
       
       ------------- YAML --------------
       to be continued...

       ------------- CORS --------------
       curl -s -u 'ayrton.senna@bravo.com' -H 'Origin: http://localhost:3000' -H 'Content-Type: application/json' \
       -L -X POST 'http://localhost:8080/api/member/v1/member-create' \
       -d '{"memberName":"Ayrton Senna","memberEmail":"ayrton.senna@bravo.com","memberMobileNumber":"(11) 98765-4321","memberAuthorities":null}' \
       | jq
     */
    @Override
    @PostMapping(value = "/member-create",
	    consumes = { _APPLICATION_YAML_VALUE,
		    MediaType.APPLICATION_JSON_VALUE, 
		    MediaType.APPLICATION_XML_VALUE }, 
	    produces = { _APPLICATION_YAML_VALUE,
		    MediaType.APPLICATION_JSON_VALUE, 
		    MediaType.APPLICATION_XML_VALUE })
    public @ResponseBody ResponseEntity<MemberResponse> createMember(@RequestBody @Valid MemberCreateRequest request) 
	    throws Exception {

        log.info("MemberController - creating member");

	MemberResponse savedMember;
        try {
            savedMember = this.memberService.saveMember(request);
            savedMember.add(linkTo(methodOn(MemberControllerImpl.class)
        	    .getMemberByUsername(savedMember.getMemberEmail())).withSelfRel());
        } catch (Exception ex) {
            throw new Exception(ex);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMember);
    }

    /* ============= cURL ==============
      
       ------------- JSON - PAGINATED --------------
       curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' -L -X GET 'http://localhost:8080/api/member/v1/list?page=1&size=8&sort=desc' | jq
       curl -s -u 'ayrton.senna@bravo.com' -L -X GET 'http://localhost:8080/api/member/v1/list?page=1&size=8&sort=desc' | jq
       
     * Base64: 
       curl -s -u 'YXlydG9uLnNlbm5hQGJlc3QuY29tOmF5cnRvbl9wYXNz' -L -X GET 'http://localhost:8080/api/member/v1/list?page=1&size=8&sort=desc' | jq
       
       curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' -L -X GET 'http://localhost:8080/api/member/v1/list?page=1&size=8' | jq
       curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' -L -X GET 'http://localhost:8080/api/member/v1/list?page=1' | jq
       curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' -L -X GET 'http://localhost:8080/api/member/v1/list' | jq

       -------------- XML - PAGINATED --------------
       curl -s -u 'ayrton.senna@bravo.com' -H 'Accept: application/xml' \
       -L -X GET 'http://localhost:8080/api/member/v1/list?page=1&size=8&sort=desc' | xmllint --format -
       
       ------------- YAML - PAGINATED --------------
       curl -s -u 'ayrton.senna@bravo.com' -H 'Accept: application/x-yaml' \
       -L -X GET 'http://localhost:8080/api/member/v1/list?page=1&size=8&sort=desc' | yq

       ------------- CORS - PAGINATED --------------
       curl -s -u 'ayrton.senna@bravo.com' -H 'Origin: http://localhost:3000' \
       -L -X GET 'http://localhost:8080/api/member/v1/list?page=1&size=8&sort=desc' | jq
     */
    @Override
    @GetMapping(value = "/list", 
	    produces = { _APPLICATION_YAML_VALUE, 
		    MediaType.APPLICATION_JSON_VALUE,
		    MediaType.APPLICATION_XML_VALUE })
    public @ResponseBody ResponseEntity<PagedModel<EntityModel<MemberResponse>>> getMembers(
	    @RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "8") Integer size,
	    @RequestParam(defaultValue = "asc") String sort) { 
	
        log.info("MemberController - reading all members");

	var sortDirection = "desc".equalsIgnoreCase(sort) ? Direction.DESC : Direction.ASC;
	Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "email"));
	Page<MemberResponse> members = memberService.readMembers(pageable);
	members.map(m -> m.add(linkTo(methodOn(MemberControllerImpl.class)
		.getMemberByUsername(m.getMemberEmail())).withSelfRel()));
	Link link = linkTo(methodOn(MemberControllerImpl.class)
		.getMembers(pageable.getPageNumber(), 
			pageable.getPageSize(), "asc")).withSelfRel();

	return ResponseEntity.ok(membersPage_Assembler.toModel(members, link));
    }

    /* ============= cURL ==============
      
       ------------- JSON --------------
       curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' \
       -L -X GET 'http://localhost:8080/api/member/v1/member-details/ayrton.senna@bravo.com' | jq

       -------------- XML --------------
       curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' -H 'Accept: application/xml' \
       -L -X GET 'http://localhost:8080/api/member/v1/member-details/ayrton.senna@bravo.com' | xmllint --format -
       
       ------------- YAML --------------
       curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' -H 'Accept: application/x-yaml' \
       -L -X GET 'http://localhost:8080/api/member/v1/member-details/ayrton.senna@bravo.com' | yq

       ------------- CORS --------------
       curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' -H 'Origin: http://localhost:3000' \
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

    /* ============= cURL ==============
     
       ------------- JSON --------------
       curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' -H 'Content-Type: application/json' \ 
       -L -X POST 'http://localhost:8080/api/member/v1/member-update' \
       -d '{"memberId":3,"memberName":"Cody Johnson","memberEmail":"cody.johnson@bravo.com","memberMobileNumber":"+1 (865) 765-4321", "memberAuthorities": ["ROLE_MANAGER"]}' \
       | jq

       -------------- XML --------------
       curl -s -u 'ayrton.senna@bravo.com' -H 'Accept: application/xml' -H 'Content-Type: application/xml' \
       -L -X POST 'http://localhost:8080/api/member/v1/member-update' \
       -d '<MemberResponse><memberId>3</memberId><memberName>Toby Keith</memberName><memberEmail>toby.keith@bravo.com</memberEmail><memberMobileNumber>+1 (865) 765-4321</memberMobileNumber></MemberResponse>' \
       | xmllint --format -       
       
       ------------- YAML --------------
       to be continued...

       ------------- CORS --------------
       curl -s -u 'ayrton.senna@bravo.com' -H 'Origin: http://localhost:3000' -H 'Content-Type: application/json' \
       -L -X POST 'http://localhost:8080/api/member/v1/member-update' \
       -d '{"memberId":3,"memberName":"Jo Dee Messina","memberEmail":"jo.dee@bravo.com","memberMobileNumber":"+1 (865) 765-4321", "memberAuthorities": ["ROLE_MANAGER"]}' \
       | jq
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

    /* ============= cURL ==============
      
       ------------- JSON --------------
       curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' \
       -L -X GET 'http://localhost:8080/api/member/v1/member-password' \
       -d '{"newPassword": "mynewpassword"}' \
       | jq
    
     * Base64: 
       curl -s -u 'YXlydG9uLnNlbm5hQGJlc3QuY29tOmF5cnRvbl9wYXNz' \
       -L -X GET 'http://localhost:8080/api/member/v1/member-password' \
       -d '{"newPassword": "mynewpassword"}' \
       | jq
    
       -------------- XML --------------
       curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' -H 'Accept: application/xml' \
       -L -X GET 'http://localhost:8080/api/member/v1/member-password' \
       -d '<MemberUpdatePasswordRequest><newPassword>mynewpassword</newPassword></MemberUpdatePasswordRequest>' \
       | xmllint --format -
       
       ------------- YAML --------------
       to be continued...
    
       ------------- CORS --------------
       curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' -H 'Origin: http://localhost:3000' \
       -L -X GET 'http://localhost:8080/api/member/v1/member-password' \
       -d '{"newPassword": "mynewpassword"}' \
       | jq
    */
    @Override
    @PatchMapping(value = "/member-password",
	    consumes = { _APPLICATION_YAML_VALUE,
		    MediaType.APPLICATION_JSON_VALUE, 
		    MediaType.APPLICATION_XML_VALUE}, 
	    produces = { _APPLICATION_YAML_VALUE, 
		    MediaType.APPLICATION_JSON_VALUE, 
		    MediaType.APPLICATION_XML_VALUE })
    public @ResponseBody ResponseEntity<MemberResponse> updateMemberPassword(@RequestBody @Valid MemberUpdatePasswordRequest request, Authentication userAuth) {

        log.info("MemberController - updating member {} password", userAuth.getName());

	MemberResponse member = memberService.changeMemberPassword(userAuth.getName(), request);
	member.add(linkTo(methodOn(MemberControllerImpl.class)
		.getMemberByUsername(member.getMemberEmail())).withSelfRel());

	return ResponseEntity.ok(member);
    }

    /* ============= cURL ==============
      
       ------------- JSON --------------
       curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' \
       -L -X GET 'http://localhost:8080/api/member/v1/manage-member-password' \
       -d '{"memberUsername": "mfredson2@amazon.com", "memberPassword": "mynewpassword"}' \
       | jq
    
     * Base64: 
       curl -s -u 'YXlydG9uLnNlbm5hQGJlc3QuY29tOmF5cnRvbl9wYXNz' \
       -L -X GET 'http://localhost:8080/api/member/v1/manage-member-password' \
       -d '{"memberUsername": "mfredson2@amazon.com", "memberPassword": "mynewpassword"}' \
       | jq
    
       -------------- XML --------------
       curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' -H 'Accept: application/xml' \
       -L -X GET 'http://localhost:8080/api/member/v1/manage-member-password' \
       -d '<MemberUpdatePasswordRequest><memberUsername>mfredson2@amazon.com</memberUsername><memberPassword>mynewpassword</memberPassword></MemberUpdatePasswordRequest>' \
       | xmllint --format -
       
       ------------- YAML --------------
       to be continued...
    
       ------------- CORS --------------
       curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' -H 'Origin: http://localhost:3000' \
       -L -X GET 'http://localhost:8080/api/member/v1/manage-member-password' \
       -d '{"memberUsername": "mfredson2@amazon.com", "memberPassword": "mynewpassword"}' \
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

    /* ============= cURL ==============
      
       ------------- JSON --------------
       curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' \
       -L -X GET 'http://localhost:8080/api/member/v1/member-disable/3' | jq
    
     * Base64: 
       curl -s -u 'YXlydG9uLnNlbm5hQGJlc3QuY29tOmF5cnRvbl9wYXNz' \
       -L -X GET 'http://localhost:8080/api/member/v1/member-disable/3' | jq
    
       -------------- XML --------------
       curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' -H 'Accept: application/xml' \
       -L -X GET 'http://localhost:8080/api/member/v1/member-disable/3' | xmllint --format -
       
       ------------- YAML --------------
       curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' -H 'Accept: application/x-yaml' \
       -L -X GET 'http://localhost:8080/api/member/v1/member-disable/3' | yq
    
       ------------- CORS --------------
       curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' -H 'Origin: http://localhost:3000' \
       -L -X GET 'http://localhost:8080/api/member/v1/member-disable/3' | jq
    */
    @Override
    @PatchMapping(value = "/member-disable/{id}",
	    produces = { _APPLICATION_YAML_VALUE, 
		    MediaType.APPLICATION_JSON_VALUE, 
		    MediaType.APPLICATION_XML_VALUE })
    public @ResponseBody ResponseEntity<MemberResponse> inactivateMember(@PathVariable Long id) {

        log.info("MemberController - ennabling member id: {}", id);

	MemberResponse member = memberService.changeMemberStatus(id, MemberStatus.DISABLE);
	member.add(linkTo(methodOn(MemberControllerImpl.class)
		.getMemberByUsername(member.getMemberEmail())).withSelfRel());

	return ResponseEntity.ok(member);
    }

    /* ============= cURL ==============
      
       ------------- JSON --------------
       curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' \
       -L -X GET 'http://localhost:8080/api/member/v1/member-enable/3' | jq

     * Base64: 
       curl -s -u 'YXlydG9uLnNlbm5hQGJlc3QuY29tOmF5cnRvbl9wYXNz' \
       -L -X GET 'http://localhost:8080/api/member/v1/member-enable/3' | jq

       -------------- XML --------------
       curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' -H 'Accept: application/xml' \
       -L -X GET 'http://localhost:8080/api/member/v1/member-enable/3' | xmllint --format -
       
       ------------- YAML --------------
       curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' -H 'Accept: application/x-yaml' \
       -L -X GET 'http://localhost:8080/api/member/v1/member-enable/3' | yq

       ------------- CORS --------------
       curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' -H 'Origin: http://localhost:3000' \
       -L -X GET 'http://localhost:8080/api/member/v1/member-enable/3' | jq
     */
    @Override
    @PatchMapping(value = "/member-enable/{id}",
	    produces = { _APPLICATION_YAML_VALUE, 
		    MediaType.APPLICATION_JSON_VALUE, 
		    MediaType.APPLICATION_XML_VALUE })
    public @ResponseBody ResponseEntity<MemberResponse> activateMember(@PathVariable Long id) {

        log.info("MemberController - disabling member id: {}", id);

	MemberResponse member = memberService.changeMemberStatus(id, MemberStatus.ENABLE);
	member.add(linkTo(methodOn(MemberControllerImpl.class)
		.getMemberByUsername(member.getMemberEmail())).withSelfRel());

	return ResponseEntity.ok(member);
    }
}
