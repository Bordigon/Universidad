
package cursos;

import java.sql.*;

public class AddColumn implements DataBaseTask {
    /**
     * Pone una nueva columna en `edificio`.
     *
     * @param conn La conexion ya abierta
     * @param data No es necesaria, se asume vacia.
     *
     * @throws BBDDException (no es necesario lanzarla)
     * @throws SQLException, cuando se produzca la misma al ejecutar
     *         la modificacion de la tabla.
     */
    @Override
    public void run(Connection conn, String data) throws BBDDException, SQLException {
	String sql = "ALTER TABLE edificio ADD COLUMN foto BLOB NULL";
    // Usamos try-with-resources para asegurar que el Statement se cierre correctamente
    try (Statement stmt = conn.createStatement()) {
        stmt.executeUpdate(sql);}

    }
}