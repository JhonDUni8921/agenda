package Clases;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContactoDAO {

    private Connection con;

    public ContactoDAO() {
        con = new Cconexion().conectarbase();
    }

    public List<Contacto> listarContactos() {
        List<Contacto> lista = new ArrayList<>();
        String sql = "SELECT * FROM contactos";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Contacto c = new Contacto();
                c.setId(rs.getInt("id"));
                c.setNombre(rs.getString("nombre"));
                c.setTelefono(rs.getString("telefono"));
                c.setEmail(rs.getString("email"));
                c.setDireccion(rs.getString("direccion"));
                lista.add(c);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar contactos: " + e);
        }

        return lista;
    }

    public boolean insertarContacto(Contacto c) {
        String sql = "INSERT INTO contactos(nombre, telefono, email, direccion) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, c.getNombre());
            ps.setString(2, c.getTelefono());
            ps.setString(3, c.getEmail());
            ps.setString(4, c.getDireccion());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al insertar: " + e);
            return false;
        }
    }

    public boolean actualizarContacto(Contacto c) {
        String sql = "UPDATE contactos SET nombre=?, telefono=?, email=?, direccion=? WHERE id=?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, c.getNombre());
            ps.setString(2, c.getTelefono());
            ps.setString(3, c.getEmail());
            ps.setString(4, c.getDireccion());
            ps.setInt(5, c.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al actualizar: " + e);
            return false;
        }
    }

    public boolean eliminarContacto(int id) {
        String sql = "DELETE FROM contactos WHERE id=?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al eliminar: " + e);
            return false;
        }
    }
}
