package servidor;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import vo.Apercibimiento;
import vo.ClaseApercibimiento;

public class ClientePrueba {

	public static final int PORT = 4444;

	public static void main(String[] args) {
		//apercibimientos();
		materias();
		
	}

	private static void materias() {
		Socket socketCliente = null;
		ObjectInputStream entrada = null;
		ObjectOutputStream salida = null;


		try {
			socketCliente = new Socket("localhost", PORT);
			salida = new ObjectOutputStream(socketCliente.getOutputStream());
			entrada = new ObjectInputStream(socketCliente.getInputStream());
		} catch (IOException e) {
			System.err.println("No puede establer canales de E/S para la conexión");
			System.exit(-1);
		}
		String linea = "";
		try {
			int op = 3;
			salida.writeInt(op);
			salida.flush();
			linea = (String) entrada.readObject();

			List<ClaseApercibimiento> materias = jsonToListMateria(linea);

			for (ClaseApercibimiento claseApercibimiento : materias) {
				System.out.println(claseApercibimiento);
			}

		} catch (IOException e) {
			System.out.println("IOException: " + e.getMessage());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try {
			entrada.close();
			socketCliente.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private static void apercibimientos() {
		Socket socketCliente = null;
		ObjectInputStream entrada = null;
		ObjectOutputStream salida = null;


		try {
			socketCliente = new Socket("localhost", PORT);
			salida = new ObjectOutputStream(socketCliente.getOutputStream());
			entrada = new ObjectInputStream(socketCliente.getInputStream());
		} catch (IOException e) {
			System.err.println("No puede establer canales de E/S para la conexión");
			System.exit(-1);
		}
		String linea = "";
		try {
			int op = 1;
			salida.writeInt(op);
			salida.flush();
			linea = (String) entrada.readObject();

			List<Apercibimiento> apercibimientos = jsonToListApercibimiento(linea);

			for (Apercibimiento apercibimiento : apercibimientos) {
				System.out.println(apercibimiento);
			}

		} catch (IOException e) {
			System.out.println("IOException: " + e.getMessage());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try {
			entrada.close();
			socketCliente.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private static List<Apercibimiento> jsonToListApercibimiento(String json) {
		Type typeList = new TypeToken<List<Apercibimiento>>() {
		}.getType();
		Gson gson = new Gson();
		List<Apercibimiento> apercibimientos = gson.fromJson(json, typeList);
		return apercibimientos;
	}
	
	private static List<ClaseApercibimiento> jsonToListMateria(String json) {
		Type typeList = new TypeToken<List<ClaseApercibimiento>>() {
		}.getType();
		Gson gson = new Gson();
		List<ClaseApercibimiento> materias = gson.fromJson(json, typeList);
		return materias;
	}
}
