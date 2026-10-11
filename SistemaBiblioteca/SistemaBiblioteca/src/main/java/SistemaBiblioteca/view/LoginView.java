package SistemaBiblioteca.view;

import javax.swing.*;
import java.awt.*;

/**
 * Vista gráfica basada en Java Swing para la autenticación de usuarios dentro del Sistema de Biblioteca,
 * permitiendo ingresar las credenciales de acceso (RUT o correo y contraseña)
 * para ingresar al sistema según el rol correspondiente.
 *
 * @author Giuseppe Sabaini
 * @version 1.0
 */
public class LoginView extends JFrame {

    // Campo de texto para ingresar el RUT o correo electrónico del usuario.
    public final JTextField txtUsuario = new JTextField();

    // Campo seguro de contraseña para la clave de acceso.
    public final JPasswordField txtClave = new JPasswordField();

    // Botón para enviar las credenciales y ejecutar el inicio de sesión.
    public final JButton btnIngresar = new JButton("Ingresar");

    // Botón para salir o cerrar la aplicación.
    public final JButton btnSalir = new JButton("Salir");

    // Etiqueta de texto para mostrar mensajes de error o advertencia en la autenticación.
    public final JLabel lblError = new JLabel(" ");

    /**
     * Constructor que inicializa y configura los componentes visuales de la ventana
     * de inicio de sesión, estructurando el formulario, las etiquetas de error y los botones de control.
     */
    public LoginView() {
        super("Biblioteca escolar - Iniciar sesión");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 10));
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 5, 20));

        form.add(new JLabel("RUT o correo"));
        form.add(txtUsuario);
        form.add(new JLabel("Contraseña"));
        form.add(txtClave);

        lblError.setForeground(Color.RED);
        lblError.setHorizontalAlignment(SwingConstants.CENTER);

        add(form, BorderLayout.NORTH);
        add(lblError, BorderLayout.CENTER);
        add(Ui.botones(btnIngresar, btnSalir), BorderLayout.SOUTH);

        getRootPane().setDefaultButton(btnIngresar);
        Ui.configurar(this, 380, 220);
    }
}