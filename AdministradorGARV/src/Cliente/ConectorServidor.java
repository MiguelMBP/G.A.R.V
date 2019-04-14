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

    private static List<Apercibimiento> jsonToListApercibimientos(String json) {
        java.lang.reflect.Type typeList = new TypeToken<List<Apercibimiento>>() {}.getType();
        Gson gson = new Gson();
        List<Apercibimiento> apercibimientos = gson.fromJson(json, typeList);
        return apercibimientos;
    }
}
