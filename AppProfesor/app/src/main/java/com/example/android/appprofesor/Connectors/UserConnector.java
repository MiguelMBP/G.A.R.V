package com.example.android.appprofesor.Connectors;

import com.example.android.appprofesor.models.Settings;
import com.example.android.appprofesor.utils.Constants;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Realiza las peticiones del cliente al servidor multihilos para el módulo de usuarios
 */
public class UserConnector implements Constants {

    /**
     * Comprueba los credenciales del usuario
     * @param username nombre de usuario
     * @param password contraseña
     * @param settings ajustes de conexión al servidor
     * @return Lista con las cookies de la sesión del usuario
     */
    public List<String> iniciarSesion(String username, String password, Settings settings) {
        Socket socketCliente = null;
        ObjectInputStream entrada = null;
        ObjectOutputStream salida = null;
        boolean existe = false;
        List<String> cookies = new ArrayList<>();

        try {
            InetAddress address = InetAddress.getByName(settings.getAddress());
            socketCliente = new Socket(address, settings.getPort());
            salida = new ObjectOutputStream(socketCliente.getOutputStream());
            entrada = new ObjectInputStream(socketCliente.getInputStream());
            System.out.println("Conectado");
        } catch (IOException e) {
            System.err.println("No puede establer canales de E/S para la conexión");
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
                cookies.add(entrada.readUTF());
            }

            entrada.close();
            socketCliente.close();

        } catch (Exception e) {
            System.out.println("IOException: " + e.getMessage());
        }

        return cookies;

    }

    /**
     * Cambia la contraseña del usuario
     * @param username nombre de usuario
     * @param password contraseña
     * @param csrftoken csrfToken de la sesión
     * @param sessionId sessionId de la sesión
     * @param settings ajustes de la conexión al servidor
     * @return booleano indicado si se cambió la contraseña con éxito
     */
    public boolean cambiarContraseña(String username, String password, String csrftoken, String sessionId, Settings settings) {
        Socket socketCliente = null;
        ObjectInputStream entrada = null;
        ObjectOutputStream salida = null;
        boolean cambiado = false;
        List<String> cookies = new ArrayList<>();

        try {
            InetAddress address = InetAddress.getByName(settings.getAddress());
            socketCliente = new Socket(address, settings.getPort());
            salida = new ObjectOutputStream(socketCliente.getOutputStream());
            entrada = new ObjectInputStream(socketCliente.getInputStream());
            System.out.println("Conectado");
        } catch (IOException e) {
            System.err.println("No puede establer canales de E/S para la conexión");
        }
        try {
            salida.writeInt(29);
            salida.flush();
            salida.writeUTF(username);
            salida.flush();
            salida.writeUTF(password);
            salida.flush();
            salida.writeUTF(csrftoken);
            salida.flush();
            salida.writeUTF(sessionId);
            salida.flush();

            cambiado = entrada.readBoolean();

            entrada.close();
            socketCliente.close();

        } catch (Exception e) {
            System.out.println("IOException: " + e.getMessage());
        }

        return cambiado;

    }
}
