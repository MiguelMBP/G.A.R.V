package dao;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import connection.DBConnection;
import vo.Usuario;

public class UsuariosDAO {

	public List<Usuario> mostrarUsuarios() {
		List<Usuario> usuarios = new ArrayList<>();
		DBConnection conex = new DBConnection();
		String sql = "select A1.username, CONCAT(A2.nombre, ' ', A2.apellidos) Nombre, A1.email, A2.cursoTutor from auth_user A1, visitas_profesor A2 where A1.id = A2.usuario_id";
		try (Statement st = conex.getConnection().createStatement(); ResultSet rs = st.executeQuery(sql);) {

			while (rs.next()) {
				Usuario u = new Usuario();
				u.setUsuario(rs.getString(1));
				u.setNombre(rs.getString(2));
				u.setCorreo(rs.getString(3));
				u.setCursoTutor(rs.getString(4));

				usuarios.add(u);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conex.desconectar();
		}

		return usuarios;
	}
}
