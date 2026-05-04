package cursos;

import java.sql.*;
import java.math.BigDecimal;
import java.util.ArrayList;

public class ConsultaCompleja extends ConsultaConResultado<Properties> {

    private String consulta = """
        SELECT p.nombre, p.apellido1, p.apellido2, i.curso_id,
               ROUND(100.0 * SUM(m.horas) / tot.total, 4) AS porcentaje
        FROM imparte i
        JOIN modulo m ON m.curso_id = i.curso_id AND m.n_modulo = i.n_modulo
        JOIN profesor p ON p.id = i.profesor_id
        JOIN (SELECT curso_id, SUM(horas) AS total FROM modulo GROUP BY curso_id) tot
          ON tot.curso_id = i.curso_id
        GROUP BY i.profesor_id, i.curso_id, p.nombre, p.apellido1, p.apellido2, tot.total
        HAVING porcentaje >= ?
        ORDER BY p.apellido1 ASC;
        """;

    @Override
    public void run(Connection conn, String data) throws BBDDException, SQLException {

        int umbral;
        try {
            umbral = Integer.parseInt(data);
        } catch (NumberFormatException e) {
            throw new BBDDException(e, "not int");
        }

        resultado = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(consulta)) {
            ps.setInt(1, umbral);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String nombre    = rs.getString("nombre");
                    String apellido1 = rs.getString("apellido1");
                    String apellido2 = rs.getString("apellido2");
                    int IdCurso      = rs.getInt("curso_id");
                    BigDecimal porcentaje = rs.getBigDecimal("porcentaje");

                    String extra = "curso:" + IdCurso + ":" + porcentaje.toString();

                    resultado.add(new Properties(nombre, apellido1, apellido2, extra));
                }
            }
        }
    }
}
