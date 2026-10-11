package SistemaBiblioteca.model;

import java.time.LocalDate;

/**
 * Modelo que representa un préstamo de un libro realizado por un estudiante
 * dentro del Sistema de Biblioteca, incluyendo fechas de inicio, vencimiento y estado de devolución.
 *
 * @author Giuseppe Sabaini
 * @version 1.0
 */
public class Prestamo {
    private int id, idEstudiante, idLibro;
    private LocalDate fechaPrestamo;

    // Fecha límite de devolución (vencimiento).
    private LocalDate fechaDevolucion;
    private boolean devuelto;
    private String libroTitulo;

    public Prestamo() { }

    public Prestamo(int id, int idEstudiante, int idLibro, LocalDate fechaPrestamo,
                    LocalDate fechaDevolucion, boolean devuelto) {
        this.id = id;
        this.idEstudiante = idEstudiante;
        this.idLibro = idLibro;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucion = fechaDevolucion;
        this.devuelto = devuelto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(int idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public int getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }

    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(LocalDate f) {
        this.fechaPrestamo = f;
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(LocalDate f) {
        this.fechaDevolucion = f;
    }

    public boolean isDevuelto() {
        return devuelto;
    }

    public void setDevuelto(boolean devuelto) {
        this.devuelto = devuelto;
    }

    public String getLibroTitulo() {
        return libroTitulo;
    }

    public void setLibroTitulo(String libroTitulo) {
        this.libroTitulo = libroTitulo;
    }

    // Un préstamo está atrasado si no se devolvió y ya pasó su fecha límite
    public boolean estaAtrasado() {
        return !devuelto && fechaDevolucion != null && fechaDevolucion.isBefore(LocalDate.now());
    }
}