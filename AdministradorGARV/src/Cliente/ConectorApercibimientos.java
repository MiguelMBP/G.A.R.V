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
import vo.Apercibimiento;

/**
 *
 * @author miguelmbp
 */
public class ConectorApercibimientos implements Constants{

    public ConectorApercibimientos() {
    }
    
    public List<Apercibimiento> cargarApercibimientos() {
        Socket socketCliente = null;
        ObjectInputStream entrada = null;
        ObjectOutputStream salida = null;
        List<Apercibimiento> apercibimientos = new ArrayList<>();

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
        java.lang.reflect.Type typeList = new TypeToken<List<Apercibimiento>>() {}.getType();
        Gson gson = new Gson();
        List<Apercibimiento> apercibimientos = gson.fromJson(json, typeList);
        return apercibimientos;
    }

    public List<String> cargarAsignaturas() {
        Socket socketCliente = null;
        ObjectInputStream entrada = null;
        ObjectOutputStream salida = null;
        List<String> asignaturas = new ArrayList<>();

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
            salida.writeInt(9);
            salida.flush();
            linea = (String) entrada.readObject();
            asignaturas = jsonToListAsignaturas(linea);

            entrada.close();
            socketCliente.close();

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConectorApercibimientos.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return asignaturas;

    }
    
    private List<String> jsonToListAsignaturas(String linea) {
        java.lang.reflect.Type typeList = new TypeToken<List<String>>() {}.getType();
        Gson gson = new Gson();
        List<String> asignaturas = gson.fromJson(linea, typeList);
        return asignaturas;
    }

    public void crearAsignatura(String materia) {
        Socket socketCliente = null;
        ObjectInputStream entrada = null;
        ObjectOutputStream salida = null;

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
            salida.writeInt(8);
            salida.flush();
            salida.writeUTF(materia);
            salida.flush();
            
            entrada.close();
            socketCliente.close();

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }

    }

}
