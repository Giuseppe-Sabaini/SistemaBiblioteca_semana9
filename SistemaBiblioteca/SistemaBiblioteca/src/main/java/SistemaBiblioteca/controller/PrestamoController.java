package SistemaBiblioteca.controller;
/**
 * Controlador encargado de administrar la lógica de negocio para el registro y gestión de préstamos de libros
 * en el Sistema de Biblioteca, implementando operaciones asíncronas para evitar el bloqueo de la interfaz gráfica.
 *
 * @author Giuseppe Sabaini
 * @version 1.0
 */

import SistemaBiblioteca.dao.EstudianteDAO;
import SistemaBiblioteca.dao.ExcepcionStockInsuficiente;
import SistemaBiblioteca.dao.LibroDAO;
import SistemaBiblioteca.dao.PrestamoDAO;
import SistemaBiblioteca.model.Estudiante;
import SistemaBiblioteca.model.Libro;
import SistemaBiblioteca.util.Sesion;
import SistemaBiblioteca.view.PrestamosView;

import javax.swing.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class PrestamoController {
    private final PrestamosView view;
    private final EstudianteDAO estudianteDAO = new EstudianteDAO();
    private final LibroDAO libroDAO = new LibroDAO();
    private final PrestamoDAO prestamoDAO = new PrestamoDAO();


    public PrestamoController(PrestamosView view) {
        this.view = view;
        view.lblVence.setText(LocalDate.now().plusDays(prestamoDAO.DIAS_PRESTAMO).toString());

        if (Sesion.esBibliotecario()) {
            view.btnBuscarEstudiante.addActionListener(e -> buscarEstudiantes());
            view.txtBuscarEstudiante.addActionListener(e -> buscarEstudiantes());
        } else if (Sesion.getEstudiante() != null) {
            view.cargarEstudiantes(List.of(Sesion.getEstudiante()));   // el estudiante solo pide para sí mismo
        }
        view.btnBuscarLibro.addActionListener(e -> buscarLibros());
        view.txtBuscarLibro.addActionListener(e -> buscarLibros());
        view.cmbLibro.addActionListener(e -> actualizarStock());
        view.btnRegistrar.addActionListener(e -> registrarEnSegundoPlano());
        view.btnCancelar.addActionListener(e -> view.dispose());
        buscarLibros();
    }

    private void buscarEstudiantes() {
        try { view.cargarEstudiantes(estudianteDAO.buscar(view.txtBuscarEstudiante.getText().trim())); }
        catch (SQLException ex) { error(ex); }
    }

    private void buscarLibros() {
        try {
            view.cargarLibros(libroDAO.buscar(view.txtBuscarLibro.getText().trim()));
            actualizarStock();
        } catch (SQLException ex) { error(ex); }
    }

    private void actualizarStock() {
        Libro l = (Libro) view.cmbLibro.getSelectedItem();
        view.lblStock.setText(l == null ? "-" : String.valueOf(l.getStock()));
    }


     //Registrar préstamo + actualizar stock + dejar historial se hace en un HILO independiente,
     //para que la interfaz no se congele. Al terminar, el resultado vuelve al hilo de Swing (EDT).
    private void registrarEnSegundoPlano() {
        Estudiante est = (Estudiante) view.cmbEstudiante.getSelectedItem();
        Libro libro = (Libro) view.cmbLibro.getSelectedItem();
        if (est == null) { aviso(Sesion.esBibliotecario()
                ? "Busca y selecciona un estudiante."
                : "Tu usuario no tiene una ficha de estudiante asociada (mismo RUT). Habla con el bibliotecario."); return; }
        if (libro == null) { aviso("Busca y selecciona un libro."); return; }

        view.setProcesando(true);
        Thread hilo = new Thread(() -> {
            try {
                prestamoDAO.registrarPrestamo(est.getId(), libro.getId());
                SwingUtilities.invokeLater(() -> {
                    view.setProcesando(false);
                    JOptionPane.showMessageDialog(view, "Préstamo registrado. Vence el "
                            + LocalDate.now().plusDays(prestamoDAO.DIAS_PRESTAMO) + ".");
                    buscarLibros();   // refresca el stock mostrado
                });
            } catch (ExcepcionStockInsuficiente ex) {
                SwingUtilities.invokeLater(() -> { view.setProcesando(false); aviso(ex.getMessage()); buscarLibros(); });
            } catch (SQLException ex) {
                SwingUtilities.invokeLater(() -> { view.setProcesando(false); error(ex); });
            }
        }, "hilo-prestamo");
        hilo.start();
    }

    private void aviso(String m) { JOptionPane.showMessageDialog(view, m, "Aviso", JOptionPane.WARNING_MESSAGE); }
    private void error(SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(view, "Error de base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
