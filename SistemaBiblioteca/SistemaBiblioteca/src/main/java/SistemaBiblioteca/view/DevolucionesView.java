package SistemaBiblioteca.view;

import SistemaBiblioteca.model.Estudiante;
import SistemaBiblioteca.model.Prestamo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Vista gráfica basada en Java Swing para la gestión y registro de devoluciones de libros
 * dentro del Sistema de Biblioteca, permitiendo buscar estudiantes, visualizar sus préstamos activos
 * y registrar la devolución de los ejemplares.
 *
 * @author Giuseppe Sabaini
 * @version 1.0
 */
public class DevolucionesView extends JFrame {

    //Campo de texto para ingresar el nombre o RUT del estudiante a buscar.
    public final JTextField txtBuscarEstudiante = new JTextField(14);

    // Componente desplegable que contiene la lista de estudiantes seleccionables.
    public final JComboBox<Estudiante> cmbEstudiante = new JComboBox<>();

    // Botón para ejecutar la búsqueda de estudiantes.
    public final JButton btnBuscarEstudiante = new JButton("Buscar");

    // Botón para visualizar los préstamos activos del estudiante seleccionado.
    public final JButton btnVerActivos = new JButton("Ver préstamos activos");

    // Botón para registrar la devolución del libro seleccionado.
    public final JButton btnDevolver = new JButton("Registrar devolución");

    // Botón para cerrar la ventana actual.
    public final JButton btnCerrar = new JButton("Cerrar");

    // Modelo de datos para la tabla de préstamos.
    private final DefaultTableModel modelo = Ui.modelo("ID", "Libro", "Préstamo", "Vence", "Estado");

    // Componente de tabla que muestra la lista de préstamos.
    public final JTable tabla = Ui.tabla(modelo);

    /**
     * Constructor que inicializa y configura los componentes visuales de la ventana
     * de devoluciones, adaptando los controles según el rol del usuario (bibliotecario o estudiante).
     *
     * @param esBibliotecario @code true si el usuario actual tiene privilegios de bibliotecario;
     *                          @code false si es un estudiante.
     */
    public DevolucionesView(boolean esBibliotecario) {
        super("Registrar devolución");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel norte = new JPanel(new BorderLayout());
        if (esBibliotecario) {
            norte.add(Ui.botones(new JLabel("Estudiante (nombre o RUT)"), txtBuscarEstudiante, btnBuscarEstudiante), BorderLayout.NORTH);
        }
        norte.add(Ui.botones(new JLabel("Seleccionado"), cmbEstudiante, btnVerActivos), BorderLayout.SOUTH);

        add(norte, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(Ui.botones(btnDevolver, btnCerrar), BorderLayout.SOUTH);

        Ui.configurar(this, 760, 420);
    }

    /**
     * Carga una lista de estudiantes en el componente desplegable  JComboBox.
     *
     * @param lista La lista de objetos  Estudiante a mostrar.
     */
    public void cargarEstudiantes(List<Estudiante> lista) {
        cmbEstudiante.removeAllItems();
        for (Estudiante e : lista) {
            cmbEstudiante.addItem(e);
        }
    }

    /**
     * Carga y actualiza los registros de préstamos en la tabla de la interfaz gráfica.
     *
     * @param lista La lista de objetos  Prestamo a visualizar.
     */
    public void cargarPrestamos(List<Prestamo> lista) {
        modelo.setRowCount(0);
        for (Prestamo p : lista) {
            modelo.addRow(new Object[]{
                    p.getId(),
                    p.getLibroTitulo(),
                    p.getFechaPrestamo(),
                    p.getFechaDevolucion(),
                    p.estaAtrasado() ? "Atrasado" : "En plazo"
            });
        }
    }
}
