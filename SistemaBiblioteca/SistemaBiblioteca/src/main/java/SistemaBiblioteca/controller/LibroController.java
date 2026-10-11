package SistemaBiblioteca.controller;
/**
 * Controlador que gestiona la logica de negocio para la administracion del catálogo de libros
 * alta, baja, modificación, consulta y control de stock adaptándose al modo de solo lectura o administracion.
 *
 * @author Giuseppe Sabaini
 * @version 1.0
 */

import SistemaBiblioteca.dao.CategoriaDAO;
import SistemaBiblioteca.dao.LibroDAO;
import SistemaBiblioteca.model.Categoria;
import SistemaBiblioteca.model.Libro;
import SistemaBiblioteca.view.LibrosView;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LibroController {
    private final LibrosView view;
    private final LibroDAO libroDAO = new LibroDAO();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();
    private List<Libro> actuales = new ArrayList<>();

    public LibroController(LibrosView view, boolean soloLectura) {
        this.view = view;
        view.btnBuscar.addActionListener(e -> refrescar());
        view.txtBuscar.addActionListener(e -> refrescar());
        if (!soloLectura) {
            view.btnGuardar.addActionListener(e -> guardar());
            view.btnModificar.addActionListener(e -> modificar());
            view.btnEliminar.addActionListener(e -> eliminar());
            view.btnLimpiar.addActionListener(e -> view.limpiarFormulario());
            view.tabla.getSelectionModel().addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting()) llenarFormulario();
            });
            try { view.cargarCategorias(categoriaDAO.listar()); } catch (SQLException ex) { error(ex); }
        }
        refrescar();
    }

    private void refrescar() {
        try {
            actuales = libroDAO.buscar(view.txtBuscar.getText().trim());
            view.cargarLibros(actuales);
        } catch (SQLException ex) { error(ex); }
    }

    private void llenarFormulario() {
        int fila = view.tabla.getSelectedRow();
        if (fila < 0 || fila >= actuales.size()) return;
        Libro l = actuales.get(fila);
        view.txtTitulo.setText(l.getTitulo());
        view.txtAutor.setText(l.getAutor());
        view.txtEditorial.setText(l.getEditorial());
        view.txtIsbn.setText(l.getIsbn());
        view.txtStock.setText(String.valueOf(l.getStock()));
        for (int i = 0; i < view.cmbCategoria.getItemCount(); i++) {
            if (view.cmbCategoria.getItemAt(i).getId() == l.getIdCategoria()) view.cmbCategoria.setSelectedIndex(i);
        }
    }

    //Lee y valida el formulario devuelve null y avisa si hay datos inválidos.
    private Libro leerFormulario() {
        String titulo = view.txtTitulo.getText().trim(), autor = view.txtAutor.getText().trim(),
                isbn = view.txtIsbn.getText().trim(), editorial = view.txtEditorial.getText().trim();
        Categoria cat = (Categoria) view.cmbCategoria.getSelectedItem();
        if (titulo.isEmpty() || autor.isEmpty() || isbn.isEmpty() || cat == null) {
            aviso("Título, autor, ISBN y categoría son obligatorios.");
            return null;
        }
        int stock;
        try {
            stock = Integer.parseInt(view.txtStock.getText().trim());
            if (stock < 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            aviso("El stock debe ser un número entero mayor o igual a 0.");
            return null;
        }
        return new Libro(0, titulo, autor, isbn, editorial, stock, cat.getId());
    }

    private void guardar() {
        Libro l = leerFormulario();
        if (l == null) return;
        try {
            libroDAO.crear(l);
            view.limpiarFormulario();
            refrescar();
        } catch (SQLException ex) { error(ex); }
    }

    private void modificar() {
        int fila = view.tabla.getSelectedRow();
        if (fila < 0) { aviso("Selecciona un libro de la tabla."); return; }
        Libro l = leerFormulario();
        if (l == null) return;
        l.setId(actuales.get(fila).getId());
        try {
            libroDAO.actualizar(l);
            refrescar();
        } catch (SQLException ex) { error(ex); }
    }

    private void eliminar() {
        int fila = view.tabla.getSelectedRow();
        if (fila < 0) { aviso("Selecciona un libro de la tabla."); return; }
        if (JOptionPane.showConfirmDialog(view, "¿Eliminar el libro seleccionado?", "Confirmar",
                JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        try {
            libroDAO.eliminar(actuales.get(fila).getId());
            view.limpiarFormulario();
            refrescar();
        } catch (SQLException ex) {
            aviso("No se pudo eliminar. Si el libro tiene préstamos registrados no se puede borrar.");
        }
    }

    private void aviso(String m) { JOptionPane.showMessageDialog(view, m, "Aviso", JOptionPane.WARNING_MESSAGE); }
    private void error(SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(view, "Error de base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
