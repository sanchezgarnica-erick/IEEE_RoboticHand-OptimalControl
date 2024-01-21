package edu.uaeh.sagahand.database.administracion;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import edu.uaeh.sagahand.utils.EntityObject;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Alias("RolEO")
public class RolEO extends EntityObject implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	private String nombre;
	private String descripcion;
}
