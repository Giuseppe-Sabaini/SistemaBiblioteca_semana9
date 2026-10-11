package SistemaBiblioteca.dao;
/**
 * Objeto de Acceso a Datos DAO encargado de gestionar las operaciones CRUD
 * y transaccionales para los estudiantes, así como la sincronización con sus cuentas
 * de usuario asociadas en el Sistema de Biblioteca.
 *
 * @author Giuseppe Sabaini
 * @version 1.0
 */

import SistemaBiblioteca.model.Estudiante;
import SistemaBiblioteca.model.Usuario;
import SistemaBiblioteca.util.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EstudianteDAO  {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private static final String SELECT = "SELECT id, nombre, rut, curso, correo FROM estudiantes";

    private Estudiante mapear(ResultSet rs) throws SQLException {
        return new Estudiante(rs.getInt("id"), rs.getString("nombre"), rs.getString("rut"),
                rs.getString("curso"), rs.getString("correo"));
    }


    public void crear(Estudiante e) throws SQLException {
        String sql = "INSERT INTO estudiantes (nombre, rut, curso, correo) VALUES (?,?,?,?)";
        try (PreparedStatement ps = DatabaseConnection.conn().prepareStatement(sql)) {
            ps.setString(1, e.getNombre());
            ps.setString(2, e.getRut());
            ps.setString(3, e.getCurso());
            ps.setString(4, e.getCorreo());
            ps.executeUpdate();
        }
    }


    public Estudiante obtenerPorId(int id) throws SQLException {
        try (PreparedStatement ps = DatabaseConnection.conn().prepareStatement(SELECT + " WHERE id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? mapear(rs) : null; }
        }
    }

    public Estudiante obtenerPorRut(String rut) throws SQLException {
        try (PreparedStatement ps = DatabaseConnection.conn().prepareStatement(SELECT + " WHERE rut=?")) {
            ps.setString(1, rut);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? mapear(rs) : null; }
        }
    }


    public List<Estudiante> listar() throws SQLException {
        return buscar("");
    }

    //Busca por nombre o RUT (texto vacío = todos)
    public List<Estudiante> buscar(String texto) throws SQLException {
        List<Estudiante> lista = new ArrayList<>();
        String sql = SELECT + " WHERE nombre LIKE ? OR rut LIKE ? ORDER BY nombre";
        try (PreparedStatement ps = DatabaseConnection.conn().prepareStatement(sql)) {
            ps.setString(1, "%" + texto + "%");
            ps.setString(2, "%" + texto + "%");
            try (ResultSet rs = ps.executeQuery()) { while (rs.next()) lista.add(mapear(rs)); }
        }
        return lista;
    }


    public void actualizar(Estudiante e) throws SQLException {
        String sql = "UPDATE estudiantes SET nombre=?, rut=?, curso=?, correo=? WHERE id=?";
        try (PreparedStatement ps = DatabaseConnection.conn().prepareStatement(sql)) {
            ps.setString(1, e.getNombre());
            ps.setString(2, e.getRut());
            ps.setString(3, e.getCurso());
            ps.setString(4, e.getCorreo());
            ps.setInt(5, e.getId());
            ps.executeUpdate();
        }
    }


    public void eliminar(int id) throws SQLException {
        try (PreparedStatement ps = DatabaseConnection.conn().prepareStatement("DELETE FROM estudiantes WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    //Crea el estudiante y su usuario de acceso en una transacción
    public void registrar(Estudiante e, String clave) throws SQLException {
        DatabaseConnection.getInstance().transaccion(() -> {
            crear(e);
            usuarioDAO.crear(new Usuario(0, e.getNombre(), e.getRut(), e.getCorreo(), clave, Usuario.ESTUDIANTE));
        });
    }

    //Si la clave viene vacía, se mantiene la actual.
    public void actualizarConUsuario(Estudiante e, String rutOriginal, String clave) throws SQLException {
        DatabaseConnection.getInstance().transaccion(() -> {
            Usuario u = usuarioDAO.obtenerPorRut(rutOriginal);
            actualizar(e);
            if (u != null) {
                u.setNombre(e.getNombre());
                u.setRut(e.getRut());
                u.setCorreo(e.getCorreo());
                if (clave != null && !clave.isEmpty()) u.setContrasena(clave);
                usuarioDAO.actualizar(u);
            }
        });
    }

    //Falla (SQLException) si el estudiante tiene préstamos registrados.
    public void eliminarConUsuario(Estudiante e) throws SQLException {
        DatabaseConnection.getInstance().transaccion(() -> {
            eliminar(e.getId());
            Usuario u = usuarioDAO.obtenerPorRut(e.getRut());
            if (u != null) usuarioDAO.eliminar(u.getId());
        });
    }
}
