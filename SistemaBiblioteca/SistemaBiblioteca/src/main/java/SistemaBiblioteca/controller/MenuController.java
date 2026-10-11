package SistemaBiblioteca.controller;
/**
 * Controlador encargado de gestionar la navegación y las acciones del menú principal
 * del Sistema de Biblioteca, adaptando las opciones disponibles según el rol del usuario en sesión.
 *
 * @author Giuseppe Sabaini
 * @version 1.0
 */

import SistemaBiblioteca.util.Sesion;
import SistemaBiblioteca.view.*;


public class MenuController {
    private final MenuPrincipalView view;

    public MenuController(MenuPrincipalView view) {
        this.view = view;
        boolean biblio = Sesion.esBibliotecario();

        view.btnLibros.addActionListener(e -> {
            LibrosView v = new LibrosView(!biblio);
            new LibroController(v, !biblio);
            v.setVisible(true);
        });
        view.btnEstudiantes.addActionListener(e -> {
            EstudiantesView v = new EstudiantesView();
            new EstudianteController(v);
            v.setVisible(true);
        });
        view.btnPrestamos.addActionListener(e -> {
            PrestamosView v = new PrestamosView(biblio);
            new PrestamoController(v);
            v.setVisible(true);
        });
        view.btnDevoluciones.addActionListener(e -> {
            DevolucionesView v = new DevolucionesView(biblio);
            new DevolucionController(v);
            v.setVisible(true);
        });
        view.btnReportes.addActionListener(e -> abrirReportes().setVisible(true));
        view.btnSalir.addActionListener(e -> {
            Sesion.cerrar();
            view.dispose();
            LoginController.mostrar();
        });
    }

    public static ReportesView abrirReportes() {
        ReportesView v = new ReportesView();
        new ReporteController(v);
        return v;
    }
}
