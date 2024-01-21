package edu.uaeh.sagahand.database.administracion;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import edu.uaeh.sagahand.utils.EntityObject;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Alias("GrupoEO")
public class GrupoEO extends EntityObject implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	
	@NotBlank(message = "Favor de anotar el Nombre")
	private String nombre;
	
	@NotBlank(message = "Favor de anotar la Descripción")
	private String descripcion;
}
