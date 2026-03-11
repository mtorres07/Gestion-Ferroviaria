package es.studium;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ModificacionEstacion extends WindowAdapter implements ActionListener, ItemListener {
    Frame ventana = new Frame("Modificar Estación");
    Label lblElegir = new Label("Seleccione Estación:");
    Choice choEstaciones = new Choice();
    
    Label lblNombre = new Label("Nombre:");
    TextField txtNombre = new TextField(20);
    Label lblPrecio = new Label("Precio Ticket:");
    TextField txtPrecio = new TextField(20);
    
    Label lblCiudad = new Label("Ciudad:");
    Choice choCiudades = new Choice();
    Label lblTren = new Label("Tren:");
    Choice choTrenes = new Choice();
    
    Button btnModificar = new Button("Actualizar Estación");

    String driver = "com.mysql.cj.jdbc.Driver";
    String url = "jdbc:mysql://localhost:3306/PracticaPR2Trimestre";
    String loginBD = "admin_trenes";
    String passwordBD = "EstudiuM2026*";

    public ModificacionEstacion() {
        ventana.setLayout(new FlowLayout());
        ventana.setSize(300, 450);
        ventana.addWindowListener(this);

        rellenarEstaciones();
        rellenarCiudades();
        rellenarTrenes();

        ventana.add(lblElegir); ventana.add(choEstaciones);
        ventana.add(lblNombre); ventana.add(txtNombre);
        ventana.add(lblPrecio); ventana.add(txtPrecio);
        ventana.add(lblCiudad); ventana.add(choCiudades);
        ventana.add(lblTren); ventana.add(choTrenes);
        ventana.add(btnModificar);

        choEstaciones.addItemListener(this);
        btnModificar.addActionListener(this);

        ventana.setResizable(false);
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);
    }

    private void rellenarEstaciones() {
        try {
            Class.forName(driver);
            Connection con = DriverManager.getConnection(url, loginBD, passwordBD);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT idEstacion, nombreEstacion FROM Estacion");
            choEstaciones.add("Seleccione...");
            while (rs.next()) {
                choEstaciones.add(rs.getInt("idEstacion") + "-" + rs.getString("nombreEstacion"));
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

    private void rellenarTrenes() {
        try {
            Connection con = DriverManager.getConnection(url, loginBD, passwordBD);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT idTren, modeloTren FROM Tren");
            while (rs.next()) {
                choTrenes.add(rs.getInt("idTren") + "-" + rs.getString("modeloTren"));
            }
            con.close();
        } catch (Exception e) { e.printStackTrace(); }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (choEstaciones.getSelectedIndex() != 0) {
            cargarDatosEstacion();
        }
    }

    private void cargarDatosEstacion() {
        try {
            Connection con = DriverManager.getConnection(url, loginBD, passwordBD);
            Statement st = con.createStatement();
            String id = choEstaciones.getSelectedItem().split("-")[0];
            ResultSet rs = st.executeQuery("SELECT * FROM Estacion WHERE idEstacion = " + id);
            if (rs.next()) {
                txtNombre.setText(rs.getString("nombreEstacion"));
                txtPrecio.setText(rs.getString("precioTicketEstacion"));
                
                // Posicionar Choice de Ciudad
                int idC = rs.getInt("idCiudadFK");
                for (int i = 0; i < choCiudades.getItemCount(); i++) {
                    if (choCiudades.getItem(i).startsWith(idC + "-")) choCiudades.select(i);
                }
                
                // Posicionar Choice de Tren
                int idT = rs.getInt("idTrenFK");
                for (int i = 0; i < choTrenes.getItemCount(); i++) {
                    if (choTrenes.getItem(i).startsWith(idT + "-")) choTrenes.select(i);
                }
            }
            con.close();
        } catch (Exception e) { e.printStackTrace(); }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btnModificar)) {
            actualizarEstacion();
        }
    }

    private void actualizarEstacion() {
        try {
            Connection con = DriverManager.getConnection(url, loginBD, passwordBD);
            Statement st = con.createStatement();
            String idEst = choEstaciones.getSelectedItem().split("-")[0];
            String idCiu = choCiudades.getSelectedItem().split("-")[0];
            String idTre = choTrenes.getSelectedItem().split("-")[0];
            
            String sql = "UPDATE Estacion SET nombreEstacion = '" + txtNombre.getText() + 
                         "', precioTicketEstacion = " + txtPrecio.getText() + 
                         ", idCiudadFK = " + idCiu + 
                         ", idTrenFK = " + idTre + 
                         " WHERE idEstacion = " + idEst;
            
            st.executeUpdate(sql);
            System.out.println("Estación actualizada correctamente.");
            ventana.dispose();
            con.close();
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    @Override
    public void windowClosing(WindowEvent e) { ventana.dispose(); }
}