package com.thoseop.api.members.http;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.thoseop.api.members.http.request.MemberRequest;
import com.thoseop.api.members.http.response.MemberResponse;
import com.thoseop.api.members.service.MemberService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/member/v1")
@RequiredArgsConstructor
public class MemberControllerImpl implements MemberController {

    static final String _APPLICATION_YAML_VALUE = "application/x-yaml";
    private final MemberService memberService;
    private final PagedResourcesAssembler<MemberResponse> membersPage_Assembler;

    /* ============= cURL ==============
     
       ------------- JSON --------------
       curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' -H 'Content-Type: application/json' \ 
       -L -X POST 'http://localhost:8080/api/member/v1/createMember' \
       -d '{"memberName":"Ayrton Senna","memberEmail":"ayrton.senna@bravo.com","memberMobileNumber":"(11) 98765-4321","memberAuthorities":null}' | jq

       -------------- XML --------------
       curl -s -u 'ayrton.senna@bravo.com' -H 'Accept: application/xml' -H 'Content-Type: application/xml' \
       -L -X POST 'http://localhost:8080/api/member/v1/createMember' \
       -d '<MemberResponse><memberId>1</memberId><memberName>Ayrton Senna</memberName><memberEmail>ayrton.senna@bravo.com</memberEmail><memberMobileNumber>(11) 98765-4321</memberMobileNumber></MemberResponse>' | xmllint --format -       
       
       ------------- YAML --------------
       to be continued...

       ------------- CORS --------------
       curl -s -u 'ayrton.senna@bravo.com' -H 'Origin: http://localhost:3000' -H 'Content-Type: application/json' \
       -L -X POST 'http://localhost:8080/api/member/v1/createMember' \
       -d '{"memberName":"Ayrton Senna","memberEmail":"ayrton.senna@bravo.com","memberMobileNumber":"(11) 98765-4321","memberAuthorities":null}' | jq
     */
    @Override
    @PostMapping(value = "/createMember", 
	    consumes = { _APPLICATION_YAML_VALUE,
		    MediaType.APPLICATION_JSON_VALUE, 
		    MediaType.APPLICATION_XML_VALUE }, 
	    produces = { _APPLICATION_YAML_VALUE,
		    MediaType.APPLICATION_JSON_VALUE, 
		    MediaType.APPLICATION_XML_VALUE })
    public @ResponseBody ResponseEntity<MemberResponse> createMember(@RequestBody @Valid MemberRequest request) 
	    throws Exception {

	MemberResponse savedMember;
        try {
            savedMember = this.memberService.saveMember(request);
            savedMember.add(linkTo(methodOn(MemberControllerImpl.class).getMemberByUsername(savedMember.getMemberEmail())).withSelfRel());
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
	
        log.info("Reading all users");

	var sortDirection = "desc".equalsIgnoreCase(sort) ? Direction.DESC : Direction.ASC;
	Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "email"));
	Page<MemberResponse> members = memberService.readMembers(pageable);
	members.map(m -> m.add(linkTo(methodOn(MemberControllerImpl.class).getMemberByUsername(m.getMemberEmail())).withSelfRel()));

	Link link = linkTo(methodOn(MemberControllerImpl.class)
		.getMembers(pageable.getPageNumber(), 
			pageable.getPageSize(), "asc")).withSelfRel();

	return ResponseEntity.ok(membersPage_Assembler.toModel(members, link));
    }

    /* ============= cURL ==============
      
       ------------- JSON --------------
       curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' 
       -L -X GET 'http://localhost:8080/api/member/v1/member/ayrton.senna@bravo.com' | jq

       -------------- XML --------------
       curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' -H 'Accept: application/xml' \
       -L -X GET 'http://localhost:8080/api/member/v1/member/ayrton.senna@bravo.com' | xmllint --format -
       
       ------------- YAML --------------
       curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' -H 'Accept: application/x-yaml' \
       -L -X GET 'http://localhost:8080/api/member/v1/member/ayrton.senna@bravo.com' | yq

       ------------- CORS --------------
       curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' -H 'Origin: http://localhost:3000' \
       -L -X GET 'http://localhost:8080/api/member/v1/member/ayrton.senna@bravo.com' | jq
     */
    @Override
    @GetMapping(value = "/member/{username}", 
	    produces = { _APPLICATION_YAML_VALUE, 
		    MediaType.APPLICATION_JSON_VALUE, 
		    MediaType.APPLICATION_XML_VALUE })
    public @ResponseBody ResponseEntity<MemberResponse> getMemberByUsername(@PathVariable String username) {
        log.info("Reading user by Id");

	MemberResponse member = memberService.readMemberByEmail(username);
	member.add(linkTo(methodOn(MemberControllerImpl.class).getMemberByUsername(member.getMemberEmail())).withSelfRel());

//	return new ResponseEntity<MemberResponse>(member, HttpStatus.OK);
	return ResponseEntity.ok(member);
    }
}
