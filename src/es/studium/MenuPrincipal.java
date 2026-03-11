package es.studium;

import java.awt.*;
import java.awt.event.*;

public class MenuPrincipal implements ActionListener {
    // Declaración de componentes
    Frame ventana = new Frame("Menú Principal - Gestión Ferroviaria");
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

    public MenuPrincipal(String perfil) {
        ventana.setLayout(new FlowLayout());
        ventana.setSize(450, 350);
        ventana.setMenuBar(barraMenu);

        // Montar Menús
        menuCiudades.add(mniAltaCiudad);
        menuCiudades.add(mniBajaCiudad);
        menuCiudades.add(mniModificacionCiudad);
        menuCiudades.add(mniConsultaCiudad);

        menuTrenes.add(mniAltaTren);
        menuTrenes.add(mniBajaTren);
        menuTrenes.add(mniModificacionTren);
        menuTrenes.add(mniConsultaTren);

        menuEstaciones.add(mniAltaEstacion);
        menuEstaciones.add(mniBajaEstacion);
        menuEstaciones.add(mniModificacionEstacion);
        menuEstaciones.add(mniConsultaEstacion);

        menuSalir.add(mniCerrarSesion);

        barraMenu.add(menuCiudades);
        barraMenu.add(menuTrenes);
        barraMenu.add(menuEstaciones);
        barraMenu.add(menuSalir);

        // --- LÓGICA DE PERMISOS PARA EL USUARIO BÁSICO ---
        if (perfil.startsWith("Básico")) {
            // Permitimos Altas (no hacemos nada)
            // Desactivamos Bajas y Modificaciones
            mniBajaCiudad.setEnabled(false);
            mniBajaTren.setEnabled(false);
            mniBajaEstacion.setEnabled(false);
            mniModificacionCiudad.setEnabled(false);
            mniModificacionTren.setEnabled(false);
            mniModificacionEstacion.setEnabled(false);
        }

        // --- AÑADIR LOS LISTENERS DE LOS BOTONES ---
        mniAltaCiudad.addActionListener(this);
        mniBajaCiudad.addActionListener(this);
        mniModificacionCiudad.addActionListener(this);
        mniConsultaCiudad.addActionListener(this);

        mniAltaTren.addActionListener(this);
        mniBajaTren.addActionListener(this);
        mniModificacionTren.addActionListener(this);
        mniConsultaTren.addActionListener(this);

        mniAltaEstacion.addActionListener(this);
        mniBajaEstacion.addActionListener(this);
        mniModificacionEstacion.addActionListener(this);
        mniConsultaEstacion.addActionListener(this);

        mniCerrarSesion.addActionListener(this);

        // --- CÓDIGO PARA QUE LA VENTANA SE CIERRE CON LA X ---
        ventana.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        ventana.setResizable(false);
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // ACCIONES CIUDADES
        if (e.getSource().equals(mniAltaCiudad)) { new AltaCiudad(); }
        if (e.getSource().equals(mniBajaCiudad)) { new BajaCiudad(); }
        if (e.getSource().equals(mniModificacionCiudad)) { new ModificacionCiudad(); }
        if (e.getSource().equals(mniConsultaCiudad)) { new ConsultaCiudades(); }

        // ACCIONES TRENES
        if (e.getSource().equals(mniAltaTren)) { new AltaTren(); }
        if (e.getSource().equals(mniBajaTren)) { new BajaTren(); }
        if (e.getSource().equals(mniModificacionTren)) { new ModificacionTren(); }
        if (e.getSource().equals(mniConsultaTren)) { new ConsultaTrenes(); }

        // ACCIONES ESTACIONES
        if (e.getSource().equals(mniAltaEstacion)) { new AltaEstacion(); }
        if (e.getSource().equals(mniBajaEstacion)) { new BajaEstacion(); }
        if (e.getSource().equals(mniModificacionEstacion)) { new ModificacionEstacion(); }
        if (e.getSource().equals(mniConsultaEstacion)) { new ConsultaEstaciones(); }

        // SALIR
        if (e.getSource().equals(mniCerrarSesion)) {
            ventana.setVisible(false);
            new Login();
        }
    }
}
