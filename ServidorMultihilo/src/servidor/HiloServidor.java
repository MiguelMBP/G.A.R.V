package servidor;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
		try {
			OutputStreamWriter osw = new OutputStreamWriter(socket.getOutputStream());
			ApercibimientoDAO dao = new ApercibimientoDAO();
			List<Apercibimiento> apercibimientos = dao.mostrarApercibimientos();
			
			String json = convertToJson(apercibimientos);
			osw.write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String convertToJson(List<Apercibimiento> apercibimientos) {
		Gson gson = new Gson();
		//Type listType = new TypeToken
		String json = gson.toJson(apercibimientos);
		return json;
	}
	
}
