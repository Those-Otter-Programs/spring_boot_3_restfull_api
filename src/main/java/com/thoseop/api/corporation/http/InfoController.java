package com.thoseop.api.corporation.http;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/corporation/v1")
public class InfoController {

    @Value("${spring.application.name}")
    private String appName;
    
    @GetMapping(value = {"", "/", "/info", "/info/"})
    public ResponseEntity<Model> info(Model model) {
	
	model.addAttribute("Application", appName);
	model.addAttribute("Description", "A Spring Boot 3 RESTfull API sample");
	
	return ResponseEntity.ok(model);
    }
}