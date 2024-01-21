package edu.uaeh.sagahand.database.dominios;

import org.apache.ibatis.type.Alias;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Alias("TiposObjetosDO")
public enum TiposObjetosDO {
	USUARIO("Usuario"),
	ORGANIZACION("Organizacion"),
	INTEGRANTE("Integrante");
	
	private final String nombre;
}
