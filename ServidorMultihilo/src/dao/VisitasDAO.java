package dao;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import connection.DBConnection;
import vo.Alumno;
import vo.AlumnoApercibimiento;
import vo.ClaseApercibimiento;
import vo.Empresa;
import vo.RegistroVisita;
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
		String sql = "select A.id, A.nombre, A.apellidos, E.id, E.nombre, E.direccion, E.poblacion, E.latitud, E.longitud, E.distancia from visitas_alumno A, visitas_empresa E where A.empresa_id = E.id";
		try (Statement st = conex.getConnection().createStatement(); ResultSet rs = st.executeQuery(sql);) {

			while (rs.next()) {
				Alumno a = new Alumno();
				a.setId(rs.getInt(1));
				a.setNombre(rs.getString(2));
				a.setApellidos(rs.getString(3));
				
				Empresa e = new Empresa();
				e.setId(rs.getInt(4));
				e.setNombre(rs.getString(5));
				e.setDireccion(rs.getString(6));
				e.setPoblacion(rs.getString(7));
				e.setLatitud(rs.getFloat(8));
				e.setLongitud(rs.getFloat(9));
				e.setDistancia(rs.getFloat(10));
				
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
	
	public List<Empresa> mostrarEmpresas() {
		List<Empresa> empresas = new ArrayList<>();
		DBConnection conex = new DBConnection();
		String sql = "SELECT * FROM visitas_empresa";
		try (Statement st = conex.getConnection().createStatement(); ResultSet rs = st.executeQuery(sql);) {

			while (rs.next()) {
				
				Empresa e = new Empresa();
				e.setId(rs.getInt(1));
				e.setCif(rs.getString(2));
				e.setNombre(rs.getString(3));
				e.setDireccion(rs.getString(4));
				e.setPoblacion(rs.getString(5));
				e.setLatitud(rs.getFloat(6));
				e.setLongitud(rs.getFloat(7));
				e.setDistancia(rs.getFloat(8));


				
				empresas.add(e);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conex.desconectar();
		}

		return empresas;
	}

	public int insertarEmpresa(Empresa empresa) {
		DBConnection conex = new DBConnection();
		String sql = "Insert into visitas_empresa values (0, '" + empresa.getCif() + "', '" + empresa.getNombre() + "', '" + empresa.getPoblacion() + "', "
				+ "'" + empresa.getDireccion() + "', " + empresa.getLatitud() + ", " + empresa.getLongitud() + ", " + empresa.getDistancia() + ")";
		int id = -1;
		
		try (Statement st = conex.getConnection().createStatement();) {

			st.executeQuery(sql);
			
			sql = "SELECT MAX(id) FROM visitas_empresa";
			
			ResultSet rs = st.executeQuery(sql);
			rs.next();
			id = rs.getInt(1);
			
			st.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		} finally {
			conex.desconectar();
		}
		return id;
	}

	public int insertarAlumno(Alumno alumno) {
		DBConnection conex = new DBConnection();
		String sql = "Insert into visitas_alumno values (0, '" + alumno.getDni() + "', '" + alumno.getNombre() + "', '" + alumno.getApellidos() + "', "
				+ "'" + alumno.getCurso() + "', " + alumno.getEmpresa().getId() + ")";
		int id = -1;
		
		try (Statement st = conex.getConnection().createStatement();) {

			st.executeQuery(sql);
			
			sql = "SELECT MAX(id) FROM visitas_alumno";
			
			ResultSet rs = st.executeQuery(sql);
			rs.next();
			id = rs.getInt(1);
			
			st.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		} finally {
			conex.desconectar();
		}
		return id;
	}
	
	//TODO
	public int insertarVisita(RegistroVisita visita) {
		try {
			byte[] byteArray = visita.getImagen();
			FileOutputStream fileOuputStream = 
		              new FileOutputStream("testing2.jpeg", false); 
		    fileOuputStream.write(byteArray);
		    fileOuputStream.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return -1;
	}
}
