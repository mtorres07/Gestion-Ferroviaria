package es.studium;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AltaTren extends WindowAdapter implements ActionListener {
    Frame ventana = new Frame("Alta de Tren");
    
    Label lblModelo = new Label("Modelo del Tren:");
    TextField txtModelo = new TextField(20);
    
    Label lblCapacidad = new Label("Capacidad (Pasajeros):");
    TextField txtCapacidad = new TextField(20);
    
    Label lblFecha = new Label("Fecha Fabricación (AAAA-MM-DD):");
    TextField txtFecha = new TextField(20);
    
    Label lblCiudad = new Label("Ciudad de Origen:");
    Choice choCiudades = new Choice(); // Aquí cargaremos las ciudades de la BD
    
    Button btnGuardar = new Button("Guardar");
    Button btnLimpiar = new Button("Limpiar");

    // Datos de tu conexión
    String driver = "com.mysql.cj.jdbc.Driver";
    String url = "jdbc:mysql://localhost:3306/PracticaPR2Trimestre";
    String loginBD = "admin_trenes";
    String passwordBD = "EstudiuM2026*";

    public AltaTren() {
        ventana.setLayout(new FlowLayout());
        ventana.setSize(280, 400);
        ventana.addWindowListener(this);

        // 1. Antes de mostrar la ventana, rellenamos el Choice con la BD
        rellenarCiudades();

        ventana.add(lblModelo); ventana.add(txtModelo);
        ventana.add(lblCapacidad); ventana.add(txtCapacidad);
        ventana.add(lblFecha); ventana.add(txtFecha);
        ventana.add(lblCiudad); ventana.add(choCiudades);
        
        ventana.add(btnGuardar);
        ventana.add(btnLimpiar);

        btnGuardar.addActionListener(this);
        btnLimpiar.addActionListener(this);

        ventana.setResizable(false);
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);
    }

    private void rellenarCiudades() {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, loginBD, passwordBD);
            statement = connection.createStatement();
            
            // Sacamos el ID y el Nombre para que el usuario sepa qué elige
            rs = statement.executeQuery("SELECT idCiudad, nombreCiudad FROM Ciudad");
            
            choCiudades.add("Seleccione una ciudad...");
            while (rs.next()) {
                // Metemos en el Choice algo como "1-Sevilla"
                choCiudades.add(rs.getInt("idCiudad") + "-" + rs.getString("nombreCiudad"));
            }
        } catch (Exception e) {
            System.err.println("Error cargando ciudades: " + e.getMessage());
        } finally {
            try { if (connection != null) connection.close(); } catch (SQLException se) {}
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btnGuardar)) {
            // Validamos que haya seleccionado una ciudad real (no el primer mensaje)
            if (choCiudades.getSelectedIndex() != 0) {
                insertarTren();
            } else {
                System.out.println("Por favor, selecciona una ciudad válida.");
            }
        } else if (e.getSource().equals(btnLimpiar)) {
            txtModelo.setText("");
            txtCapacidad.setText("");
            txtFecha.setText("");
            choCiudades.select(0);
        }
    }

    private void insertarTren() {
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, loginBD, passwordBD);
            statement = connection.createStatement();
            
            // TRUCO: Extraemos el ID que está antes del guión "-" en el Choice
            // Si el item es "2-Madrid", idCiudad será "2"
            String itemSeleccionado = choCiudades.getSelectedItem();
            String idCiudad = itemSeleccionado.split("-")[0];

            // Sentencia SQL según tu tabla Tren: idTren(null), modelo, capacidad, fecha, idCiudadFK
            String sql = "INSERT INTO Tren VALUES (null, '" + 
                         txtModelo.getText() + "', " + 
                         txtCapacidad.getText() + ", '" + 
                         txtFecha.getText() + "', " + 
                         idCiudad + ")";
            
            statement.executeUpdate(sql);
            System.out.println("Tren insertado con éxito.");
            ventana.dispose(); // Cerramos al terminar
            
        } catch (Exception ex) {
            System.err.println("Error al insertar tren: " + ex.getMessage());
        } finally {
            try { if (connection != null) connection.close(); } catch (SQLException se) {}
        }
    }

    @Override
    public void windowClosing(WindowEvent e) {
        ventana.dispose();
    }
}