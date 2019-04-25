/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import Util.Constants;
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
import vo.Usuario;

/**
 *
 * @author mmbernal
 */
public class ConectorUsuarios implements Constants{
    public boolean crearUsuario(List<String> cookies, String usuarioCrear, String contraseñaCrear, String correo, String nombre, String apellidos, String curso, String dni) {
        Socket socketCliente = null;
        ObjectInputStream entrada = null;
        ObjectOutputStream salida = null;
        List<Usuario> usuarios = new ArrayList<>();

        try {
            socketCliente = new Socket(ADDRESS, PORT);
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
        java.lang.reflect.Type typeList = new TypeToken<List<Usuario>>() {}.getType();
        Gson gson = new Gson();
        List<Usuario> usuarios = gson.fromJson(linea, typeList);
        return usuarios;
    }

    public List<Usuario> cargarUsuarios() {
        Socket socketCliente = null;
        ObjectInputStream entrada = null;
        ObjectOutputStream salida = null;
        List<Usuario> usuarios = new ArrayList<>();

        try {
            socketCliente = new Socket(ADDRESS, PORT);
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
            Logger.getLogger(ConectorUsuarios.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return usuarios;

    }
    
        
    
    public List<String> iniciarSesion(String username, String password) {
        Socket socketCliente = null;
        ObjectInputStream entrada = null;
        ObjectOutputStream salida = null;
        boolean existe = false;
        List<String> cookies = new ArrayList<>();

        try {
            socketCliente = new Socket(ADDRESS, PORT);
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
            
            if (existe) {
                cookies.add(entrada.readUTF());
                cookies.add(entrada.readUTF());
            }

            entrada.close();
            socketCliente.close();

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } 
        
        return cookies;

    }
}
