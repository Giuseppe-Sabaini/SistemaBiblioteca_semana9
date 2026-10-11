package SistemaBiblioteca.model;
/**
 * Modelo que representa una categoría de libros dentro del Sistema de Biblioteca,
 * utilizada para clasificar y organizar el catálogo bibliográfico.
 *
 * @author Giuseppe Sabaini
 * @version 1.0
 */

public class Categoria {
    private int id;
    private String nombre;

    //Constructor Sin nada
    public Categoria() {
    }

    //Constructor Completo
    public Categoria(int id, String nombre) { this.id = id; this.nombre = nombre; }


    //Getters y Setters
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

    @Override public String toString() { return nombre; }
}
