package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import connection.DBConnection;
import vo.Alumno;
import vo.AlumnoApercibimiento;
import vo.Apercibimiento;
import vo.ClaseApercibimiento;
import vo.Empresa;
import vo.TutorAlumno;
import vo.TutorAsignatura;

public class ApercibimientoDAO {

	public List<Apercibimiento> mostrarApercibimientos() {
		List<Apercibimiento> apercibimientos = new ArrayList<>();
		DBConnection conex = new DBConnection();
		String sql = "SELECT * FROM apercibimientos_apercibimiento order by unidad";
		try (Statement st = conex.getConnection().createStatement(); ResultSet rs = st.executeQuery(sql);) {

			while (rs.next()) {
				Apercibimiento a = new Apercibimiento();
				a.setId(rs.getInt(1));
				a.setAlumno(rs.getString(2));
				a.setPeriodoAcademico(rs.getInt(3));
				a.setCurso(rs.getString(4));
				a.setUnidad(rs.getString(5));
				a.setMateria(rs.getString(6));
				a.setFechaInicio(rs.getDate(7));
				a.setFechaFin(rs.getDate(8));
				a.setHorasJustificadas(rs.getString(9));
				a.setPorcentajeJustificado(rs.getFloat(10));
				a.setHorasInjustificadas(rs.getString(11));
				a.setPorcentajeInjustificado(rs.getFloat(12));
				a.setRetrasos(rs.getString(13));
				a.setActivo(rs.getBoolean(14));

				apercibimientos.add(a);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conex.desconectar();
		}

		return apercibimientos;
	}

	public List<ClaseApercibimiento> mostrarMaterias() {
		List<ClaseApercibimiento> materias = new ArrayList<>();
		DBConnection conex = new DBConnection();
		String sql = "select unidad, materia from apercibimientos_apercibimiento group by materia, unidad order by unidad";
		try (Statement st = conex.getConnection().createStatement(); ResultSet rs = st.executeQuery(sql);) {
			ResultSet rsAlumnos = null;

			while (rs.next()) {
				ClaseApercibimiento m = new ClaseApercibimiento();
				m.setUnidad(rs.getString(1));
				m.setMateria(rs.getString(2));

				sql = "select alumno, GROUP_CONCAT(month(fecha_inicio)) meses from apercibimientos_apercibimiento "
						+ "where materia like '" + m.getMateria() + "' and unidad like '" + m.getUnidad()
						+ "' group by alumno";
				rsAlumnos = st.executeQuery(sql);

				while (rsAlumnos.next()) {
					AlumnoApercibimiento alumno = new AlumnoApercibimiento();

					alumno.setNombre(rsAlumnos.getString(1));
					String[] meses = rsAlumnos.getString(2).split(",");
					alumno.setMeses(convertirMeses(meses));

					m.getAlumnos().add(alumno);
				}

				materias.add(m);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conex.desconectar();
		}

		return materias;
	}

	private List<String> convertirMeses(String[] meses) {
		List<String> mesesString = new ArrayList<>();

		for (String string : meses) {
			int mes = Integer.parseInt(string);
			switch (mes) {
			case 1:
				mesesString.add("Enero");
				break;
			case 2:
				mesesString.add("Febrero");
				break;
			case 3:
				mesesString.add("Marzo");
				break;
			case 4:
				mesesString.add("Abril");
				break;
			case 5:
				mesesString.add("Mayo");
				break;
			case 6:
				mesesString.add("Junio");
				break;
			case 7:
				mesesString.add("Julio");
				break;
			case 8:
				mesesString.add("Agosto");
				break;
			case 9:
				mesesString.add("Septiembre");
				break;
			case 10:
				mesesString.add("Octubre");
				break;
			case 11:
				mesesString.add("Noviembre");
				break;
			case 12:
				mesesString.add("Diciembre");
				break;

			}
		}
		return mesesString;
	}

	public List<TutorAlumno> mostrarAlumnosTutor(String username) {
		List<TutorAlumno> alumnos = new ArrayList<>();
		DBConnection conex = new DBConnection();
		String sql = "select alumno, unidad from apercibimientos_apercibimiento where unidad = (select P.cursoTutor from visitas_profesor P, auth_user U "
				+ "where U.username = ? and P.usuario_id = U.id) group by alumno, unidad";

		try {
			PreparedStatement st = conex.getConnection().prepareStatement(sql);
			st.setString(1, username);
			ResultSet rs = st.executeQuery();
			ResultSet rsAsignatura = null;

			while (rs.next()) {
				TutorAlumno t = new TutorAlumno();
				t.setNombre(rs.getString(1));
				t.setCurso(rs.getString(2));

				sql = "select materia, GROUP_CONCAT(month(fecha_inicio)) meses from apercibimientos_apercibimiento where alumno like ? and unidad like (select P.cursoTutor from visitas_profesor P, auth_user U "
						+ "where U.username = ? and P.usuario_id = U.id) group by materia";
				st = conex.getConnection().prepareStatement(sql);
				st.setString(1, t.getNombre());
				st.setString(2, username);
				rsAsignatura = st.executeQuery();
				List<TutorAsignatura> asignaturas = new ArrayList<>();

				while (rsAsignatura.next()) {
					TutorAsignatura asignatura = new TutorAsignatura();

					asignatura.setNombre(rsAsignatura.getString(1));
					String[] meses = rsAsignatura.getString(2).split(",");
					asignatura.setMeses(convertirMeses(meses));

					t.getAsignaturas().add(asignatura);
				}

				alumnos.add(t);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conex.desconectar();
		}

		return alumnos;
	}

	public List<String> getAsignaturasEspeciales() {
		List<String> asignaturas = new ArrayList<>();
		DBConnection conex = new DBConnection();
		String sql = "SELECT * FROM apercibimientos_asignaturasespeciales";

		try (Statement st = conex.getConnection().createStatement(); ResultSet rs = st.executeQuery(sql);) {

			while (rs.next()) {
				asignaturas.add(rs.getString(1) + "--" + rs.getString(2));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conex.desconectar();
		}

		return asignaturas;
	}

	public void insertarAsignatura(String asignatura) {
		DBConnection db = new DBConnection();
		String sql = "insert into apercibimientos_asignaturasespeciales values (0, ?)";
		try (PreparedStatement st = db.getConnection().prepareStatement(sql);) {
			st.setString(1, asignatura);
			st.executeUpdate();
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.desconectar();
		}
	}

	public void desActivarApercibimiento(int id, boolean activo) {
		DBConnection db = new DBConnection();
		String sql = "update apercibimientos_apercibimiento set activo = ? where id = ?";
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

	public List<String> getAnnoAcademico() {
		DBConnection db = new DBConnection();
		String sql = "SELECT distinct(periodo_academico) FROM GARV.apercibimientos_apercibimiento order by periodo_academico";
		List<String> lista = new ArrayList<>();
		try (Statement st = db.getConnection().createStatement(); ResultSet rs = st.executeQuery(sql);) {

			while (rs.next()) {
				lista.add(rs.getString(1));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.desconectar();
		}

		return lista;

	}

	public List<String> getMeses(String anno) {
		DBConnection db = new DBConnection();
		String sql = "SELECT distinct(month(fecha_inicio)) FROM GARV.apercibimientos_apercibimiento where periodo_academico = ?";
		List<String> lista = new ArrayList<>();
		try (PreparedStatement st = db.getConnection().prepareStatement(sql);) {
			st.setInt(1, Integer.parseInt(anno));
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				lista.add(rs.getString(1));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.desconectar();
		}

		return lista;
	}

	public List<String> getCursos(String anno, String mes) {
		DBConnection db = new DBConnection();
		String sql = "SELECT distinct(unidad) FROM GARV.apercibimientos_apercibimiento where periodo_academico = ? and month(fecha_inicio) = ? order by unidad";
		List<String> lista = new ArrayList<>();
		try (PreparedStatement st = db.getConnection().prepareStatement(sql);) {
			st.setInt(1, Integer.parseInt(anno));
			st.setInt(2, Integer.parseInt(mes));
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				lista.add(rs.getString(1));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.desconectar();
		}

		return lista;
	}

	public void modificarAsignatura(int id, String asignatura) {

		DBConnection db = new DBConnection();
		String sql = "update apercibimientos_asignaturasespeciales set materia = ? where id = ?";
		try (PreparedStatement st = db.getConnection().prepareStatement(sql);) {
			st.setString(1, asignatura);
			st.setInt(2, id);
			st.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.desconectar();
		}

	}

	public void eliminarAsignatura(int id) {
		DBConnection db = new DBConnection();
		String sql = "delete from apercibimientos_asignaturasespeciales where id = ?";
		try (PreparedStatement st = db.getConnection().prepareStatement(sql);) {
			st.setInt(1, id);
			st.executeUpdate();
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.desconectar();
		}
		
	}

	public List<String> getCursosfiltro(String anno) {
		DBConnection db = new DBConnection();
		String sql = "SELECT distinct(unidad) FROM apercibimientos_apercibimiento where periodo_academico = ? order by unidad";
		List<String> lista = new ArrayList<>();
		try (PreparedStatement st = db.getConnection().prepareStatement(sql); ) {
			st.setInt(1, Integer.parseInt(anno));
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				lista.add(rs.getString(1));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.desconectar();
		}

		return lista;
	}

	public List<String> getAlumnosFiltro(String anno, String curso) {
		DBConnection db = new DBConnection();
		String sql = "SELECT distinct(alumno) FROM apercibimientos_apercibimiento where periodo_academico = ? and unidad like ? order by alumno;";
		List<String> lista = new ArrayList<>();
		try (PreparedStatement st = db.getConnection().prepareStatement(sql);) {
			st.setInt(1, Integer.parseInt(anno));
			st.setString(2, curso);
			 ResultSet rs = st.executeQuery();
			while (rs.next()) {
				lista.add(rs.getString(1));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.desconectar();
		}

		return lista;
	}
	
	public List<Apercibimiento> mostrarApercibimientosFiltroAno(String ano) {
		List<Apercibimiento> apercibimientos = new ArrayList<>();
		DBConnection conex = new DBConnection();
		String sql = "SELECT * FROM apercibimientos_apercibimiento where periodo_academico = ?";
		try (PreparedStatement st = conex.getConnection().prepareStatement(sql); ) {
			st.setInt(1, Integer.parseInt(ano));
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				Apercibimiento a = new Apercibimiento();
				a.setId(rs.getInt(1));
				a.setAlumno(rs.getString(2));
				a.setPeriodoAcademico(rs.getInt(3));
				a.setCurso(rs.getString(4));
				a.setUnidad(rs.getString(5));
				a.setMateria(rs.getString(6));
				a.setFechaInicio(rs.getDate(7));
				a.setFechaFin(rs.getDate(8));
				a.setHorasJustificadas(rs.getString(9));
				a.setPorcentajeJustificado(rs.getFloat(10));
				a.setHorasInjustificadas(rs.getString(11));
				a.setPorcentajeInjustificado(rs.getFloat(12));
				a.setRetrasos(rs.getString(13));
				a.setActivo(rs.getBoolean(14));

				apercibimientos.add(a);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conex.desconectar();
		}

		return apercibimientos;
	}
	
	public List<Apercibimiento> mostrarApercibimientosFiltroCurso(String ano, String curso) {
		List<Apercibimiento> apercibimientos = new ArrayList<>();
		DBConnection conex = new DBConnection();
		String sql = "SELECT * FROM apercibimientos_apercibimiento where periodo_academico = ? and unidad like ?";
		try (PreparedStatement st = conex.getConnection().prepareStatement(sql); ) {
			st.setInt(1, Integer.parseInt(ano));
			st.setString(2, curso);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				Apercibimiento a = new Apercibimiento();
				a.setId(rs.getInt(1));
				a.setAlumno(rs.getString(2));
				a.setPeriodoAcademico(rs.getInt(3));
				a.setCurso(rs.getString(4));
				a.setUnidad(rs.getString(5));
				a.setMateria(rs.getString(6));
				a.setFechaInicio(rs.getDate(7));
				a.setFechaFin(rs.getDate(8));
				a.setHorasJustificadas(rs.getString(9));
				a.setPorcentajeJustificado(rs.getFloat(10));
				a.setHorasInjustificadas(rs.getString(11));
				a.setPorcentajeInjustificado(rs.getFloat(12));
				a.setRetrasos(rs.getString(13));
				a.setActivo(rs.getBoolean(14));

				apercibimientos.add(a);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conex.desconectar();
		}

		return apercibimientos;
	}
	
	public List<Apercibimiento> mostrarApercibimientosFiltroAlumno(String ano, String curso, String alumno) {
		List<Apercibimiento> apercibimientos = new ArrayList<>();
		DBConnection conex = new DBConnection();
		String sql = "SELECT * FROM apercibimientos_apercibimiento where periodo_academico = ? and unidad like ? and alumno like ?";
		try (PreparedStatement st = conex.getConnection().prepareStatement(sql); ) {
			st.setInt(1, Integer.parseInt(ano));
			st.setString(2, curso);
			st.setString(3, alumno);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Apercibimiento a = new Apercibimiento();
				a.setId(rs.getInt(1));
				a.setAlumno(rs.getString(2));
				a.setPeriodoAcademico(rs.getInt(3));
				a.setCurso(rs.getString(4));
				a.setUnidad(rs.getString(5));
				a.setMateria(rs.getString(6));
				a.setFechaInicio(rs.getDate(7));
				a.setFechaFin(rs.getDate(8));
				a.setHorasJustificadas(rs.getString(9));
				a.setPorcentajeJustificado(rs.getFloat(10));
				a.setHorasInjustificadas(rs.getString(11));
				a.setPorcentajeInjustificado(rs.getFloat(12));
				a.setRetrasos(rs.getString(13));
				a.setActivo(rs.getBoolean(14));

				apercibimientos.add(a);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conex.desconectar();
		}

		return apercibimientos;
	}
}
