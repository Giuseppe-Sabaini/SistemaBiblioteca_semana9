package SistemaBiblioteca.model;

/**
 * Modelo que representa una cuenta de usuario dentro del Sistema de Biblioteca,
 * almacenando sus credenciales de acceso, información personal y rol asignado
 * (Bibliotecario o Estudiante).
 *
 * @author Giuseppe Sabaini
 * @version 1.0
 */
public class Usuario {
    public static final String BIBLIOTECARIO = "bibliotecario";
    public static final String ESTUDIANTE = "estudiante";

    private int id;
    private String nombre, rut, correo, contrasena, rol;

    public Usuario() { }

    public Usuario(int id, String nombre, String rut, String correo, String contrasena, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.rut = rut;
        this.correo = correo;
        this.contrasena = contrasena;
        this.rol = rol;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public boolean esBibliotecario() {
        return BIBLIOTECARIO.equals(rol);
    }
}