package dao;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import connection.DBConnection;
import vo.AlumnoApercibimiento;
import vo.Apercibimiento;
import vo.ClaseApercibimiento;

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
		try (
				Statement st = conex.getConnection().createStatement(); 
				ResultSet rs = st.executeQuery(sql);) {
			ResultSet rsAlumnos = null;

			while (rs.next()) {
				ClaseApercibimiento m = new ClaseApercibimiento();
				m.setUnidad(rs.getString(1));
				m.setMateria(rs.getString(2));

				sql = "select alumno, GROUP_CONCAT(month(fecha_inicio)) meses from apercibimientos_apercibimiento "
						+ "where materia like '" + m.getMateria() + "' and unidad like '" + m.getUnidad() + "' group by alumno";
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
}
