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

	
	public static void main(String[] args) {
		conectar();
	}
	
	private static void conectar() {
		HttpURLConnection connection = null;
		try {
			String url = "http://127.0.0.1:8000/accounts/login/";
			String charset = "UTF-8";  
			String param1 = "admin";
			String param2 = "admin";
			
			String query = String.format("username=%s&password=%s", 
				     URLEncoder.encode(param1, charset), 
				     URLEncoder.encode(param2, charset));
			final String COOKIES_HEADER = "Set-Cookie";

			CookieManager cookieManager = new CookieManager();  
			CookieHandler.setDefault(cookieManager);
			
			connection = (HttpURLConnection) new URL(url).openConnection();
			InputStream response = connection.getInputStream();
			try (Scanner scanner = new Scanner(response)) {
			    String responseBody = scanner.useDelimiter("\\A").next();
			    System.out.println(responseBody);
			}

			connection.disconnect();

			connection = (HttpURLConnection) new URL(url).openConnection();
			
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
			    System.out.println(responseBody);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
