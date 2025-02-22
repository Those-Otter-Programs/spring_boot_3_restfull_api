package com.thoseop.api.corporation.http;

import static com.thoseop.config.OtterWebMvcConfig._APPLICATION_YAML_VALUE;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.thoseop.api.corporation.http.response.InfoCorpResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/corporation/v1")
public class InfoControllerImpl implements InfoController {

    @Value("${spring.application.name}")
    private String appName;
    
    /* ============= cURL ==============

       ------------- JSON --------------
       curl -s -H 'Accept: application/json' -L -X GET 'http://localhost:8080/api/corporation/v1/info' | jq

       -------------- XML --------------
       curl -s -H 'Accept: application/xml' -L -X GET 'http://localhost:8080/api/corporation/v1/info' | xmllint --format -
       
       ------------- YAML --------------
       curl -s -H 'Accept: application/x-yaml' -L -X GET 'http://localhost:8080/api/corporation/v1/info' | yq

       ------------- CORS --------------
       curl -s -H 'Accept: application/json' -H 'Origin: http://localhost:3000' \
       	-L -X GET 'http://localhost:8080/api/corporation/v1/info' | jq
     */
    @Override
    @GetMapping(value = {"", "/", "/info", "/info/"},
    		produces = { _APPLICATION_YAML_VALUE,
    			MediaType.APPLICATION_JSON_VALUE, 
    			MediaType.APPLICATION_XML_VALUE })
    public @ResponseBody ResponseEntity<Model> info(Model model) {
	
	model.addAttribute("Application", appName);
	model.addAttribute("Description", "A Spring Boot 3 RESTfull API sample");
	
	return ResponseEntity.ok(model);
    }

    /* ============= cURL ==============

       ------------- JSON --------------
       curl -s -u 'ayrton.senna@bravo.com' -H 'Accept: application/json' -L -X GET 'http://localhost:8080/api/corporation/v1/info-corp' | jq
       -- 
       curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' -H 'Accept: application/json' -L -X GET 'http://localhost:8080/api/corporation/v1/info-corp' | jq

       -------------- XML --------------
       curl -s -u 'ayrton.senna@bravo.com' -H 'Accept: application/xml' -L -X GET 'http://localhost:8080/api/corporation/v1/info-corp' | xmllint --format -
       --
       curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' -H 'Accept: application/xml' -L -X GET 'http://localhost:8080/api/corporation/v1/info-corp' | xmllint --format -
       
       ------------- YAML --------------
       curl -s -u 'ayrton.senna@bravo.com' -H 'Accept: application/x-yaml' -L -X GET 'http://localhost:8080/api/corporation/v1/info-corp' | yq
       --
       curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' -H 'Accept: application/x-yaml' -L -X GET 'http://localhost:8080/api/corporation/v1/info-corp' | yq

       ------------- CORS --------------
       curl -s -u 'ayrton.senna@bravo.com' -H 'Accept: application/json' -H 'Origin: http://localhost:3000' \
       	-L -X GET 'http://localhost:8080/api/corporation/v1/info-corp' | jq
       --
       curl -s -u 'ayrton.senna@bravo.com:ayrton_pass' -H 'Accept: application/json' -H 'Origin: http://localhost:3000' \
       	-L -X GET 'http://localhost:8080/api/corporation/v1/info-corp' | jq
     */
    @Override
    @GetMapping(value = {"/info-corp", "/info-corp/"},
    		produces = { _APPLICATION_YAML_VALUE,
    			MediaType.APPLICATION_JSON_VALUE, 
    			MediaType.APPLICATION_XML_VALUE })
    public @ResponseBody ResponseEntity<InfoCorpResponse> infoCorp() {

	InfoCorpResponse response = new InfoCorpResponse(
		"All facilities operating", 
		new Date().toString(),
		"Toronto, Canada");

	response.add(WebMvcLinkBuilder
		.linkTo(WebMvcLinkBuilder.methodOn(InfoControllerImpl.class).infoCorp()).withSelfRel());

	return ResponseEntity.ok(response);
    }
}