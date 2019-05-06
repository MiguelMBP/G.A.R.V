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
import vo.Visita;

/**
 *
 * @author mmbernal
 */
public class ConectorVisitas implements Constants{
    public List<Visita> cargarVisitas() throws ConfigurationFileException, FileNotFoundException, IOException {
        Socket socketCliente = null;
        ObjectInputStream entrada = null;
        ObjectOutputStream salida = null;
        List<Visita> visitas = new ArrayList<>();

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
    
    public void inValidarVisita(int id, boolean activo) throws ConfigurationFileException, FileNotFoundException, IOException {
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
            salida.writeInt(17);
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

    public String getImagen(int id, List<String> cookies) throws ConfigurationFileException, FileNotFoundException, IOException {
        Socket socketCliente = null;
        ObjectInputStream entrada = null;
        ObjectOutputStream salida = null;
        String base64 = null;

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
            salida.writeInt(18);
            salida.flush();
            salida.writeInt(id);
            salida.flush();
            salida.writeUTF(cookies.get(0));
            salida.flush();
            salida.writeUTF(cookies.get(1));
            salida.flush();
            
            base64 = (String) entrada.readObject();
            
            entrada.close();
            socketCliente.close();

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConectorVisitas.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return base64;
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
