package cursos;

import java.sql.Connection;
import java.sql.SQLException;

public interface DataBaseTask {

    /**
     * Realiza una tarea en una base de datos
     *
     * Ejecuta una tarea en la base de datos dada por la conexion.
     * Si la tarea no se puede realizar correctamente
     * se lanza una BBDDException que se contruye y lanza con
     * cualquier otra Exception que se pueda producir.
     *
     * Se deben cerrar todos los Statement o PreparedStatement
     * que se hayan utilizado.
     *
     * PRE: conn abierta correctamente.
     * POST: conn permanece abierta.
     * @param conn
     * @param data
     * @throws SQLException si se produce un error sobre la bbdd.
     * @throws BBDDException si se produce otros errores
     */
    void run(Connection conn, String data) throws BBDDException, SQLException;

}
