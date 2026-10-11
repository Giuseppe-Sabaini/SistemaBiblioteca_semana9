package SistemaBiblioteca.dao;
/**
 * Objeto de Acceso a Datos DAO encargado de gestionar las operaciones CRUD,
 * control transaccional, sincronización concurrente y reportes vinculados a los préstamos
 * de libros en el Sistema de Biblioteca.
 *
 * @author Giuseppe Sabaini
 * @version 1.0
 */

import SistemaBiblioteca.model.Prestamo;
import SistemaBiblioteca.model.ReporteResultado;
import SistemaBiblioteca.util.DatabaseConnection;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;


public class PrestamoDAO {

    public static final int DIAS_PRESTAMO = 7;

    // Lock compartido por TODAS las instancias de PrestamoDAO
    private static final Object LOCK = new Object();

    private final LibroDAO libroDAO = new LibroDAO();

    private static final String SELECT =
            "SELECT p.id, p.id_estudiante, p.id_libro, p.fecha_prestamo, p.fecha_devolucion, p.devuelto, l.titulo "
          + "FROM prestamos p JOIN libros l ON l.id = p.id_libro";

    private static final String ESTADO =
            "CASE WHEN p.devuelto THEN 'Devuelto' WHEN p.fecha_devolucion < CURDATE() THEN 'Atrasado' ELSE 'En préstamo' END";

    private Prestamo mapear(ResultSet rs) throws SQLException {
        Prestamo p = new Prestamo(rs.getInt("id"), rs.getInt("id_estudiante"), rs.getInt("id_libro"),
                rs.getDate("fecha_prestamo").toLocalDate(), rs.getDate("fecha_devolucion").toLocalDate(),
                rs.getBoolean("devuelto"));
        p.setLibroTitulo(rs.getString("titulo"));
        return p;
    }


    public void crear(Prestamo p) throws SQLException {
        String sql = "INSERT INTO prestamos (id_estudiante, id_libro, fecha_prestamo, fecha_devolucion, devuelto) VALUES (?,?,?,?,FALSE)";
        try (PreparedStatement ps = DatabaseConnection.conn().prepareStatement(sql)) {
            ps.setInt(1, p.getIdEstudiante());
            ps.setInt(2, p.getIdLibro());
            ps.setDate(3, Date.valueOf(p.getFechaPrestamo()));
            ps.setDate(4, Date.valueOf(p.getFechaDevolucion()));
            ps.executeUpdate();
        }
    }


