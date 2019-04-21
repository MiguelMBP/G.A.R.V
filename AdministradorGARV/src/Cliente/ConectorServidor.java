/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import Interfaz.Apercibimientos;
import static Interfaz.Apercibimientos.PORT;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import vo.Apercibimiento;
import vo.Usuario;
import vo.Visita;

/**
 *
 * @author miguelmbp
 */
public class ConectorServidor {

    public ConectorServidor() {
    }
    
    public List<Apercibimiento> cargarApercibimientos() {
        Socket socketCliente = null;
        ObjectInputStream entrada = null;
        ObjectOutputStream salida = null;
        List<Apercibimiento> apercibimientos = new ArrayList<>();

        try {
            socketCliente = new Socket("localhost", PORT);
            salida = new ObjectOutputStream(socketCliente.getOutputStream());
            entrada = new ObjectInputStream(socketCliente.getInputStream());
            System.out.println("Conectado");
        } catch (IOException e) {
            System.err.println("No puede establer canales de E/S para la conexión");
            System.exit(-1);
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
            Logger.getLogger(Apercibimientos.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return apercibimientos;

    }
    
    public boolean iniciarSesion(String username, String password) {
        Socket socketCliente = null;
        ObjectInputStream entrada = null;
        ObjectOutputStream salida = null;
        boolean existe = false;

        try {
            socketCliente = new Socket("localhost", PORT);
            salida = new ObjectOutputStream(socketCliente.getOutputStream());
            entrada = new ObjectInputStream(socketCliente.getInputStream());
            System.out.println("Conectado");
        } catch (IOException e) {
            System.err.println("No puede establer canales de E/S para la conexión");
            System.exit(-1);
        }
        try {
            salida.writeInt(5);
            salida.flush();
            salida.writeUTF(username);
            salida.flush();
            salida.writeUTF(password);
            salida.flush();
            
            existe = entrada.readBoolean();

            entrada.close();
            socketCliente.close();

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } 
        
        return existe;

    }

    private List<Apercibimiento> jsonToListApercibimientos(String json) {
        java.lang.reflect.Type typeList = new TypeToken<List<Apercibimiento>>() {}.getType();
        Gson gson = new Gson();
        List<Apercibimiento> apercibimientos = gson.fromJson(json, typeList);
        return apercibimientos;
    }
    
    public List<Visita> cargarVisitas() {
        Socket socketCliente = null;
        ObjectInputStream entrada = null;
        ObjectOutputStream salida = null;
        List<Visita> visitas = new ArrayList<>();

        try {
            socketCliente = new Socket("localhost", PORT);
            salida = new ObjectOutputStream(socketCliente.getOutputStream());
            entrada = new ObjectInputStream(socketCliente.getInputStream());
            System.out.println("Conectado");
        } catch (IOException e) {
            System.err.println("No puede establer canales de E/S para la conexión");
            System.exit(-1);
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
            Logger.getLogger(Apercibimientos.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return visitas;
    }

    private List<Visita> jsonToListVisita(String linea) {
        java.lang.reflect.Type typeList = new TypeToken<List<Visita>>() {}.getType();
        Gson gson = new Gson();
        List<Visita> visitas = gson.fromJson(linea, typeList);
        return visitas;
    }
    
    public List<Usuario> cargarUsuarios() {
        Socket socketCliente = null;
        ObjectInputStream entrada = null;
        ObjectOutputStream salida = null;
        List<Usuario> usuarios = new ArrayList<>();

        try {
            socketCliente = new Socket("localhost", PORT);
            salida = new ObjectOutputStream(socketCliente.getOutputStream());
            entrada = new ObjectInputStream(socketCliente.getInputStream());
            System.out.println("Conectado");
        } catch (IOException e) {
            System.err.println("No puede establer canales de E/S para la conexión");
            System.exit(-1);
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
            Logger.getLogger(Apercibimientos.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return usuarios;

    }

    private List<Usuario> jsonToListUsuarios(String linea) {
        java.lang.reflect.Type typeList = new TypeToken<List<Usuario>>() {}.getType();
        Gson gson = new Gson();
        List<Usuario> usuarios = gson.fromJson(linea, typeList);
        return usuarios;
    }

    public boolean crearUsuario(String username, String password, String usuarioCrear, String contraseñaCrear, String correo, String nombre, String apellidos, String curso, String dni) {
        Socket socketCliente = null;
        ObjectInputStream entrada = null;
        ObjectOutputStream salida = null;
        List<Usuario> usuarios = new ArrayList<>();

        try {
            socketCliente = new Socket("localhost", PORT);
            salida = new ObjectOutputStream(socketCliente.getOutputStream());
            entrada = new ObjectInputStream(socketCliente.getInputStream());
            System.out.println("Conectado");
        } catch (IOException e) {
            System.err.println("No puede establer canales de E/S para la conexión");
            System.exit(-1);
        }
        boolean creado = false;
        try {
            salida.writeInt(7);
            salida.flush();
            salida.writeUTF(username);
            salida.flush();
            salida.writeUTF(password);
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
}
