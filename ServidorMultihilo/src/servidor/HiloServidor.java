package servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import connection.DjangoConnection;
import dao.ApercibimientoDAO;
import vo.Apercibimiento;
import vo.ClaseApercibimiento;
import vo.TutorAlumno;

public class HiloServidor extends Thread {

	private Socket socket = null;

	public HiloServidor(Socket socket) {
		super();
		this.socket = socket;
	}

	@Override
	public void run() {
		loadQuery();
	}

	private void loadQuery() {
		try {
			ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());
			ObjectOutputStream salida = new ObjectOutputStream(socket.getOutputStream());
			int op = entrada.readInt();

			switch (op) {
			case 1:
				getApercibimientos(salida);
				break;

			case 3:
				getMaterias(salida);
				break;
			case 4:
				getApercibimientosTutor(salida, entrada);
				break;
			case 5:
				inicioSesion(entrada, salida);

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void inicioSesion(ObjectInputStream entrada, ObjectOutputStream salida) {
		try {
			String username = entrada.readUTF();
			String password = entrada.readUTF();
			DjangoConnection dc = new DjangoConnection();
			boolean existe = dc.conectar(username, password);

			salida.writeBoolean(existe);
			salida.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private void getApercibimientosTutor(ObjectOutputStream salida, ObjectInputStream entrada) {
		try {
			String unidad = (String) entrada.readObject();
			ApercibimientoDAO dao = new ApercibimientoDAO();
			List<TutorAlumno> alumnos = dao.mostrarAlumnosTutor(unidad);

			String json = alumnosJson(alumnos);
			salida.writeObject(json);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}

	private void getMaterias(ObjectOutputStream salida) {
		try {
			ApercibimientoDAO dao = new ApercibimientoDAO();
			List<ClaseApercibimiento> materias = dao.mostrarMaterias();

			String json = materiasJson(materias);
			salida.writeObject(json);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void getApercibimientos(ObjectOutputStream salida) {
		try {
			ApercibimientoDAO dao = new ApercibimientoDAO();
			List<Apercibimiento> apercibimientos = dao.mostrarApercibimientos();

			String json = apercibimientosJson(apercibimientos);
			salida.writeObject(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String apercibimientosJson(List<Apercibimiento> apercibimientos) {
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
		return gson.toJson(apercibimientos);
	}

	private String materiasJson(List<ClaseApercibimiento> materias) {
		Gson gson = new Gson();
		return gson.toJson(materias);
	}
	
	private String alumnosJson(List<TutorAlumno> alumnos) {
		Gson gson = new Gson();
		return gson.toJson(alumnos);
	}

}
