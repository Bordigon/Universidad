package cursos;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import entregas.Env;

import org.junit.jupiter.api.Test;

public class MainS4 {

    private static final String USER = Env.getUser();
    private static final String PASS = Env.getPass(); // cambia si tu MySQL tiene contraseña

    private static String ejecutarConManager(DataBaseTask task, String data) {
        BBDDManager manager = new BBDDManager(USER, PASS);

        StringWriter sw = manager.run(
            new DataBaseTask[] { task },
            new String[] { data },
            true
        );

        return String.join("", sw);
    }

    private static Connection conn() throws SQLException {
        return DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/cursos_db",
            USER,
            PASS
        );
    }

    private static void insertarEdificioSiNoExiste(Connection c, int id, String nombre, String hex) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(
            "INSERT INTO edificio (id, nombre, foto) VALUES (?, ?, UNHEX(?)) " +
            "ON DUPLICATE KEY UPDATE nombre = VALUES(nombre), foto = VALUES(foto)"
        )) {
            ps.setInt(1, id);
            ps.setString(2, nombre);
            ps.setString(3, hex);
            ps.executeUpdate();
        }
    }

    /*
     * =========================================================
     * NO TOCAR: TESTS CONSULTA BLOB
     * =========================================================
     */

    @Test
    public void comprobandoConsultaBlobCuandoTodoFunciona() {
        String r = ejecutarConManager(new ConsultaBlob(), "Edificio Central");

        assertEquals(
            "fin",
            r,
            "Si no hay errores run debe devolver exactamente 'fin'"
        );
    }

    @Test
    public void comprobandoConsultaBlobSinResultados() {
        String r = ejecutarConManager(new ConsultaBlob(), "EDIFICIO_QUE_NO_EXISTE_999");

        assertTrue(
            r.contains("Otro: 3no existe") || r.contains("no existe"),
            "Cuando no existe el edificio debe indicarse 'no existe'"
        );
    }

    @Test
    public void comprobandoConsultaBlobConIOException() throws Exception {
        try (Connection c = conn()) {
            insertarEdificioSiNoExiste(c, 9999, "", "4142");
        }

        String r = ejecutarConManager(new ConsultaBlob(), "");

        assertTrue(
            r.contains("Task:error archivo"),
            "Con IOException debe lanzarse BBDDException con when='error archivo'"
        );
    }

    /*
     * =========================================================
     * TESTS CONSULTA COMPLEJA JUNIT 5
     * =========================================================
     */

    @Test
    public void comprobandoConsultaComplejaCuandoTodoFunciona() {
        String r = ejecutarConManager(new ConsultaCompleja(), "40");

        assertEquals(
            "fin",
            r,
            "Si no hay errores run debe devolver exactamente 'fin'"
        );
    }

    @Test
    public void comprobandoConsultaComplejaSinEntero() {
        String r = ejecutarConManager(new ConsultaCompleja(), "abc");

        assertTrue(
            r.contains("Task:not int"),
            "Con data no entero debe lanzarse BBDDException con when='not int'"
        );
    }
}