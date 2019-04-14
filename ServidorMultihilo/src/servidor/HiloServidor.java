package servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dao.ApercibimientoDAO;
import vo.Apercibimiento;

public class HiloServidor extends Thread {

	private Socket socket = null;

	public HiloServidor(Socket socket) {
		super();
		this.socket = socket;
	}

	@Override
	public void run() {
		loadQuery();
	}

	private void loadQuery() {
		try {
			ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());
			ObjectOutputStream salida = new ObjectOutputStream(socket.getOutputStream());
			int op = entrada.readInt();

			switch (op) {
			case 1:
				getApercibimientos(salida);
				break;

			}
		} catch (IOException e) {
			e.printStackTrace();
		} 

	}

	private void getApercibimientos(ObjectOutputStream salida) {
		try {
			ApercibimientoDAO dao = new ApercibimientoDAO();
			List<Apercibimiento> apercibimientos = dao.mostrarApercibimientos();

			String json = convertToJson(apercibimientos);
			salida.writeObject(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String convertToJson(List<Apercibimiento> apercibimientos) {
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
		return gson.toJson(apercibimientos);
	}

}
