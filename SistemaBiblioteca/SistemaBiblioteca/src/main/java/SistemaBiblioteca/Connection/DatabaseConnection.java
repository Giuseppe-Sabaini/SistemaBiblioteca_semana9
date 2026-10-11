package SistemaBiblioteca.Connection;
/**
 * Administra la conexión a la base de datos MySQL aplicando el patrón de diseño Singleton.
 * Garantiza la existencia de una única instancia de conexión activa en toda la aplicación.
 *
 * @author Giuseppe Sabaini
 * @version 1.0
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // Variable estatica para almacenar la unica instancia de la clase
    private static DatabaseConnection instance;
    private Connection connection;

    private static final String URL = "jdbc:mysql://localhost:3306/speedfast";
    private static final String USER = "root";
    private static final String PASSWORD = "Jerrie1000._*";

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Constructor privado para evitar que se instancie con new
    private DatabaseConnection(){
        try {
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e){
            throw new RuntimeException("Error Al Conectar Con la Base de Datos MySQL", e);
        }
    }

    // Metodo publico estatico para obtener la unica instancia Thread-safe básico
    public static synchronized DatabaseConnection getInstance(){
        try {
            if (instance == null || instance.getConnection().isClosed()) {
                instance = new DatabaseConnection();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al Verificar Estado de Conexion");
        }
        return instance;
    }

    // Metodo para obtener el objeto Connection de JDBC
    public Connection getConnection() {
        return connection;
    }
}
