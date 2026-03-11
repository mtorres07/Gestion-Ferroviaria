package es.studium;
import java.sql.*;

public class Conexion {
    String driver = "com.mysql.cj.jdbc.Driver";
    String url = "jdbc:mysql://localhost:3306/PracticaPR2Trimestre";
    String login = "admin_trenes";
    String password = "EstudiuM2026*";

    public Connection conectar() {
        Connection con = null;
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url, login, password);
        } catch (Exception e) {
            System.out.println("Error al conectar: " + e.getMessage());
        }
        return con;
    }
}