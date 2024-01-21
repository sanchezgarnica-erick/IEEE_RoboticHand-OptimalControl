package edu.uaeh.sagahand.utils;

public class EntityObject implements Cloneable {
	
	public EntityObject() {
		super();
	}
	
	public Integer getId(){
		return 0;
	}
	
	@Override
	public boolean equals(Object obj) {
		EntityObject check;
		
		try {
			check = (EntityObject) obj;
		} catch(Exception e) {
			return false;
		}
		
		
		if(this.getClass().equals(obj.getClass()))
			if(this.getId().equals(check.getId()))
				return true;

		return false;
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
}
