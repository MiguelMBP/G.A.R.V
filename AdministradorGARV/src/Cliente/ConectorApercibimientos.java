/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import Util.ConfigurationFileException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import vo.Apercibimiento;

/**
 * Realiza las operaciones entre el cliente y el servidor multihilos para el módulo de apercibimientos
 * @author miguelmbp
 */
public class ConectorApercibimientos {

    public ConectorApercibimientos() {
    }

    /**
     * Recoge todos los apercibimientos del servidor
     * @return Una lista con los apercibimientos
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ConfigurationFileException 
     */
    public List<Apercibimiento> cargarApercibimientos() throws FileNotFoundException, IOException, ConfigurationFileException {
        Socket socketCliente = null;
        ObjectInputStream entrada = null;
        ObjectOutputStream salida = null;
        List<Apercibimiento> apercibimientos = new ArrayList<>();

        try {
            String[] parametros = leerConfiguración();
            if (parametros[0] == null || parametros[1] == null) {
                throw new ConfigurationFileException();
            }
            socketCliente = new Socket(parametros[0], Integer.parseInt(parametros[1]));
            salida = new ObjectOutputStream(socketCliente.getOutputStream());
            entrada = new ObjectInputStream(socketCliente.getInputStream());
            System.out.println("Conectado");
        } catch (FileNotFoundException ex) {
            throw new FileNotFoundException();
        } catch (IOException e) {
            throw new IOException();
        } catch (NumberFormatException e) {
            throw new ConfigurationFileException();
        }
        String linea = "";
        try {
            salida.writeInt(1);
            salida.flush();
            linea = (String) entrada.readObject();
            apercibimientos = jsonToListApercibimientos(linea);

            entrada.close();
            socketCliente.close();

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConectorApercibimientos.class.getName()).log(Level.SEVERE, null, ex);
        }

