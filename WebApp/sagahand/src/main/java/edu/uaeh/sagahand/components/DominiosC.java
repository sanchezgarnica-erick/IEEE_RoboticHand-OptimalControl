package edu.uaeh.sagahand.components;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("DominiosC")
public class DominiosC {
	
	public DominiosC() {
		super();
		log.debug("Se crea componente DominiosC");
	}

//	public Modelo<GenerosDO> generos() {
//		return new Modelo<>(Arrays.asList(GenerosDO.values()));
//	}
	
}
