package com.example.android.appprofesor.Connectors;

import com.example.android.appprofesor.models.AlumnoApercibimiento;
import com.example.android.appprofesor.models.ClaseApercibimiento;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Type;

import com.example.android.appprofesor.models.TutorAlumno;
import com.example.android.appprofesor.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class WarningConnector implements Constants {

    private WarningConnector() {
    }

    public static List<ClaseApercibimiento> getMaterias() {
        Socket socketCliente = null;
        ObjectInputStream entrada = null;
        ObjectOutputStream salida = null;
        List<ClaseApercibimiento> materias = new ArrayList<>();


        try {
            InetAddress address = InetAddress.getByName(ADDRESS);
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

    public static List<TutorAlumno> getAlumnosTutor() {
        Socket socketCliente = null;
        ObjectInputStream entrada = null;
        ObjectOutputStream salida = null;
        List<TutorAlumno> alumnos = new ArrayList<>();


        try {
            InetAddress address = InetAddress.getByName(ADDRESS);
            socketCliente = new Socket(address, PORT);
            salida = new ObjectOutputStream(socketCliente.getOutputStream());
            entrada = new ObjectInputStream(socketCliente.getInputStream());
        } catch (IOException e) {
            System.err.println("No puede establer canales de E/S para la conexión" + e);
            System.exit(-1);
        }
        String linea = "";
        try {
            int op = 4;
            salida.writeInt(op);
            salida.flush();
            salida.writeObject("2º DAM B");
            salida.flush();
            linea = (String) entrada.readObject();

            alumnos = jsonToListAlumnoTutor(linea);

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

        return alumnos;

    }


    private static List<TutorAlumno> jsonToListAlumnoTutor(String json) {
        Type typeList = new TypeToken<List<TutorAlumno>>() {
        }.getType();
        Gson gson = new Gson();
        List<TutorAlumno> alumnos = gson.fromJson(json, typeList);
        return alumnos;
    }


}
