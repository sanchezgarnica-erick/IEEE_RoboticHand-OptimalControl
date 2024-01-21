package edu.uaeh.sagahand.components.administracion;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import edu.uaeh.sagahand.beans.seguridad.Usuario;
import edu.uaeh.sagahand.components.MensajesC;
import edu.uaeh.sagahand.database.administracion.GrupoEO;
import edu.uaeh.sagahand.database.administracion.UsuarioEO;
import edu.uaeh.sagahand.utils.Modelo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("UsuariosC")
public class UsuariosC {
	
	@Autowired 
	private SqlSession sqlSession;
	
	@Autowired
	private MensajesC mensajesC;
	
	public UsuariosC() {
		super();
		log.debug("Se crea componente UsuariosC");
	}

	public Modelo<UsuarioEO> modelo() {
		List<UsuarioEO> listado;
		listado = sqlSession.selectList("administracion.usuarios.listado");
		return new Modelo<>(listado);
	}
	
	public Modelo<UsuarioEO> modeloPorGrupo(GrupoEO grupo) {
		List<UsuarioEO> listado;
		listado = sqlSession.selectList("administracion.usuariosGrupos.usuariosPorGrupo", grupo);
		return new Modelo<>(listado);
	}
	
	public UsuarioEO nuevo() {
		return new UsuarioEO();
	}
	
	public void guardar(UsuarioEO usuario) {
		if(usuario.getId() == null) {
			sqlSession.insert("administracion.usuarios.insertar", usuario);
			mensajesC.mensajeInfo("Usuario agregado exitosamente");
		} else {
			sqlSession.update("administracion.usuarios.actualizar", usuario); 
			mensajesC.mensajeInfo("Usuario actualizado exitosamente");
		}
	}
	
	public boolean cambiarPassword(String actual, String nueva){
		boolean respuesta;
		Boolean actualValida;
		HashMap<String, Object> parametros;
		UsuarioEO usuario;
		respuesta = false;
		parametros = new HashMap<>();
		usuario = ((Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsuario();
		parametros.put("actual", actual);
		parametros.put("nueva", nueva);
		parametros.put("usuario", usuario);
		actualValida = sqlSession.selectOne("administracion.usuarios.comprobarPassword", parametros);
		if(actualValida){
			sqlSession.update("administracion.usuarios.cambiarPassword", parametros);
			respuesta = true;
		} else {
			respuesta = false;
		}
		return respuesta;
	}
	
}
