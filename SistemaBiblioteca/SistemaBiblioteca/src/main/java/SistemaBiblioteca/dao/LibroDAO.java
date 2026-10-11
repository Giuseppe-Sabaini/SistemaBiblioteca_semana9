package SistemaBiblioteca.dao;
/**
 * Objeto de Acceso a Datos DAO encargado de gestionar las operaciones CRUD
 * y el control de inventario/stock para los libros en la base de datos del Sistema de Biblioteca.
 *
 * @author Giuseppe Sabaini
 * @version 1.0
 */

import SistemaBiblioteca.model.Libro;
import SistemaBiblioteca.util.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LibroDAO {

    private static final String SELECT =
            "SELECT l.id, l.titulo, l.autor, l.isbn, l.editorial, l.stock, l.id_categoria, c.nombre AS categoria "
          + "FROM libros l LEFT JOIN categorias c ON c.id = l.id_categoria";

    private Libro mapear(ResultSet rs) throws SQLException {
        Libro l = new Libro(rs.getInt("id"), rs.getString("titulo"), rs.getString("autor"),
                rs.getString("isbn"), rs.getString("editorial"), rs.getInt("stock"), rs.getInt("id_categoria"));
        l.setCategoriaNombre(rs.getString("categoria"));
        return l;
    }


    public void crear(Libro l) throws SQLException {
        String sql = "INSERT INTO libros (titulo, autor, isbn, editorial, stock, id_categoria) VALUES (?,?,?,?,?,?)";
        try (PreparedStatement ps = DatabaseConnection.conn().prepareStatement(sql)) {
            ps.setString(1, l.getTitulo());
            ps.setString(2, l.getAutor());
            ps.setString(3, l.getIsbn());
            ps.setString(4, l.getEditorial());
            ps.setInt(5, l.getStock());
            ps.setInt(6, l.getIdCategoria());
            ps.executeUpdate();
        }
    }


    public Libro obtenerPorId(int id) throws SQLException {
        try (PreparedStatement ps = DatabaseConnection.conn().prepareStatement(SELECT + " WHERE l.id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? mapear(rs) : null; }
        }
    }


    public List<Libro> listar() throws SQLException {
        return buscar("");
    }

    //Busca por título, autor o ISBN (texto vacío = todos)
    public List<Libro> buscar(String texto) throws SQLException {
        List<Libro> lista = new ArrayList<>();
        String sql = SELECT + " WHERE l.titulo LIKE ? OR l.autor LIKE ? OR l.isbn LIKE ? ORDER BY l.titulo";
        try (PreparedStatement ps = DatabaseConnection.conn().prepareStatement(sql)) {
            String like = "%" + texto + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, like);
            try (ResultSet rs = ps.executeQuery()) { while (rs.next()) lista.add(mapear(rs)); }
        }
        return lista;
    }


    public void actualizar(Libro l) throws SQLException {
        String sql = "UPDATE libros SET titulo=?, autor=?, isbn=?, editorial=?, stock=?, id_categoria=? WHERE id=?";
        try (PreparedStatement ps = DatabaseConnection.conn().prepareStatement(sql)) {
            ps.setString(1, l.getTitulo());
            ps.setString(2, l.getAutor());
            ps.setString(3, l.getIsbn());
            ps.setString(4, l.getEditorial());
            ps.setInt(5, l.getStock());
            ps.setInt(6, l.getIdCategoria());
            ps.setInt(7, l.getId());
            ps.executeUpdate();
        }
    }


    public void eliminar(int id) throws SQLException {
        try (PreparedStatement ps = DatabaseConnection.conn().prepareStatement("DELETE FROM libros WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    /**
     * Resta 1 al stock solo si hay unidades. La condición "stock > 0" va dentro del UPDATE,
     * así la base de datos también protege la consistencia. Método synchronized.
     *
     * @return true si se pudo descontar; false si no había stock
     */
    public synchronized boolean decrementarStock(int idLibro) throws SQLException {
        try (PreparedStatement ps = DatabaseConnection.conn()
                .prepareStatement("UPDATE libros SET stock = stock - 1 WHERE id=? AND stock > 0")) {
            ps.setInt(1, idLibro);
            return ps.executeUpdate() == 1;
        }
    }

    public synchronized void incrementarStock(int idLibro) throws SQLException {
        try (PreparedStatement ps = DatabaseConnection.conn()
                .prepareStatement("UPDATE libros SET stock = stock + 1 WHERE id=?")) {
            ps.setInt(1, idLibro);
            ps.executeUpdate();
        }
    }
}
