package com.thoseop.api.corporation.http;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/corporation/v1")
public class InfoController {

    static final String _APPLICATION_YAML_VALUE = "application/x-yaml";

    @Value("${spring.application.name}")
    private String appName;
    
    /**
       ------------- JSON --------------
       curl -s -H 'Accept: application/json' -L -X GET 'http://localhost:8080/api/corporation/v1/info' | jq

       -------------- XML --------------
       curl -s -H 'Accept: application/xml' -L -X GET 'http://localhost:8080/api/corporation/v1/info' | xmllint --format -
       
       ------------- YAML --------------
       curl -s -H 'Accept: application/x-yaml' -L -X GET 'http://localhost:8080/api/corporation/v1/info' | yq

       ------------- CORS --------------
       curl -s -H 'Accept: application/json' -H 'Origin: http://localhost:3000' \
       	-L -X GET 'http://localhost:8080/api/corporation/v1/info' | jq
        
     * @param model
     * @return
     */
    @GetMapping(value = {"", "/", "/info", "/info/"},
    		produces = { MediaType.APPLICATION_JSON_VALUE, 
    			MediaType.APPLICATION_XML_VALUE, 
    			_APPLICATION_YAML_VALUE })
    public @ResponseBody ResponseEntity<Model> info(Model model) {
	
	model.addAttribute("Application", appName);
	model.addAttribute("Description", "A Spring Boot 3 RESTfull API sample");
	
	return ResponseEntity.ok(model);
    }
}