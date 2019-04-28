package dao;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import connection.DBConnection;
import vo.Alumno;
import vo.AlumnoApercibimiento;
import vo.ClaseApercibimiento;
import vo.Empresa;
import vo.Visita;

public class VisitasDAO {
	
	
	public List<Visita> mostrarVisitas() {
		List<Visita> visitas = new ArrayList<>();
		DBConnection conex = new DBConnection();
		String sql = "select CONCAT(A3.nombre, ' ', A3.apellidos) Docente, CONCAT(A1.nombre, ' ', A1.apellidos) Alumno, A2.nombre, A2.poblacion, A4.fecha, A2.distancia, A4.validada from visitas_alumno A1, visitas_empresa A2, visitas_profesor A3, visitas_visita A4 where A1.id = A4.alumno_id and A2.id = A1.empresa_id and A3.id = A4.profesor_id";
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
	
	public List<Alumno> mostrarAlumnoVisitas(String username) {
		List<Alumno> alumnos = new ArrayList<>();
		DBConnection conex = new DBConnection();
		String sql = "select A.nombre, A.apellidos, E.nombre, E.direccion, E.poblacion, E.latitud, E.longitud, E.distancia, V.fecha from visitas_alumno A, visitas_empresa E, visitas_profesor P, visitas_visita V "
				+ "where P.usuario_id = (select id from auth_user U where username = '" + username + "') and A.empresa_id = E.id and P.id = V.profesor_id and A.id = V.alumno_id";
		try (Statement st = conex.getConnection().createStatement(); ResultSet rs = st.executeQuery(sql);) {

			while (rs.next()) {
				Alumno a = new Alumno();
				a.setNombre(rs.getString(1));
				a.setApellidos(rs.getString(2));
				
				Empresa e = new Empresa();
				e.setNombre(rs.getString(3));
				e.setDireccion(rs.getString(4));
				e.setPoblacion(rs.getString(5));
				e.setLatitud(rs.getFloat(6));
				e.setLongitud(rs.getFloat(7));
				e.setDistancia(rs.getFloat(8));
				
				a.setEmpresa(e);
				a.setFecha(rs.getDate(9));

				
				alumnos.add(a);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conex.desconectar();
		}

		return alumnos;
	}

	public List<Alumno> mostrarTodosAlumnoVisitas() {
		List<Alumno> alumnos = new ArrayList<>();
		DBConnection conex = new DBConnection();
		String sql = "select A.nombre, A.apellidos, E.nombre, E.direccion, E.poblacion, E.latitud, E.longitud, E.distancia from visitas_alumno A, visitas_empresa E where A.empresa_id = E.id";
		try (Statement st = conex.getConnection().createStatement(); ResultSet rs = st.executeQuery(sql);) {

			while (rs.next()) {
				Alumno a = new Alumno();
				a.setNombre(rs.getString(1));
				a.setApellidos(rs.getString(2));
				
				Empresa e = new Empresa();
				e.setNombre(rs.getString(3));
				e.setDireccion(rs.getString(4));
				e.setPoblacion(rs.getString(5));
				e.setLatitud(rs.getFloat(6));
				e.setLongitud(rs.getFloat(7));
				e.setDistancia(rs.getFloat(8));
				
				a.setEmpresa(e);

				
				alumnos.add(a);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conex.desconectar();
		}

		return alumnos;
	}
}
