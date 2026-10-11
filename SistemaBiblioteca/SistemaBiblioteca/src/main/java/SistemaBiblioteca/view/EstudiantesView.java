package SistemaBiblioteca.view;

import SistemaBiblioteca.model.Estudiante;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Vista gráfica basada en Java Swing para la gestión integral de estudiantes dentro del Sistema de Biblioteca,
 * permitiendo registrar nuevos alumnos, modificar sus datos, eliminarlos, consultar su historial
 * y realizar búsquedas por nombre o RUT.
 *
 * @author Giuseppe Sabaini
 * @version 1.0
 */
public class EstudiantesView extends JFrame {

    // Campo de texto para ingresar o editar el nombre completo del estudiante.
    public final JTextField txtNombre = new JTextField();

    // Campo de texto para ingresar o editar el número de RUT del estudiante.
    public final JTextField txtRut = new JTextField();

    // Campo de texto para ingresar o editar el curso o nivel académico del estudiante.
    public final JTextField txtCurso = new JTextField();

    // Campo de texto para ingresar o editar el correo electrónico de contacto.
    public final JTextField txtCorreo = new JTextField();

    // Campo de texto para buscar estudiantes mediante nombre o RUT.
    public final JTextField txtBuscar = new JTextField(18);

    // Campo seguro de contraseña para la cuenta de acceso del estudiante.
    public final JPasswordField txtClave = new JPasswordField();

    // Botón para registrar un nuevo estudiante en el sistema.
    public final JButton btnRegistrar = new JButton("Registrar");

    // Botón para modificar los datos del estudiante seleccionado.
    public final JButton btnModificar = new JButton("Modificar");

    // Botón para eliminar al estudiante seleccionado.
    public final JButton btnEliminar = new JButton("Eliminar");

    // Botón para ver el historial de préstamos del estudiante seleccionado.
    public final JButton btnHistorial = new JButton("Ver historial");

    // Botón para limpiar los campos del formulario y la selección de la tabla.
    public final JButton btnLimpiar = new JButton("Limpiar");

    // Botón para ejecutar la búsqueda de estudiantes.
    public final JButton btnBuscar = new JButton("Buscar");

    // Modelo de datos para la tabla de estudiantes.
    private final DefaultTableModel modelo = Ui.modelo("ID", "Nombre", "RUT", "Curso", "Correo");

    // Componente de tabla que muestra la lista de estudiantes registrados. */
    public final JTable tabla = Ui.tabla(modelo);

    /**
     * Constructor que inicializa y configura los componentes visuales de la ventana
     * de gestión de estudiantes, organizando el formulario de datos, los botones de acción
     * y la tabla principal.
     */
    public EstudiantesView() {
        super("Gestión de estudiantes");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel f = Ui.formulario();
        f.add(new JLabel("Nombre"));
        f.add(txtNombre);
        f.add(new JLabel("RUT"));
        f.add(txtRut);
        f.add(new JLabel("Curso"));
        f.add(txtCurso);
        f.add(new JLabel("Correo"));
        f.add(txtCorreo);
        f.add(new JLabel("Contraseña (de acceso)"));
        f.add(txtClave);
        f.add(new JLabel("(vacía al modificar = no cambia)"));
        f.add(new JLabel());

        JPanel norte = new JPanel(new BorderLayout());
        norte.add(f, BorderLayout.NORTH);
        norte.add(Ui.botones(btnRegistrar, btnModificar, btnEliminar, btnHistorial, btnLimpiar), BorderLayout.CENTER);
        norte.add(Ui.botones(new JLabel("Buscar (nombre o RUT)"), txtBuscar, btnBuscar), BorderLayout.SOUTH);

        add(norte, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        Ui.configurar(this, 780, 540);
    }

    /**
     * Carga y actualiza los registros de estudiantes en la tabla de la interfaz gráfica.
     *
     * @param lista La lista de objetos  Estudiante a visualizar.
     */
    public void cargar(List<Estudiante> lista) {
        modelo.setRowCount(0);
        for (Estudiante e : lista) {
            modelo.addRow(new Object[]{
                    e.getId(),
                    e.getNombre(),
                    e.getRut(),
                    e.getCurso(),
                    e.getCorreo()
            });
        }
    }

    /**
     * Limpia todos los campos del formulario de entrada, la contraseña
     * y restablece la selección actual en la tabla.
     */
    public void limpiarFormulario() {
        txtNombre.setText("");
        txtRut.setText("");
        txtCurso.setText("");
        txtCorreo.setText("");
        txtClave.setText("");
        tabla.clearSelection();
    }
}
