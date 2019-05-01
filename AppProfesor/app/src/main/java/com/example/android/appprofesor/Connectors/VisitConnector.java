package com.example.android.appprofesor.Connectors;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.android.appprofesor.models.Alumno;
import com.example.android.appprofesor.models.ClaseApercibimiento;
import com.example.android.appprofesor.models.Empresa;
import com.example.android.appprofesor.models.RegistroVisita;
import com.example.android.appprofesor.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class VisitConnector implements Constants {
    
    public static List<Alumno> getAlumnos(Context context) {
        Socket socketCliente = null;
        ObjectInputStream entrada = null;
        ObjectOutputStream salida = null;
        List<Alumno> materias = new ArrayList<>();
        SharedPreferences prefs =
                context.getSharedPreferences("userData", Context.MODE_PRIVATE);


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
            int op = 10;
            salida.writeInt(op);
            salida.flush();
            salida.writeUTF(prefs.getString("username", "error"));
            salida.flush();
            linea = (String) entrada.readObject();

            materias = jsonToListVisita(linea);

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

    public static List<Alumno> getTodosAlumnos() {
        Socket socketCliente = null;
        ObjectInputStream entrada = null;
        ObjectOutputStream salida = null;
        List<Alumno> materias = new ArrayList<>();


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
            int op = 11;
            salida.writeInt(op);
            salida.flush();
            linea = (String) entrada.readObject();

            materias = jsonToListVisita(linea);

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

    public static List<Empresa> getEmpresas() {
        Socket socketCliente = null;
        ObjectInputStream entrada = null;
        ObjectOutputStream salida = null;
        List<Empresa> empresas = new ArrayList<>();


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
            int op = 12;
            salida.writeInt(op);
            salida.flush();
            linea = (String) entrada.readObject();

            empresas = jsonToListEmpresa(linea);

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

        return empresas;

    }

    private static List<Empresa> jsonToListEmpresa(String linea) {
        java.lang.reflect.Type typeList = new TypeToken<List<Empresa>>() {}.getType();
        Gson gson = new Gson();
        List<Empresa> empresas = gson.fromJson(linea, typeList);
        return empresas;
    }

    private static List<Alumno> jsonToListVisita(String linea) {
        java.lang.reflect.Type typeList = new TypeToken<List<Alumno>>() {}.getType();
        Gson gson = new Gson();
        List<Alumno> alumnos = gson.fromJson(linea, typeList);
        return alumnos;
    }

    public static int addEmpresa(Empresa empresa) {
        Socket socketCliente = null;
        ObjectInputStream entrada = null;
        ObjectOutputStream salida = null;

        try {
            InetAddress address = InetAddress.getByName(ADDRESS);
            socketCliente = new Socket(address, PORT);
            salida = new ObjectOutputStream(socketCliente.getOutputStream());
            entrada = new ObjectInputStream(socketCliente.getInputStream());
        } catch (IOException e) {
            System.err.println("No puede establer canales de E/S para la conexión" + e);
            System.exit(-1);
        }
        int id = -1;
        try {
            int op = 13;
            salida.writeInt(op);
            salida.flush();
            salida.writeObject(new Gson().toJson(empresa));
            id = entrada.readInt();
            System.out.println(id);

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }

        try {
            entrada.close();
            socketCliente.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return id;
    }

    public static int addAlumno(Alumno alumno) {
        Socket socketCliente = null;
        ObjectInputStream entrada = null;
        ObjectOutputStream salida = null;

        try {
            InetAddress address = InetAddress.getByName(ADDRESS);
            socketCliente = new Socket(address, PORT);
            salida = new ObjectOutputStream(socketCliente.getOutputStream());
            entrada = new ObjectInputStream(socketCliente.getInputStream());
        } catch (IOException e) {
            System.err.println("No puede establer canales de E/S para la conexión" + e);
            System.exit(-1);
        }
        int id = -1;
        try {
            int op = 14;
            salida.writeInt(op);
            salida.flush();
            salida.writeObject(new Gson().toJson(alumno));
            id = entrada.readInt();
            System.out.println(id);

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }

        try {
            entrada.close();
            socketCliente.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return id;
    }

    public static int addVisita(RegistroVisita visita) {
        Socket socketCliente = null;
        ObjectInputStream entrada = null;
        ObjectOutputStream salida = null;

        try {
            InetAddress address = InetAddress.getByName(ADDRESS);
            socketCliente = new Socket(address, PORT);
            salida = new ObjectOutputStream(socketCliente.getOutputStream());
            entrada = new ObjectInputStream(socketCliente.getInputStream());
        } catch (IOException e) {
            System.err.println("No puede establer canales de E/S para la conexión" + e);
            System.exit(-1);
        }
        int id = -1;
        try {
            int op = 15;
            salida.writeInt(op);
            salida.flush();
            Gson gson = new GsonBuilder().setDateFormat("yyyy-mm-dd").create();
            salida.writeObject(gson.toJson(visita));
            salida.flush();
            id = entrada.readInt();
            System.out.println(id);

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }

        try {
            entrada.close();
            socketCliente.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return id;
    }
}