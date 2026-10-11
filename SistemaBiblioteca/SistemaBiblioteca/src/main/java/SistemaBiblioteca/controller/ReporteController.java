package SistemaBiblioteca.controller;
/**
 * Controlador encargado de administrar la lógica de negocio para la generación y visualización
 * de reportes estadísticos e historiales de préstamos dentro del Sistema de Biblioteca.
 *
 * @author Giuseppe Sabaini
 * @version 1.0
 */

import SistemaBiblioteca.dao.EstudianteDAO;
import SistemaBiblioteca.dao.PrestamoDAO;
import SistemaBiblioteca.model.Estudiante;
import SistemaBiblioteca.model.ReporteResultado;
import SistemaBiblioteca.view.ReportesView;

import javax.swing.*;
import java.sql.SQLException;

public class ReporteController {
    private final ReportesView view;
    private final PrestamoDAO prestamoDAO = new PrestamoDAO();

    public ReporteController(ReportesView view) {
        this.view = view;
        try { view.cargarEstudiantes(new EstudianteDAO().listar()); } catch (SQLException ex) { error(ex); }
        view.cmbReporte.addActionListener(e ->
                view.cmbEstudiante.setEnabled(ReportesView.R_HISTORIAL.equals(view.cmbReporte.getSelectedItem())));
        view.cmbEstudiante.setEnabled(false);
        view.btnGenerar.addActionListener(e -> generar());
    }

    private void generar() {
        try {
            String tipo = (String) view.cmbReporte.getSelectedItem();
            ReporteResultado r;
            if (ReportesView.R_HISTORIAL.equals(tipo)) {
                Estudiante est = (Estudiante) view.cmbEstudiante.getSelectedItem();
                if (est == null) { JOptionPane.showMessageDialog(view, "Selecciona un estudiante."); return; }
                r = prestamoDAO.historialPorEstudiante(est.getId());
            } else if (ReportesView.R_EN_PRESTAMO.equals(tipo)) {
                r = prestamoDAO.librosEnPrestamo();
            } else {
                r = prestamoDAO.librosMasPrestados();
            }
            view.mostrar(r);
        } catch (SQLException ex) { error(ex); }
    }

    private void error(SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
