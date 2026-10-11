package SistemaBiblioteca.controller;
/**
 * Controlador encargado de gestionar el proceso de autenticación e inicio de sesión
 * de los usuarios en el Sistema de Biblioteca, validando credenciales y abriendo
 * el menú principal según el rol correspondiente.
 *
 * @author Giuseppe Sabaini
 * @version 1.0
 */

import SistemaBiblioteca.dao.EstudianteDAO;
import SistemaBiblioteca.dao.UsuarioDAO;
import SistemaBiblioteca.model.Estudiante;
import SistemaBiblioteca.model.Usuario;
import SistemaBiblioteca.util.Sesion;
import SistemaBiblioteca.view.LoginView;
import SistemaBiblioteca.view.MenuPrincipalView;

import java.sql.SQLException;

public class LoginController {
    private final LoginView view;
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final EstudianteDAO estudianteDAO = new EstudianteDAO();

    public LoginController(LoginView view) {
        this.view = view;
        view.btnIngresar.addActionListener(e -> ingresar());
        view.btnSalir.addActionListener(e -> System.exit(0));
    }

    public static void mostrar() {
        LoginView v = new LoginView();
        new LoginController(v);
        v.setVisible(true);
    }

    private void ingresar() {
        String login = view.txtUsuario.getText().trim();
        String clave = new String(view.txtClave.getPassword());
        if (login.isEmpty() || clave.isEmpty()) {
            view.lblError.setText("Ingresa tu RUT o correo y tu contraseña.");
            return;
        }
        try {
            Usuario u = usuarioDAO.autenticar(login, clave);
            if (u == null) {
                view.lblError.setText("Usuario o contraseña incorrectos.");
                return;
            }
            Estudiante est = u.esBibliotecario() ? null : estudianteDAO.obtenerPorRut(u.getRut());
            Sesion.iniciar(u, est);
            MenuPrincipalView menu = new MenuPrincipalView(u);
            new MenuController(menu);
            menu.setVisible(true);
            view.dispose();
        } catch (SQLException ex) {
            view.lblError.setText("No se pudo conectar a la base de datos.");
            ex.printStackTrace();
        }
    }
}
