package SistemaBiblioteca.dao;
/**
 * Excepción personalizada lanzada cuando se intenta registrar un préstamo
 * pero la cantidad disponible en stock del libro es insuficiente.
 *
 * @author Giuseppe Sabaini
 * @version 1.0
 */

public class ExcepcionStockInsuficiente extends Exception {
    public ExcepcionStockInsuficiente(String mensaje) { super(mensaje); }
}

