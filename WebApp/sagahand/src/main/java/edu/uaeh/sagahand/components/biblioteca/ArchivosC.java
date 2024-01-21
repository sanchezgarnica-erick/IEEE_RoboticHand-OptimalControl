package edu.uaeh.sagahand.components.biblioteca;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.webflow.execution.RequestContextHolder;

import edu.uaeh.sagahand.beans.seguridad.Usuario;
import edu.uaeh.sagahand.components.MensajesC;
import edu.uaeh.sagahand.database.biblioteca.ArchivoEO;
import edu.uaeh.sagahand.database.dominios.TiposObjetosDO;
import edu.uaeh.sagahand.utils.EntityObject;
import edu.uaeh.sagahand.utils.Modelo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("ArchivosC")
public class ArchivosC {
	
	@Autowired
	private SqlSession sqlSession;
	
	@Autowired
	private MensajesC mensajesC;

	public ArchivosC() {
		super();
		log.debug("Se crea Componente ArchivosC");
	}
	
	public ArchivoEO nuevo() {
		return new ArchivoEO();
	}
	
	public void iniciar() {
		HashMap<String, Object> flowScope, conversationScope;
		TiposObjetosDO tipoObjeto;
		EntityObject objeto;
		Boolean botonSalir;
		
		flowScope = (HashMap<String, Object>) RequestContextHolder.getRequestContext().getFlowScope().asMap();
		conversationScope = (HashMap<String, Object>) RequestContextHolder.getRequestContext().getConversationScope().asMap();
		tipoObjeto = (TiposObjetosDO) conversationScope.get("tipoObjeto");
		objeto = (EntityObject) conversationScope.get("objeto");
		
		if(tipoObjeto == null) {
			tipoObjeto = TiposObjetosDO.USUARIO;
			objeto = ((Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsuario();
			botonSalir = false;
		} else {
			botonSalir = true;
		}
		
		flowScope.put("tipoObjeto", tipoObjeto);
		flowScope.put("objeto", objeto);
		flowScope.put("botonSalir", botonSalir);
	}
	
	public Modelo<ArchivoEO> modelo(TiposObjetosDO tipoObjeto, EntityObject objeto){
		HashMap<String, Object> parametros;
		List<ArchivoEO> listado;
		
		parametros = new HashMap<>();
		parametros.put("tipoObjeto", tipoObjeto);
		parametros.put("objeto", objeto);
		
		listado = sqlSession.selectList("biblioteca.archivos.listado", parametros);
		
		return new Modelo<>(listado);
	}
	
	public void cargarObjeto(ArchivoEO archivo) {
		byte[] objeto;
		
		if(archivo != null) {
			if(archivo.getObjeto() == null) {
				objeto = sqlSession.selectOne("biblioteca.archivos.obtenerObjeto", archivo);
				archivo.setObjeto(objeto);
			}
		}
	}
	
	public void guardar(ArchivoEO archivo, TiposObjetosDO tipoObjeto, EntityObject objeto) {
		HashMap<String, Object> parametros;
		
		parametros = new HashMap<>();
		parametros.put("tipoObjeto", tipoObjeto);
		parametros.put("objeto", objeto);
		parametros.put("archivo", archivo);
		
		sqlSession.insert("biblioteca.archivos.insertar", parametros);
		sqlSession.insert("biblioteca.archivos.insertarRelacion", parametros);
		mensajesC.mensajeInfo("Archivo agregado exitosamente");
	}
	
	public void eliminar(ArchivoEO archivo) {
		sqlSession.delete("biblioteca.archivos.eliminar", archivo);
		mensajesC.mensajeInfo("Archivo eliminado exitosamente");
	}
}
