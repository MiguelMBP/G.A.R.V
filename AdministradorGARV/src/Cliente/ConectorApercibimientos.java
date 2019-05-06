/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import Util.ConfigurationFileException;
import Util.Constants;
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
import vo.Apercibimiento;

/**
 *
 * @author miguelmbp
 */
public class ConectorApercibimientos implements Constants {

    public ConectorApercibimientos() {
    }

    public List<Apercibimiento> cargarApercibimientos() throws FileNotFoundException, IOException, ConfigurationFileException {
        Socket socketCliente = null;
        ObjectInputStream entrada = null;
        ObjectOutputStream salida = null;
        List<Apercibimiento> apercibimientos = new ArrayList<>();

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
        java.lang.reflect.Type typeList = new TypeToken<List<Apercibimiento>>() {
        }.getType();
        Gson gson = new Gson();
        List<Apercibimiento> apercibimientos = gson.fromJson(json, typeList);
        return apercibimientos;
    }

    public List<String> cargarAsignaturas() throws ConfigurationFileException, FileNotFoundException, IOException {
        Socket socketCliente = null;
        ObjectInputStream entrada = null;
        ObjectOutputStream salida = null;
        List<String> asignaturas = new ArrayList<>();

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
            salida.writeInt(9);
            salida.flush();
            linea = (String) entrada.readObject();
            asignaturas = jsonToListString(linea);

            entrada.close();
            socketCliente.close();

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConectorApercibimientos.class.getName()).log(Level.SEVERE, null, ex);
        }

        return asignaturas;

    }

    private List<String> jsonToListString(String linea) {
        java.lang.reflect.Type typeList = new TypeToken<List<String>>() {
        }.getType();
        Gson gson = new Gson();
        List<String> asignaturas = gson.fromJson(linea, typeList);
        return asignaturas;
    }

    public void crearAsignatura(String materia) throws ConfigurationFileException, FileNotFoundException, IOException {
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

    public void desActivarApercibimiento(int id, boolean activo) throws ConfigurationFileException, FileNotFoundException, IOException {
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
            salida.writeInt(16);
            salida.flush();
            salida.writeInt(id);
            salida.flush();
            salida.writeBoolean(activo);
            salida.flush();

            entrada.close();
            socketCliente.close();

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }

    }

    public List<String> cargarAño() throws ConfigurationFileException, FileNotFoundException, IOException {
        Socket socketCliente = null;
        ObjectInputStream entrada = null;
        ObjectOutputStream salida = null;
        List<String> año = new ArrayList<>();

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
            salida.writeInt(19);
            salida.flush();
            linea = (String) entrada.readObject();
            año = jsonToListString(linea);

            entrada.close();
            socketCliente.close();

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConectorApercibimientos.class.getName()).log(Level.SEVERE, null, ex);
        }

        return año;

    }

    public List<String> cargarMeses(String año) throws ConfigurationFileException, FileNotFoundException, IOException {
        Socket socketCliente = null;
        ObjectInputStream entrada = null;
        ObjectOutputStream salida = null;
        List<String> meses = new ArrayList<>();

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
            salida.writeInt(20);
            salida.flush();
            salida.writeUTF(año);
            salida.flush();
            linea = (String) entrada.readObject();
            meses = jsonToListString(linea);

            entrada.close();
            socketCliente.close();

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConectorApercibimientos.class.getName()).log(Level.SEVERE, null, ex);
        }

        return meses;
    }

    public List<String> cargarCursos(String año, String mes) throws ConfigurationFileException, FileNotFoundException, IOException {
        Socket socketCliente = null;
        ObjectInputStream entrada = null;
        ObjectOutputStream salida = null;
        List<String> meses = new ArrayList<>();

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
            salida.writeInt(21);
            salida.flush();
            salida.writeUTF(año);
            salida.flush();
            salida.writeUTF(mes);
            salida.flush();
            linea = (String) entrada.readObject();
            meses = jsonToListString(linea);

            entrada.close();
            socketCliente.close();

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConectorApercibimientos.class.getName()).log(Level.SEVERE, null, ex);
        }

        return meses;
    }

    private String[] leerConfiguración() throws FileNotFoundException, IOException {
        String[] parametros = new String[2];

        try (BufferedReader br = new BufferedReader(new FileReader("config.txt"));) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] parametro = line.split(":");
                if (parametro[0].equalsIgnoreCase("address")) {
                    parametros[0] = parametro[1];
                } else if (parametro[0].equalsIgnoreCase("port")) {
                    parametros[1] = parametro[1];
                }
            }
        } catch (FileNotFoundException ex) {
            throw new FileNotFoundException();
        } catch (IOException ex) {
            throw new IOException();
        }
        return parametros;
    }

}
