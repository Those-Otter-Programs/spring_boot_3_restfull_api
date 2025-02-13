package com.thoseop.serialization.converter;

import org.springframework.http.MediaType;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

public class OtterYamlJackson2HttpMessageConverter extends AbstractJackson2HttpMessageConverter {

    public OtterYamlJackson2HttpMessageConverter() {
	// JsonInclude.Include.NON_NULL - sinaliza a engine para serializar somente as propriedades
	// que não tiverem valor null.
	super(new YAMLMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL), // só serializa props. com valor não null
		MediaType.parseMediaType("application/x-yaml") // message converter customizado - yaml 
	);
    }
}