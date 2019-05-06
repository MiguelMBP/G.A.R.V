package servidor;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;

import Util.Constants;

public class Servidor {

	public static void main(String[] args) throws IOException {
		ServerSocket socketServidor = null;
		boolean seguir = true;
		String[] parametros = leerConfiguración();
		try {
			socketServidor = new ServerSocket(Integer.parseInt(parametros[0]));
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("No puede escuchar en el puerto: " + parametros[0]);
			System.exit(-1);
		}
		while (seguir) {
			try {
				HiloServidor servidorThread = new HiloServidor(socketServidor.accept());
				servidorThread.start();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		socketServidor.close();

	}
	
	private static String[] leerConfiguración() {
        String[] parametros = new String[1];

        try (BufferedReader br = new BufferedReader(new FileReader("config.txt"));) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] parametro = line.split(":");
                if (parametro[0].equalsIgnoreCase("this_server_port")) {
                    parametros[0] = parametro[1];
                }
            }
        } catch (FileNotFoundException ex) {
        	ex.printStackTrace();
        } catch (IOException ex) {
        	ex.printStackTrace();
        }
        return parametros;
    }
}
