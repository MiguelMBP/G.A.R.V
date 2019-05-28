package connection;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
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

import vo.RegistroVisita;

/**
 * Clase que se encarga de realizar peticiones al servidor Django
 *
 * @author mmbernal
 *
 */
public class DjangoConnection {

    /**
     * Realiza una conexi�n HTTP al servidor Django para verificar usuario y
     * contrase�a
     *
     * @param username
     * @param password
     * @return La lista de cookies de la conexi�n para acceder a los otros
     * m�todos
     */
    public List<String> conectar(String username, String password) {
        HttpURLConnection connection = null;
        boolean existe = false;
        List<String> cookies = new ArrayList<>();
        String[] parametros = leerConfiguracion();
        String cookiesString = "";
        if (parametros[0] == null || parametros[1] == null) {
            return cookies;
        }
        try {
            String url = "http://" + parametros[0] + ":" + parametros[1] + "/accounts/login/";
            String charset = "UTF-8";

            String query = String.format("username=%s&password=%s", URLEncoder.encode(username, charset),
                    URLEncoder.encode(password, charset));

            String csrftoken = "";
            String sessionid = "";

            CookieManager cookieManager = new CookieManager();
            CookieHandler.setDefault(cookieManager);

            connection = (HttpURLConnection) new URL(url).openConnection();
            InputStream response = connection.getInputStream();

            connection.disconnect();

            connection = (HttpURLConnection) new URL(
                    "http://" + parametros[0] + ":" + parametros[1] + "/usuarios/login/").openConnection();

            if (cookieManager.getCookieStore().getCookies().size() > 0) {
                for (HttpCookie cookie : cookieManager.getCookieStore().getCookies()) {
                    if (cookie.getName().equals("csrftoken")) {
                        connection.setRequestProperty("X-CSRFToken", cookie.getValue());
                    }
                    cookiesString += cookie.getName() + "=" + cookie.getValue() + ";";
                }
            }

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Accept-Charset", charset);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
            connection.setRequestProperty("Cookie", cookiesString);

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
        } finally {
            connection.disconnect();
        }

        return cookies;
    }

    /**
     * Realiza una conexi�n HTTP POST al servidor Django para registrar un
     * usuario. Utiliza las cookies CRSFToken y SessionId para verificar que el
     * usuario que realiza la petici�n es v�lido
     *
     * @param crsftoken
     * @param sessionid
     * @param usuarioCrear
     * @param contrasenaCrear
     * @param correo
     * @param dni
     * @param nombre
     * @param apellidos
     * @param curso
     * @return Booleano que indica si el usuario se ha creado con exito
     */
    public boolean createUser(String csrftoken, String sessionid, String usuarioCrear, String contrasenaCrear,
            String correo, String dni, String nombre, String apellidos, String curso) {
        HttpURLConnection connection = null;
        boolean creado = false;
        String[] parametros = leerConfiguracion();
        String cookies = "";
        if (parametros[0] == null || parametros[1] == null) {
            return false;
        }
        try {
            String url = "http://" + parametros[0] + ":" + parametros[1] + "/usuarios/createuser/";
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
                    cookies += cookie.getName() + "=" + cookie.getValue() + ";";
                }
            }

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Accept-Charset", charset);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
            connection.setRequestProperty("Cookie", cookies);

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

    /**
     * Realiza una conexi�n HTTP POST al servidor Django para registrar una
     * visita. Utiliza las cookies CRSFToken y SessionId para verificar que el
     * usuario que realiza la petici�n es v�lido
     *
     * @param visita
     * @param id
     */
    public void insertarVisita(RegistroVisita visita, int id) {
        HttpURLConnection connection = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String[] parametros = leerConfiguracion();
        String cookies = "";
        if (parametros[0] == null || parametros[1] == null) {
            return;
        }

        try {
            String url = "http://" + parametros[0] + ":" + parametros[1] + "/visitas/registervisit/";
            String charset = "UTF-8";

            String query = String.format("userId=%s&date=%s&img=%s&studentId=%s", URLEncoder.encode(id + "", charset),
                    URLEncoder.encode(sdf.format(visita.getFecha()), charset),
                    URLEncoder.encode(visita.getImagen64(), charset),
                    URLEncoder.encode(visita.getAlumno().getId() + "", charset));

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
                    cookies += cookie.getName() + "=" + cookie.getValue() + ";";
                }
            }

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Accept-Charset", charset);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
            connection.setRequestProperty("Cookie", cookies);

