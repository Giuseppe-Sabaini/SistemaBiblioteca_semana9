package SistemaBiblioteca.dao;
/**
 * Objeto de Acceso a Datos DAO encargado de gestionar las operaciones CRUD,
 * autenticación de credenciales y consultas asociadas a los usuarios del Sistema de Biblioteca.
 *
 * @author  Giuseppe Sabaini
 * @version 1.0
 */

import SistemaBiblioteca.model.Usuario;
import SistemaBiblioteca.util.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    private static final String COLS = "id, nombre, rut, correo, `contrase\u00f1a` AS clave, rol";

    private Usuario mapear(ResultSet rs) throws SQLException {
        return new Usuario(rs.getInt("id"), rs.getString("nombre"), rs.getString("rut"),
                rs.getString("correo"), rs.getString("clave"), rs.getString("rol"));
    }


    public void crear(Usuario u) throws SQLException {
        String sql = "INSERT INTO usuarios (nombre, rut, correo, `contrase\u00f1a`, rol) VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = DatabaseConnection.conn().prepareStatement(sql)) {
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getRut());
            ps.setString(3, u.getCorreo());
            ps.setString(4, u.getContrasena());
            ps.setString(5, u.getRol());
            ps.executeUpdate();
        }
    }


    public Usuario obtenerPorId(int id) throws SQLException {
        try (PreparedStatement ps = DatabaseConnection.conn()
                .prepareStatement("SELECT " + COLS + " FROM usuarios WHERE id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? mapear(rs) : null; }
        }
    }

    public Usuario obtenerPorRut(String rut) throws SQLException {
        try (PreparedStatement ps = DatabaseConnection.conn()
                .prepareStatement("SELECT " + COLS + " FROM usuarios WHERE rut=?")) {
            ps.setString(1, rut);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? mapear(rs) : null; }
        }
    }

    // Valida credenciales: el "usuario" puede ser el RUT o el correo.
    public Usuario autenticar(String login, String clave) throws SQLException {
        String sql = "SELECT " + COLS + " FROM usuarios WHERE (rut=? OR correo=?) AND `contrase\u00f1a`=?";
        try (PreparedStatement ps = DatabaseConnection.conn().prepareStatement(sql)) {
            ps.setString(1, login);
            ps.setString(2, login);
            ps.setString(3, clave);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? mapear(rs) : null; }
        }
    }


    public List<Usuario> listar() throws SQLException {
        List<Usuario> lista = new ArrayList<>();
        try (PreparedStatement ps = DatabaseConnection.conn()
                .prepareStatement("SELECT " + COLS + " FROM usuarios ORDER BY nombre");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }


    public void actualizar(Usuario u) throws SQLException {
        String sql = "UPDATE usuarios SET nombre=?, rut=?, correo=?, `contrase\u00f1a`=?, rol=? WHERE id=?";
        try (PreparedStatement ps = DatabaseConnection.conn().prepareStatement(sql)) {
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getRut());
            ps.setString(3, u.getCorreo());
            ps.setString(4, u.getContrasena());
            ps.setString(5, u.getRol());
            ps.setInt(6, u.getId());
            ps.executeUpdate();
        }
    }


    public void eliminar(int id) throws SQLException {
        try (PreparedStatement ps = DatabaseConnection.conn().prepareStatement("DELETE FROM usuarios WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
