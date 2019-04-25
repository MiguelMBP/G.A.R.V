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
import vo.Visita;

/**
 *
 * @author mmbernal
 */
public class ConectorVisitas implements Constants{
    public List<Visita> cargarVisitas() {
        Socket socketCliente = null;
        ObjectInputStream entrada = null;
        ObjectOutputStream salida = null;
        List<Visita> visitas = new ArrayList<>();

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
        java.lang.reflect.Type typeList = new TypeToken<List<Visita>>() {}.getType();
        Gson gson = new Gson();
        List<Visita> visitas = gson.fromJson(linea, typeList);
        return visitas;
    }


}
