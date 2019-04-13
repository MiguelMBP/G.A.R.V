package servidor;

import java.io.IOException;
import java.net.ServerSocket;

public class Servidor {

	public static final int PORT = 4444;

	public static void main(String[] args) throws IOException {
		ServerSocket socketServidor = null;
		boolean seguir = true;
		try {
			socketServidor = new ServerSocket(PORT);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("No puede escuchar en el puerto: " + PORT);
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
}
