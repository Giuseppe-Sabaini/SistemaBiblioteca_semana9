package SistemaBiblioteca.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Clase de utilidad estática que centraliza la creación y configuración común de componentes
 * gráficos en Java Swing para las vistas del Sistema de Biblioteca (tablas, modelos, paneles, formularios y ventanas).
 *
 * @author Giuseppe Sabaini
 * @version 1.0
 */
public final class Ui {

    /**
     * Constructor privado para prevenir la instanciación de esta clase de utilidad estática.
     */
    private Ui() { }

    /**
     * Crea un modelo de tabla por defecto no editable para las vistas, inicializado con las columnas especificadas.
     *
     * @param columnas Los nombres de las columnas que conformarán la cabecera de la tabla.
     * @return Un objeto  DefaultTableModel configurado como de solo lectura.
     */
    public static DefaultTableModel modelo(String... columnas) {
        return new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
    }

    /**
     * Crea y configura una tabla JTable estándar con selección única de filas y una altura optimizada.
     *
     * @param m El modelo de datos  DefaultTableModel que utilizará la tabla.
     * @return Un componente  JTable configurado y listo para ser incorporado en un panel de desplazamiento.
     */
    public static JTable tabla(DefaultTableModel m) {
        JTable t = new JTable(m);
        t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        t.setRowHeight(22);
        return t;
    }

    /**
     * Crea un panel contenedor estructurado con una retícula GridLayout de cuatro columnas
     * y márgenes estándar, ideal para organizar etiquetas y campos de entrada en los formularios de registro.
     *
     * @return Un objeto  JPanel configurado para la disposición de formularios.
     */
    public static JPanel formulario() {
        JPanel p = new JPanel(new GridLayout(0, 4, 8, 6));
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 4, 10));
        return p;
    }

    /**
     * Crea un panel contenedor organizado en flujo horizontal  FlowLayout
     * para agrupar de manera uniforme botones y elementos de control interactivos.
     *
     *  cs Un arreglo variable de componentes visuales  JComponent que se incluirán en el panel.
     * @return Un objeto JPanel conteniendo los componentes alineados con espaciado consistente.
     */
    public static JPanel botones(JComponent... cs) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        for (JComponent c : cs) {
            p.add(c);
        }
        return p;
    }

    /**
     * Configura el tamaño en píxeles de una ventana JFrame y la centra automáticamente
     * en el centro de la pantalla del usuario.
     *
     *  f La ventana JFrame a dimensionar y ubicar.
     *  ancho El ancho deseado de la ventana en píxeles.
     *  alto El alto deseado de la ventana en píxeles.
     */
    public static void configurar(JFrame f, int ancho, int alto) {
        f.setSize(ancho, alto);
        f.setLocationRelativeTo(null);
    }
}
