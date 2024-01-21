package edu.uaeh.sagahand.components.administracion;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.uaeh.sagahand.database.administracion.GrupoEO;
import edu.uaeh.sagahand.database.administracion.RolEO;
import edu.uaeh.sagahand.utils.Modelo;

@Component("RolesC")
public class RolesC {
	
	@Autowired 
	private SqlSession sqlSession;
	
	public Modelo<RolEO> modelo() {
		List<RolEO> listado;
		listado = sqlSession.selectList("administracion.roles.listado");
		return new Modelo<>(listado);
	}
	
	public Modelo<RolEO> modelo(List<RolEO> roles) {
		List<RolEO> listado;
		listado = sqlSession.selectList("administracion.roles.listado");
		for(RolEO rol : roles) {
			listado.remove(rol);
		}
		return new Modelo<>(listado);
	}
	
	public RolEO nuevo() {
		return new RolEO();
	}
	
	public Modelo<RolEO> modeloPorGrupo(GrupoEO grupo) {
		List<RolEO> listado;
		listado = sqlSession.selectList("administracion.gruposRoles.rolesPorGrupo", grupo);
		return new Modelo<>(listado);
	}
	
}
