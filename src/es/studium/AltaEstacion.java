package es.studium;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AltaEstacion extends WindowAdapter implements ActionListener {
    Frame ventana = new Frame("Alta de Estación");
    
    Label lblNombre = new Label("Nombre Estación:");
    TextField txtNombre = new TextField(20);
    
    Label lblPrecio = new Label("Precio Ticket:");
    TextField txtPrecio = new TextField(20);
    
    Label lblCiudad = new Label("Ciudad:");
    Choice choCiudades = new Choice();
    
    Label lblTren = new Label("Tren:");
    Choice choTrenes = new Choice();
    
    Button btnGuardar = new Button("Guardar");

    String driver = "com.mysql.cj.jdbc.Driver";
    String url = "jdbc:mysql://localhost:3306/PracticaPR2Trimestre";
    String loginBD = "admin_trenes";
    String passwordBD = "EstudiuM2026*";

    public AltaEstacion() {
        ventana.setLayout(new FlowLayout());
        ventana.setSize(300, 450);
        ventana.addWindowListener(this);

        // Rellenamos ambos desplegables al iniciar
        rellenarCiudades();
        rellenarTrenes();

        ventana.add(lblNombre); ventana.add(txtNombre);
        ventana.add(lblPrecio); ventana.add(txtPrecio);
        ventana.add(lblCiudad); ventana.add(choCiudades);
        ventana.add(lblTren); ventana.add(choTrenes);
        ventana.add(btnGuardar);

        btnGuardar.addActionListener(this);
        
        ventana.setResizable(false);
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);
    }

    private void rellenarCiudades() {
        try {
            Class.forName(driver);
            Connection con = DriverManager.getConnection(url, loginBD, passwordBD);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT idCiudad, nombreCiudad FROM Ciudad");
            choCiudades.add("Selecciona Ciudad...");
            while(rs.next()) {
                choCiudades.add(rs.getInt("idCiudad") + "-" + rs.getString("nombreCiudad"));
            }
            con.close();
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void rellenarTrenes() {
        try {
            Class.forName(driver);
            Connection con = DriverManager.getConnection(url, loginBD, passwordBD);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT idTren, modeloTren FROM Tren");
            choTrenes.add("Selecciona Tren...");
            while(rs.next()) {
                choTrenes.add(rs.getInt("idTren") + "-" + rs.getString("modeloTren"));
            }
            con.close();
        } catch (Exception e) { e.printStackTrace(); }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(btnGuardar)) {
            if(choCiudades.getSelectedIndex() != 0 && choTrenes.getSelectedIndex() != 0) {
                insertarEstacion();
            }
        }
    }

    private void insertarEstacion() {
        try {
            Class.forName(driver);
            Connection con = DriverManager.getConnection(url, loginBD, passwordBD);
            Statement st = con.createStatement();
            
            String idCiudad = choCiudades.getSelectedItem().split("-")[0];
            String idTren = choTrenes.getSelectedItem().split("-")[0];

            // SQL: idEstacion(null), nombre, precio, idCiudadFK, idTrenFK
            String sql = "INSERT INTO Estacion VALUES (null, '" + 
                         txtNombre.getText() + "', " + 
                         txtPrecio.getText() + ", " + 
                         idCiudad + ", " + 
                         idTren + ")";
            
            st.executeUpdate(sql);
            System.out.println("Estación guardada correctamente");
            ventana.dispose();
            con.close();
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    @Override
    public void windowClosing(WindowEvent e) { ventana.dispose(); }
}