package SistemaBiblioteca.dao;
/**
 * Objeto de Acceso a Datos DAO encargado de gestionar las operaciones CRUD
 * para las categorías de libros en la base de datos del Sistema de Biblioteca.
 *
 * @author Giuseppe Sabaini
 * @version 1.0
 */

import SistemaBiblioteca.model.Categoria;
import SistemaBiblioteca.util.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {


    public void crear(Categoria c) throws SQLException {
        try (PreparedStatement ps = DatabaseConnection.conn().prepareStatement("INSERT INTO categorias (nombre) VALUES (?)")) {
            ps.setString(1, c.getNombre());
            ps.executeUpdate();
        }
    }


    public Categoria obtenerPorId(int id) throws SQLException {
        try (PreparedStatement ps = DatabaseConnection.conn().prepareStatement("SELECT id, nombre FROM categorias WHERE id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? new Categoria(rs.getInt("id"), rs.getString("nombre")) : null;
            }
        }
    }


    public List<Categoria> listar() throws SQLException {
        List<Categoria> lista = new ArrayList<>();
        try (PreparedStatement ps = DatabaseConnection.conn().prepareStatement("SELECT id, nombre FROM categorias ORDER BY nombre");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(new Categoria(rs.getInt("id"), rs.getString("nombre")));
        }
        return lista;
    }


    public void actualizar(Categoria c) throws SQLException {
        try (PreparedStatement ps = DatabaseConnection.conn().prepareStatement("UPDATE categorias SET nombre=? WHERE id=?")) {
            ps.setString(1, c.getNombre());
            ps.setInt(2, c.getId());
            ps.executeUpdate();
        }
    }


    public void eliminar(int id) throws SQLException {
        try (PreparedStatement ps = DatabaseConnection.conn().prepareStatement("DELETE FROM categorias WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
