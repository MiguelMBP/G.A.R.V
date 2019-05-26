package servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

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

/**
 * El hilo que se encarga de responder las peticiones de los clientes.
 *
 * @author mmbernal
 *
 */
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
                case 24:
                    getCursosFiltro(entrada, salida);
                    break;
                case 25:
                    getAlumnosFiltro(entrada, salida);
                    break;
                case 26:
                    getApercibimientosPorAno(entrada, salida);
                    break;
                case 27:
                    getApercibimientoPorCurso(entrada, salida);
                    break;
                case 28:
                    getApercibimientoPorAlumno(entrada, salida);
                    break;
                case 29:
                    cambiarContrasena(entrada, salida);
                    break;
                case 30:
                    getUsuario(entrada, salida);
                    break;
                case 31:
                    modificarUsuario(entrada, salida);
                    break;
                case 32:
                    enviarApercibimientos(entrada);
                    break;
                case 33:
                    importarUsuarios(entrada);
                    break;
                case 34:
                    getAlumnos(salida);
                    break;
                case 35:
                    getEmpresaPorId(entrada, salida);
                    break;
                case 36:
                    modificarEmpresa(entrada, salida);
                    break;
                case 37:
                    getAlumnoPorId(entrada, salida);
                    break;
                case 38:
                    modificarAlumno(entrada, salida);
                    break;
                case 39:
                    inicioSesionStaff(entrada, salida);
                    break;
                case 40:
                    eliminarUsuario(entrada, salida);
                    break;
                default:
                    System.out.println(op);
                    break;

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void eliminarUsuario(ObjectInputStream entrada, ObjectOutputStream salida) {
        try {
            DjangoConnection dc = new DjangoConnection();
            String username = entrada.readUTF();
            String crsftoken = entrada.readUTF();
            String sessionId = entrada.readUTF();

            boolean eliminado = dc.eliminarUsuario(username, crsftoken, sessionId);
            salida.writeBoolean(eliminado);
            salida.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void inicioSesionStaff(ObjectInputStream entrada, ObjectOutputStream salida) {
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
                boolean staff = dc.comprobarStaff(username, cookies.get(0), cookies.get(1));
                salida.writeBoolean(staff);
                salida.flush();
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

    private void modificarAlumno(ObjectInputStream entrada, ObjectOutputStream salida) {
        try {
            VisitasDAO dao = new VisitasDAO();
            String json = (String) entrada.readObject();
            Alumno alumno = jsonToAlumno(json);
            int id = dao.modificarAlumno(alumno);
            salida.writeInt(id);
            salida.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private Alumno jsonToAlumno(String json) {
        java.lang.reflect.Type typeList = new TypeToken<Alumno>() {
        }.getType();
        Gson gson = new Gson();
        Alumno alumno = gson.fromJson(json, typeList);
        return alumno;
    }

    private void getAlumnoPorId(ObjectInputStream entrada, ObjectOutputStream salida) {
        try {
            VisitasDAO dao = new VisitasDAO();
            String id = entrada.readUTF();
            Alumno alumno = dao.mostrarAlumno(id);

            String json = new Gson().toJson(alumno);
            salida.writeObject(json);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void modificarEmpresa(ObjectInputStream entrada, ObjectOutputStream salida) {
        try {
            VisitasDAO dao = new VisitasDAO();
            String json = (String) entrada.readObject();
            Empresa empresa = jsonToEmpresa(json);
            int id = dao.modificarEmpresa(empresa);
            salida.writeInt(id);
            salida.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private Empresa jsonToEmpresa(String json) {
        java.lang.reflect.Type typeList = new TypeToken<Empresa>() {
        }.getType();
        Gson gson = new Gson();
        Empresa empresa = gson.fromJson(json, typeList);
        return empresa;
    }

    private void getEmpresaPorId(ObjectInputStream entrada, ObjectOutputStream salida) {
        try {
            VisitasDAO dao = new VisitasDAO();
            String id = entrada.readUTF();
            Empresa empresa = dao.mostrarEmpresa(id);

            String json = new Gson().toJson(empresa);
            salida.writeObject(json);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void getAlumnos(ObjectOutputStream salida) {
        try {
            VisitasDAO dao = new VisitasDAO();
            List<Alumno> visitas = dao.mostrarAlumnos();

            String json = alumnoVisitasJson(visitas);
            salida.writeObject(json);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void importarUsuarios(ObjectInputStream entrada) {
        try {
            DjangoConnection dc = new DjangoConnection();
            String base64 = (String) entrada.readObject();
            String csrftoken = entrada.readUTF();
            String sessionId = entrada.readUTF();
            dc.importarUsuarios(base64, csrftoken, sessionId);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void enviarApercibimientos(ObjectInputStream entrada) {
        try {
            DjangoConnection dc = new DjangoConnection();
            String base64 = (String) entrada.readObject();
            String extension = entrada.readUTF();
            String csrftoken = entrada.readUTF();
            String sessionId = entrada.readUTF();
            dc.subirApercibimientos(base64, extension, csrftoken, sessionId);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void modificarUsuario(ObjectInputStream entrada, ObjectOutputStream salida) {
        try {
            UsuariosDAO dao = new UsuariosDAO();
            String json = (String) entrada.readObject();
            Usuario usuario = jsonToUsuarios(json);
            dao.modificarUsuario(usuario);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private Usuario jsonToUsuarios(String linea) {
        java.lang.reflect.Type typeList = new TypeToken<Usuario>() {
        }.getType();
        Gson gson = new Gson();
        Usuario usuario = gson.fromJson(linea, typeList);
        return usuario;
    }

    private void getUsuario(ObjectInputStream entrada, ObjectOutputStream salida) {
        try {
            UsuariosDAO dao = new UsuariosDAO();
            String id = entrada.readUTF();
            Usuario usuario = dao.mostrarUsuario(id);

            String json = new Gson().toJson(usuario);
            salida.writeObject(json);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void cambiarContrasena(ObjectInputStream entrada, ObjectOutputStream salida) {
        try {
            DjangoConnection dc = new DjangoConnection();
            String username = entrada.readUTF();
            String password = entrada.readUTF();
            String crsftoken = entrada.readUTF();
            String sessionId = entrada.readUTF();

            boolean cambiado = dc.cambiarContrasena(username, password, crsftoken, sessionId);
            salida.writeBoolean(cambiado);
            salida.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void getApercibimientoPorAlumno(ObjectInputStream entrada, ObjectOutputStream salida) {
        try {
            ApercibimientoDAO dao = new ApercibimientoDAO();
            String ano = entrada.readUTF();
            String curso = entrada.readUTF();
            String alumno = entrada.readUTF();
            List<Apercibimiento> apercibimientos = dao.mostrarApercibimientosFiltroAlumno(ano, curso, alumno);

            String json = apercibimientosJson(apercibimientos);
            salida.writeObject(json);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void getApercibimientoPorCurso(ObjectInputStream entrada, ObjectOutputStream salida) {
        try {
            ApercibimientoDAO dao = new ApercibimientoDAO();
            String ano = entrada.readUTF();
            String curso = entrada.readUTF();
            List<Apercibimiento> apercibimientos = dao.mostrarApercibimientosFiltroCurso(ano, curso);

            String json = apercibimientosJson(apercibimientos);
            salida.writeObject(json);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void getApercibimientosPorAno(ObjectInputStream entrada, ObjectOutputStream salida) {
        try {
            ApercibimientoDAO dao = new ApercibimientoDAO();
            String ano = entrada.readUTF();
            List<Apercibimiento> apercibimientos = dao.mostrarApercibimientosFiltroAno(ano);

            String json = apercibimientosJson(apercibimientos);
            salida.writeObject(json);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void getAlumnosFiltro(ObjectInputStream entrada, ObjectOutputStream salida) {
        try {
            ApercibimientoDAO dao = new ApercibimientoDAO();
            String anno = entrada.readUTF();
            String curso = entrada.readUTF();

            List<String> lista = dao.getAlumnosFiltro(anno, curso);
            salida.writeObject(new Gson().toJson(lista));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void getCursosFiltro(ObjectInputStream entrada, ObjectOutputStream salida) {
        try {
            ApercibimientoDAO dao = new ApercibimientoDAO();
            String anno = entrada.readUTF();

            List<String> lista = dao.getCursosfiltro(anno);
            salida.writeObject(new Gson().toJson(lista));

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
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
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
