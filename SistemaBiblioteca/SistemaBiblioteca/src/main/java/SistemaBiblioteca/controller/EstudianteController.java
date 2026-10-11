package SistemaBiblioteca.controller;

/**
 * Controlador que gestiona la lógica de negocio para la administración de estudiantes
 * en la biblioteca (registro, modificación, eliminación, consulta y vinculación con usuarios).
 *
 * @author Giuseppe Sabaini
 * @version 1.0
 */

import SistemaBiblioteca.dao.EstudianteDAO;
import SistemaBiblioteca.model.Estudiante;
import SistemaBiblioteca.view.EstudiantesView;
import SistemaBiblioteca.view.ReportesView;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EstudianteController {
    private final EstudiantesView view;
    private final EstudianteDAO dao = new EstudianteDAO();
    private List<Estudiante> actuales = new ArrayList<>();

    public EstudianteController(EstudiantesView view) {
        this.view = view;
        view.btnBuscar.addActionListener(e -> refrescar());
        view.txtBuscar.addActionListener(e -> refrescar());
        view.btnRegistrar.addActionListener(e -> registrar());
        view.btnModificar.addActionListener(e -> modificar());
        view.btnEliminar.addActionListener(e -> eliminar());
        view.btnLimpiar.addActionListener(e -> view.limpiarFormulario());
        view.btnHistorial.addActionListener(e -> verHistorial());
        view.tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) llenarFormulario();
        });
        refrescar();
    }

    private void refrescar() {
        try {
            actuales = dao.buscar(view.txtBuscar.getText().trim());
            view.cargar(actuales);
        } catch (SQLException ex) { error(ex); }
    }

    private Estudiante seleccionado() {
        int fila = view.tabla.getSelectedRow();
        return (fila >= 0 && fila < actuales.size()) ? actuales.get(fila) : null;
    }

    private void llenarFormulario() {
        Estudiante e = seleccionado();
        if (e == null) return;
        view.txtNombre.setText(e.getNombre());
        view.txtRut.setText(e.getRut());
        view.txtCurso.setText(e.getCurso());
        view.txtCorreo.setText(e.getCorreo());
        view.txtClave.setText("");
    }

    private Estudiante leerFormulario() {
        String nombre = view.txtNombre.getText().trim(), rut = view.txtRut.getText().trim(),
                curso = view.txtCurso.getText().trim(), correo = view.txtCorreo.getText().trim();
        if (nombre.isEmpty() || rut.isEmpty() || correo.isEmpty()) {
            aviso("Nombre, RUT y correo son obligatorios.");
            return null;
        }
        return new Estudiante(0, nombre, rut, curso, correo);
    }

    private void registrar() {
        Estudiante e = leerFormulario();
        if (e == null) return;
        String clave = new String(view.txtClave.getPassword());
        if (clave.isEmpty()) { aviso("Ingresa una contraseña para que el estudiante pueda iniciar sesión."); return; }
        try {
            dao.registrar(e, clave);
            view.limpiarFormulario();
            refrescar();
        } catch (SQLException ex) {
            aviso("No se pudo registrar. ¿El RUT ya existe?\n" + ex.getMessage());
        }
    }

    private void modificar() {
        Estudiante sel = seleccionado();
        if (sel == null) { aviso("Selecciona un estudiante de la tabla."); return; }
        Estudiante e = leerFormulario();
        if (e == null) return;
        e.setId(sel.getId());
        try {
            dao.actualizarConUsuario(e, sel.getRut(), new String(view.txtClave.getPassword()));
            refrescar();
        } catch (SQLException ex) { error(ex); }
    }

    private void eliminar() {
        Estudiante sel = seleccionado();
        if (sel == null) { aviso("Selecciona un estudiante de la tabla."); return; }
        if (JOptionPane.showConfirmDialog(view, "¿Eliminar a " + sel.getNombre() + "?", "Confirmar",
                JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        try {
            dao.eliminarConUsuario(sel);
            view.limpiarFormulario();
            refrescar();
        } catch (SQLException ex) {
            aviso("No se pudo eliminar. Si el estudiante tiene préstamos registrados no se puede borrar.");
        }
    }

    private void verHistorial() {
        Estudiante sel = seleccionado();
        if (sel == null) { aviso("Selecciona un estudiante de la tabla."); return; }
        ReportesView rv = MenuController.abrirReportes();
        rv.cmbReporte.setSelectedItem(ReportesView.R_HISTORIAL);
        for (int i = 0; i < rv.cmbEstudiante.getItemCount(); i++) {
            if (rv.cmbEstudiante.getItemAt(i).getId() == sel.getId()) rv.cmbEstudiante.setSelectedIndex(i);
        }
        rv.btnGenerar.doClick();
        rv.setVisible(true);
    }

    private void aviso(String m) { JOptionPane.showMessageDialog(view, m, "Aviso", JOptionPane.WARNING_MESSAGE); }
    private void error(SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(view, "Error de base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
