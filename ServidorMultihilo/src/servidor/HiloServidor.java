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
import dao.UsuariosDAO;
import dao.VisitasDAO;
import vo.Alumno;
import vo.Apercibimiento;
import vo.ClaseApercibimiento;
import vo.TutorAlumno;
import vo.Usuario;
import vo.Visita;

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
			case 2:
				getVisitas(salida);
				break;
			case 3:
				getMaterias(salida);
				break;
			case 4:
				getApercibimientosTutor(salida, entrada);
				break;
			case 5:
				inicioSesion(entrada, salida);
				break;
			case 6:
				getUsuarios(salida);
				break;
			case 7:
				crearUsuario(entrada, salida);
				break;
			case 8:
				insertarAsignatura(entrada);
				break;
			case 9:
				getAsignaturasEspeciales(salida);
				break;
			case 10:
				getAlumnoVisitas(entrada, salida);
			default:
				System.out.println(op);
				break;

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void getAlumnoVisitas(ObjectInputStream entrada, ObjectOutputStream salida) {

		try {
			VisitasDAO dao = new VisitasDAO();
			String username = entrada.readUTF();
			List<Alumno> visitas = dao.mostrarAlumnoVisitas(username);

			String json = alumnoVisitasJson(visitas);
			salida.writeObject(json);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private String alumnoVisitasJson(List<Alumno> visitas) {
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
		return gson.toJson(visitas);
	}

	private void insertarAsignatura(ObjectInputStream entrada) {
		try {
			String asignatura = entrada.readUTF();
			ApercibimientoDAO dao = new ApercibimientoDAO();
			dao.insertarAsignatura(asignatura);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void getAsignaturasEspeciales(ObjectOutputStream salida) {
		try {
			ApercibimientoDAO dao = new ApercibimientoDAO();
			List<String> asignaturas = dao.getAsignaturasEspeciales();

			String json = new Gson().toJson(asignaturas);
			salida.writeObject(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void crearUsuario(ObjectInputStream entrada, ObjectOutputStream salida) {
		try {
			String csrftoken = entrada.readUTF();
			String sessionid = entrada.readUTF();

			String usuarioCrear = entrada.readUTF();
			String contrasenaCrear = entrada.readUTF();
			String correo = entrada.readUTF();
			String dni = entrada.readUTF();
			String nombre = entrada.readUTF();
			String apellidos = entrada.readUTF();
			String curso = entrada.readUTF();

			DjangoConnection con = new DjangoConnection();
			boolean creado = con.createUser(csrftoken, sessionid, usuarioCrear, contrasenaCrear, correo, dni, nombre,
					apellidos, curso);

			salida.writeBoolean(creado);
			salida.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void getUsuarios(ObjectOutputStream salida) {
		try {
			UsuariosDAO dao = new UsuariosDAO();
			List<Usuario> usuarios = dao.mostrarUsuarios();

			String json = usuariosJson(usuarios);
			salida.writeObject(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String usuariosJson(List<Usuario> usuarios) {
		Gson gson = new Gson();
		return gson.toJson(usuarios);
	}

	private void getVisitas(ObjectOutputStream salida) {
		try {
			VisitasDAO dao = new VisitasDAO();
			List<Visita> visitas = dao.mostrarVisitas();

			String json = visitasJson(visitas);
			salida.writeObject(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String visitasJson(List<Visita> visitas) {
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
		return gson.toJson(visitas);
	}

	private void inicioSesion(ObjectInputStream entrada, ObjectOutputStream salida) {
		try {
			String username = entrada.readUTF();
			String password = entrada.readUTF();
			DjangoConnection dc = new DjangoConnection();
			List<String> cookies = dc.conectar(username, password);

			boolean existe = false;

			if (!cookies.isEmpty() && !cookies.get(0).equals("") && !cookies.get(1).equals("")) {
				existe = true;
			}

			salida.writeBoolean(existe);
			salida.flush();

			if (existe) {
				salida.writeUTF(cookies.get(0));
				salida.flush();
				salida.writeUTF(cookies.get(1));
				salida.flush();
				salida.writeUTF(username);
			}

			salida.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void getApercibimientosTutor(ObjectOutputStream salida, ObjectInputStream entrada) {
		try {
			String username = (String) entrada.readObject();
			ApercibimientoDAO dao = new ApercibimientoDAO();
			List<TutorAlumno> alumnos = dao.mostrarAlumnosTutor(username);

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
