package SistemaBiblioteca.model;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Modelo que representa el resultado genérico de un reporte estadístico o historial
 * dentro del Sistema de Biblioteca, encapsulando de manera dinámica las cabeceras,
 * nombres de columnas y las filas de datos tabulares extraídas de la base de datos.
 *
 * @author Giuseppe Sabaini
 * @version 1.0
 */
public class ReporteResultado {
    private final String[] columnas;
    private final List<Object[]> filas;

    public ReporteResultado(String[] columnas, List<Object[]> filas) {
        this.columnas = columnas;
        this.filas = filas;
    }

    public String[] getColumnas() {
        return columnas;
    }

    public List<Object[]> getFilas() {
        return filas;
    }

    public static ReporteResultado desde(ResultSet rs) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        int n = md.getColumnCount();
        String[] cols = new String[n];
        for (int i = 0; i < n; i++) cols[i] = md.getColumnLabel(i + 1);
        List<Object[]> filas = new ArrayList<>();
        while (rs.next()) {
            Object[] f = new Object[n];
            for (int i = 0; i < n; i++) f[i] = rs.getObject(i + 1);
            filas.add(f);
        }
        return new ReporteResultado(cols, filas);
    }
}