    public Prestamo obtenerPorId(int id) throws SQLException {
        try (PreparedStatement ps = DatabaseConnection.conn().prepareStatement(SELECT + " WHERE p.id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? mapear(rs) : null; }
        }
    }


    public List<Prestamo> listar() throws SQLException {
        List<Prestamo> lista = new ArrayList<>();
        try (PreparedStatement ps = DatabaseConnection.conn().prepareStatement(SELECT + " ORDER BY p.id DESC");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public List<Prestamo> listarActivosPorEstudiante(int idEstudiante) throws SQLException {
        List<Prestamo> lista = new ArrayList<>();
        String sql = SELECT + " WHERE p.id_estudiante=? AND p.devuelto=FALSE ORDER BY p.fecha_devolucion";
        try (PreparedStatement ps = DatabaseConnection.conn().prepareStatement(sql)) {
            ps.setInt(1, idEstudiante);
            try (ResultSet rs = ps.executeQuery()) { while (rs.next()) lista.add(mapear(rs)); }
        }
        return lista;
    }


    public void actualizar(Prestamo p) throws SQLException {
        String sql = "UPDATE prestamos SET id_estudiante=?, id_libro=?, fecha_prestamo=?, fecha_devolucion=?, devuelto=? WHERE id=?";
        try (PreparedStatement ps = DatabaseConnection.conn().prepareStatement(sql)) {
            ps.setInt(1, p.getIdEstudiante());
            ps.setInt(2, p.getIdLibro());
            ps.setDate(3, Date.valueOf(p.getFechaPrestamo()));
            ps.setDate(4, Date.valueOf(p.getFechaDevolucion()));
            ps.setBoolean(5, p.isDevuelto());
            ps.setInt(6, p.getId());
            ps.executeUpdate();
        }
    }


    public void eliminar(int id) throws SQLException {
        try (PreparedStatement ps = DatabaseConnection.conn().prepareStatement("DELETE FROM prestamos WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    // @return true si se marcó como devuelto (false si ya estaba devuelto o no existe).
    public boolean marcarDevuelto(int idPrestamo) throws SQLException {
        try (PreparedStatement ps = DatabaseConnection.conn()
                .prepareStatement("UPDATE prestamos SET devuelto=TRUE WHERE id=? AND devuelto=FALSE")) {
            ps.setInt(1, idPrestamo);
            return ps.executeUpdate() == 1;
        }
    }

    // ---------- Reportes ----------

    public ReporteResultado librosMasPrestados() throws SQLException {
        String sql = "SELECT l.titulo AS Libro, l.autor AS Autor, COUNT(p.id) AS Veces_prestado "
                   + "FROM prestamos p JOIN libros l ON l.id = p.id_libro "
                   + "GROUP BY l.id, l.titulo, l.autor ORDER BY Veces_prestado DESC LIMIT 10";
        try (PreparedStatement ps = DatabaseConnection.conn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return ReporteResultado.desde(rs);
        }
    }

    public ReporteResultado historialPorEstudiante(int idEstudiante) throws SQLException {
        String sql = "SELECT l.titulo AS Libro, p.fecha_prestamo AS Prestamo, p.fecha_devolucion AS Vence, "
                   + ESTADO + " AS Estado FROM prestamos p JOIN libros l ON l.id = p.id_libro "
                   + "WHERE p.id_estudiante=? ORDER BY p.fecha_prestamo DESC";
        try (PreparedStatement ps = DatabaseConnection.conn().prepareStatement(sql)) {
            ps.setInt(1, idEstudiante);
            try (ResultSet rs = ps.executeQuery()) { return ReporteResultado.desde(rs); }
        }
    }

    public ReporteResultado librosEnPrestamo() throws SQLException {
        String sql = "SELECT l.titulo AS Libro, e.nombre AS Estudiante, p.fecha_prestamo AS Prestamo, "
                   + "p.fecha_devolucion AS Vence, " + ESTADO + " AS Estado "
                   + "FROM prestamos p JOIN libros l ON l.id = p.id_libro "
                   + "JOIN estudiantes e ON e.id = p.id_estudiante WHERE p.devuelto=FALSE ORDER BY p.fecha_devolucion";
        try (PreparedStatement ps = DatabaseConnection.conn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return ReporteResultado.desde(rs);
        }
    }

    //Registra el préstamo y descuenta stock en una transacción.
    public Prestamo registrarPrestamo(int idEstudiante, int idLibro)
            throws SQLException, ExcepcionStockInsuficiente {
        synchronized (LOCK) {
            Prestamo p = new Prestamo();
            p.setIdEstudiante(idEstudiante);
            p.setIdLibro(idLibro);
            p.setFechaPrestamo(LocalDate.now());
            p.setFechaDevolucion(LocalDate.now().plusDays(DIAS_PRESTAMO));

            boolean[] sinStock = {false};
            DatabaseConnection.getInstance().transaccion(() -> {
                if (!libroDAO.decrementarStock(idLibro)) {
                    sinStock[0] = true;
                    return;
                }
                crear(p);
            });
            if (sinStock[0]) throw new ExcepcionStockInsuficiente("No hay stock disponible de este libro.");
            return p;
        }
    }


     // Marca el préstamo como devuelto y repone el stock.
     // @return true si la devolución fue con atraso

    public boolean registrarDevolucion(int idPrestamo) throws SQLException {
        synchronized (LOCK) {
            Prestamo p = obtenerPorId(idPrestamo);
            if (p == null || p.isDevuelto()) throw new SQLException("El préstamo no existe o ya fue devuelto.");
            boolean atrasado = p.estaAtrasado();
            DatabaseConnection.getInstance().transaccion(() -> {
                marcarDevuelto(idPrestamo);
                libroDAO.incrementarStock(p.getIdLibro());
            });
            return atrasado;
        }
    }
}
