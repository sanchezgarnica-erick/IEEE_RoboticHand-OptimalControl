package edu.uaeh.sagahand.beans.seguridad;

import org.apache.ibatis.session.SqlSession;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;

import edu.uaeh.sagahand.database.administracion.UsuarioEO;
import lombok.Setter;

@Setter
public class UsuariosServ extends JdbcDaoImpl {
	
	private SqlSession sqlSession;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user;
		Usuario usuario;
		
		user = (User) super.loadUserByUsername(username);
		usuario = new Usuario(user.getUsername(), user.getPassword(), user.isEnabled(), user.isAccountNonExpired(), user.isCredentialsNonExpired(), user.isAccountNonLocked(), user.getAuthorities());
		usuario.setUsuario(porNick(user.getUsername()));
		
		return usuario;
	}
	
	public UsuarioEO porNick(String nick) {
		UsuarioEO usuario;
		usuario = sqlSession.selectOne("administracion.usuarios.porNick", nick);
		return usuario;
	}
	
}