        return apercibimientos;

    }

    private List<Apercibimiento> jsonToListApercibimientos(String json) {
        java.lang.reflect.Type typeList = new TypeToken<List<Apercibimiento>>() {
        }.getType();
        Gson gson = new Gson();
        List<Apercibimiento> apercibimientos = gson.fromJson(json, typeList);
        return apercibimientos;
    }

    /**
     * Recoge las asignaturas especiales del servidor
     * @return
     * @throws ConfigurationFileException
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public List<String> cargarAsignaturas() throws ConfigurationFileException, FileNotFoundException, IOException {
        Socket socketCliente = null;
        ObjectInputStream entrada = null;
        ObjectOutputStream salida = null;
        List<String> asignaturas = new ArrayList<>();

        try {
            String[] parametros = leerConfiguración();
            if (parametros[0] == null || parametros[1] == null) {
                throw new ConfigurationFileException();
            }
            socketCliente = new Socket(parametros[0], Integer.parseInt(parametros[1]));
            salida = new ObjectOutputStream(socketCliente.getOutputStream());
            entrada = new ObjectInputStream(socketCliente.getInputStream());
            System.out.println("Conectado");
        } catch (FileNotFoundException ex) {
            throw new FileNotFoundException();
        } catch (IOException e) {
            throw new IOException();
        } catch (NumberFormatException e) {
            throw new ConfigurationFileException();
        }
        String linea = "";
        try {
            salida.writeInt(9);
            salida.flush();
            linea = (String) entrada.readObject();
            asignaturas = jsonToListString(linea);

            entrada.close();
            socketCliente.close();

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConectorApercibimientos.class.getName()).log(Level.SEVERE, null, ex);
        }

        return asignaturas;

    }

    private List<String> jsonToListString(String linea) {
        java.lang.reflect.Type typeList = new TypeToken<List<String>>() {
        }.getType();
        Gson gson = new Gson();
        List<String> asignaturas = gson.fromJson(linea, typeList);
        return asignaturas;
    }

    /**
     * Envía al servidor una asignatura para introducirla en la base de datos
     * @param materia la asignatura a introducir
     * @return la lista de asignaturas en la base de datos
     * @throws ConfigurationFileException
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public List<String> crearAsignatura(String materia) throws ConfigurationFileException, FileNotFoundException, IOException {
        Socket socketCliente = null;
        ObjectInputStream entrada = null;
        ObjectOutputStream salida = null;
        List<String> asignaturas = new ArrayList<>();
        try {
            String[] parametros = leerConfiguración();
            if (parametros[0] == null || parametros[1] == null) {
                throw new ConfigurationFileException();
            }
            socketCliente = new Socket(parametros[0], Integer.parseInt(parametros[1]));
            salida = new ObjectOutputStream(socketCliente.getOutputStream());
            entrada = new ObjectInputStream(socketCliente.getInputStream());
            System.out.println("Conectado");
        } catch (FileNotFoundException ex) {
            throw new FileNotFoundException();
        } catch (IOException e) {
            throw new IOException();
        } catch (NumberFormatException e) {
            throw new ConfigurationFileException();
        }
        String linea = "";
        try {
            salida.writeInt(8);
            salida.flush();
            salida.writeUTF(materia);
            salida.flush();
            linea = (String) entrada.readObject();
            asignaturas = jsonToListString(linea);

            entrada.close();
            socketCliente.close();

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConectorApercibimientos.class.getName()).log(Level.SEVERE, null, ex);
        }

        return asignaturas;
    }

    /**
     * Cambia el estado del apercibimientos a activado/descativado
     * @param id el id del apercibimiento a modificar
     * @param activo el estado que asignar
     * @return la lista de apercibimientos de la base de datos
     * @throws ConfigurationFileException
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public List<Apercibimiento> desActivarApercibimiento(int id, boolean activo) throws ConfigurationFileException, FileNotFoundException, IOException {
        Socket socketCliente = null;
        ObjectInputStream entrada = null;
        ObjectOutputStream salida = null;
        List<Apercibimiento> apercibimientos = new ArrayList<>();

        try {
            String[] parametros = leerConfiguración();
            if (parametros[0] == null || parametros[1] == null) {
                throw new ConfigurationFileException();
            }
            socketCliente = new Socket(parametros[0], Integer.parseInt(parametros[1]));
            salida = new ObjectOutputStream(socketCliente.getOutputStream());
            entrada = new ObjectInputStream(socketCliente.getInputStream());
            System.out.println("Conectado");
        } catch (FileNotFoundException ex) {
            throw new FileNotFoundException();
        } catch (IOException e) {
            throw new IOException();
        } catch (NumberFormatException e) {
            throw new ConfigurationFileException();
        }
        String linea = "";
        try {
            salida.writeInt(16);
            salida.flush();
            salida.writeInt(id);
            salida.flush();
            salida.writeBoolean(activo);
            salida.flush();
            linea = (String) entrada.readObject();
            apercibimientos = jsonToListApercibimientos(linea);

            entrada.close();
            socketCliente.close();

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConectorApercibimientos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return apercibimientos;

    }

    /**
     * Recoge los periodos academicos del servidor
     * @return
     * @throws ConfigurationFileException
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public List<String> cargarAño() throws ConfigurationFileException, FileNotFoundException, IOException {
        Socket socketCliente = null;
        ObjectInputStream entrada = null;
        ObjectOutputStream salida = null;
        List<String> año = new ArrayList<>();

        try {
            String[] parametros = leerConfiguración();
            if (parametros[0] == null || parametros[1] == null) {
                throw new ConfigurationFileException();
            }
            socketCliente = new Socket(parametros[0], Integer.parseInt(parametros[1]));
            salida = new ObjectOutputStream(socketCliente.getOutputStream());
            entrada = new ObjectInputStream(socketCliente.getInputStream());
            System.out.println("Conectado");
        } catch (FileNotFoundException ex) {
            throw new FileNotFoundException();
        } catch (IOException e) {
            throw new IOException();
        } catch (NumberFormatException e) {
            throw new ConfigurationFileException();
        }
        String linea = "";
        try {
            salida.writeInt(19);
            salida.flush();
            linea = (String) entrada.readObject();
            año = jsonToListString(linea);

            entrada.close();
            socketCliente.close();

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConectorApercibimientos.class.getName()).log(Level.SEVERE, null, ex);
        }

        return año;

    }

    /**
     * Recoge los meses dentro de un peiodo academico del servidor
     * @param año
     * @return
     * @throws ConfigurationFileException
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public List<String> cargarMeses(String año) throws ConfigurationFileException, FileNotFoundException, IOException {
        Socket socketCliente = null;
        ObjectInputStream entrada = null;
        ObjectOutputStream salida = null;
        List<String> meses = new ArrayList<>();

        try {
            String[] parametros = leerConfiguración();
            if (parametros[0] == null || parametros[1] == null) {
                throw new ConfigurationFileException();
            }
            socketCliente = new Socket(parametros[0], Integer.parseInt(parametros[1]));
            salida = new ObjectOutputStream(socketCliente.getOutputStream());
            entrada = new ObjectInputStream(socketCliente.getInputStream());
            System.out.println("Conectado");
        } catch (FileNotFoundException ex) {
            throw new FileNotFoundException();
        } catch (IOException e) {
            throw new IOException();
        } catch (NumberFormatException e) {
            throw new ConfigurationFileException();
        }
        String linea = "";
        try {
            salida.writeInt(20);
            salida.flush();
            salida.writeUTF(año);
            salida.flush();
            linea = (String) entrada.readObject();
            meses = jsonToListString(linea);

            entrada.close();
            socketCliente.close();

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConectorApercibimientos.class.getName()).log(Level.SEVERE, null, ex);
        }

        return meses;
    }

    /**
     * Recoge los cursos dentro de un periodo academico y un mes del servidor
     * @param año
     * @param mes
     * @return
     * @throws ConfigurationFileException
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public List<String> cargarCursos(String año, String mes) throws ConfigurationFileException, FileNotFoundException, IOException {
        Socket socketCliente = null;
        ObjectInputStream entrada = null;
        ObjectOutputStream salida = null;
        List<String> meses = new ArrayList<>();

        try {
            String[] parametros = leerConfiguración();
            if (parametros[0] == null || parametros[1] == null) {
                throw new ConfigurationFileException();
            }
            socketCliente = new Socket(parametros[0], Integer.parseInt(parametros[1]));
            salida = new ObjectOutputStream(socketCliente.getOutputStream());
            entrada = new ObjectInputStream(socketCliente.getInputStream());
            System.out.println("Conectado");
        } catch (FileNotFoundException ex) {
            throw new FileNotFoundException();
        } catch (IOException e) {
            throw new IOException();
        } catch (NumberFormatException e) {
            throw new ConfigurationFileException();
        }
        String linea = "";
        try {
            salida.writeInt(21);
            salida.flush();
            salida.writeUTF(año);
            salida.flush();
            salida.writeUTF(mes);
            salida.flush();
            linea = (String) entrada.readObject();
            meses = jsonToListString(linea);

            entrada.close();
            socketCliente.close();

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConectorApercibimientos.class.getName()).log(Level.SEVERE, null, ex);
        }

        return meses;
    }

    private String[] leerConfiguración() throws FileNotFoundException, IOException {
        String[] parametros = new String[2];

        try (BufferedReader br = new BufferedReader(new FileReader("config.txt"));) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] parametro = line.split(":");
                if (parametro[0].equalsIgnoreCase("address")) {
                    parametros[0] = parametro[1];
                } else if (parametro[0].equalsIgnoreCase("port")) {
                    parametros[1] = parametro[1];
                }
            }
        } catch (FileNotFoundException ex) {
            throw new FileNotFoundException();
        } catch (IOException ex) {
            throw new IOException();
        } catch (Exception e){
            throw new ConfigurationFileException();
        }
        return parametros;
    }

    /**
     * Modifica una asignatura 
     * @param id id de a asgnatura a modificar
     * @param materia nuevo nombre de la asignatura
     * @return la lista con las asignaturas de la base de datos
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public List<String> modificarAsignatura(int id, String materia) throws FileNotFoundException, IOException {
        Socket socketCliente = null;
        ObjectInputStream entrada = null;
        ObjectOutputStream salida = null;
        List<String> asignaturas = new ArrayList<>();
        try {
            String[] parametros = leerConfiguración();
            if (parametros[0] == null || parametros[1] == null) {
                throw new ConfigurationFileException();
            }
            socketCliente = new Socket(parametros[0], Integer.parseInt(parametros[1]));
            salida = new ObjectOutputStream(socketCliente.getOutputStream());
            entrada = new ObjectInputStream(socketCliente.getInputStream());
            System.out.println("Conectado");
        } catch (FileNotFoundException ex) {
            throw new FileNotFoundException();
        } catch (IOException e) {
            throw new IOException();
        } catch (NumberFormatException e) {
            throw new ConfigurationFileException();
        }
        String linea = "";
        try {
            salida.writeInt(22);
            salida.flush();
            salida.writeInt(id);
            salida.flush();
            salida.writeUTF(materia);
            salida.flush();
            linea = (String) entrada.readObject();
            asignaturas = jsonToListString(linea);

            entrada.close();
            socketCliente.close();

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConectorApercibimientos.class.getName()).log(Level.SEVERE, null, ex);
        }

        return asignaturas;
    }

    /**
     * Elimina una asignatura
     * @param id id de la asignatura a eliminar
     * @return
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public List<String> eliminarAsignatura(int id) throws FileNotFoundException, IOException {
        Socket socketCliente = null;
        ObjectInputStream entrada = null;
        ObjectOutputStream salida = null;
        List<String> asignaturas = new ArrayList<>();
        try {
            String[] parametros = leerConfiguración();
            if (parametros[0] == null || parametros[1] == null) {
                throw new ConfigurationFileException();
            }
            socketCliente = new Socket(parametros[0], Integer.parseInt(parametros[1]));
            salida = new ObjectOutputStream(socketCliente.getOutputStream());
            entrada = new ObjectInputStream(socketCliente.getInputStream());
            System.out.println("Conectado");
        } catch (FileNotFoundException ex) {
            throw new FileNotFoundException();
        } catch (IOException e) {
            throw new IOException();
        } catch (NumberFormatException e) {
            throw new ConfigurationFileException();
        }
        String linea = "";
        try {
            salida.writeInt(23);
            salida.flush();
            salida.writeInt(id);
            salida.flush();
            linea = (String) entrada.readObject();
            asignaturas = jsonToListString(linea);

            entrada.close();
            socketCliente.close();

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConectorApercibimientos.class.getName()).log(Level.SEVERE, null, ex);
        }

        return asignaturas;
    }

    /**
     * Recoge los cursos dentro de un periodo académico 
     * @param año
     * @return
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public List<String> cargarCursosFiltro(String año) throws FileNotFoundException, IOException{
        Socket socketCliente = null;
        ObjectInputStream entrada = null;
        ObjectOutputStream salida = null;
        List<String> cursos = new ArrayList<>();

        try {
            String[] parametros = leerConfiguración();
            if (parametros[0] == null || parametros[1] == null) {
                throw new ConfigurationFileException();
            }
            socketCliente = new Socket(parametros[0], Integer.parseInt(parametros[1]));
            salida = new ObjectOutputStream(socketCliente.getOutputStream());
            entrada = new ObjectInputStream(socketCliente.getInputStream());
            System.out.println("Conectado");
        } catch (FileNotFoundException ex) {
            throw new FileNotFoundException();
        } catch (IOException e) {
            throw new IOException();
        } catch (NumberFormatException e) {
            throw new ConfigurationFileException();
        }
        String linea = "";
        try {
            salida.writeInt(24);
            salida.flush();
            salida.writeUTF(año);
            salida.flush();
            linea = (String) entrada.readObject();
            cursos = jsonToListString(linea);

            entrada.close();
            socketCliente.close();

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConectorApercibimientos.class.getName()).log(Level.SEVERE, null, ex);
        }

        return cursos;
    }

    /**
     * Recoge los alumnos dentro de un periodo académico y un curso
     * @param año
     * @param curso
     * @return
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public List<String> cargarAlumnoFiltro(String año, String curso) throws FileNotFoundException, IOException {
        Socket socketCliente = null;
        ObjectInputStream entrada = null;
        ObjectOutputStream salida = null;
        List<String> alumnos = new ArrayList<>();

        try {
            String[] parametros = leerConfiguración();
            if (parametros[0] == null || parametros[1] == null) {
                throw new ConfigurationFileException();
            }
            socketCliente = new Socket(parametros[0], Integer.parseInt(parametros[1]));
            salida = new ObjectOutputStream(socketCliente.getOutputStream());
            entrada = new ObjectInputStream(socketCliente.getInputStream());
            System.out.println("Conectado");
        } catch (FileNotFoundException ex) {
            throw new FileNotFoundException();
        } catch (IOException e) {
            throw new IOException();
        } catch (NumberFormatException e) {
            throw new ConfigurationFileException();
        }
        String linea = "";
        try {
            salida.writeInt(25);
            salida.flush();
            salida.writeUTF(año);
            salida.flush();
            salida.writeUTF(curso);
            salida.flush();
            linea = (String) entrada.readObject();
            alumnos = jsonToListString(linea);

            entrada.close();
            socketCliente.close();

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConectorApercibimientos.class.getName()).log(Level.SEVERE, null, ex);
        }

        return alumnos;
    }

    /**
     * Recoge los apercibimientos de un periodo académico
     * @param año
     * @return
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public List<Apercibimiento> apercibimientosPorAño(String año) throws FileNotFoundException, IOException{
        Socket socketCliente = null;
        ObjectInputStream entrada = null;
        ObjectOutputStream salida = null;
        List<Apercibimiento> apercibimientos = new ArrayList<>();

        try {
            String[] parametros = leerConfiguración();
            if (parametros[0] == null || parametros[1] == null) {
                throw new ConfigurationFileException();
            }
            socketCliente = new Socket(parametros[0], Integer.parseInt(parametros[1]));
            salida = new ObjectOutputStream(socketCliente.getOutputStream());
            entrada = new ObjectInputStream(socketCliente.getInputStream());
            System.out.println("Conectado");
        } catch (FileNotFoundException ex) {
            throw new FileNotFoundException();
        } catch (IOException e) {
            throw new IOException();
        } catch (NumberFormatException e) {
            throw new ConfigurationFileException();
        }
        String linea = "";
        try {
            salida.writeInt(26);
            salida.flush();
            salida.writeUTF(año);
            salida.flush();
            linea = (String) entrada.readObject();
            apercibimientos = jsonToListApercibimientos(linea);

            entrada.close();
            socketCliente.close();

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConectorApercibimientos.class.getName()).log(Level.SEVERE, null, ex);
        }

        return apercibimientos;
    }

    /**
     * Recoge los apercibimientos de un curso en un periodo académico
     * @param año
     * @param curso
     * @return
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public List<Apercibimiento> apercibimientosPorCurso(String año, String curso) throws FileNotFoundException, IOException{
        Socket socketCliente = null;
        ObjectInputStream entrada = null;
        ObjectOutputStream salida = null;
        List<Apercibimiento> apercibimientos = new ArrayList<>();

        try {
            String[] parametros = leerConfiguración();
            if (parametros[0] == null || parametros[1] == null) {
                throw new ConfigurationFileException();
            }
            socketCliente = new Socket(parametros[0], Integer.parseInt(parametros[1]));
            salida = new ObjectOutputStream(socketCliente.getOutputStream());
            entrada = new ObjectInputStream(socketCliente.getInputStream());
            System.out.println("Conectado");
        } catch (FileNotFoundException ex) {
            throw new FileNotFoundException();
        } catch (IOException e) {
            throw new IOException();
        } catch (NumberFormatException e) {
            throw new ConfigurationFileException();
        }
        String linea = "";
        try {
            salida.writeInt(27);
            salida.flush();
            salida.writeUTF(año);
            salida.flush();
            salida.writeUTF(curso);
            salida.flush();
            linea = (String) entrada.readObject();
            apercibimientos = jsonToListApercibimientos(linea);

            entrada.close();
            socketCliente.close();

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConectorApercibimientos.class.getName()).log(Level.SEVERE, null, ex);
        }

        return apercibimientos;
    }

    /**
     * Recoge los apercibimientos de un alumno por curso en un periodo académico
     * @param año
     * @param curso
     * @param alumno
     * @return
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public List<Apercibimiento> apercibimientosPorAlumno(String año, String curso, String alumno) throws FileNotFoundException, IOException{
        Socket socketCliente = null;
        ObjectInputStream entrada = null;
        ObjectOutputStream salida = null;
        List<Apercibimiento> apercibimientos = new ArrayList<>();

        try {
            String[] parametros = leerConfiguración();
            if (parametros[0] == null || parametros[1] == null) {
                throw new ConfigurationFileException();
            }
            socketCliente = new Socket(parametros[0], Integer.parseInt(parametros[1]));
            salida = new ObjectOutputStream(socketCliente.getOutputStream());
            entrada = new ObjectInputStream(socketCliente.getInputStream());
            System.out.println("Conectado");
        } catch (FileNotFoundException ex) {
            throw new FileNotFoundException();
        } catch (IOException e) {
            throw new IOException();
        } catch (NumberFormatException e) {
            throw new ConfigurationFileException();
        }
        String linea = "";
        try {
            salida.writeInt(28);
            salida.flush();
            salida.writeUTF(año);
            salida.flush();
            salida.writeUTF(curso);
            salida.flush();
            salida.writeUTF(alumno);
            salida.flush();
            linea = (String) entrada.readObject();
            apercibimientos = jsonToListApercibimientos(linea);

            entrada.close();
            socketCliente.close();

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConectorApercibimientos.class.getName()).log(Level.SEVERE, null, ex);
        }

        return apercibimientos;
    }

    /**
     * Envia un archivo zip o pdf codificado en base64 al servidor
     * @param base64 Archivo codificado
     * @param extension extensión del archivo
     * @param csrfToken csrfToken de la sesión del usuario
     * @param sessionId sessionId de la sesión del usuario
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public void enviarArchivo(String base64, String extension, String csrfToken, String sessionId) throws FileNotFoundException, IOException{
        Socket socketCliente = null;
        ObjectInputStream entrada = null;
        ObjectOutputStream salida = null;

        try {
            String[] parametros = leerConfiguración();
            if (parametros[0] == null || parametros[1] == null) {
                throw new ConfigurationFileException();
            }
            socketCliente = new Socket(parametros[0], Integer.parseInt(parametros[1]));
            salida = new ObjectOutputStream(socketCliente.getOutputStream());
            entrada = new ObjectInputStream(socketCliente.getInputStream());
            System.out.println("Conectado");
        } catch (FileNotFoundException ex) {
            throw new FileNotFoundException();
        } catch (IOException e) {
            throw new IOException();
        } catch (NumberFormatException e) {
            throw new ConfigurationFileException();
        }
        try {
            salida.writeInt(32);
            salida.flush();
            salida.writeObject(base64);
            salida.flush();
            salida.writeUTF(extension);
            salida.flush();
            salida.writeUTF(csrfToken);
            salida.flush();
            salida.writeUTF(sessionId);
            salida.flush();

            entrada.close();
            socketCliente.close();

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }
}
