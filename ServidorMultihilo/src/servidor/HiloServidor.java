package servidor;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.text.html.CSS;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import connection.DjangoConnection;
import dao.ApercibimientoDAO;
import dao.UsuariosDAO;
import dao.VisitasDAO;
import vo.Alumno;
import vo.Apercibimiento;
import vo.ClaseApercibimiento;
import vo.Empresa;
import vo.RegistroVisita;
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
				insertarAsignatura(entrada, salida);
				break;
			case 9:
				getAsignaturasEspeciales(salida);
				break;
			case 10:
				getAlumnoVisitas(entrada, salida);
				break;
			case 11:
				getTodosAlumnosVisitas(salida);
				break;
			case 12:
				getEmpresas(salida);
				break;
			case 13:
				insertarEmpresa(entrada, salida);
				break;
			case 14:
				insertarAlumno(entrada, salida);
				break;
			case 15:
				registrarVisita(entrada, salida);
				break;
			case 16:
				desActivarApercibimiento(entrada, salida);
				break;
			case 17:
				inValidarVisita(entrada, salida);
				break;
			case 18:
				getImagen(entrada, salida);
				break;
			case 19:
				getAnnoAcademico(salida);
				break;
			case 20:
				getMeses(entrada, salida);
				break;
			case 21:
				getCursos(entrada, salida);
				break;
			case 22:
				modificarAsignatura(entrada, salida);
				break;
			case 23:
				eliminarAsignatura(entrada, salida);
				break;
			default:
				System.out.println(op);
				break;

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void eliminarAsignatura(ObjectInputStream entrada, ObjectOutputStream salida) {
		try {
			int id = entrada.readInt();
			ApercibimientoDAO dao = new ApercibimientoDAO();

			dao.eliminarAsignatura(id);
			List<String> asignaturas = dao.getAsignaturasEspeciales();

			String json = new Gson().toJson(asignaturas);
			salida.writeObject(json);

		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private void modificarAsignatura(ObjectInputStream entrada, ObjectOutputStream salida) {
		try {
			int id = entrada.readInt();
			String asignatura = entrada.readUTF();
			ApercibimientoDAO dao = new ApercibimientoDAO();

			dao.modificarAsignatura(id, asignatura);
			List<String> asignaturas = dao.getAsignaturasEspeciales();

			String json = new Gson().toJson(asignaturas);
			salida.writeObject(json);

		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private void getCursos(ObjectInputStream entrada, ObjectOutputStream salida) {
		try {
			ApercibimientoDAO dao = new ApercibimientoDAO();
			String anno = entrada.readUTF();
			String mes = entrada.readUTF();

			List<String> lista = dao.getCursos(anno, mes);
			salida.writeObject(new Gson().toJson(lista));

		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private void getMeses(ObjectInputStream entrada, ObjectOutputStream salida) {
		try {
			ApercibimientoDAO dao = new ApercibimientoDAO();
			String anno = entrada.readUTF();

			List<String> lista = dao.getMeses(anno);
			salida.writeObject(new Gson().toJson(lista));

		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private void getAnnoAcademico(ObjectOutputStream salida) {
		try {
			ApercibimientoDAO dao = new ApercibimientoDAO();

			List<String> lista = dao.getAnnoAcademico();
			salida.writeObject(new Gson().toJson(lista));

		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private void getImagen(ObjectInputStream entrada, ObjectOutputStream salida) {
		DjangoConnection dc = new DjangoConnection();
		try {
			int id = entrada.readInt();
			String crsf = entrada.readUTF();
			String session = entrada.readUTF();
			
			String base64 = dc.getImagen(id, crsf, session);
			
			salida.writeObject(base64);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void inValidarVisita(ObjectInputStream entrada, ObjectOutputStream salida) {
		try {
			VisitasDAO dao = new VisitasDAO();
			int id = entrada.readInt();
			boolean activo = entrada.readBoolean();
			dao.inValidarVisita(id, activo);
			List<Visita> visitas = dao.mostrarVisitas();

			String json = visitasJson(visitas);
			salida.writeObject(json);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void desActivarApercibimiento(ObjectInputStream entrada, ObjectOutputStream salida) {
		try {
			ApercibimientoDAO dao = new ApercibimientoDAO();
			int id = entrada.readInt();
			boolean activo = entrada.readBoolean();
			dao.desActivarApercibimiento(id, activo);
			List<Apercibimiento> apercibimientos = dao.mostrarApercibimientos();

			String json = apercibimientosJson(apercibimientos);
			salida.writeObject(json);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void registrarVisita(ObjectInputStream entrada, ObjectOutputStream salida) {
		try {
			DjangoConnection dc = new DjangoConnection();
			VisitasDAO dao = new VisitasDAO();
			String json = (String) entrada.readObject();
			RegistroVisita visita = VisitaObjetoJson(json);

			int id = dao.idUsuario(visita.getDocente());
			dc.insertarVisita(visita, id);
			salida.writeInt(id);
			salida.flush();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	private RegistroVisita VisitaObjetoJson(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, RegistroVisita.class);
	}

	private void insertarAlumno(ObjectInputStream entrada, ObjectOutputStream salida) {
		try {
			VisitasDAO dao = new VisitasDAO();
			String json = (String) entrada.readObject();
			Alumno alumno = alumnoObjetoJson(json);
			int id = dao.insertarAlumno(alumno);
			salida.writeInt(id);
			salida.flush();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	private Alumno alumnoObjetoJson(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, Alumno.class);
	}

	private void insertarEmpresa(ObjectInputStream entrada, ObjectOutputStream salida) {
		try {
			VisitasDAO dao = new VisitasDAO();
			String json = (String) entrada.readObject();
			Empresa empresa = empresaObjetoJson(json);
			int id = dao.insertarEmpresa(empresa);
			salida.writeInt(id);
			salida.flush();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	private Empresa empresaObjetoJson(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, Empresa.class);
	}

	private void getEmpresas(ObjectOutputStream salida) {
		try {
			VisitasDAO dao = new VisitasDAO();
			List<Empresa> empresas = dao.mostrarEmpresas();

			String json = empresaJson(empresas);
			salida.writeObject(json);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private String empresaJson(List<Empresa> empresas) {
		Gson gson = new Gson();
		return gson.toJson(empresas);
	}

	private void getTodosAlumnosVisitas(ObjectOutputStream salida) {
		try {
			VisitasDAO dao = new VisitasDAO();
			List<Alumno> visitas = dao.mostrarTodosAlumnoVisitas();

			String json = alumnoVisitasJson(visitas);
			salida.writeObject(json);
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
		Gson gson = new GsonBuilder().setDateFormat("yyyy-mm-dd").create();
		return gson.toJson(visitas);
	}

	private void insertarAsignatura(ObjectInputStream entrada, ObjectOutputStream salida) {
		try {
			String asignatura = entrada.readUTF();
			ApercibimientoDAO dao = new ApercibimientoDAO();
			dao.insertarAsignatura(asignatura);
			List<String> asignaturas = dao.getAsignaturasEspeciales();

			String json = new Gson().toJson(asignaturas);
			salida.writeObject(json);

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