            try (OutputStream output = connection.getOutputStream()) {
                output.write(query.getBytes(charset));
            }

            InputStream response = connection.getInputStream();
            try (Scanner scanner = new Scanner(response)) {
                String responseBody = scanner.useDelimiter("\\A").next();
                if (responseBody.equals("success")) {

                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * Realiza una conexi�n HTTP POST al servidor Django para recoger la imagen
     * de una visita. Utiliza las cookies CRSFToken y SessionId para verificar
     * que el usuario que realiza la petici�n es v�lido
     *
     * @param id
     * @param crsftoken
     * @param sessionId
     * @return La im�gen codificada en base64
     */
    public String getImagen(int id, String crsftoken, String sessionId) {
        HttpURLConnection connection = null;
        String responseBody = null;
        String[] parametros = leerConfiguracion();
        String cookies = "";
        if (parametros[0] == null || parametros[1] == null) {
            return null;
        }

        try {
            String url = "http://" + parametros[0] + ":" + parametros[1] + "/visitas/sendimage/";
            String charset = "UTF-8";

            String query = String.format("id=%s", URLEncoder.encode(id + "", charset));

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
                    cookies += cookie.getName() + "=" + cookie.getValue() + ";";
                }
            }

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Accept-Charset", charset);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
            connection.setRequestProperty("Cookie", cookies);

            try (OutputStream output = connection.getOutputStream()) {
                output.write(query.getBytes(charset));
            }

            InputStream response = connection.getInputStream();
            try (Scanner scanner = new Scanner(response)) {
                responseBody = scanner.useDelimiter("\\A").next();
                if (responseBody.equals("success")) {

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

    private String[] leerConfiguracion() {
        String[] parametros = new String[2];

        try (BufferedReader br = new BufferedReader(new FileReader("config.txt"));) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] parametro = line.split(":");
                if (parametro.length == 2 && parametro[0].equalsIgnoreCase("django_address")) {
                    parametros[0] = parametro[1];
                } else if (parametro.length == 2 && parametro[0].equalsIgnoreCase("django_port")) {
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

    /**
     * Realiza una conexi�n HTTP POST al servidor Django para cambiar la
     * contrase�a de un usuario. Utiliza las cookies CRSFToken y SessionId para
     * verificar que el usuario que realiza la petici�n es v�lido
     *
     * @param username
     * @param password
     * @param crsftoken
     * @param sessionId
     * @return Booleano que indica si se ha cambiado la contrase�a con exito
     */
    public boolean cambiarContrasena(String username, String password, String crsftoken, String sessionId) {
        HttpURLConnection connection = null;
        boolean creado = false;
        String[] parametros = leerConfiguracion();
        if (parametros[0] == null || parametros[1] == null) {
            return false;
        }
        try {
            String url = "http://" + parametros[0] + ":" + parametros[1] + "/usuarios/changePassword/";
            String charset = "UTF-8";
            String cookies = "";

            String query = String.format("username=%s&password=%s", URLEncoder.encode(username + "", charset),
                    URLEncoder.encode(password, charset));

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
                    cookies += cookie.getName() + "=" + cookie.getValue() + ";";
                }
            }

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Accept-Charset", charset);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
            connection.setRequestProperty("Cookie", cookies);

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

    /**
     * Realiza una conexi�n HTTP POST al servidor Django para enviar un archivo
     * que analizar para sacar apercibimientos. Utiliza las cookies CRSFToken y
     * SessionId para verificar que el usuario que realiza la petici�n es v�lido
     *
     * @param base64
     * @param extension
     * @param csrfToken
     * @param sessionId
     */
    public void subirApercibimientos(String base64, String extension, String csrfToken, String sessionId) {
        HttpURLConnection connection = null;
        String[] parametros = leerConfiguracion();
        String cookies = "";
        if (parametros[0] == null || parametros[1] == null) {
            return;
        }

        try {
            String url = "http://" + parametros[0] + ":" + parametros[1] + "/apercibimientos/subirPost/";
            String charset = "UTF-8";

            String query = String.format("archivo=%s&extension=%s", URLEncoder.encode(base64, charset),
                    URLEncoder.encode(extension, charset));

            CookieManager cookieManager = new CookieManager();
            CookieHandler.setDefault(cookieManager);

            HttpCookie csrf = new HttpCookie("csrftoken", csrfToken);
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
                    cookies += cookie.getName() + "=" + cookie.getValue() + ";";
                }
            }

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Accept-Charset", charset);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
            connection.setRequestProperty("Cookie", cookies);

            try (OutputStream output = connection.getOutputStream()) {
                output.write(query.getBytes(charset));
            }

            InputStream response = connection.getInputStream();
            try (Scanner scanner = new Scanner(response)) {
                String responseBody = scanner.useDelimiter("\\A").next();
                if (responseBody.split(" ")[0].equals("success")) {

                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

    /**
     * Realiza una conexi�n HTTP POST al servidor Django para enviar un archivo
     * que analizar para importar usuarios. Utiliza las cookies CRSFToken y
     * SessionId para verificar que el usuario que realiza la petici�n es v�lido
     *
     * @param base64
     * @param csrfToken
     * @param sessionId
     */
    public void importarUsuarios(String base64, String csrfToken, String sessionId) {
        HttpURLConnection connection = null;
        String[] parametros = leerConfiguracion();
        String cookies = "";
        if (parametros[0] == null || parametros[1] == null) {
            return;
        }

        try {
            String url = "http://" + parametros[0] + ":" + parametros[1] + "/usuarios/importarPost/";
            String charset = "UTF-8";

            String query = String.format("archivo=%s", URLEncoder.encode(base64, charset));

            CookieManager cookieManager = new CookieManager();
            CookieHandler.setDefault(cookieManager);

            HttpCookie csrf = new HttpCookie("csrftoken", csrfToken);
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
                    cookies += cookie.getName() + "=" + cookie.getValue() + ";";
                }
            }

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Accept-Charset", charset);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
            connection.setRequestProperty("Cookie", cookies);

            try (OutputStream output = connection.getOutputStream()) {
                output.write(query.getBytes(charset));
            }

            InputStream response = connection.getInputStream();
            try (Scanner scanner = new Scanner(response)) {
                String responseBody = scanner.useDelimiter("\\A").next();
                if (responseBody.equals("success")) {

                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

    /**
     * Comprueba si el usuario pasado por parámetros pertenece al grupo Staff
     * @param username
     * @param csrftoken
     * @param sessionid
     * @return 
     */
    public boolean comprobarStaff(String username, String csrftoken, String sessionid) {
        HttpURLConnection connection = null;
        String[] parametros = leerConfiguracion();
        String cookies = "";
        boolean staff = false;
        if (parametros[0] == null || parametros[1] == null) {
            return false;
        }

        try {
            String url = "http://" + parametros[0] + ":" + parametros[1] + "/usuarios/isStaff/";
            String charset = "UTF-8";

            String query = String.format("username=%s", URLEncoder.encode(username, charset));

            CookieManager cookieManager = new CookieManager();
            CookieHandler.setDefault(cookieManager);

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
                    cookies += cookie.getName() + "=" + cookie.getValue() + ";";
                }
            }

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Accept-Charset", charset);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
            connection.setRequestProperty("Cookie", cookies);

            try (OutputStream output = connection.getOutputStream()) {
                output.write(query.getBytes(charset));
            }

            InputStream response = connection.getInputStream();
            try (Scanner scanner = new Scanner(response)) {
                String responseBody = scanner.useDelimiter("\\A").next();
                if (responseBody.equals("True")) {
                    staff = true;
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return staff;
    }

    /**
     * Elimina el usuario indicado
     * @param username
     * @param csrftoken
     * @param sessionid
     * @return 
     */
    public boolean eliminarUsuario(String username, String csrftoken, String sessionid) {
        HttpURLConnection connection = null;
        String[] parametros = leerConfiguracion();
        String cookies = "";
        boolean eliminado = false;
        if (parametros[0] == null || parametros[1] == null) {
            return false;
        }

        try {
            String url = "http://" + parametros[0] + ":" + parametros[1] + "/usuarios/eliminarUsuarios/";
            String charset = "UTF-8";

            String query = String.format("username=%s", URLEncoder.encode(username, charset));

            CookieManager cookieManager = new CookieManager();
            CookieHandler.setDefault(cookieManager);

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
                    cookies += cookie.getName() + "=" + cookie.getValue() + ";";
                }
            }

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Accept-Charset", charset);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
            connection.setRequestProperty("Cookie", cookies);

            try (OutputStream output = connection.getOutputStream()) {
                output.write(query.getBytes(charset));
            }

            InputStream response = connection.getInputStream();
            try (Scanner scanner = new Scanner(response)) {
                String responseBody = scanner.useDelimiter("\\A").next();
                if (responseBody.equals("Eliminado")) {
                    eliminado = true;
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return eliminado;
    }

}
