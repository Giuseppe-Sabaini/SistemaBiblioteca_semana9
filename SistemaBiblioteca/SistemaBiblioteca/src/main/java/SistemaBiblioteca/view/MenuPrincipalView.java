package SistemaBiblioteca.view;

import SistemaBiblioteca.model.Usuario;

import javax.swing.*;
import java.awt.*;

/**
 * Vista gráfica basada en Java Swing que representa el menú principal del Sistema de Biblioteca,
 * adaptando y mostrando de forma dinámica las opciones y botones correspondientes
 * según el rol del usuario autenticado (Bibliotecario o Estudiante).
 *
 * @author Giuseppe Sabaini
 * @version 1.0
 */
public class MenuPrincipalView extends JFrame {

    // Botón para acceder al catálogo de libros (consulta o gestión según el rol).
    public final JButton btnLibros = new JButton();

    // Botón para acceder a la gestión integral de estudiantes (exclusivo para bibliotecarios).
    public final JButton btnEstudiantes = new JButton("Estudiantes");

    // Botón para gestionar o solicitar préstamos de libros.
    public final JButton btnPrestamos = new JButton();

    // Botón para registrar o realizar devoluciones de libros.
    public final JButton btnDevoluciones = new JButton();

    // Botón para acceder al módulo de reportes y estadísticas (exclusivo para bibliotecarios).
    public final JButton btnReportes = new JButton("Reportes");

    // Botón para cerrar la sesión actual y volver a la pantalla de autenticación.
    public final JButton btnSalir = new JButton("Cerrar sesión");

    /**
     * Constructor que inicializa y configura los componentes visuales del menú principal,
     * personalizando el título de la ventana y el texto de los botones según los privilegios
     * del usuario recibido.
     *
     * @param u El objeto Usuario que ha iniciado sesión actualmente en el sistema.
     */
    public MenuPrincipalView(Usuario u) {
        super("Menú principal - " + u.getNombre() + " (" + u.getRol() + ")");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        boolean biblio = u.esBibliotecario();
        btnLibros.setText(biblio ? "Libros" : "Consultar libros");
        btnPrestamos.setText(biblio ? "Registrar préstamo" : "Pedir préstamo");
        btnDevoluciones.setText(biblio ? "Devoluciones" : "Devolver libro");

        JPanel p = new JPanel(new GridLayout(0, 2, 12, 12));
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        p.add(btnLibros);
        if (biblio) {
            p.add(btnEstudiantes);
        }
        p.add(btnPrestamos);
        p.add(btnDevoluciones);
        if (biblio) {
            p.add(btnReportes);
        }
        p.add(btnSalir);

        add(p);
        Ui.configurar(this, 460, biblio ? 260 : 220);
    }
}
