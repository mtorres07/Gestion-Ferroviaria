package es.studium;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class BajaCiudad extends WindowAdapter implements ActionListener {
    Frame ventana = new Frame("Baja de Ciudad");
    Label lblElegir = new Label("Elegir ciudad para borrar:");
    Choice choCiudades = new Choice();
    Button btnBorrar = new Button("Borrar");

    // Componentes para el mensaje de confirmación
    Dialog dlgConfirmacion = new Dialog(ventana, "Confirmación", true);
    Label lblMensaje = new Label("¿Seguro que desea borrar esta ciudad?");
    Button btnSi = new Button("Sí");
    Button btnNo = new Button("No");

    String driver = "com.mysql.cj.jdbc.Driver";
    String url = "jdbc:mysql://localhost:3306/PracticaPR2Trimestre";
    String loginBD = "admin_trenes";
    String passwordBD = "EstudiuM2026*";

    public BajaCiudad() {
        ventana.setLayout(new FlowLayout());
        ventana.setSize(300, 150);
        ventana.addWindowListener(this);

        rellenarChoice();

        ventana.add(lblElegir);
        ventana.add(choCiudades);
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
        choCiudades.removeAll();
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, loginBD, passwordBD);
            statement = connection.createStatement();
            rs = statement.executeQuery("SELECT idCiudad, nombreCiudad FROM Ciudad");
            choCiudades.add("Seleccione una ciudad...");
            while (rs.next()) {
                choCiudades.add(rs.getInt("idCiudad") + "-" + rs.getString("nombreCiudad"));
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
            if (choCiudades.getSelectedIndex() != 0) {
                // Posicionamos el diálogo y lo mostramos
                dlgConfirmacion.setLocationRelativeTo(ventana);
                dlgConfirmacion.setVisible(true);
            }
        } else if (e.getSource().equals(btnSi)) {
            eliminarCiudad();
            dlgConfirmacion.setVisible(false);
            rellenarChoice(); // Refrescamos la lista para que ya no salga la borrada
        } else if (e.getSource().equals(btnNo)) {
            dlgConfirmacion.setVisible(false);
        }
    }

    private void eliminarCiudad() {
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, loginBD, passwordBD);
            statement = connection.createStatement();
            
            String idCiudad = choCiudades.getSelectedItem().split("-")[0];
            String sql = "DELETE FROM Ciudad WHERE idCiudad = " + idCiudad;
            
            statement.executeUpdate(sql);
            System.out.println("Ciudad eliminada con éxito.");
        } catch (SQLException se) {
            // Si intentas borrar una ciudad que tiene trenes, saltará un error de FK
            System.err.println("No se puede borrar: existen trenes en esta ciudad.");
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