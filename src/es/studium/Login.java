package es.studium;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Login extends WindowAdapter implements ActionListener {
    Frame ventana = new Frame("Acceso al Sistema");
    Label lblUsuario = new Label("Usuario:");
    TextField txtUsuario = new TextField(20);
    Label lblClave = new Label("Contraseña:");
    TextField txtClave = new TextField(20);
    Button btnConectar = new Button("Conectar");

    String driver = "com.mysql.cj.jdbc.Driver";
    String url = "jdbc:mysql://localhost:3306/PracticaPR2Trimestre";
    String loginBD = "admin_trenes";
    String passwordBD = "EstudiuM2026*";

    public Login() {
        ventana.setLayout(new FlowLayout());
        ventana.setSize(230, 180);
        ventana.addWindowListener(this);
        txtClave.setEchoChar('*');

        ventana.add(lblUsuario);
        ventana.add(txtUsuario);
        ventana.add(lblClave);
        ventana.add(txtClave);
        ventana.add(btnConectar);

        btnConectar.addActionListener(this);
        ventana.setResizable(false);
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btnConectar)) {
            comprobarCredenciales();
        }
    }

    private void comprobarCredenciales() {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, loginBD, passwordBD);
            statement = connection.createStatement();
            String sql = "SELECT perfilUsuario FROM Usuario WHERE nombreUsuario = '" 
                         + txtUsuario.getText() + "' AND claveUsuario = '" 
                         + txtClave.getText() + "'";
            rs = statement.executeQuery(sql);

            if (rs.next()) {
                String perfilEncontrado = rs.getString("perfilUsuario");
                
                // Imprime en consola para ver si el dato llega bien
                System.out.println("Perfil detectado: [" + perfilEncontrado + "]"); 
                
                ventana.dispose();
                new MenuPrincipal(perfilEncontrado);
            } else {
                System.out.println("Error: Usuario o clave incorrectos.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try { if (connection != null) connection.close(); } catch (SQLException se) {}
        }
    }

    @Override
    public void windowClosing(WindowEvent e) {
        System.exit(0);
    }

    public static void main(String[] args) {
        new Login();
    }
}