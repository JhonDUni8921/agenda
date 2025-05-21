package com.mycompany.AgendaProyect;

import Clases.Cconexion;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;

public class FormContacto extends JFrame {

    JTextField txtNombre, txtTelefono, txtEmail, txtDireccion;
    JTable tablaContactos;
    DefaultTableModel modelo;
    JButton btnAgregar, btnEditar, btnEliminar, btnLimpiar;

    public FormContacto() {
        setTitle("Agenda de Contactos");
        setSize(950, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanelConFondo fondo = new JPanelConFondo("/Images/AgendaImage.png");
        fondo.setLayout(new BorderLayout(15, 15));
        setContentPane(fondo);

        Font fontLabel = new Font("Segoe UI", Font.BOLD, 16);
        Font fontField = new Font("Segoe UI", Font.PLAIN, 16);
        Border padding = new EmptyBorder(20, 20, 20, 20);

        JPanel panelCampos = new JPanel(new GridLayout(4, 2, 15, 15));
        panelCampos.setBorder(padding);
        panelCampos.setOpaque(false);

        txtNombre = crearCampoTexto();
        txtTelefono = crearCampoTexto();
        txtEmail = crearCampoTexto();
        txtDireccion = crearCampoTexto();

        JLabel[] labels = {
            crearLabel("Nombre:"), crearLabel("Teléfono:"),
            crearLabel("Email:"), crearLabel("Dirección:")
        };

        JTextField[] fields = { txtNombre, txtTelefono, txtEmail, txtDireccion };

        for (int i = 0; i < labels.length; i++) {
            panelCampos.add(labels[i]);
            panelCampos.add(fields[i]);
        }

        add(panelCampos, BorderLayout.NORTH);

        modelo = new DefaultTableModel(new String[]{"Nombre", "Teléfono", "Email", "Dirección"}, 0);
        tablaContactos = new JTable(modelo);
        tablaContactos.setFont(fontField);
        tablaContactos.setRowHeight(26);
        tablaContactos.setShowGrid(false);
        tablaContactos.setIntercellSpacing(new Dimension(0, 0));
        tablaContactos.setSelectionBackground(new Color(173, 216, 230));
        tablaContactos.setSelectionForeground(Color.BLACK);
        tablaContactos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tablaContactos.getTableHeader().setBackground(new Color(100, 149, 237));
        tablaContactos.getTableHeader().setForeground(Color.WHITE);
        JScrollPane scroll = new JScrollPane(tablaContactos);
        scroll.setBorder(padding);
        add(scroll, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.setOpaque(false);

        btnAgregar = crearBoton("Agregar", new Color(46, 204, 113));
        btnEditar = crearBoton("Editar", new Color(241, 196, 15));
        btnEliminar = crearBoton("Eliminar", new Color(231, 76, 60));
        btnLimpiar = crearBoton("Limpiar", new Color(52, 152, 219));

        JButton[] botones = { btnAgregar, btnEditar, btnEliminar, btnLimpiar };
        for (JButton btn : botones) {
            panelBotones.add(btn);
        }

        add(panelBotones, BorderLayout.SOUTH);

        listarContactos();

        tablaContactos.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int fila = tablaContactos.getSelectedRow();
                txtNombre.setText(tablaContactos.getValueAt(fila, 0).toString());
                txtTelefono.setText(tablaContactos.getValueAt(fila, 1).toString());
                txtEmail.setText(tablaContactos.getValueAt(fila, 2).toString());
                txtDireccion.setText(tablaContactos.getValueAt(fila, 3).toString());
            }
        });

        btnAgregar.addActionListener(e -> agregarContacto());
        btnEditar.addActionListener(e -> editarContacto());
        btnEliminar.addActionListener(e -> eliminarContacto());
        btnLimpiar.addActionListener(e -> limpiarCampos());

        setVisible(true);
    }

    private JLabel crearLabel(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lbl.setForeground(new Color(33, 33, 33));
        return lbl;
    }

    private JTextField crearCampoTexto() {
        JTextField campo = new JTextField();
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return campo;
    }

    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return boton;
    }

    void listarContactos() {
        modelo.setRowCount(0);
        try {
            Cconexion con = new Cconexion();
            Connection conn = con.conectarbase();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT nombre, telefono, email, direccion FROM agenda");
            while (rs.next()) {
                modelo.addRow(new Object[]{
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
        if (!validarCampos()) return;

        try {
            Cconexion con = new Cconexion();
            Connection conn = con.conectarbase();
            PreparedStatement ps = conn.prepareStatement("UPDATE agenda SET telefono=?, email=?, direccion=? WHERE nombre=?");
            ps.setString(1, txtTelefono.getText().trim());
            ps.setString(2, txtEmail.getText().trim());
            ps.setString(3, txtDireccion.getText().trim());
            ps.setString(4, txtNombre.getText().trim());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Contacto editado.");
                limpiarCampos();
                listarContactos();
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró el contacto.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al editar: " + e);
        }
    }

    void eliminarContacto() {
        if (txtNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un contacto.");
            return;
        }
        try {
            Cconexion con = new Cconexion();
            Connection conn = con.conectarbase();
            PreparedStatement ps = conn.prepareStatement("DELETE FROM agenda WHERE nombre=?");
            ps.setString(1, txtNombre.getText().trim());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Contacto eliminado.");
                limpiarCampos();
                listarContactos();
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró el contacto.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar: " + e);
        }
    }

    void limpiarCampos() {
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

    class JPanelConFondo extends JPanel {
        private Image imagen;

        public JPanelConFondo(String ruta) {
            try {
                imagen = new ImageIcon(getClass().getResource(ruta)).getImage();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "No se pudo cargar la imagen de fondo: " + e);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (imagen != null) {
                g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }
}
