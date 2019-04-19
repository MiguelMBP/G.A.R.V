package com.example.android.appprofesor.Connectors;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class LoginConnector {

    public static final int PORT = 4444;

    public boolean iniciarSesion(String username, String password) {
        Socket socketCliente = null;
        ObjectInputStream entrada = null;
        ObjectOutputStream salida = null;
        boolean existe = false;

        try {
            socketCliente = new Socket("192.168.1.65", PORT);
            salida = new ObjectOutputStream(socketCliente.getOutputStream());
            entrada = new ObjectInputStream(socketCliente.getInputStream());
            System.out.println("Conectado");
        } catch (IOException e) {
            System.err.println("No puede establer canales de E/S para la conexión");
            System.exit(-1);
        }
        try {
            salida.writeInt(5);
            salida.flush();
            salida.writeUTF(username);
            salida.flush();
            salida.writeUTF(password);
            salida.flush();

            existe = entrada.readBoolean();

            entrada.close();
            socketCliente.close();

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }

        return existe;

    }
}
