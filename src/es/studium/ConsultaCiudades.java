package es.studium;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ConsultaCiudades extends WindowAdapter {
    Frame ventana = new Frame("Consulta de Ciudades");
    TextArea txtLista = new TextArea(10, 40);

    public ConsultaCiudades() {
        ventana.setLayout(new FlowLayout());
        ventana.setSize(400, 300);
        ventana.addWindowListener(this);
        txtLista.setEditable(false);
        ventana.add(txtLista);
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/PracticaPR2Trimestre", "admin_trenes", "EstudiuM2026*");
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Ciudad");
            while (rs.next()) {
                txtLista.append(rs.getInt("idCiudad") + " - " + rs.getString("nombreCiudad") + " (" + rs.getString("paisCiudad") + ")\n");
            }
            con.close();
        } catch (Exception e) { e.printStackTrace(); }

        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);
    }

    @Override
    public void windowClosing(WindowEvent e) { ventana.dispose(); }
}