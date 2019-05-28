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
import vo.Usuario;

/**
 * Clase que realiza las operaciones entre el cliente y el servidor multihilos para el módulo de usuarios
 * @author miguelmbp
 */
public class ConectorUsuarios {

    /**
     * Envía una petición al servidor para crear un usuario
     * @param cookies csrfToken y sessionId de la sesión del usuario
     * @param usuarioCrear Nombre del usuario
     * @param contraseñaCrear contraseña del usuario
     * @param correo correo del usuario
     * @param nombre nombre del usuario
     * @param apellidos apellidos del usuario
     * @param curso curso tutor del usuario
     * @param dni dni del usuario
     * @return booleano indicando si el usuario se creó con éxito
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ConfigurationFileException 
     */
    public boolean crearUsuario(List<String> cookies, String usuarioCrear, String contraseñaCrear, String correo, String nombre, String apellidos, String curso, String dni) throws FileNotFoundException, IOException, ConfigurationFileException {
        Socket socketCliente = null;
        ObjectInputStream entrada = null;
        ObjectOutputStream salida = null;
        List<Usuario> usuarios = new ArrayList<>();

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
        boolean creado = false;
        try {
            salida.writeInt(7);
            salida.flush();
            salida.writeUTF(cookies.get(0));
            salida.flush();
            salida.writeUTF(cookies.get(1));
            salida.flush();
            salida.writeUTF(usuarioCrear);
            salida.flush();
            salida.writeUTF(contraseñaCrear);
            salida.flush();
            salida.writeUTF(correo);
            salida.flush();
            salida.writeUTF(dni);
            salida.flush();
            salida.writeUTF(nombre);
            salida.flush();
            salida.writeUTF(apellidos);
            salida.flush();
            salida.writeUTF(curso);
            salida.flush();

            creado = entrada.readBoolean();

            entrada.close();
            socketCliente.close();

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }

        return creado;
    }

    private List<Usuario> jsonToListUsuarios(String linea) {
        java.lang.reflect.Type typeList = new TypeToken<List<Usuario>>() {
        }.getType();
        Gson gson = new Gson();
        List<Usuario> usuarios = gson.fromJson(linea, typeList);
        return usuarios;
    }

    /**
     * Recoge los usuarios del servidor
     * @return
     * @throws ConfigurationFileException
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public List<Usuario> cargarUsuarios() throws ConfigurationFileException, FileNotFoundException, IOException {
        Socket socketCliente = null;
        ObjectInputStream entrada = null;
        ObjectOutputStream salida = null;
        List<Usuario> usuarios = new ArrayList<>();

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
            salida.writeInt(6);
            salida.flush();
            linea = (String) entrada.readObject();
            usuarios = jsonToListUsuarios(linea);

            entrada.close();
            socketCliente.close();

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConectorUsuarios.class.getName()).log(Level.SEVERE, null, ex);
        }

        return usuarios;

    }

    /**
     * Realiza una petición al servidor para iniciar sesión
     * @param username
     * @param password
     * @return Lista con las cookies de la sesión
     * @throws ConfigurationFileException
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public List<String> iniciarSesion(String username, String password) throws ConfigurationFileException, FileNotFoundException, IOException {
        Socket socketCliente = null;
        ObjectInputStream entrada = null;
        ObjectOutputStream salida = null;
        boolean existe = false;
        boolean staff = false;
        List<String> cookies = new ArrayList<>();

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
            salida.writeInt(39);
            salida.flush();
            salida.writeUTF(username);
            salida.flush();
            salida.writeUTF(password);
            salida.flush();

            existe = entrada.readBoolean();

            if (existe) {
                staff = entrada.readBoolean();
                if (staff) {
                    cookies.add(entrada.readUTF());
                    cookies.add(entrada.readUTF());
                } else {
                    cookies = null;
                }
            }

            entrada.close();
            socketCliente.close();

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }

        return cookies;

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
     * Cambia la contraseña de un usuario
     * @param cookies cookies de la sesión
     * @param usuario usuario a cambiar la contraseña
     * @param contraseña nueva contraseña
     * @return booleano indicando si la contraseña se cambió con éxito
     * @throws ConfigurationFileException
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public boolean cambiarContraseña(List<String> cookies, String usuario, String contraseña) throws ConfigurationFileException, FileNotFoundException, IOException {
        Socket socketCliente = null;
        ObjectInputStream entrada = null;
        ObjectOutputStream salida = null;
        boolean cambiado = false;

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
            salida.writeInt(29);
            salida.flush();
            salida.writeUTF(usuario);
            salida.flush();
            salida.writeUTF(contraseña);
            salida.flush();
            salida.writeUTF(cookies.get(0));
            salida.flush();
            salida.writeUTF(cookies.get(1));
            salida.flush();

            cambiado = entrada.readBoolean();

            entrada.close();
            socketCliente.close();

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }

        return cambiado;
    }

    /**
     * Modifica un usuario
     * @param usuario el usuario a modificar con los nuevos datos
     * @throws ConfigurationFileException
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public void modificarUsuario(Usuario usuario) throws ConfigurationFileException, FileNotFoundException, IOException {
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
        String linea = "";
        try {
            salida.writeInt(31);
            salida.flush();
            salida.writeObject(new Gson().toJson(usuario));
            salida.flush();

            entrada.close();
            socketCliente.close();

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }

    }

    /**
     * Recoge los datos de un usuario del servidor
     * @param usuario Nombre dle usuario
     * @return Los datos del usuario
     * @throws ConfigurationFileException
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public Usuario cargarUsuario(String usuario) throws ConfigurationFileException, FileNotFoundException, IOException {
        Socket socketCliente = null;
        ObjectInputStream entrada = null;
        ObjectOutputStream salida = null;
        Usuario u = new Usuario();

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
            salida.writeInt(30);
            salida.flush();
            salida.writeUTF(usuario);
            salida.flush();
            linea = (String) entrada.readObject();
            u = jsonToUsuarios(linea);

            entrada.close();
            socketCliente.close();

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConectorUsuarios.class.getName()).log(Level.SEVERE, null, ex);
        }

        return u;

    }

    private Usuario jsonToUsuarios(String linea) {
        java.lang.reflect.Type typeList = new TypeToken<Usuario>() {
        }.getType();
        Gson gson = new Gson();
        Usuario usuario = gson.fromJson(linea, typeList);
        return usuario;
    }

    /**
     * Envía un archivo csv al servidor
     * @param base64 archivo codificado en base64
     * @param csrfToken csrfToken de la sesión
     * @param sessionId sessionId de la sesión
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public void enviarArchivo(String base64, String csrfToken, String sessionId) throws FileNotFoundException, IOException {
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
            salida.writeInt(33);
            salida.flush();
            salida.writeObject(base64);
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
    
    /**
     * Elimina un usuario 
     * @param cookies cookies de la sesión
     * @param usuario nombre del usuario a eliminar
     * @return booleano indicando si el usuario se eliminó con éxito
     * @throws ConfigurationFileException
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public boolean eliminarUsuario(List<String> cookies, String usuario) throws ConfigurationFileException, FileNotFoundException, IOException {
        Socket socketCliente = null;
        ObjectInputStream entrada = null;
        ObjectOutputStream salida = null;
        boolean eliminado = false;

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
            salida.writeInt(40);
            salida.flush();
            salida.writeUTF(usuario);
            salida.flush();
            salida.writeUTF(cookies.get(0));
            salida.flush();
            salida.writeUTF(cookies.get(1));
            salida.flush();

            eliminado = entrada.readBoolean();

            entrada.close();
            socketCliente.close();

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }

        return eliminado;
    }
}
