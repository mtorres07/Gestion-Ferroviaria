package es.studium;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ModificacionTren extends WindowAdapter implements ActionListener, ItemListener {
    Frame ventana = new Frame("Modificar Tren");
    Label lblElegir = new Label("Seleccione Tren:");
    Choice choTrenes = new Choice();
    
    Label lblModelo = new Label("Modelo:");
    TextField txtModelo = new TextField(20);
    Label lblCapacidad = new Label("Capacidad:");
    TextField txtCapacidad = new TextField(20);
    Label lblCiudad = new Label("Ciudad (Sede):");
    Choice choCiudades = new Choice();
    
    Button btnModificar = new Button("Actualizar Tren");

    String driver = "com.mysql.cj.jdbc.Driver";
    String url = "jdbc:mysql://localhost:3306/PracticaPR2Trimestre";
    String loginBD = "admin_trenes";
    String passwordBD = "EstudiuM2026*";

    public ModificacionTren() {
        ventana.setLayout(new FlowLayout());
        ventana.setSize(300, 350);
        ventana.addWindowListener(this);

        rellenarTrenes();
        rellenarCiudades();

        ventana.add(lblElegir); ventana.add(choTrenes);
        ventana.add(lblModelo); ventana.add(txtModelo);
        ventana.add(lblCapacidad); ventana.add(txtCapacidad);
        ventana.add(lblCiudad); ventana.add(choCiudades);
        ventana.add(btnModificar);

        choTrenes.addItemListener(this);
        btnModificar.addActionListener(this);

        ventana.setResizable(false);
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);
    }

    private void rellenarTrenes() {
        try {
            Class.forName(driver);
            Connection con = DriverManager.getConnection(url, loginBD, passwordBD);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT idTren, modeloTren FROM Tren");
            choTrenes.add("Seleccione...");
            while (rs.next()) {
                choTrenes.add(rs.getInt("idTren") + "-" + rs.getString("modeloTren"));
            }
            con.close();
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void rellenarCiudades() {
        try {
            Connection con = DriverManager.getConnection(url, loginBD, passwordBD);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT idCiudad, nombreCiudad FROM Ciudad");
            while (rs.next()) {
                choCiudades.add(rs.getInt("idCiudad") + "-" + rs.getString("nombreCiudad"));
            }
            con.close();
        } catch (Exception e) { e.printStackTrace(); }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (choTrenes.getSelectedIndex() != 0) {
            cargarDatosTren();
        }
    }

    private void cargarDatosTren() {
        try {
            Connection con = DriverManager.getConnection(url, loginBD, passwordBD);
            Statement st = con.createStatement();
            String id = choTrenes.getSelectedItem().split("-")[0];
            ResultSet rs = st.executeQuery("SELECT * FROM Tren WHERE idTren = " + id);
            if (rs.next()) {
                txtModelo.setText(rs.getString("modeloTren"));
                txtCapacidad.setText(rs.getString("capacidadTren"));
                // Seleccionar en el choice la ciudad que ya tiene el tren
                int idCiudadFK = rs.getInt("idCiudadFK");
                for (int i = 0; i < choCiudades.getItemCount(); i++) {
                    if (choCiudades.getItem(i).startsWith(idCiudadFK + "-")) {
                        choCiudades.select(i);
                    }
                }
            }
            con.close();
        } catch (Exception e) { e.printStackTrace(); }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btnModificar)) {
            actualizarTren();
        }
    }

    private void actualizarTren() {
        try {
            Connection con = DriverManager.getConnection(url, loginBD, passwordBD);
            Statement st = con.createStatement();
            String idTren = choTrenes.getSelectedItem().split("-")[0];
            String idCiudad = choCiudades.getSelectedItem().split("-")[0];
            
            String sql = "UPDATE Tren SET modeloTren = '" + txtModelo.getText() + 
                         "', capacidadTren = " + txtCapacidad.getText() + 
                         ", idCiudadFK = " + idCiudad + 
                         " WHERE idTren = " + idTren;
            
            st.executeUpdate(sql);
            System.out.println("Tren actualizado.");
            ventana.dispose();
            con.close();
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    @Override
    public void windowClosing(WindowEvent e) { ventana.dispose(); }
}