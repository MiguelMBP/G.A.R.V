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
import vo.ClaseApercibimiento;

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

			case 3:
				getMaterias(salida);

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void getMaterias(ObjectOutputStream salida) {
		try {
			ApercibimientoDAO dao = new ApercibimientoDAO();
			List<ClaseApercibimiento> materias = dao.mostrarMaterias();

			String json = materiasJson(materias);
			salida.writeObject(json);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void getApercibimientos(ObjectOutputStream salida) {
		try {
			ApercibimientoDAO dao = new ApercibimientoDAO();
			List<Apercibimiento> apercibimientos = dao.mostrarApercibimientos();

			String json = apercibimientosJson(apercibimientos);
			salida.writeObject(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String apercibimientosJson(List<Apercibimiento> apercibimientos) {
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
		return gson.toJson(apercibimientos);
	}

	private String materiasJson(List<ClaseApercibimiento> materias) {
		Gson gson = new Gson();
		return gson.toJson(materias);
	}

}
