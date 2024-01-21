package edu.uaeh.sagahand.utils;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class Modelo<T> implements Serializable {
	private static final long serialVersionUID = -7395537356554198650L;
	private List<T> listado;
	private T seleccionado;
	private List<T> seleccionadoMultiple;
	
	public Modelo() {
		super();
	}
	
	public Modelo(List<T> listado) {
		super();
		this.listado = listado;
		
		if(this.listado != null){
			if(!this.listado.isEmpty()){
				seleccionado = this.listado.get(0);
				seleccionadoMultiple = new LinkedList<T>(); 
			} else {
				seleccionado = null;
			}
		} else {
			this.listado = new LinkedList<T>();
			seleccionado = null;
		}
	}	
	
	public Modelo(List<T> listado, List<T> seleccionadoMultiple) {
		super();
		this.listado = listado;
		
		if(this.listado != null){
			if(!this.listado.isEmpty()){
				seleccionado = this.listado.get(0);
				if (seleccionadoMultiple != null) {
					this.seleccionadoMultiple = seleccionadoMultiple;
				}else {
					this.seleccionadoMultiple = new LinkedList<T>(); 
				}
			} else {
				seleccionado = null;
			}
		} else {
			seleccionado = null;
		}
	}
	
	public void quitarElemento(T elemento) {
		if(elemento.equals(seleccionado)) {
			seleccionado = null;
		}
		
		if(listado.contains(elemento)) {
			listado.remove(elemento);
		}
		
		if(seleccionado == null && !listado.isEmpty()) {
			seleccionado = listado.get(0);
		}
	}
	
	public boolean agregarElemento(T elemento, boolean seleccionar) {
		if(!listado.contains(elemento)) {
			listado.add(elemento);
			if(seleccionar) {
				seleccionado = listado.get(listado.indexOf(elemento));
			}
			return true;
		}
		return false; //Elemento ya existe en listado
	}
	
	public void agregarElemento(T elemento, boolean seleccionar, boolean sinRetorno) {
		agregarElemento(elemento, seleccionar);
	}
	
	public void reiniciarSeleccion() {
		if(!listado.isEmpty()) {
			seleccionado = listado.get(0);
		} else {
			seleccionado = null;
		}
	}
}
