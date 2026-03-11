package es.studium;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class BajaEstacion extends WindowAdapter implements ActionListener {
    Frame ventana = new Frame("Baja de Estación");
    Label lblElegir = new Label("Elegir estación para borrar:");
    Choice choEstaciones = new Choice();
    Button btnBorrar = new Button("Borrar");

    // Componentes para el mensaje de confirmación
    Dialog dlgConfirmacion = new Dialog(ventana, "Confirmación", true);
    Label lblMensaje = new Label("¿Seguro que desea borrar esta estación?");
    Button btnSi = new Button("Sí");
    Button btnNo = new Button("No");

    String driver = "com.mysql.cj.jdbc.Driver";
    String url = "jdbc:mysql://localhost:3306/PracticaPR2Trimestre";
    String loginBD = "admin_trenes";
    String passwordBD = "EstudiuM2026*";

    public BajaEstacion() {
        ventana.setLayout(new FlowLayout());
        ventana.setSize(300, 150);
        ventana.addWindowListener(this);

        rellenarChoice();

        ventana.add(lblElegir);
        ventana.add(choEstaciones);
        ventana.add(btnBorrar);

        btnBorrar.addActionListener(this);

        // Configuración del Dialog (Ventana de confirmación)
        dlgConfirmacion.setLayout(new FlowLayout());
        dlgConfirmacion.setSize(250, 100);
        dlgConfirmacion.addWindowListener(this);
        dlgConfirmacion.add(lblMensaje);
        dlgConfirmacion.add(btnSi);
        dlgConfirmacion.add(btnNo);
        btnSi.addActionListener(this);
        btnNo.addActionListener(this);

        ventana.setResizable(false);
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);
    }

    private void rellenarChoice() {
        choEstaciones.removeAll();
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, loginBD, passwordBD);
            statement = connection.createStatement();
            // Sacamos ID y Nombre de la estación
            rs = statement.executeQuery("SELECT idEstacion, nombreEstacion FROM Estacion");
            choEstaciones.add("Seleccione estación...");
            while (rs.next()) {
                choEstaciones.add(rs.getInt("idEstacion") + "-" + rs.getString("nombreEstacion"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (connection != null) connection.close(); } catch (SQLException se) {}
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btnBorrar)) {
            if (choEstaciones.getSelectedIndex() != 0) {
                // Mostramos el diálogo de confirmación
                dlgConfirmacion.setLocationRelativeTo(ventana);
                dlgConfirmacion.setVisible(true);
            }
        } else if (e.getSource().equals(btnSi)) {
            eliminarEstacion();
            dlgConfirmacion.setVisible(false);
            rellenarChoice(); // Refrescamos la lista
        } else if (e.getSource().equals(btnNo)) {
            dlgConfirmacion.setVisible(false);
        }
    }

    private void eliminarEstacion() {
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, loginBD, passwordBD);
            statement = connection.createStatement();
            
            String idEstacion = choEstaciones.getSelectedItem().split("-")[0];
            String sql = "DELETE FROM Estacion WHERE idEstacion = " + idEstacion;
            
            statement.executeUpdate(sql);
            System.out.println("Estación eliminada con éxito.");
        } catch (SQLException se) {
            System.err.println("Error al borrar: " + se.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try { if (connection != null) connection.close(); } catch (SQLException se) {}
        }
    }

    @Override
    public void windowClosing(WindowEvent e) {
        if (dlgConfirmacion.isActive()) {
            dlgConfirmacion.setVisible(false);
        } else {
            ventana.dispose();
        }
    }
}