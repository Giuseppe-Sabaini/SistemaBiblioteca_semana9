package SistemaBiblioteca.controller;
/**
 * Controlador encargado de administrar la lógica de negocio para las devoluciones de libros
 * en el Sistema de Biblioteca, controlando el acceso y los flujos según el rol del usuario
 * (Bibliotecario o Estudiante).
 *
 * @author Giuseppe Sabaini
 * @version 1.0
 */

import SistemaBiblioteca.dao.EstudianteDAO;
import SistemaBiblioteca.dao.PrestamoDAO;
import SistemaBiblioteca.model.Estudiante;
import SistemaBiblioteca.model.Prestamo;
import SistemaBiblioteca.util.Sesion;
import SistemaBiblioteca.view.DevolucionesView;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DevolucionController {
    private final DevolucionesView view;
    private final EstudianteDAO estudianteDAO = new EstudianteDAO();
    private final PrestamoDAO prestamoDAO = new PrestamoDAO();
    private List<Prestamo> actuales = new ArrayList<>();

    public DevolucionController(DevolucionesView view) {
        this.view = view;
        if (Sesion.esBibliotecario()) {
            view.btnBuscarEstudiante.addActionListener(e -> buscarEstudiantes());
            view.txtBuscarEstudiante.addActionListener(e -> buscarEstudiantes());
        } else if (Sesion.getEstudiante() != null) {
            view.cargarEstudiantes(List.of(Sesion.getEstudiante()));
            verActivos();
        }
        view.btnVerActivos.addActionListener(e -> verActivos());
        view.btnDevolver.addActionListener(e -> devolver());
        view.btnCerrar.addActionListener(e -> view.dispose());
    }

    private void buscarEstudiantes() {
        try { view.cargarEstudiantes(estudianteDAO.buscar(view.txtBuscarEstudiante.getText().trim())); }
        catch (SQLException ex) { error(ex); }
    }

    private void verActivos() {
        Estudiante est = (Estudiante) view.cmbEstudiante.getSelectedItem();
        if (est == null) { aviso("Busca y selecciona un estudiante."); return; }
        try {
            actuales = prestamoDAO.listarActivosPorEstudiante(est.getId());
            view.cargarPrestamos(actuales);
        } catch (SQLException ex) { error(ex); }
    }

    private void devolver() {
        int fila = view.tabla.getSelectedRow();
        if (fila < 0 || fila >= actuales.size()) { aviso("Selecciona un préstamo de la tabla."); return; }
        try {
            boolean atrasado = prestamoDAO.registrarDevolucion(actuales.get(fila).getId());
            JOptionPane.showMessageDialog(view, atrasado
                    ? "Devolución registrada CON ATRASO."
                    : "Devolución registrada a tiempo.");
            verActivos();
        } catch (SQLException ex) { error(ex); }
    }

    private void aviso(String m) { JOptionPane.showMessageDialog(view, m, "Aviso", JOptionPane.WARNING_MESSAGE); }
    private void error(SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
