package SistemaBiblioteca.app;
/**
 * Clase principal que sirve como punto de entrada para la aplicación
 * del Sistema de Gestión de Biblioteca.
 *
 * @author Giuseppe Sabaini
 * @version 1.0
 */

import SistemaBiblioteca.controller.LoginController;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginController::mostrar);
    }
}
