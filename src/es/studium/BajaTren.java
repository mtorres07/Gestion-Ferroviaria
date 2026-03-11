package es.studium;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class BajaTren extends WindowAdapter implements ActionListener {
    Frame ventana = new Frame("Baja de Tren");
    Label lblElegir = new Label("Elegir tren para eliminar:");
    Choice choTrenes = new Choice();
    Button btnBorrar = new Button("Borrar");

    // Componentes para el Dialog de confirmación
    Dialog dlgConfirmacion = new Dialog(ventana, "Confirmación", true);
    Label lblMensaje = new Label("¿Seguro que desea eliminar este tren?");
    Button btnSi = new Button("Sí");
    Button btnNo = new Button("No");

    String driver = "com.mysql.cj.jdbc.Driver";
    String url = "jdbc:mysql://localhost:3306/PracticaPR2Trimestre";
    String loginBD = "admin_trenes";
    String passwordBD = "EstudiuM2026*";

    public BajaTren() {
        ventana.setLayout(new FlowLayout());
        ventana.setSize(300, 150);
        ventana.addWindowListener(this);

        rellenarChoice();

        ventana.add(lblElegir);
        ventana.add(choTrenes);
        ventana.add(btnBorrar);
        btnBorrar.addActionListener(this);

        // Configuración del Dialog
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
        choTrenes.removeAll();
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, loginBD, passwordBD);
            statement = connection.createStatement();
            // Sacamos ID y Modelo
            rs = statement.executeQuery("SELECT idTren, modeloTren FROM Tren");
            choTrenes.add("Seleccione un tren...");
            while (rs.next()) {
                choTrenes.add(rs.getInt("idTren") + "-" + rs.getString("modeloTren"));
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
            if (choTrenes.getSelectedIndex() != 0) {
                dlgConfirmacion.setLocationRelativeTo(ventana);
                dlgConfirmacion.setVisible(true);
            }
        } else if (e.getSource().equals(btnSi)) {
            eliminarTren();
            dlgConfirmacion.setVisible(false);
            rellenarChoice();
        } else if (e.getSource().equals(btnNo)) {
            dlgConfirmacion.setVisible(false);
        }
    }

    private void eliminarTren() {
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, loginBD, passwordBD);
            statement = connection.createStatement();
            
            String idTren = choTrenes.getSelectedItem().split("-")[0];
            String sql = "DELETE FROM Tren WHERE idTren = " + idTren;
            
            statement.executeUpdate(sql);
            System.out.println("Tren eliminado correctamente.");
        } catch (SQLException se) {
            // Error común: intentar borrar un tren que está asignado a una estación
            System.err.println("Error: El tren tiene estaciones asignadas.");
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