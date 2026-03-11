package es.studium;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ModificacionCiudad extends WindowAdapter implements ActionListener, ItemListener {
    Frame ventana = new Frame("Modificar Ciudad");
    Label lblElegir = new Label("Seleccione Ciudad:");
    Choice choCiudades = new Choice();
    
    Label lblNombre = new Label("Nuevo Nombre:");
    TextField txtNombre = new TextField(20);
    Label lblPais = new Label("Nuevo País:");
    TextField txtPais = new TextField(20);
    
    Button btnModificar = new Button("Actualizar Datos");

    // Datos de conexión
    String driver = "com.mysql.cj.jdbc.Driver";
    String url = "jdbc:mysql://localhost:3306/PracticaPR2Trimestre";
    String loginBD = "admin_trenes";
    String passwordBD = "EstudiuM2026*";

    public ModificacionCiudad() {
        ventana.setLayout(new FlowLayout());
        ventana.setSize(300, 250);
        ventana.addWindowListener(this);

        rellenarChoice();

        ventana.add(lblElegir);
        ventana.add(choCiudades);
        ventana.add(lblNombre);
        ventana.add(txtNombre);
        ventana.add(lblPais);
        ventana.add(txtPais);
        ventana.add(btnModificar);

        // Importante: Registramos los dos tipos de eventos
        choCiudades.addItemListener(this);
        btnModificar.addActionListener(this);

        ventana.setResizable(false);
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);
    }

    private void rellenarChoice() {
        try {
            Class.forName(driver);
            Connection con = DriverManager.getConnection(url, loginBD, passwordBD);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT idCiudad, nombreCiudad FROM Ciudad");
            
            choCiudades.add("Seleccione...");
            while (rs.next()) {
                choCiudades.add(rs.getInt("idCiudad") + "-" + rs.getString("nombreCiudad"));
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Este método se activa al cambiar la selección del desplegable
    @Override
    public void itemStateChanged(ItemEvent e) {
        if (choCiudades.getSelectedIndex() != 0) {
            cargarDatosCiudad();
        } else {
            txtNombre.setText("");
            txtPais.setText("");
        }
    }

    private void cargarDatosCiudad() {
        try {
            Connection con = DriverManager.getConnection(url, loginBD, passwordBD);
            Statement st = con.createStatement();
            // Sacamos el ID (lo que hay antes del guion)
            String id = choCiudades.getSelectedItem().split("-")[0];
            
            ResultSet rs = st.executeQuery("SELECT * FROM Ciudad WHERE idCiudad = " + id);
            if (rs.next()) {
                txtNombre.setText(rs.getString("nombreCiudad"));
                txtPais.setText(rs.getString("paisCiudad"));
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btnModificar)) {
            if (choCiudades.getSelectedIndex() != 0) {
                actualizarCiudad();
            }
        }
    }

    private void actualizarCiudad() {
        try {
            Connection con = DriverManager.getConnection(url, loginBD, passwordBD);
            Statement st = con.createStatement();
            String id = choCiudades.getSelectedItem().split("-")[0];
            
            // SQL UPDATE para cambiar los datos
            String sql = "UPDATE Ciudad SET nombreCiudad = '" + txtNombre.getText() + 
                         "', paisCiudad = '" + txtPais.getText() + 
                         "' WHERE idCiudad = " + id;
            
            st.executeUpdate(sql);
            System.out.println("Ciudad actualizada.");
            ventana.dispose();
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void windowClosing(WindowEvent e) {
        ventana.dispose();
    }
}