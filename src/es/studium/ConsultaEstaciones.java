package es.studium;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ConsultaEstaciones extends WindowAdapter {
    Frame ventana = new Frame("Consulta de Estaciones");
    // Usamos un TextArea para mostrar los resultados tipo "listado"
    TextArea txtLista = new TextArea(15, 70);

    String driver = "com.mysql.cj.jdbc.Driver";
    String url = "jdbc:mysql://localhost:3306/PracticaPR2Trimestre";
    String loginBD = "admin_trenes";
    String passwordBD = "EstudiuM2026*";

    public ConsultaEstaciones() {
        ventana.setLayout(new FlowLayout());
        ventana.setSize(600, 350);
        ventana.addWindowListener(this);

        txtLista.setEditable(false); // Para que el usuario no pueda borrar el texto
        txtLista.setFont(new Font("Monospaced", Font.PLAIN, 12)); // Fuente fija para que las columnas cuadren
        ventana.add(txtLista);

        rellenarConsulta();

        ventana.setResizable(false);
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);
    }

    private void rellenarConsulta() {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;

        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, loginBD, passwordBD);
            statement = connection.createStatement();

            // SQL con TRIPLE JOIN: Estacion + Ciudad + Tren
            String sql = "SELECT e.nombreEstacion, e.precioTicketEstacion, c.nombreCiudad, t.modeloTren " +
                         "FROM Estacion e " +
                         "INNER JOIN Ciudad c ON e.idCiudadFK = c.idCiudad " +
                         "INNER JOIN Tren t ON e.idTrenFK = t.idTren";

            rs = statement.executeQuery(sql);

            // Cabecera
            txtLista.append("ESTACIÓN\t\tPRECIO\tCIUDAD\t\tTREN\n");
            txtLista.append("----------------------------------------------------------------------\n");

            while (rs.next()) {
                txtLista.append(
                    rs.getString("nombreEstacion") + "\t\t" +
                    rs.getBigDecimal("precioTicketEstacion") + "€\t" +
                    rs.getString("nombreCiudad") + "\t\t" +
                    rs.getString("modeloTren") + "\n"
                );
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            try { if (connection != null) connection.close(); } catch (SQLException se) {}
        }
    }

    @Override
    public void windowClosing(WindowEvent e) {
        ventana.dispose();
    }
}