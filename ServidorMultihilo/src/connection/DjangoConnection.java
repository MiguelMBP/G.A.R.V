package connection;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Util.Constants;
import vo.RegistroVisita;

public class DjangoConnection {

	public List<String> conectar(String username, String password) {
		HttpURLConnection connection = null;
		boolean existe = false;
		List<String> cookies = new ArrayList<>();
		String[] parametros = leerConfiguración();
		try {
			String url = "http://" + parametros[0] + ":" + parametros[1] + "/accounts/login/";
			String charset = "UTF-8";

			String query = String.format("username=%s&password=%s", URLEncoder.encode(username, charset),
					URLEncoder.encode(password, charset));
			final String COOKIES_HEADER = "Set-Cookie";

			String csrftoken = "";
			String sessionid = "";

			CookieManager cookieManager = new CookieManager();
			CookieHandler.setDefault(cookieManager);

			connection = (HttpURLConnection) new URL(url).openConnection();
			InputStream response = connection.getInputStream();

			connection.disconnect();

			connection = (HttpURLConnection) new URL("http://" + parametros[0] + ":" + parametros[1] + "/apercibimientos/login/")
					.openConnection();

			if (cookieManager.getCookieStore().getCookies().size() > 0) {
				for (HttpCookie cookie : cookieManager.getCookieStore().getCookies()) {
					if (cookie.getName().equals("csrftoken")) {
						connection.setRequestProperty("X-CSRFToken", cookie.getValue());
					}
				}
			}

			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setRequestProperty("Accept-Charset", charset);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);

			try (OutputStream output = connection.getOutputStream()) {
				output.write(query.getBytes(charset));
			}

			response = connection.getInputStream();
			try (Scanner scanner = new Scanner(response)) {
				String responseBody = scanner.useDelimiter("\\A").next();
				if (responseBody.equals("login successful")) {
					existe = true;
				}
			}

			if (existe) {
				for (HttpCookie cookie : cookieManager.getCookieStore().getCookies()) {
					if (cookie.getName().equals("csrftoken")) {
						csrftoken = cookie.getValue();
					}
					if (cookie.getName().equals("sessionid")) {
						sessionid = cookie.getValue();
					}
				}
			}

			cookies.add(csrftoken);
			cookies.add(sessionid);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return cookies;
	}

