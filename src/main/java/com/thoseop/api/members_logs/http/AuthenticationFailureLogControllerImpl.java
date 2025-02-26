package com.thoseop.api.members_logs.http;

import static com.thoseop.config.OtterWebMvcConfig._APPLICATION_YAML_VALUE;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.thoseop.api.members_logs.http.response.AuthenticationFailureLogResponse;
import com.thoseop.api.members_logs.service.AuthenticationFailureLogService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/authentication-failure/v1")
@RequiredArgsConstructor
public class AuthenticationFailureLogControllerImpl implements AuthenticationFailureLogController {

    private final AuthenticationFailureLogService authFailLogService;
    private final PagedResourcesAssembler<AuthenticationFailureLogResponse> authFailLogsPage_Assembler;

    /*  ============= cURL ==============
      	# BASH:

        # get the JWT token and stores it in a bash variable:
        myJWTToken=`curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' -L -X GET 'http://localhost:8080/api/member/v1/token' | jq -r '.token'`

        # run cURL using the variable as the authorization token:
        
        # ------------- JSON --------------
     	curl -s -H "Authorization: $myJWTToken" \
     		-L -X GET 'http://localhost:8080/api/authentication-failure/v1/member/ayrton.senna@bravo.com?page=0&size=8&sort=desc' | jq

       	curl -s -H "Authorization: $myJWTToken" \
       		-L -X GET 'http://localhost:8080/api/authentication-failure/v1/member/ayrton.senna@bravo.com?page=0&size=8' | jq
       		
       	curl -s -H "Authorization: $myJWTToken" \
       		-L -X GET 'http://localhost:8080/api/authentication-failure/v1/member/ayrton.senna@bravo.com?page=0' | jq
       		
       	curl -s -H "Authorization: $myJWTToken" \
       		-L -X GET 'http://localhost:8080/api/authentication-failure/v1/member/ayrton.senna@bravo.com' | jq

       	# -------------- XML - PAGINATED --------------
       	curl -s -H "Authorization: $myJWTToken" -H 'Accept: application/xml' \
       		-L -X GET 'http://localhost:8080/api/authentication-failure/v1/member/ayrton.senna@bravo.com?page=0&size=8&sort=desc' \
       		| xmllint --format -
       
       	# ------------- YAML - PAGINATED --------------
       	curl -s -H "Authorization: $myJWTToken" -H 'Accept: application/x-yaml' \
       		-L -X GET 'http://localhost:8080/api/authentication-failure/v1/member/ayrton.senna@bravo.com?page=0&size=8&sort=desc' \
       		| yq

       	# ------------- CORS - PAGINATED --------------
       	curl -s -H "Authorization: $myJWTToken" -H 'Origin: http://localhost:3000' \
       		-L -X GET 'http://localhost:8080/api/authentication-failure/v1/member/ayrton.senna@bravo.com?page=0&size=8&sort=desc' \
       		| jq
     */
    @Override
    @GetMapping(value = "/member/{username}", 
	    produces = { _APPLICATION_YAML_VALUE, 
		    MediaType.APPLICATION_JSON_VALUE,
		    MediaType.APPLICATION_XML_VALUE })
    public @ResponseBody ResponseEntity<PagedModel<EntityModel<AuthenticationFailureLogResponse>>> getMemberAuthenticationFailures(
	    @PathVariable String username,
	    @RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "8") Integer size,
	    @RequestParam(defaultValue = "asc") String sort) { 
	
        log.info("AuthenticationFailureLogControllerImpl - reading all members");

	var sortDirection = "desc".equalsIgnoreCase(sort) ? Direction.DESC : Direction.ASC;
	Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "id"));
	Page<AuthenticationFailureLogResponse> memberAuthFailures = authFailLogService.readMemberAuthenticationFailLogs(username, pageable);
	
	memberAuthFailures.map(l -> l.add(linkTo(methodOn(AuthenticationFailureLogControllerImpl.class)
		.getLogById(l.getLogId())).withSelfRel()));
	
	Link link = linkTo(methodOn(AuthenticationFailureLogControllerImpl.class)
		.getMemberAuthenticationFailures(username, pageable.getPageNumber(), 
			pageable.getPageSize(), "asc")).withSelfRel();

	return ResponseEntity.ok(authFailLogsPage_Assembler.toModel(memberAuthFailures, link));
    }

    /*  ============= cURL ==============
      	# BASH:

        # get the JWT token and stores it in a bash variable:
        myJWTToken=`curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' -L -X GET 'http://localhost:8080/api/member/v1/token' | jq -r '.token'`

        # run cURL using the variable as the authorization token:
       	# ------------- JSON --------------
       	curl -s -H "Authorization: $myJWTToken" \
       		-L -X GET 'http://localhost:8080/api/authentication-failure/v1/log/1' | jq

       	# -------------- XML --------------
       	curl -s -H "Authorization: $myJWTToken" -H 'Accept: application/xml' \
       		-L -X GET 'http://localhost:8080/api/authentication-failure/v1/log/1' | xmllint --format -
       
       	# ------------- YAML --------------
       	curl -s -H "Authorization: $myJWTToken" -H 'Accept: application/x-yaml' \
       		-L -X GET 'http://localhost:8080/api/authentication-failure/v1/log/1' | yq

       	# ------------- CORS --------------
       	curl -s -H "Authorization: $myJWTToken" -H 'Origin: http://localhost:3000' \
       		-L -X GET 'http://localhost:8080/api/authentication-failure/v1/log/1' | jq

    */
    @GetMapping(value = "/log/{id}", 
	    produces = { _APPLICATION_YAML_VALUE, 
		    MediaType.APPLICATION_JSON_VALUE,
		    MediaType.APPLICATION_XML_VALUE })
    public @ResponseBody ResponseEntity<AuthenticationFailureLogResponse> getLogById(
	    @PathVariable Long id) {

	AuthenticationFailureLogResponse authLog = authFailLogService.readlogById(id);
	authLog.add(linkTo(methodOn(AuthenticationFailureLogControllerImpl.class)
		.getLogById(id)).withSelfRel());
	
	return ResponseEntity.ok(authLog);
    }
}
