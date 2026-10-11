package SistemaBiblioteca.view;

import SistemaBiblioteca.model.Categoria;
import SistemaBiblioteca.model.Libro;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Vista gráfica basada en Java Swing para la gestión y consulta del catálogo de libros
 * dentro del Sistema de Biblioteca, adaptando sus controles e interfaz según el rol
 * del usuario (modo de solo lectura para estudiantes o gestión completa para bibliotecarios).
 *
 * @author Giuseppe Sabaini
 * @version 1.0
 */
public class LibrosView extends JFrame {

    // Campo de texto para ingresar o editar el título del libro.
    public final JTextField txtTitulo = new JTextField();

    // Campo de texto para ingresar o editar el autor del libro.
    public final JTextField txtAutor = new JTextField();

    // Campo de texto para ingresar o editar la editorial del libro.
    public final JTextField txtEditorial = new JTextField();

    // Campo de texto para ingresar o editar el código ISBN del libro.
    public final JTextField txtIsbn = new JTextField();

    // Campo de texto para ingresar o editar la cantidad de stock disponible.
    public final JTextField txtStock = new JTextField("0");

    // Campo de texto para buscar libros mediante título, autor o ISBN.
    public final JTextField txtBuscar = new JTextField(18);

    // Componente desplegable que contiene la lista de categorías seleccionables.
    public final JComboBox<Categoria> cmbCategoria = new JComboBox<>();

    // Botón para guardar o registrar un nuevo libro en el sistema.
    public final JButton btnGuardar = new JButton("Guardar");

    // Botón para modificar los datos del libro seleccionado.
    public final JButton btnModificar = new JButton("Modificar");

    // Botón para eliminar el libro seleccionado del catálogo.
    public final JButton btnEliminar = new JButton("Eliminar");

    // Botón para limpiar los campos del formulario y la selección de la tabla.
    public final JButton btnLimpiar = new JButton("Limpiar");

    // Botón para ejecutar la búsqueda de libros.
    public final JButton btnBuscar = new JButton("Buscar");

    // Modelo de datos para la tabla de libros.
    private final DefaultTableModel modelo = Ui.modelo("ID", "Título", "Autor", "Categoría", "ISBN", "Editorial", "Stock");

    // Componente de tabla que muestra el catálogo de libros.
    public final JTable tabla = Ui.tabla(modelo);

    /**
     * Constructor que inicializa y configura los componentes visuales de la ventana
     * de libros, habilitando o restringiendo las opciones de edición según si se encuentra
     * en modo de solo lectura (estudiante) o gestión administrativa (bibliotecario).
     *
     * @param soloLectura  true si el usuario es un estudiante (solo consulta);
     *                       falsesi es un bibliotecario acceso completo.
     */
    public LibrosView(boolean soloLectura) {
        super(soloLectura ? "Consultar libros" : "Gestión de libros");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel norte = new JPanel(new BorderLayout());
        if (!soloLectura) {
            JPanel f = Ui.formulario();
            f.add(new JLabel("Título"));
            f.add(txtTitulo);
            f.add(new JLabel("Autor"));
            f.add(txtAutor);
            f.add(new JLabel("Editorial"));
            f.add(txtEditorial);
            f.add(new JLabel("ISBN"));
            f.add(txtIsbn);
            f.add(new JLabel("Categoría"));
            f.add(cmbCategoria);
            f.add(new JLabel("Stock"));
            f.add(txtStock);

            norte.add(f, BorderLayout.NORTH);
            norte.add(Ui.botones(btnGuardar, btnModificar, btnEliminar, btnLimpiar), BorderLayout.CENTER);
        }

        JPanel busqueda = Ui.botones(new JLabel("Buscar (título, autor o ISBN)"), txtBuscar, btnBuscar);
        norte.add(busqueda, BorderLayout.SOUTH);

        add(norte, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        Ui.configurar(this, 820, soloLectura ? 380 : 560);
    }

    /**
     * Carga y actualiza los registros de libros en la tabla de la interfaz gráfica.
     *
     * @param libros La lista de objetos Libro a visualizar en el catálogo.
     */
    public void cargarLibros(List<Libro> libros) {
        modelo.setRowCount(0);
        for (Libro l : libros) {
            modelo.addRow(new Object[]{
                    l.getId(),
                    l.getTitulo(),
                    l.getAutor(),
                    l.getCategoriaNombre(),
                    l.getIsbn(),
                    l.getEditorial(),
                    l.getStock()
            });
        }
    }

    /**
     * Carga una lista de categorías en el componente desplegable JComboBox
     *
     * @param categorias La lista de objetos Categoria a mostrar.
     */
    public void cargarCategorias(List<Categoria> categorias) {
        cmbCategoria.removeAllItems();
        for (Categoria c : categorias) {
            cmbCategoria.addItem(c);
        }
    }

    /**
     * Limpia todos los campos del formulario de entrada de libros,
     * restablece el stock a su valor por defecto ("0"), selecciona la primera categoría
     * disponible y borra la selección actual en la tabla.
     */
    public void limpiarFormulario() {
        txtTitulo.setText("");
        txtAutor.setText("");
        txtEditorial.setText("");
        txtIsbn.setText("");
        txtStock.setText("0");

        if (cmbCategoria.getItemCount() > 0) {
            cmbCategoria.setSelectedIndex(0);
        }

        tabla.clearSelection();
    }
}
