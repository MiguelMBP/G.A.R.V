package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import connection.DBConnection;
import vo.Usuario;
/**
 * Clase que se encarga de realizar consultas a la base de datos para las tablas de usuarios
 * @author mmbernal
 *
 */
public class UsuariosDAO {

	/**
	 * Recoge los usuarios de la base de datos
	 * @return Lista con objetos Usuario
	 */
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
	
	/**
	 * Recoge el usuario de la base de datos con el ID pasado por parámetros
	 * @param id
	 * @return El objeto Usuario
	 */
	public Usuario mostrarUsuario(String id) {
		Usuario u = new Usuario();
		DBConnection conex = new DBConnection();
		String sql = "select A1.username, A1.first_name, A1.last_name, A1.email, A2.cursoTutor, A2.dni from auth_user A1, visitas_profesor A2 where A1.id = A2.usuario_id and A1.username like ?";
		try (PreparedStatement st = conex.getConnection().prepareStatement(sql); ) {
			st.setString(1, id);
			ResultSet rs = st.executeQuery();
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

	/**
	 * Actualiza el usuario de la base de datos con los datos del objeto pasado por parámetros
	 * @param usuario
	 */
	public void modificarUsuario(Usuario usuario) {
		DBConnection db = new DBConnection();
		String sql = "update visitas_profesor A1, auth_user A2 set A2.first_name = ?, A2.last_name = ?, A2.email = ?, A1.cursoTutor = ?, A1.dni = ? where A2.username = ? and A1.usuario_id = A2.id";
		try (PreparedStatement st = db.getConnection().prepareStatement(sql);) {
			st.setString(1, usuario.getNombre());
			st.setString(2, usuario.getApellidos());
			st.setString(3, usuario.getCorreo());
			st.setString(4, usuario.getCursoTutor());
			st.setString(5, usuario.getDni());
			st.setString(6, usuario.getUsuario());
			st.executeUpdate();
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.desconectar();
		}
		
	}

}
