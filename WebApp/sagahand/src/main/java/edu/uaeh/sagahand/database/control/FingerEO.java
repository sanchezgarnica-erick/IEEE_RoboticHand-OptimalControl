package edu.uaeh.sagahand.database.control;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import edu.uaeh.sagahand.utils.EntityObject;
import lombok.Setter;
import lombok.ToString;
import lombok.Getter;

@Getter
@Setter
@ToString
@Alias("FingerEO")
public class FingerEO extends EntityObject implements Serializable {
	private static final long serialVersionUID = 2070373023640454425L;

	private Integer id;
	private double control;
	private double state;
	private double setPoint;
	private double error;
	private double kp;
	private double ki;
	private double kd;
	
	public FingerEO() {
		super();
		control = 0;
		state = 0;
		setPoint = 0;
		error = 0;
		kp = 1;
		ki = 0;
		kd = 0;
	}
	
	public FingerEO(double kp, double ki) {
		this();
		this.kp = kp;
		this.ki = ki;
	}
	
	public void compute() {
		error = setPoint - state;
	}
	
}
