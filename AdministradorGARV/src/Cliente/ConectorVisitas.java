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
import vo.Alumno;
import vo.Empresa;
import vo.Visita;

/**
 * Clase que realiza las operaciones entre el cliente y el servidor multihilos para el módulo de visitas
 * @author miguelmbp
 */
public class ConectorVisitas {

    /**
     * Recoge las visitas del servidor
     * @return
     * @throws ConfigurationFileException
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public List<Visita> cargarVisitas() throws ConfigurationFileException, FileNotFoundException, IOException {
        Socket socketCliente = null;
        ObjectInputStream entrada = null;
        ObjectOutputStream salida = null;
        List<Visita> visitas = new ArrayList<>();

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
            salida.writeInt(2);
            salida.flush();
            linea = (String) entrada.readObject();
            visitas = jsonToListVisita(linea);

            entrada.close();
            socketCliente.close();

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConectorApercibimientos.class.getName()).log(Level.SEVERE, null, ex);
        }

        return visitas;
    }

    private List<Visita> jsonToListVisita(String linea) {
        java.lang.reflect.Type typeList = new TypeToken<List<Visita>>() {
        }.getType();
        Gson gson = new Gson();
        List<Visita> visitas = gson.fromJson(linea, typeList);
        return visitas;
    }

    /**
     * Actualiza una visita para validarla/invalidarla
     * @param id id de la visita a actualizar
     * @param activo nuevo estado de la visita
     * @return
     * @throws ConfigurationFileException
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public List<Visita> inValidarVisita(int id, boolean activo) throws ConfigurationFileException, FileNotFoundException, IOException {
        Socket socketCliente = null;
        ObjectInputStream entrada = null;
        ObjectOutputStream salida = null;
        List<Visita> visitas = new ArrayList<>();
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
            salida.writeInt(17);
            salida.flush();
            salida.writeInt(id);
            salida.flush();
            salida.writeBoolean(activo);
            salida.flush();
            linea = (String) entrada.readObject();
            visitas = jsonToListVisita(linea);

            entrada.close();
            socketCliente.close();

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConectorVisitas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return visitas;
    }

    /**
     * Recoge la imagen de una visita del servidor
     * @param id id de la visita
     * @param cookies cookies de la sesión
     * @return Imagen codificada en base64
     * @throws ConfigurationFileException
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public String getImagen(int id, List<String> cookies) throws ConfigurationFileException, FileNotFoundException, IOException {
        Socket socketCliente = null;
        ObjectInputStream entrada = null;
        ObjectOutputStream salida = null;
        String base64 = null;

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
            salida.writeInt(18);
            salida.flush();
            salida.writeInt(id);
            salida.flush();
            salida.writeUTF(cookies.get(0));
            salida.flush();
            salida.writeUTF(cookies.get(1));
            salida.flush();

            base64 = (String) entrada.readObject();

            entrada.close();
            socketCliente.close();

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConectorVisitas.class.getName()).log(Level.SEVERE, null, ex);
        }

        return base64;
    }

    private String[] leerConfiguración() throws FileNotFoundException, IOException {
        String[] parametros = new String[2];

        try (BufferedReader br = new BufferedReader(new FileReader("config.txt"));) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] parametro = line.split(":");
                if (parametro.length == 2 && parametro[0].equalsIgnoreCase("address")) {
                    parametros[0] = parametro[1];
                } else if (parametro.length == 2 && parametro[0].equalsIgnoreCase("port")) {
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
     * Recoge las empresas del servidor
     * @return
     * @throws ConfigurationFileException
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public List<Empresa> cargarEmpresas() throws ConfigurationFileException, FileNotFoundException, IOException {
        Socket socketCliente = null;
        ObjectInputStream entrada = null;
        ObjectOutputStream salida = null;
        List<Empresa> empresas = new ArrayList<>();

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
            salida.writeInt(12);
            salida.flush();
            linea = (String) entrada.readObject();
            empresas = jsonToListEmpresa(linea);

            entrada.close();
            socketCliente.close();

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConectorApercibimientos.class.getName()).log(Level.SEVERE, null, ex);
        }

        return empresas;
    }

    private List<Empresa> jsonToListEmpresa(String linea) {
        java.lang.reflect.Type typeList = new TypeToken<List<Empresa>>() {
        }.getType();
        Gson gson = new Gson();
        List<Empresa> visitas = gson.fromJson(linea, typeList);
        return visitas;
    }

    /**
     * Recoge los alumnos del servidor
     * @return
     * @throws ConfigurationFileException
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public List<Alumno> cargarAlumnos() throws ConfigurationFileException, FileNotFoundException, IOException {
        Socket socketCliente = null;
        ObjectInputStream entrada = null;
        ObjectOutputStream salida = null;
        List<Alumno> alumnos = new ArrayList<>();

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
            salida.writeInt(34);
            salida.flush();
            linea = (String) entrada.readObject();
            alumnos = jsonToListAlumno(linea);

            entrada.close();
            socketCliente.close();

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConectorApercibimientos.class.getName()).log(Level.SEVERE, null, ex);
        }

        return alumnos;
    }

    private List<Alumno> jsonToListAlumno(String linea) {
        java.lang.reflect.Type typeList = new TypeToken<List<Alumno>>() {
        }.getType();
        Gson gson = new Gson();
        List<Alumno> alumnos = gson.fromJson(linea, typeList);
        return alumnos;    
    }
    
    /**
     * Inserta una empresa en el servidor
     * @param empresa
     * @return
     * @throws ConfigurationFileException
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public int insertarEmpresa(Empresa empresa) throws ConfigurationFileException, FileNotFoundException, IOException {
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
        int linea = -1;
        try {
            salida.writeInt(13);
            salida.flush();
            salida.writeObject(new Gson().toJson(empresa));
            salida.flush();
            linea = entrada.readInt();

            entrada.close();
            socketCliente.close();

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }

        return linea;
    }

    /**
     * Recoge una empresa del servidor
     * @param id id de la empresa
     * @return
     * @throws ConfigurationFileException
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public Empresa cargarEmpresa(String id) throws ConfigurationFileException, FileNotFoundException, IOException{
        Socket socketCliente = null;
        ObjectInputStream entrada = null;
        ObjectOutputStream salida = null;
        Empresa empresa = new Empresa();

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
            salida.writeInt(35);
            salida.flush();
            salida.writeUTF(id);
            salida.flush();
            linea = (String) entrada.readObject();
            empresa = jsonToEmpresaObjeto(linea);

            entrada.close();
            socketCliente.close();

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConectorApercibimientos.class.getName()).log(Level.SEVERE, null, ex);
        }

        return empresa;
    }

    private Empresa jsonToEmpresaObjeto(String linea) {
        java.lang.reflect.Type typeList = new TypeToken<Empresa>() {
        }.getType();
        Gson gson = new Gson();
        Empresa empresa = gson.fromJson(linea, typeList);
        return empresa;
    }

    /**
     * Actualiza una empresa
     * @param empresa empresa a actualizar con los nuevos datos
     * @return
     * @throws ConfigurationFileException
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public int modificarEmpresa(Empresa empresa) throws ConfigurationFileException, FileNotFoundException, IOException{
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
        int linea = -1;
        try {
            salida.writeInt(36);
            salida.flush();
            salida.writeObject(new Gson().toJson(empresa));
            salida.flush();
            linea = entrada.readInt();

            entrada.close();
            socketCliente.close();

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }

        return linea;
    }

    /**
     * Inserta un alumno
     * @param alumno
     * @return
     * @throws ConfigurationFileException
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public int insertarAlumno(Alumno alumno) throws ConfigurationFileException, FileNotFoundException, IOException{
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
        int linea = -1;
        try {
            salida.writeInt(14);
            salida.flush();
            salida.writeObject(new Gson().toJson(alumno));
            salida.flush();
            linea = entrada.readInt();

            entrada.close();
            socketCliente.close();

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }

        return linea;
    }

    /**
     * Recoge un alumno de la base de datos
     * @param id id del alumno
     * @return
     * @throws ConfigurationFileException
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public Alumno cargarAlumno(String id) throws ConfigurationFileException, FileNotFoundException, IOException{
        Socket socketCliente = null;
        ObjectInputStream entrada = null;
        ObjectOutputStream salida = null;
        Alumno alumno = new Alumno();

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
            salida.writeInt(37);
            salida.flush();
            salida.writeUTF(id);
            salida.flush();
            linea = (String) entrada.readObject();
            alumno = jsonToAlumnoObjeto(linea);

            entrada.close();
            socketCliente.close();

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConectorApercibimientos.class.getName()).log(Level.SEVERE, null, ex);
        }

        return alumno;
    }

    private Alumno jsonToAlumnoObjeto(String linea) {
        java.lang.reflect.Type typeList = new TypeToken<Alumno>() {
        }.getType();
        Gson gson = new Gson();
        Alumno alumno = gson.fromJson(linea, typeList);
        return alumno;
    }

    /**
     * Modifica un alumno
     * @param alumno alumno a modificar con los datos actualzados
     * @return
     * @throws ConfigurationFileException
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public int modificarAlumno(Alumno alumno) throws ConfigurationFileException, FileNotFoundException, IOException{
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
        int linea = -1;
        try {
            salida.writeInt(38);
            salida.flush();
            salida.writeObject(new Gson().toJson(alumno));
            salida.flush();
            linea = entrada.readInt();

            entrada.close();
            socketCliente.close();

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }

        return linea;
    }
}
