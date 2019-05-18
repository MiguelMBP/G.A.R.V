package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import connection.DBConnection;
import vo.Usuario;

public class UsuariosDAO {

	public List<Usuario> mostrarUsuarios() {
		List<Usuario> usuarios = new ArrayList<>();
		DBConnection conex = new DBConnection();
		String sql = "select A1.username, A1.first_name, A1.last_name, A1.email, (select A2.cursoTutor from visitas_profesor A2 where A1.id = A2.usuario_id),(select A2.dni from visitas_profesor A2 where A1.id = A2.usuario_id) from auth_user A1";
		try (Statement st = conex.getConnection().createStatement(); ResultSet rs = st.executeQuery(sql);) {

			while (rs.next()) {
				Usuario u = new Usuario();
				u.setUsuario(rs.getString(1));
				u.setNombre(rs.getString(2));
				u.setApellidos(rs.getString(3));
				u.setCorreo(rs.getString(4));
				u.setCursoTutor(rs.getString(5));
				u.setDni(rs.getString(6));

				usuarios.add(u);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conex.desconectar();
		}

		return usuarios;
	}
	
	public Usuario mostrarUsuario(String id) {
		Usuario u = new Usuario();
		DBConnection conex = new DBConnection();
		String sql = "select A1.username, A1.first_name, A1.last_name, A1.email, A2.cursoTutor, A2.dni from auth_user A1, visitas_profesor A2 where A1.id = A2.usuario_id and A1.username like '" + id + "'";
		try (Statement st = conex.getConnection().createStatement(); ResultSet rs = st.executeQuery(sql);) {

			while (rs.next()) {
				
				u.setUsuario(rs.getString(1));
				u.setNombre(rs.getString(2));
				u.setApellidos(rs.getString(3));
				u.setCorreo(rs.getString(4));
				u.setCursoTutor(rs.getString(5));
				u.setDni(rs.getString(6));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conex.desconectar();
		}

		return u;
	}

	public void modificarUsuario(Usuario usuario) {
		DBConnection db = new DBConnection();
		String sql = "update visitas_profesor A1, auth_user A2 set A2.first_name = '" + usuario.getNombre() +"', A2.last_name = '" + usuario.getApellidos() + 
				"', A2.email = '" + usuario.getCorreo() + "', A1.cursoTutor = '" + usuario.getCursoTutor() + "', A1.dni = '" + usuario.getDni() + "' where A2.username = '" + usuario.getUsuario() + "' and A1.usuario_id = A2.id";
		try (Statement st = db.getConnection().createStatement();) {

			st.executeQuery(sql);
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.desconectar();
		}
		
	}

}
