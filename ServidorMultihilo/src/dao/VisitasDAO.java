package dao;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import connection.DBConnection;
import vo.AlumnoApercibimiento;
import vo.ClaseApercibimiento;
import vo.Visita;

public class VisitasDAO {
	
	
	public List<Visita> mostrarVisitas() {
		List<Visita> visitas = new ArrayList<>();
		DBConnection conex = new DBConnection();
		String sql = "select CONCAT(A3.nombre, ' ', A3.apellidos) Docente, CONCAT(A1.nombre, ' ', A1.apellidos) Alumno, A2.nombre, A2.poblacion, A4.fecha, A4.distancia, A4.validada from visitas_alumno A1, visitas_empresa A2, visitas_profesor A3, visitas_visita A4 where A1.id = A4.alumno_id and A2.id = A1.empresa_id and A3.id = A4.profesor_id";
		try (Statement st = conex.getConnection().createStatement(); ResultSet rs = st.executeQuery(sql);) {

			while (rs.next()) {
				Visita v = new Visita();
				v.setDocente(rs.getString(1));
				v.setAlumno(rs.getString(2));
				v.setEmpresa(rs.getString(3));
				v.setPoblacion(rs.getString(4));
				v.setFecha(rs.getDate(5));
				v.setDistancia(rs.getDouble(6));
				v.setValidada(rs.getBoolean(7));

				visitas.add(v);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conex.desconectar();
		}

		return visitas;
	}
}
