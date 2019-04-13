package dao;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import connection.DBConnection;
import vo.Apercibimiento;

public class ApercibimientoDAO {

	public List<Apercibimiento> mostrarApercibimientos() {
		List<Apercibimiento> apercibimientos = new ArrayList<>();
		DBConnection conex = new DBConnection();
		String sql = "SELECT * FROM apercibimientos_apercibimiento";
		try (
				Statement st = conex.getConnection().createStatement(); 
				ResultSet rs = st.executeQuery(sql);) {

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

			for (Apercibimiento apercibimiento : apercibimientos) {
				System.out.println(apercibimiento);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conex.desconectar();
		}

		return apercibimientos;
	}
}
