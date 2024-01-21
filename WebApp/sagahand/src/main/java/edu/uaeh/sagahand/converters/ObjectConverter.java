package edu.uaeh.sagahand.converters;

import java.util.Base64;
import java.util.StringTokenizer;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@FacesConverter("ObjectConverter")
public class ObjectConverter implements Converter {
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(ObjectConverter.class);

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		String cadena;
		ObjectMapper objectMapper;
		StringTokenizer tokenizer;
		Object objeto;
		String clase;
		String json;
		
		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		
		cadena = new String(Base64.getDecoder().decode(value));
		
		tokenizer = new StringTokenizer(cadena, "@", true);
		
		if(tokenizer.countTokens() >= 3){
			clase = tokenizer.nextToken();
			tokenizer.nextToken(); //Descartar la @
			json = tokenizer.nextToken("");
		} else {
			clase = null;
			json = null;
		}
		
		try {
			objeto = objectMapper.readValue(json, Class.forName(clase));
		} catch (Exception e) {
			objeto = null;
			e.printStackTrace();
		}
		
		return objeto;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		StringBuilder stringBuilder;
		ObjectMapper objectMapper;
		String respuesta;
		
		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		
		stringBuilder = new StringBuilder(value.getClass().getName());
		stringBuilder.append("@");
		try {
			stringBuilder.append(objectMapper.writeValueAsString(value));
		} catch (JsonProcessingException e1) {
			e1.printStackTrace();
		}
		
		respuesta = Base64.getEncoder().encodeToString(stringBuilder.toString().getBytes());
		
		return respuesta;
	}

}

