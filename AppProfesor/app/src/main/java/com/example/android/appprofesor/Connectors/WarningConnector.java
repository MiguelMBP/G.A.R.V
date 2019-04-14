package com.example.android.appprofesor.Connectors;

import com.example.android.appprofesor.models.ClaseApercibimiento;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class WarningConnector {

    public static final int PORT = 4444;

    private WarningConnector() {
    }

    public static List<ClaseApercibimiento> getMaterias() {
        Socket socketCliente = null;
        ObjectInputStream entrada = null;
        ObjectOutputStream salida = null;
        List<ClaseApercibimiento> materias = new ArrayList<>();


        try {
            InetAddress address = InetAddress.getByName("192.168.1.65");
            socketCliente = new Socket(address, PORT);
            salida = new ObjectOutputStream(socketCliente.getOutputStream());
            entrada = new ObjectInputStream(socketCliente.getInputStream());
        } catch (IOException e) {
            System.err.println("No puede establer canales de E/S para la conexión" + e);
            System.exit(-1);
        }
        String linea = "";
        try {
            int op = 3;
            salida.writeInt(op);
            salida.flush();
            linea = (String) entrada.readObject();

            materias = jsonToListMateria(linea);

            /*for (ClaseApercibimiento claseApercibimiento : materias) {
                System.out.println(claseApercibimiento);
            }*/



        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            entrada.close();
            socketCliente.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return materias;

    }

    private static List<ClaseApercibimiento> jsonToListMateria(String json) {
        Type typeList = new TypeToken<List<ClaseApercibimiento>>() {
        }.getType();
        Gson gson = new Gson();
        List<ClaseApercibimiento> materias = gson.fromJson(json, typeList);
        return materias;
    }
}
