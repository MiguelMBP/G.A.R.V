package com.example.android.appprofesor.Connectors;

import android.content.Context;
import android.content.SharedPreferences;

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

import com.example.android.appprofesor.models.Settings;
import com.example.android.appprofesor.models.TutorAlumno;
import com.example.android.appprofesor.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Realiza las peticiones del cliente al servidor multihilos para el módulo de apercibimientos
 */
public class WarningConnector implements Constants {

    private WarningConnector() {
    }

    /**
     * Recoge los apercibimientos agrupados por asignatura, curso y alumno
     * @param settings
     * @return
     */
    public static List<ClaseApercibimiento> getMaterias(Settings settings) {
        Socket socketCliente = null;
        ObjectInputStream entrada = null;
        ObjectOutputStream salida = null;
        List<ClaseApercibimiento> materias = new ArrayList<>();


        try {
            InetAddress address = InetAddress.getByName(settings.getAddress());
            socketCliente = new Socket(address, settings.getPort());
            salida = new ObjectOutputStream(socketCliente.getOutputStream());
            entrada = new ObjectInputStream(socketCliente.getInputStream());
        } catch (IOException e) {
            System.err.println("No puede establer canales de E/S para la conexión" + e);
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

    /**
     * Recoge los apercibimientos agrupados por alumno y asignatura del curso tutor del usuario
     * @param context
     * @param settings
     * @return
     */
    public static List<TutorAlumno> getAlumnosTutor(Context context, Settings settings) {
        Socket socketCliente = null;
        ObjectInputStream entrada = null;
        ObjectOutputStream salida = null;
        List<TutorAlumno> alumnos = new ArrayList<>();
        SharedPreferences prefs =
                context.getSharedPreferences("userData", Context.MODE_PRIVATE);


        try {
            InetAddress address = InetAddress.getByName(settings.getAddress());
            socketCliente = new Socket(address, settings.getPort());
            salida = new ObjectOutputStream(socketCliente.getOutputStream());
            entrada = new ObjectInputStream(socketCliente.getInputStream());
        } catch (IOException e) {
            System.err.println("No puede establer canales de E/S para la conexión" + e);
        }
        String linea = "";
        try {
            int op = 4;
            salida.writeInt(op);
            salida.flush();
            salida.writeObject(prefs.getString("username", "error"));
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
