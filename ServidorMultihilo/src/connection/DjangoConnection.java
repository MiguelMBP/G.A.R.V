package connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class DjangoConnection {
	
	public boolean conectar(String username, String password) {
		HttpURLConnection connection = null;
		boolean existe = false;
		try {
			String url = "http://127.0.0.1:8000/accounts/login/";
			String charset = "UTF-8";  
			
			String query = String.format("username=%s&password=%s", 
				     URLEncoder.encode(username, charset), 
				     URLEncoder.encode(password, charset));
			final String COOKIES_HEADER = "Set-Cookie";
			
			CookieManager cookieManager = new CookieManager();  
			CookieHandler.setDefault(cookieManager);
			
			connection = (HttpURLConnection) new URL(url).openConnection();
			InputStream response = connection.getInputStream();

			connection.disconnect();

			connection = (HttpURLConnection) new URL("http://127.0.0.1:8000/apercibimientos/login/").openConnection();
			
			if (cookieManager.getCookieStore().getCookies().size() > 0) {
				for (HttpCookie cookie : cookieManager.getCookieStore().getCookies()) {
					if (cookie.getName().equals("csrftoken")) {
						connection.setRequestProperty("X-CSRFToken",cookieManager.getCookieStore().getCookies().get(0).getValue());    
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
			    if(responseBody.equals("login successful")) {
			    	existe = true;
			    }
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return existe;
	}
	
	public boolean conectar(String username, String password, CookieManager cookieManager) {
		HttpURLConnection connection = null;
		boolean existe = false;
		try {
			String url = "http://127.0.0.1:8000/accounts/login/";
			String charset = "UTF-8";  
			
			String query = String.format("username=%s&password=%s", 
				     URLEncoder.encode(username, charset), 
				     URLEncoder.encode(password, charset));
			final String COOKIES_HEADER = "Set-Cookie";
			
			CookieHandler.setDefault(cookieManager);
			
			connection = (HttpURLConnection) new URL(url).openConnection();
			InputStream response = connection.getInputStream();

			connection.disconnect();

			connection = (HttpURLConnection) new URL("http://127.0.0.1:8000/apercibimientos/login/").openConnection();
			
			if (cookieManager.getCookieStore().getCookies().size() > 0) {
				for (HttpCookie cookie : cookieManager.getCookieStore().getCookies()) {
					if (cookie.getName().equals("csrftoken")) {
						connection.setRequestProperty("X-CSRFToken",cookieManager.getCookieStore().getCookies().get(0).getValue());    
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
			    if(responseBody.equals("login successful")) {
			    	existe = true;
			    }
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return existe;
	}
	
	public boolean createUser(String username, String password, String usernameCreate, String passwordCreate, String emailCreate) {
		HttpURLConnection connection = null;
		boolean creado = false;
		
		try {
			String url = "http://127.0.0.1:8000/apercibimientos/createuser/";
			String charset = "UTF-8";
			
			String query = String.format("username=%s&password=%s&email=%s", 
				     URLEncoder.encode(usernameCreate, charset), 
				     URLEncoder.encode(passwordCreate, charset),
				     URLEncoder.encode(emailCreate, charset));
			
			CookieManager cookieManager = new CookieManager();  
			CookieHandler.setDefault(cookieManager);
			
			conectar(username, password, cookieManager);
			
			connection = (HttpURLConnection) new URL(url).openConnection();
			
			if (cookieManager.getCookieStore().getCookies().size() > 0) {
				for (HttpCookie cookie : cookieManager.getCookieStore().getCookies()) {
					if (cookie.getName().equals("csrftoken")) {
						connection.setRequestProperty("X-CSRFToken",cookieManager.getCookieStore().getCookies().get(0).getValue());    
					}
				}
			}
			
			try (OutputStream output = connection.getOutputStream()) {
			    output.write(query.getBytes(charset));
			}
			
			InputStream response = connection.getInputStream();
			try (Scanner scanner = new Scanner(response)) {
			    String responseBody = scanner.useDelimiter("\\A").next();
			    if(responseBody.equals("success")) {
			    	creado = true;
			    }
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return creado;
	}
}
