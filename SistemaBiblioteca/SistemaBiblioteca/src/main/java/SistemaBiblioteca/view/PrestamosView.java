package SistemaBiblioteca.view;

import SistemaBiblioteca.model.Estudiante;
import SistemaBiblioteca.model.Libro;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Vista gráfica basada en Java Swing para el registro de nuevos préstamos de libros
 * dentro del Sistema de Biblioteca, permitiendo seleccionar al estudiante, buscar libros por
 * múltiples criterios, verificar stock, fecha de vencimiento y gestionar procesos en segundo plano.
 *
 * @author Giuseppe Sabaini
 * @version 1.0
 */
public class PrestamosView extends JFrame {

    // Campo de texto para buscar estudiantes mediante nombre o RUT (visible solo para bibliotecarios).
    public final JTextField txtBuscarEstudiante = new JTextField(14);

    // Campo de texto para buscar libros mediante título, autor o ISBN.
    public final JTextField txtBuscarLibro = new JTextField(14);

    // Botón para ejecutar la búsqueda de estudiantes.
    public final JButton btnBuscarEstudiante = new JButton("Buscar");

    // Botón para ejecutar la búsqueda de libros.
    public final JButton btnBuscarLibro = new JButton("Buscar");

    // Botón para registrar el préstamo del libro seleccionado.
    public final JButton btnRegistrar = new JButton("Registrar préstamo");

    // Botón para cerrar o cancelar la ventana actual.
    public final JButton btnCancelar = new JButton("Cerrar");

    // Componente desplegable que contiene la lista de estudiantes seleccionables.
    public final JComboBox<Estudiante> cmbEstudiante = new JComboBox<>();

    // Componente desplegable que contiene la lista de libros seleccionables del catálogo.
    public final JComboBox<Libro> cmbLibro = new JComboBox<>();

    // Etiqueta para mostrar el stock disponible del libro seleccionado.
    public final JLabel lblStock = new JLabel("-");

    // Etiqueta para mostrar la fecha estimada de vencimiento del préstamo.
    public final JLabel lblVence = new JLabel("-");

    // Etiqueta de estado para mostrar mensajes informativos o de carga.
    public final JLabel lblEstado = new JLabel(" ");

    // Barra de progreso indeterminada utilizada durante operaciones asíncronas de registro.
    public final JProgressBar barra = new JProgressBar();

    /**
     * Constructor que inicializa y configura los componentes visuales de la ventana
     * de préstamos, adaptando los campos de búsqueda según el rol del usuario autenticado.
     *
     * @param esBibliotecario  true si el usuario actual posee privilegios de bibliotecario;
     *                           false si es un estudiante.
     */
    public PrestamosView(boolean esBibliotecario) {
        super("Registrar préstamo");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel f = new JPanel(new GridLayout(0, 3, 8, 8));
        f.setBorder(BorderFactory.createEmptyBorder(12, 12, 4, 12));

        f.add(new JLabel("Estudiante"));
        f.add(esBibliotecario ? txtBuscarEstudiante : new JLabel());
        f.add(esBibliotecario ? btnBuscarEstudiante : new JLabel());

        f.add(new JLabel("Seleccionado"));
        f.add(cmbEstudiante);
        f.add(new JLabel());

        f.add(new JLabel("Libro (título, autor o ISBN)"));
        f.add(txtBuscarLibro);
        f.add(btnBuscarLibro);

        f.add(new JLabel("Seleccionado"));
        f.add(cmbLibro);
        f.add(new JLabel());

        f.add(new JLabel("Stock disponible"));
        f.add(lblStock);
        f.add(new JLabel());

        f.add(new JLabel("Fecha de vencimiento"));
        f.add(lblVence);
        f.add(new JLabel());

        barra.setIndeterminate(true);
        barra.setVisible(false);

        JPanel sur = new JPanel(new BorderLayout());
        sur.add(Ui.botones(btnRegistrar, btnCancelar), BorderLayout.NORTH);
        sur.add(lblEstado, BorderLayout.CENTER);
        sur.add(barra, BorderLayout.SOUTH);

        add(f, BorderLayout.NORTH);
        add(sur, BorderLayout.CENTER);

        Ui.configurar(this, 720, 360);
    }

    /**
     * Carga una lista de estudiantes en el componente desplegable  JComboBox
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
     * Carga una lista de libros en el componente desplegable JComboBox.
     *
     * @param lista La lista de objetos  Libro a mostrar.
     */
    public void cargarLibros(List<Libro> lista) {
        cmbLibro.removeAllItems();
        for (Libro l : lista) {
            cmbLibro.addItem(l);
        }
    }

    /**
     * Muestra u oculta el indicador visual de procesamiento ("Procesando...")
     * mientras se ejecuta una tarea asíncrona en segundo plano.
     *
     * @param procesando true para activar el estado de carga y deshabilitar controles;
     *                    false para restaurar la interfaz a su estado normal.
     */
    public void setProcesando(boolean procesando) {
        barra.setVisible(procesando);
        btnRegistrar.setEnabled(!procesando);
        lblEstado.setText(procesando ? "Procesando..." : " ");
    }
}
