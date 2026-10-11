package SistemaBiblioteca.util;

import SistemaBiblioteca.model.Estudiante;
import SistemaBiblioteca.model.Usuario;

/**
 * Utilidad que gestiona el estado global de la sesión activa en el Sistema de Biblioteca,
 * almacenando la información del usuario autenticado y, opcionalmente, la ficha de estudiante
 * vinculada en caso de que corresponda a dicho rol.
 *
 * @author Giuseppe Sabaini
 * @version 1.0
 */
public final class Sesion {
    private static Usuario usuario;
    private static Estudiante estudiante;

    private Sesion() { }

    public static void iniciar(Usuario u, Estudiante e) {
        usuario = u;
        estudiante = e;
    }

    public static void cerrar() {
        usuario = null;
        estudiante = null;
    }

    public static Usuario getUsuario() {
        return usuario;
    }

    public static Estudiante getEstudiante() {
        return estudiante;
    }

    public static boolean esBibliotecario() {
        return usuario != null && usuario.esBibliotecario();
    }
}