	public boolean createUser(String csrftoken, String sessionid, String usuarioCrear, String contrasenaCrear,
			String correo, String dni, String nombre, String apellidos, String curso) {
		HttpURLConnection connection = null;
		boolean creado = false;
		String[] parametros = leerConfiguración();

		try {
			String url = "http://" + parametros[0] + ":" + parametros[1] + "/visitas/createuser/";
			String charset = "UTF-8";

			String query = String.format("username=%s&password=%s&email=%s&dni=%s&nombre=%s&apellidos=%s&curso=%s",
					URLEncoder.encode(usuarioCrear, charset), URLEncoder.encode(contrasenaCrear, charset),
					URLEncoder.encode(correo, charset), URLEncoder.encode(dni, charset),
					URLEncoder.encode(nombre, charset), URLEncoder.encode(apellidos, charset),
					URLEncoder.encode(curso, charset));

			CookieManager cookieManager = new CookieManager();
			CookieHandler.setDefault(cookieManager);

			// conectar(usuario, contrasena, cookieManager);

			HttpCookie csrf = new HttpCookie("csrftoken", csrftoken);
			csrf.setPath("/");
			csrf.setDomain(parametros[0]);
			csrf.setHttpOnly(true);
			HttpCookie session = new HttpCookie("sessionid", sessionid);
			session.setPath("/");
			session.setDomain(parametros[0]);
			session.setHttpOnly(true);

			cookieManager.getCookieStore().add(new URI(parametros[0]), csrf);
			cookieManager.getCookieStore().add(new URI(parametros[0]), session);

			connection = (HttpURLConnection) new URL(url).openConnection();

			if (cookieManager.getCookieStore().getCookies().size() > 0) {
				for (HttpCookie cookie : cookieManager.getCookieStore().getCookies()) {
					if (cookie.getName().equals("csrftoken")) {
						connection.setRequestProperty("X-CSRFToken", cookie.getValue());
					}
				}
			}

			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setRequestProperty("Accept-Charset", charset);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);

			try (OutputStream output = connection.getOutputStream()) {
				output.write(query.getBytes(charset));
			}

			InputStream response = connection.getInputStream();
			try (Scanner scanner = new Scanner(response)) {
				String responseBody = scanner.useDelimiter("\\A").next();
				if (responseBody.equals("success")) {
					creado = true;
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return creado;
	}

	public int insertarVisita(RegistroVisita visita, int id) {
		HttpURLConnection connection = null;
		boolean creado = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String[] parametros = leerConfiguración();

		try {
			String url = "http://" + parametros[0] + ":" + parametros[1] + "/visitas/registervisit/";
			String charset = "UTF-8";

			String query = String.format("userId=%s&date=%s&img=%s&studentId=%s",
					URLEncoder.encode(id+"", charset), URLEncoder.encode(sdf.format(visita.getFecha()), charset),
					URLEncoder.encode(visita.getImagen64(), charset), URLEncoder.encode(visita.getAlumno().getId()+"", charset));

			CookieManager cookieManager = new CookieManager();
			CookieHandler.setDefault(cookieManager);

			HttpCookie csrf = new HttpCookie("csrftoken", visita.getCsrfToken());
			csrf.setPath("/");
			csrf.setDomain(parametros[0]);
			csrf.setHttpOnly(true);
			HttpCookie session = new HttpCookie("sessionid", visita.getSessionId());
			session.setPath("/");
			session.setDomain(parametros[0]);
			session.setHttpOnly(true);

			cookieManager.getCookieStore().add(new URI(parametros[0]), csrf);
			cookieManager.getCookieStore().add(new URI(parametros[0]), session);

			connection = (HttpURLConnection) new URL(url).openConnection();

			if (cookieManager.getCookieStore().getCookies().size() > 0) {
				for (HttpCookie cookie : cookieManager.getCookieStore().getCookies()) {
					if (cookie.getName().equals("csrftoken")) {
						connection.setRequestProperty("X-CSRFToken", cookie.getValue());
					}
				}
			}

			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setRequestProperty("Accept-Charset", charset);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);

			try (OutputStream output = connection.getOutputStream()) {
				output.write(query.getBytes(charset));
			}

			InputStream response = connection.getInputStream();
			try (Scanner scanner = new Scanner(response)) {
				String responseBody = scanner.useDelimiter("\\A").next();
				if (responseBody.equals("success")) {
					creado = true;
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public String getImagen(int id, String crsftoken, String sessionId) {
		HttpURLConnection connection = null;
		boolean creado = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String responseBody  = null;
		String[] parametros = leerConfiguración();

		try {
			String url = "http://" + parametros[0] + ":" + parametros[1] + "/visitas/sendimage/";
			String charset = "UTF-8";

			String query = String.format("id=%s",
					URLEncoder.encode(id+"", charset));

			CookieManager cookieManager = new CookieManager();
			CookieHandler.setDefault(cookieManager);

			HttpCookie csrf = new HttpCookie("csrftoken", crsftoken);
			csrf.setPath("/");
			csrf.setDomain(parametros[0]);
			csrf.setHttpOnly(true);
			HttpCookie session = new HttpCookie("sessionid", sessionId);
			session.setPath("/");
			session.setDomain(parametros[0]);
			session.setHttpOnly(true);

			cookieManager.getCookieStore().add(new URI(parametros[0]), csrf);
			cookieManager.getCookieStore().add(new URI(parametros[0]), session);

			connection = (HttpURLConnection) new URL(url).openConnection();

			if (cookieManager.getCookieStore().getCookies().size() > 0) {
				for (HttpCookie cookie : cookieManager.getCookieStore().getCookies()) {
					if (cookie.getName().equals("csrftoken")) {
						connection.setRequestProperty("X-CSRFToken", cookie.getValue());
					}
				}
			}

			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setRequestProperty("Accept-Charset", charset);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);

			try (OutputStream output = connection.getOutputStream()) {
				output.write(query.getBytes(charset));
			}

			InputStream response = connection.getInputStream();
			try (Scanner scanner = new Scanner(response)) {
				responseBody = scanner.useDelimiter("\\A").next();
				if (responseBody.equals("success")) {
					creado = true;
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return responseBody;
	}
	
	private String[] leerConfiguración() {
        String[] parametros = new String[2];

        try (BufferedReader br = new BufferedReader(new FileReader("config.txt"));) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] parametro = line.split(":");
                if (parametro[0].equalsIgnoreCase("django_address")) {
                    parametros[0] = parametro[1];
                } else if (parametro[0].equalsIgnoreCase("django_port")) {
                    parametros[1] = parametro[1];
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
