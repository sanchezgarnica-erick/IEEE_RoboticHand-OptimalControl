package edu.uaeh.sagahand.components.administracion;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.uaeh.sagahand.components.MensajesC;
import edu.uaeh.sagahand.database.administracion.GrupoEO;
import edu.uaeh.sagahand.database.administracion.RolEO;
import edu.uaeh.sagahand.database.administracion.UsuarioEO;
import edu.uaeh.sagahand.utils.Modelo;

@Component("GruposC")
public class GruposC {
	
	@Autowired 
	private SqlSession sqlSession;
	
	@Autowired
	private MensajesC mensajesC;
	
	public Modelo<GrupoEO> modelo() {
		List<GrupoEO> listado;
		listado = sqlSession.selectList("administracion.grupos.listado");
		return new Modelo<>(listado);
	}
	
	public GrupoEO nuevo() {
		return new GrupoEO();
	}
	
	public void guardar(GrupoEO grupo) {
		if(grupo.getId() == null) {
			sqlSession.insert("administracion.grupos.insertar", grupo);
		} else {
			sqlSession.update("administracion.grupos.actualizar", grupo);
		}
	}
	
	public boolean agregarRol(Modelo<RolEO> modelo, RolEO rol) {
		if(!modelo.agregarElemento(rol, true)) {
			mensajesC.mensajeError("El Rol ya existe en el Grupo");
			return false;
		} else {
			mensajesC.mensajeInfo("Rol agregado exitosamente");
			return true;
		}
		
	}
	
	public void guardarRoles(GrupoEO grupo, List<RolEO> roles) {
		HashMap<String, Object> parametros;
		
		sqlSession.delete("administracion.gruposRoles.eliminarPorGrupo", grupo);
		
		if(!roles.isEmpty()) {
			parametros = new HashMap<>();
			parametros.put("grupo", grupo);
			parametros.put("roles", roles);
			sqlSession.insert("administracion.gruposRoles.insertar", parametros);
		}
	}
	
	public boolean agregarUsuario(Modelo<UsuarioEO> modelo, UsuarioEO usuario) {
		if(!modelo.agregarElemento(usuario, true)) {
			mensajesC.mensajeError("El Usuario ya existe en el Grupo");
			return false;
		} else {
			mensajesC.mensajeInfo("Usuario agregado exitosamente");
			return true;
		}
		
	}
	
	public void guardarUsuarios(GrupoEO grupo, List<UsuarioEO> usuarios) {
		HashMap<String, Object> parametros;
		
		sqlSession.delete("administracion.usuariosGrupos.eliminarPorGrupo", grupo);
		
		if(!usuarios.isEmpty()) {
			parametros = new HashMap<>();
			parametros.put("grupo", grupo);
			parametros.put("usuarios", usuarios);
			sqlSession.insert("administracion.usuariosGrupos.insertar", parametros);
		}
	}
	
	public void insertarUsuario(GrupoEO grupo, UsuarioEO usuario) {
		HashMap<String, Object> parametros;
		parametros = new HashMap<>();
		parametros.put("grupo", grupo);
		parametros.put("usuario", usuario);
		sqlSession.insert("administracion.usuariosGrupos.insertarUno", parametros);
	}
	
}
