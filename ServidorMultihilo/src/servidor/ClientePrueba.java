package servidor;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import vo.Apercibimiento;

public class ClientePrueba {

	public static final int PORT = 4444;

	public static void main(String[] args) {
		Socket socketCliente = null;
		InputStreamReader entrada = null;

		try {
			socketCliente = new Socket("localhost", PORT);
			entrada = new InputStreamReader(socketCliente.getInputStream());
		} catch (IOException e) {
			System.err.println("No puede establer canales de E/S para la conexión");
			System.exit(-1);
		}
		String linea = "";
		int i;
		try {
			
			while((i = entrada.read())!=-1) {
	            linea += (char)i;
	         }
		
			List<Apercibimiento> apercibimientos = jsonToList(linea);
			
			for (Apercibimiento apercibimiento : apercibimientos) {
				System.out.println(apercibimiento);
			}

		} catch (IOException e) {
			System.out.println("IOException: " + e.getMessage());
		}

		try {
			entrada.close();
			socketCliente.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static List<Apercibimiento> jsonToList(String json) {
		Type typeList = new TypeToken<List<Apercibimiento>>() {}.getType();
		Gson gson = new Gson();
		List<Apercibimiento> apercibimientos = gson.fromJson(json, typeList);
		return apercibimientos;
	}
}
