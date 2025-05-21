package Clases;

import com.mycompany.AgendaProyect.FormContacto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;

public class Clogin {

    public void ValidarUsuario(JFrame loginFrame, JTextField usuario, JPasswordField contrasenia) {
        try {
            ResultSet rs = null;
            PreparedStatement ps = null;

            Clases.Cconexion objetoConexion = new Clases.Cconexion();

            String consulta = "SELECT * FROM usuarios WHERE nombre = ? AND contrasenia = ?";
            ps = objetoConexion.conectarbase().prepareStatement(consulta);

            String contra = String.valueOf(contrasenia.getPassword());

            ps.setString(1, usuario.getText());
            ps.setString(2, contra);

            rs = ps.executeQuery();

            if (rs.next()) {
                //JOptionPane.showMessageDialog(null, "El usuario es correcto");
                loginFrame.dispose(); // << cerrar ventana de login
                FormContacto objetoContacto = new FormContacto();
                objetoContacto.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "El usuario es incorrecto, Intente nuevamente");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.toString());
        }
    }
}
