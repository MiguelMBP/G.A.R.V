package dao;

public class VisitasDAO {
	String sql = "select A3.nombre, A3.apellidos, A1.nombre, A1.apellidos, A2.nombre, A2.poblacion, A4.distancia from visitas_alumno A1, visitas_empresa A2, visitas_profesor A3, visitas_visita A4 where A1.id = A4.alumno_id and A2.id = A4.empresa_id and A3.id = A4.profesor_id";
}
