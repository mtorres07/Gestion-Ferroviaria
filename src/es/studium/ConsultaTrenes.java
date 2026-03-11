package es.studium;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ConsultaTrenes extends WindowAdapter {
    Frame ventana = new Frame("Consulta de Trenes");
    TextArea txtLista = new TextArea(10, 50);

    String driver = "com.mysql.cj.jdbc.Driver";
    String url = "jdbc:mysql://localhost:3306/PracticaPR2Trimestre";
    String loginBD = "admin_trenes";
    String passwordBD = "EstudiuM2026*";

    public ConsultaTrenes() {
        ventana.setLayout(new FlowLayout());
        ventana.setSize(500, 300);
        ventana.addWindowListener(this);
        txtLista.setEditable(false);
        ventana.add(txtLista);

        rellenarConsulta();

        ventana.setResizable(false);
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);
    }

    private void rellenarConsulta() {
        try {
            Class.forName(driver);
            Connection con = DriverManager.getConnection(url, loginBD, passwordBD);
            Statement st = con.createStatement();
            // JOIN para ver el nombre de la ciudad
            String sql = "SELECT t.idTren, t.modeloTren, t.capacidadTren, c.nombreCiudad " +
                         "FROM Tren t INNER JOIN Ciudad c ON t.idCiudadFK = c.idCiudad";
            ResultSet rs = st.executeQuery(sql);

            txtLista.setText("ID\tMODELO\tCAP.\tCIUDAD\n");
            txtLista.append("----------------------------------------------------------\n");
            while (rs.next()) {
                txtLista.append(rs.getInt("idTren") + "\t" + rs.getString("modeloTren") + 
                "\t" + rs.getInt("capacidadTren") + "\t" + rs.getString("nombreCiudad") + "\n");
            }
            con.close();
        } catch (Exception e) { e.printStackTrace(); }
    }

    @Override
    public void windowClosing(WindowEvent e) { ventana.dispose(); }
}