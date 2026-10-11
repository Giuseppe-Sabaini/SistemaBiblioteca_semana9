package SistemaBiblioteca.view;

import SistemaBiblioteca.model.Estudiante;
import SistemaBiblioteca.model.ReporteResultado;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Vista gráfica basada en Java Swing para la generación y visualización de reportes estadísticos
 * e historiales dentro del Sistema de Biblioteca, permitiendo seleccionar diferentes tipos de reportes
 * (libros más prestados, historial por estudiante, libros en préstamo) y consultar sus resultados en formato tabular.
 *
 * @author Giuseppe Sabaini
 * @version 1.0
 */
public class ReportesView extends JFrame {

    // Constante que define el reporte estadístico de libros más solicitados en préstamo.
    public static final String R_MAS_PRESTADOS = "Libros más prestados";

    // Constante que define el reporte del historial de préstamos por alumno específico.
    public static final String R_HISTORIAL = "Historial por estudiante";

    // Constante que define el reporte de libros que se encuentran actualmente en préstamo activo.
    public static final String R_EN_PRESTAMO = "Libros actualmente en préstamo";

    // Componente desplegable para seleccionar el tipo de reporte a generar.
    public final JComboBox<String> cmbReporte = new JComboBox<>(new String[]{R_MAS_PRESTADOS, R_HISTORIAL, R_EN_PRESTAMO});

    // Componente desplegable para seleccionar el estudiante objetivo en los reportes correspondientes.
    public final JComboBox<Estudiante> cmbEstudiante = new JComboBox<>();

    // Botón para ejecutar la generación y visualización del reporte seleccionado.
    public final JButton btnGenerar = new JButton("Generar");

    // Modelo de datos dinámico para la tabla de resultados del reporte.
    private final DefaultTableModel modelo = Ui.modelo();

    // Componente de tabla que muestra los datos tabulares del reporte.
    public final JTable tabla = Ui.tabla(modelo);

    /**
     * Constructor que inicializa y configura los componentes visuales de la ventana
     * de reportes, organizando los selectores de tipo de reporte, el combo de estudiantes
     * y la tabla de visualización de datos.
     */
    public ReportesView() {
        super("Reportes");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        add(Ui.botones(new JLabel("Reporte"), cmbReporte, new JLabel("Estudiante"), cmbEstudiante, btnGenerar), BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        Ui.configurar(this, 860, 420);
    }

    /**
     * Carga una lista de estudiantes en el componente desplegable ({@link JComboBox}).
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
     * Actualiza y despliega los resultados del reporte generado en la tabla de la interfaz gráfica,
     * configurando dinámicamente las cabeceras de las columnas y las filas de datos.
     *
     * @param r El objeto ReporteResultado que contiene las columnas y filas obtenidas de la base de datos.
     */
    public void mostrar(ReporteResultado r) {
        modelo.setDataVector(new Object[0][0], r.getColumnas());
        for (Object[] fila : r.getFilas()) {
            modelo.addRow(fila);
        }
    }
}
