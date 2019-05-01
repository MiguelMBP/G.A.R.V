package connection;

import java.io.FileOutputStream;
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

public class DjangoConnection implements Constants {

	public List<String> conectar(String username, String password) {
		HttpURLConnection connection = null;
		boolean existe = false;
		List<String> cookies = new ArrayList<>();
		try {
			String url = "http://localhost:8000/accounts/login/";
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

			connection = (HttpURLConnection) new URL("http://" + ADDRESS + ":" + 8000 + "/apercibimientos/login/")
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

		try {
			String url = "http://" + ADDRESS + ":" + 8000 + "/visitas/createuser/";
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
			csrf.setDomain(ADDRESS);
			csrf.setHttpOnly(true);
			HttpCookie session = new HttpCookie("sessionid", sessionid);
			session.setPath("/");
			session.setDomain(ADDRESS);
			session.setHttpOnly(true);

			cookieManager.getCookieStore().add(new URI("localhost"), csrf);
			cookieManager.getCookieStore().add(new URI("localhost"), session);

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

	// TODO
	public int insertarVisita(RegistroVisita visita, int id) {
		HttpURLConnection connection = null;
		boolean creado = false;
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD");

		try {
			String url = "http://" + ADDRESS + ":" + 8000 + "/visitas/registervisit/";
			String charset = "UTF-8";

			String query = String.format("userId=%s&date=%s&img=%s&studentId=%s",
					URLEncoder.encode(id+"", charset), URLEncoder.encode(sdf.format(visita.getFecha()), charset),
					URLEncoder.encode(visita.getImagen64(), charset), URLEncoder.encode(visita.getAlumno().getId()+"", charset));

			CookieManager cookieManager = new CookieManager();
			CookieHandler.setDefault(cookieManager);

			HttpCookie csrf = new HttpCookie("csrftoken", visita.getCsrfToken());
			csrf.setPath("/");
			csrf.setDomain(ADDRESS);
			csrf.setHttpOnly(true);
			HttpCookie session = new HttpCookie("sessionid", visita.getSessionId());
			session.setPath("/");
			session.setDomain(ADDRESS);
			session.setHttpOnly(true);

			cookieManager.getCookieStore().add(new URI(ADDRESS), csrf);
			cookieManager.getCookieStore().add(new URI(ADDRESS), session);

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
}
