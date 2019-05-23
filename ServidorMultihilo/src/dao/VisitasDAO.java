package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import connection.DBConnection;
import vo.Alumno;
import vo.Empresa;
import vo.Visita;

/**
 * Clase que se encarga de realizar consultas a la base de datos para las tablas de visitas
 * @author mmbernal
 *
 */
public class VisitasDAO {

	/**
	 * Recoge las visitas de la base de datos
	 * @return Lista con objetos Visita 
	 */
	public List<Visita> mostrarVisitas() {
		List<Visita> visitas = new ArrayList<>();
		DBConnection conex = new DBConnection();
		String sql = "select A4.id, CONCAT(A5.first_name, ' ', A5.last_name) Docente, CONCAT(A1.nombre, ' ', A1.apellidos) Alumno, A2.nombre, A2.poblacion, A4.fecha, A2.distancia, A4.validada from visitas_alumno A1, visitas_empresa A2, visitas_profesor A3, visitas_visita A4, auth_user A5 where A1.id = A4.alumno_id and A2.id = A1.empresa_id and A3.id = A4.profesor_id and A3.usuario_id = A5.id";
		try (Statement st = conex.getConnection().createStatement(); ResultSet rs = st.executeQuery(sql);) {

			while (rs.next()) {
				Visita v = new Visita();
				v.setId(rs.getInt(1));
				v.setDocente(rs.getString(2));
				v.setAlumno(rs.getString(3));
				v.setEmpresa(rs.getString(4));
				v.setPoblacion(rs.getString(5));
				v.setFecha(rs.getDate(6));
				v.setDistancia(rs.getDouble(7));
				v.setValidada(rs.getBoolean(8));

				visitas.add(v);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conex.desconectar();
		}

		return visitas;
	}

	/**
	 * Recoge las visitas realizadas por un profesor de la base de datos
	 * @param username
	 * @return Lista con objetos alumno
	 */
	public List<Alumno> mostrarAlumnoVisitas(String username) {
		List<Alumno> alumnos = new ArrayList<>();
		DBConnection conex = new DBConnection();
		String sql = "select A.nombre, A.apellidos, E.nombre, E.direccion, E.poblacion, E.latitud, E.longitud, E.distancia, V.fecha from visitas_alumno A, visitas_empresa E, visitas_profesor P, visitas_visita V "
				+ "where P.usuario_id = (select id from auth_user U where username = ?) and A.empresa_id = E.id and P.id = V.profesor_id and A.id = V.alumno_id order by V.fecha";
		try (PreparedStatement st = conex.getConnection().prepareStatement(sql);) {

			st.setString(1, username);
			ResultSet rs = st.executeQuery();
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

	/**
	 * Recoge los alumnos y empresas asignadas del módulo de visitas de la base de datos
	 * @return Lista con objetos alumno
	 */
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

	/**
	 * Recoge las empresas de la base de datos
	 * @return Lista con objetos Empresa
	 */
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

	/**
	 * Inserta una empresa en la base de datos
	 * @param empresa
	 * @return Entero indicando la fila insertada
	 */
	public int insertarEmpresa(Empresa empresa) {
		DBConnection conex = new DBConnection();
		String sql = "Insert into visitas_empresa values (0, ?, ?, ?, ?, ?, ?, ?)";
		int id = -1;

		try (PreparedStatement st = conex.getConnection().prepareStatement(sql);) {
			st.setString(1, empresa.getCif());
			st.setString(2, empresa.getNombre());
			st.setString(3, empresa.getPoblacion());
			st.setString(4, empresa.getDireccion());
			st.setFloat(5, empresa.getLatitud());
			st.setFloat(6, empresa.getLongitud());
			st.setFloat(7, empresa.getDistancia());
			id = st.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		} finally {
			conex.desconectar();
		}
		return id;
	}

	/**
	 * Inserta una empresa en la base de datos
	 * @param alumno
	 * @return Entero indicando la fila insertada
	 */
	public int insertarAlumno(Alumno alumno) {
		DBConnection conex = new DBConnection();
		String sql = "Insert into visitas_alumno values (0, ?, ?, ?, ?, ?)";
		int id = -1;

		try (PreparedStatement st = conex.getConnection().prepareStatement(sql);) {
			st.setString(1, alumno.getDni());
			st.setString(2, alumno.getNombre());
			st.setString(3, alumno.getApellidos());
			st.setString(4, alumno.getCurso());
			st.setInt(5, alumno.getEmpresa().getId());
			id = st.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			conex.desconectar();
		}
		return id;
	}

	/**
	 * Recoge el id de la tabla profesor que corresponde al usuario insertado
	 * @param usuario
	 * @return El id del usuario (Entero)
	 */
	public int idUsuario(String usuario) {
		DBConnection conex = new DBConnection();
		String sql = "SELECT B.id FROM auth_user A, visitas_profesor B where username= ? and A.id = B.usuario_id";
		int id = -1;

		try (PreparedStatement st = conex.getConnection().prepareStatement(sql);) {
			st.setString(1, usuario);

			ResultSet rs = st.executeQuery();
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

	/**
	 * Cambia el estado de la visita a validado/invalidado
	 * @param id
	 * @param activo
	 */
	public void inValidarVisita(int id, boolean activo) {
		DBConnection db = new DBConnection();
		String sql = "update visitas_visita set validada = ? where id = ?";
		try (PreparedStatement st = db.getConnection().prepareStatement(sql);) {
			st.setBoolean(1, activo);
			st.setInt(2, id);
			st.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.desconectar();
		}
	}

	/**
	 * Recoge los alumnos del módulo de visitas de la base de datos
	 * @return Lista de objetos Alumno
	 */
	public List<Alumno> mostrarAlumnos() {
		List<Alumno> alumnos = new ArrayList<>();
		DBConnection conex = new DBConnection();
		String sql = "select A.id, A.nombre, A.apellidos, A.dni, A.curso, E.id, E.nombre from visitas_alumno A, visitas_empresa E where A.empresa_id = E.id";
		try (Statement st = conex.getConnection().createStatement(); ResultSet rs = st.executeQuery(sql);) {

			while (rs.next()) {
				Alumno a = new Alumno();
				a.setId(rs.getInt(1));
				a.setNombre(rs.getString(2));
				a.setApellidos(rs.getString(3));
				a.setDni(rs.getString(4));
				a.setCurso(rs.getString(5));

				Empresa e = new Empresa();
				e.setId(rs.getInt(6));
				e.setNombre(rs.getString(7));

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

	/**
	 * Recoge la empresa de la base de datos que corresponde al id pasado por parámetros
	 * @param id
	 * @return La empresa seleccionada
	 */
	public Empresa mostrarEmpresa(String id) {
		DBConnection conex = new DBConnection();
		String sql = "SELECT * FROM visitas_empresa where id = ?";
		Empresa e = new Empresa();
		try (PreparedStatement st = conex.getConnection().prepareStatement(sql);) {
			st.setInt(1, Integer.parseInt(id));
			ResultSet rs = st.executeQuery();
			rs.next();

			e.setId(rs.getInt(1));
			e.setCif(rs.getString(2));
			e.setNombre(rs.getString(3));
			e.setDireccion(rs.getString(4));
			e.setPoblacion(rs.getString(5));
			e.setLatitud(rs.getFloat(6));
			e.setLongitud(rs.getFloat(7));
			e.setDistancia(rs.getFloat(8));

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			conex.desconectar();
		}

		return e;
	}

	/**
	 * Actualiza una empresa de la base de datos
	 * @param empresa
	 * @return El número de fila modificada
	 */
	public int modificarEmpresa(Empresa empresa) {
		DBConnection conex = new DBConnection();
		String sql = "update visitas_empresa set cif=?, nombre=?, poblacion=?, direccion=?, latitud=?, longitud=?, distancia=? where id = ?";
		int id = -1;

		try (PreparedStatement st = conex.getConnection().prepareStatement(sql);) {
			st.setString(1, empresa.getCif());
			st.setString(2, empresa.getNombre());
			st.setString(3, empresa.getPoblacion());
			st.setString(4, empresa.getDireccion());
			st.setFloat(5, empresa.getLatitud());
			st.setFloat(6, empresa.getLongitud());
			st.setFloat(7, empresa.getDistancia());
			st.setInt(8,  empresa.getId());
			id = st.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		} finally {
			conex.desconectar();
		}
		return id;
		
	}
	
	/**
	 * Recoge el alumno de la base de datos que corresponde al id pasado por parámetros
	 * @param id
	 * @return El alumno seleccionado
	 */
	public Alumno mostrarAlumno(String id) {
		Alumno a = new Alumno();
		DBConnection conex = new DBConnection();
		String sql = "select A.id, A.nombre, A.apellidos, A.dni, A.curso, E.id, E.nombre from visitas_alumno A, visitas_empresa E where A.empresa_id = E.id and A.id = ?";
		try (PreparedStatement st = conex.getConnection().prepareStatement(sql); ) {
			st.setInt(1, Integer.parseInt(id));
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				
				a.setId(rs.getInt(1));
				a.setNombre(rs.getString(2));
				a.setApellidos(rs.getString(3));
				a.setDni(rs.getString(4));
				a.setCurso(rs.getString(5));

				Empresa e = new Empresa();
				e.setId(rs.getInt(6));
				e.setNombre(rs.getString(7));

				a.setEmpresa(e);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conex.desconectar();
		}

		return a;
	}

	/**
	 * Actualiza un alumno de la base de datos
	 * @param alumno
	 * @return El número de fila modificada
	 */
	public int modificarAlumno(Alumno alumno) {
		DBConnection conex = new DBConnection();
		String sql = "update visitas_alumno set dni=?, nombre=?, apellidos=?, curso=?, empresa_id=? where id = ?";
		int id = -1;

		try (PreparedStatement st = conex.getConnection().prepareStatement(sql);) {
			st.setString(1, alumno.getDni());
			st.setString(2, alumno.getNombre());
			st.setString(3, alumno.getApellidos());
			st.setString(4, alumno.getCurso());
			st.setInt(5, alumno.getEmpresa().getId());
			st.setInt(6, alumno.getId());
			id = st.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			conex.desconectar();
		}
		return id;
	}

}
