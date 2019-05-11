package connection;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author miguelmbp
 */
public class DBConnection {
    
    /**Parametros de conexion*/
   /*static String bd = "GARV";
   static String login = "miguelmbp";
   static String password = "pass";
   static String url = "jdbc:mysql://localhost/"+bd;*/
   
   Connection connection = null;
 
   /** Constructor de DbConnection */
   public DBConnection() {
	  String[] parametros = leerConfiguracion();
	  String bd = parametros[0];
	  String login = parametros[1];
	  String password = parametros[2];
	  String url = "jdbc:mysql://" + parametros[3] + "/"+bd;
      try{
         //obtenemos el driver de para mysql
         Class.forName("org.mariadb.jdbc.Driver");
         //obtenemos la conexión
         connection = DriverManager.getConnection(url,login,password);
 
         if (connection!=null){
            System.out.println("Conexión a base de datos "+bd+" OK\n");
         }
      }
      catch(SQLException e){
         System.out.println(e);
      }catch(ClassNotFoundException e){
         System.out.println(e);
      }catch(Exception e){
         System.out.println(e);
      }
   }
   /**Permite retornar la conexión*/
   public Connection getConnection(){
      return connection;
   }
 
   public void desconectar(){
      connection = null;
   }
   
   private String[] leerConfiguracion() {
       String[] parametros = new String[4];

       try (BufferedReader br = new BufferedReader(new FileReader("config.txt"));) {
           String line;

           while ((line = br.readLine()) != null) {
               String[] parametro = line.split(":");
               if (parametro[0].equalsIgnoreCase("db_name")) {
                   parametros[0] = parametro[1];
               } else if (parametro[0].equalsIgnoreCase("db_user")) {
                   parametros[1] = parametro[1];
               } else if (parametro[0].equalsIgnoreCase("db_password")) {
                   parametros[2] = parametro[1];
               } else if (parametro[0].equalsIgnoreCase("db_address")) {
                   parametros[3] = parametro[1];
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
