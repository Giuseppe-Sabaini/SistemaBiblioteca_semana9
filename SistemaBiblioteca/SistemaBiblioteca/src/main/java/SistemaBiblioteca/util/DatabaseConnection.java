package SistemaBiblioteca.util;
/**
 * Utilidad que implementa el patrón Singleton de forma segura para hilos (thread-safe),
 * encargándose de administrar la conexión única a la base de datos MySQL
 * y  soporte para transacciones atómicas commit/rollback en el Sistema de Biblioteca.
 *
 * @author Giuseppe Sabaini
 * @version 1.0
 */

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

//Singleton: una única instancia que administra la conexión a la base de datos.
public final class DatabaseConnection {

    @FunctionalInterface
    public interface TareaSql { void ejecutar() throws SQLException; }

    private static DatabaseConnection instance;

    private final String url, user, password;
    private Connection connection;

    private DatabaseConnection() {
        Properties p = new Properties();
        try (InputStream in = DatabaseConnection.class.getResourceAsStream("/db.properties")) {
            if (in == null) throw new IllegalStateException("No se encontró db.properties");
            p.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo leer db.properties", e);
        }
        url = p.getProperty("db.url");
        user = p.getProperty("db.user");
        password = p.getProperty("db.password");
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) instance = new DatabaseConnection();
        return instance;
    }

    //Devuelve la conexión única; si se cayó, la vuelve a abrir.
    public synchronized Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed() || !connection.isValid(2)) {
            connection = DriverManager.getConnection(url, user, password);
        }
        return connection;
    }

    //Atajo para los DAO.
    public static Connection conn() throws SQLException {
        return getInstance().getConnection();
    }

    // Ejecuta varias operaciones como una sola transacción (commit o rollback).
    public synchronized void transaccion(TareaSql tarea) throws SQLException {
        Connection c = getConnection();
        boolean auto = c.getAutoCommit();
        c.setAutoCommit(false);
        try {
            tarea.ejecutar();
            c.commit();
        } catch (SQLException | RuntimeException e) {
            c.rollback();
            throw e;
        } finally {
            c.setAutoCommit(auto);
        }
    }

    public synchronized void close() {
        try { if (connection != null) connection.close(); } catch (SQLException ignored) { }
        connection = null;
    }
}
