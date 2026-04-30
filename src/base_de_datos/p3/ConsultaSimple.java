package cursos;

import java.sql.*;

import java.util.*;

public class ConsultaSimple extends ConsultaConResultado<Properties> {
    /**
     * Obtiene los profesores ordenados por apellido1
     *
     * @param conn La conexion ya abierta
     * @param data o bien ASC o bien DESC (debe ser case-insentive)
     *
     * @throws BBDDException, cuando `data` sea distinto de ACS y DESC.
     * @throws SQLException, cuando se produzca la misma al ejecutar
     *         modificar la tabla.
     */
    @Override
    public void run(Connection conn, String data) throws BBDDException, SQLException {
    }
}