package Clases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;

public class Cconexion {
    Connection conectar;
    
    String usuario ="root";
    String contrasenia = "";
    String bd = "proyectonb";
    String ip = "localhost";
    String puerto = "3306";
    
    String cadena = "jdbc:mysql://"+ip+":"+puerto+"/"+bd;
    PreparedStatement conectarbase;
    
    public Connection conectarbase() {
        try {
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            conectar = DriverManager.getConnection(cadena,usuario,contrasenia);
            //JOptionPane.showMessageDialog(null, "Se conecto correcta mente");
          
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Problemas en la Coneccion"+ e.toString());
        }
        return conectar;
    }
} 
