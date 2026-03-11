package es.studium;

import java.awt.*;
import java.awt.event.*;

public class MenuPrincipal extends WindowAdapter implements ActionListener {
    Frame ventana = new Frame("Menú Principal - Gestión de Trenes");
    MenuBar barraMenu = new MenuBar();

    Menu menuCiudades = new Menu("Ciudades");
    Menu menuTrenes = new Menu("Trenes");
    Menu menuEstaciones = new Menu("Estaciones");
    Menu menuSalir = new Menu("Salir");

    MenuItem mniAltaCiudad = new MenuItem("Alta");
    MenuItem mniBajaCiudad = new MenuItem("Baja");
    MenuItem mniModificacionCiudad = new MenuItem("Modificación");
    MenuItem mniConsultaCiudad = new MenuItem("Consulta");

    MenuItem mniAltaTren = new MenuItem("Alta");
    MenuItem mniBajaTren = new MenuItem("Baja");
    MenuItem mniModificacionTren = new MenuItem("Modificación");
    MenuItem mniConsultaTren = new MenuItem("Consulta");

    MenuItem mniAltaEstacion = new MenuItem("Alta");
    MenuItem mniBajaEstacion = new MenuItem("Baja");
    MenuItem mniModificacionEstacion = new MenuItem("Modificación");
    MenuItem mniConsultaEstacion = new MenuItem("Consulta");

    MenuItem mniCerrarSesion = new MenuItem("Cerrar Sesión");
    
    String perfilUsuario;

    public MenuPrincipal(String perfil) {
        this.perfilUsuario = perfil;

        ventana.setLayout(new FlowLayout());
        ventana.setSize(400, 300);
        ventana.setMenuBar(barraMenu);
        ventana.addWindowListener(this);

        menuCiudades.add(mniAltaCiudad); menuCiudades.add(mniBajaCiudad);
        menuCiudades.add(mniModificacionCiudad); menuCiudades.add(mniConsultaCiudad);
        menuTrenes.add(mniAltaTren); menuTrenes.add(mniBajaTren);
        menuTrenes.add(mniModificacionTren); menuTrenes.add(mniConsultaTren);
        menuEstaciones.add(mniAltaEstacion); menuEstaciones.add(mniBajaEstacion);
        menuEstaciones.add(mniModificacionEstacion); menuEstaciones.add(mniConsultaEstacion);
        menuSalir.add(mniCerrarSesion);

        barraMenu.add(menuCiudades); barraMenu.add(menuTrenes);
        barraMenu.add(menuEstaciones); barraMenu.add(menuSalir);

        // CONTROL DE PERFILES (Lógica corregida con startsWith)
        if (perfilUsuario != null && perfilUsuario.startsWith("Admin")) {
            ventana.setTitle("Menú Principal - ADMINISTRADOR");
        } else {
            ventana.setTitle("Menú Principal - CONSULTOR");
            mniAltaCiudad.setEnabled(false);
            mniBajaCiudad.setEnabled(false);
            mniModificacionCiudad.setEnabled(false);
            mniAltaTren.setEnabled(false);
            mniBajaTren.setEnabled(false);
            mniModificacionTren.setEnabled(false);
            mniAltaEstacion.setEnabled(false);
            mniBajaEstacion.setEnabled(false);
            mniModificacionEstacion.setEnabled(false);
        }

        mniAltaCiudad.addActionListener(this); mniBajaCiudad.addActionListener(this);
        mniModificacionCiudad.addActionListener(this); mniConsultaCiudad.addActionListener(this);
        mniAltaTren.addActionListener(this); mniBajaTren.addActionListener(this);
        mniModificacionTren.addActionListener(this); mniConsultaTren.addActionListener(this);
        mniAltaEstacion.addActionListener(this); mniBajaEstacion.addActionListener(this);
        mniModificacionEstacion.addActionListener(this); mniConsultaEstacion.addActionListener(this);
        mniCerrarSesion.addActionListener(this);

        ventana.setResizable(false);
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(mniAltaCiudad)) { new AltaCiudad(); }
        else if (e.getSource().equals(mniBajaCiudad)) { new BajaCiudad(); }
        else if (e.getSource().equals(mniModificacionCiudad)) { new ModificacionCiudad(); }
        else if (e.getSource().equals(mniConsultaCiudad)) { new ConsultaCiudades(); }
        else if (e.getSource().equals(mniAltaTren)) { new AltaTren(); }
        else if (e.getSource().equals(mniBajaTren)) { new BajaTren(); }
        else if (e.getSource().equals(mniModificacionTren)) { new ModificacionTren(); }
        else if (e.getSource().equals(mniConsultaTren)) { new ConsultaTrenes(); }
        else if (e.getSource().equals(mniAltaEstacion)) { new AltaEstacion(); }
        else if (e.getSource().equals(mniBajaEstacion)) { new BajaEstacion(); }
        else if (e.getSource().equals(mniModificacionEstacion)) { new ModificacionEstacion(); }
        else if (e.getSource().equals(mniConsultaEstacion)) { new ConsultaEstaciones(); }
        else if (e.getSource().equals(mniCerrarSesion)) { ventana.dispose(); new Login(); }
    }

    @Override
    public void windowClosing(WindowEvent e) { System.exit(0); }
}