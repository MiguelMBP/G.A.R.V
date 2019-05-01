package com.example.android.appprofesor.Connectors;

import com.example.android.appprofesor.utils.Constants;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class LoginConnector implements Constants {

    public List<String> iniciarSesion(String username, String password) {
        Socket socketCliente = null;
        ObjectInputStream entrada = null;
        ObjectOutputStream salida = null;
        boolean existe = false;
        List<String> cookies = new ArrayList<>();

        try {
            InetAddress address = InetAddress.getByName(ADDRESS);
            socketCliente = new Socket(address, PORT);
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

            if (existe) {
                cookies.add(entrada.readUTF());
                cookies.add(entrada.readUTF());
                cookies.add(entrada.readUTF());
            }

            entrada.close();
            socketCliente.close();

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }

        return cookies;

    }
}