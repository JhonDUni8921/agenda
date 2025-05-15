package com.mycompany.AgendaProyect;

import Clases.Cconexion;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;

public class FormContacto extends JFrame {

    JTextField txtId, txtNombre, txtTelefono, txtEmail, txtDireccion;
    JTable tablaContactos;
    DefaultTableModel modelo;
    JButton btnAgregar, btnEditar, btnEliminar, btnLimpiar;

    public FormContacto() {
        setTitle("Agenda de Contactos");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(240, 248, 255));

        Font font = new Font("Segoe UI", Font.PLAIN, 16);
        Border padding = new EmptyBorder(10, 10, 10, 10);

        JPanel panelCampos = new JPanel(new GridLayout(5, 2, 10, 10));
        panelCampos.setBorder(padding);
        panelCampos.setBackground(new Color(240, 248, 255));

        txtId = new JTextField(); txtId.setEditable(false);
        txtNombre = new JTextField();
        txtTelefono = new JTextField();
        txtEmail = new JTextField();
        txtDireccion = new JTextField();

        JLabel[] labels = {
            new JLabel("ID:"), new JLabel("Nombre:"),
            new JLabel("Teléfono:"), new JLabel("Email:"),
            new JLabel("Dirección:")
        };

        JTextField[] fields = { txtId, txtNombre, txtTelefono, txtEmail, txtDireccion };

        for (int i = 0; i < labels.length; i++) {
            labels[i].setFont(font);
            panelCampos.add(labels[i]);
            fields[i].setFont(font);
            panelCampos.add(fields[i]);
        }

        add(panelCampos, BorderLayout.NORTH);

        modelo = new DefaultTableModel(new String[]{"ID", "Nombre", "Teléfono", "Email", "Dirección"}, 0);
        tablaContactos = new JTable(modelo);
        tablaContactos.setFont(font);
        tablaContactos.setRowHeight(24);
        tablaContactos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        JScrollPane scroll = new JScrollPane(tablaContactos);
        scroll.setBorder(padding);
        add(scroll, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.setBackground(new Color(224, 255, 255));

        btnAgregar = new JButton("Agregar");
        btnEditar = new JButton("Editar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar");

        JButton[] botones = { btnAgregar, btnEditar, btnEliminar, btnLimpiar };
        for (JButton btn : botones) {
            btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btn.setBackground(new Color(70, 130, 180));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            panelBotones.add(btn);
        }

        add(panelBotones, BorderLayout.SOUTH);

        listarContactos();

        tablaContactos.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int fila = tablaContactos.getSelectedRow();
                txtId.setText(tablaContactos.getValueAt(fila, 0).toString());
                txtNombre.setText(tablaContactos.getValueAt(fila, 1).toString());
                txtTelefono.setText(tablaContactos.getValueAt(fila, 2).toString());
                txtEmail.setText(tablaContactos.getValueAt(fila, 3).toString());
                txtDireccion.setText(tablaContactos.getValueAt(fila, 4).toString());
            }
        });

        btnAgregar.addActionListener(e -> agregarContacto());
        btnEditar.addActionListener(e -> editarContacto());
        btnEliminar.addActionListener(e -> eliminarContacto());
        btnLimpiar.addActionListener(e -> limpiarCampos());

        setVisible(true);
    }

    void listarContactos() {
        modelo.setRowCount(0);
        try {
            Cconexion con = new Cconexion();
            Connection conn = con.conectarbase();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM agenda");
            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("telefono"),
                    rs.getString("email"),
                    rs.getString("direccion")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al listar contactos: " + e);
        }
    }

    void agregarContacto() {
        if (!validarCampos()) return;

        try {
            Cconexion con = new Cconexion();
            Connection conn = con.conectarbase();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO agenda(nombre, telefono, email, direccion) VALUES (?, ?, ?, ?)");
            ps.setString(1, txtNombre.getText().trim());
            ps.setString(2, txtTelefono.getText().trim());
            ps.setString(3, txtEmail.getText().trim());
            ps.setString(4, txtDireccion.getText().trim());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Contacto agregado.");
            limpiarCampos();
            listarContactos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al agregar: " + e);
        }
    }

    void editarContacto() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un contacto.");
            return;
        }

        if (!validarCampos()) return;

        try {
            Cconexion con = new Cconexion();
            Connection conn = con.conectarbase();
            PreparedStatement ps = conn.prepareStatement("UPDATE agenda SET nombre=?, telefono=?, email=?, direccion=? WHERE id=?");
            ps.setString(1, txtNombre.getText().trim());
            ps.setString(2, txtTelefono.getText().trim());
            ps.setString(3, txtEmail.getText().trim());
            ps.setString(4, txtDireccion.getText().trim());
            ps.setInt(5, Integer.parseInt(txtId.getText()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Contacto editado.");
            limpiarCampos();
            listarContactos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al editar: " + e);
        }
    }

    void eliminarContacto() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un contacto.");
            return;
        }
        try {
            Cconexion con = new Cconexion();
            Connection conn = con.conectarbase();
            PreparedStatement ps = conn.prepareStatement("DELETE FROM agenda WHERE id=?");
            ps.setInt(1, Integer.parseInt(txtId.getText()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Contacto eliminado.");
            limpiarCampos();
            listarContactos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar: " + e);
        }
    }

    void limpiarCampos() {
        txtId.setText("");
        txtNombre.setText("");
        txtTelefono.setText("");
        txtEmail.setText("");
        txtDireccion.setText("");
    }

    boolean validarCampos() {
        if (txtNombre.getText().trim().isEmpty() ||
            txtTelefono.getText().trim().isEmpty() ||
            txtEmail.getText().trim().isEmpty() ||
            txtDireccion.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.");
            return false;
        }

        if (!txtEmail.getText().matches("^\\S+@\\S+\\.\\S+$")) {
            JOptionPane.showMessageDialog(this, "Ingrese un correo electrónico válido.");
            return false;
        }

        return true;
    }
}
