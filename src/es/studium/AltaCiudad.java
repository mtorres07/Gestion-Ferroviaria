package es.studium;

import java.awt.*;
import java.awt.event.*;

public class AltaCiudad extends WindowAdapter {
    Frame ventana = new Frame("Alta de Ciudad");

    public AltaCiudad() {
        ventana.setLayout(new FlowLayout());
        ventana.add(new Label("Pantalla de Alta Ciudad"));
        ventana.setSize(250, 150);
        ventana.addWindowListener(this);
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);
    }

    @Override
    public void windowClosing(WindowEvent e) {
        ventana.dispose();
    }
}