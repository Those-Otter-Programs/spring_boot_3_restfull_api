package com.thoseop.serialization.converter;

import org.springframework.http.MediaType;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

public class OtterYamlJackson2HttpMessageConverter extends AbstractJackson2HttpMessageConverter {

    public OtterYamlJackson2HttpMessageConverter() {
	super(new YAMLMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL), // only serializes not null props
		MediaType.parseMediaType("application/x-yaml") // customized message converter  - yaml 
	);
    }
}