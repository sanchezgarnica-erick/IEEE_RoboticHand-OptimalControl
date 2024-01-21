package edu.uaeh.sagahand.database.control;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.apache.ibatis.type.Alias;

import edu.uaeh.sagahand.utils.EntityObject;
import edu.uaeh.sagahand.utils.LocalDateTimeFormater;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Alias("HandEO")
public class HandEO extends EntityObject implements Serializable {
	private static final long serialVersionUID = 8998182105698177840L;

	private Integer id;
	private Boolean activa;
	private LocalDateTime ultimaComunicacion;
	private FingerEO indexFinger;
	private FingerEO middleFinger;
	private FingerEO ringFinger;
	private FingerEO littleFinger;
	private FingerEO thumb;
	private String ultimaComunicacionTexto;
	
	public HandEO() {
		super();
		activa = false;
		indexFinger = new FingerEO(3.5404, 1.7874);
		middleFinger = new FingerEO(4.8642, 2.1743);
		ringFinger = new FingerEO(5.9472, 2.7556);
		littleFinger = new FingerEO(5.7690, 2.4788);
		thumb = new FingerEO(8.0262, 3.5038);
	}
	
	public void setUltimaComunicacion(LocalDateTime ultimaComunicacion) {
		this.ultimaComunicacion = ultimaComunicacion;
		ultimaComunicacionTexto = LocalDateTimeFormater.dateTimeFormat(this.ultimaComunicacion);
	}
	
	public void compute() {
		indexFinger.compute();
		middleFinger.compute();
		ringFinger.compute();
		littleFinger.compute();
		thumb.compute();
	}
}
