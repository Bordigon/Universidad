package tests;

import entregas.base_de_datos.p1.*;
import entregas.Env;
import org.junit.jupiter.api.*;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestConnection {

    private static final String USER = Env.getUser();
    private static final String PASSWORD = Env.getPass();
    private static final String URL = "jdbc:mysql://localhost:3306/cursos_db";

    @BeforeEach
    void preparar() throws SQLException {
        limpiarTablaTest();
        crearTablaTest();
    }

    @AfterEach
    void limpiar() throws SQLException {
        limpiarTablaTest();
    }

    private void limpiarTablaTest() throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement st = conn.createStatement()) {
            st.executeUpdate("DROP TABLE IF EXISTS junit_bbddmanager_test");
        }
    }

    private void crearTablaTest() throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement st = conn.createStatement()) {
            st.executeUpdate(
                    "CREATE TABLE junit_bbddmanager_test (" +
                    "id INT PRIMARY KEY, " +
                    "texto VARCHAR(50)" +
                    ")"
            );
        }
    }

    private int contarFilas() throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM junit_bbddmanager_test")) {
            rs.next();
            return rs.getInt(1);
        }
    }

    @Test
    void comprobandoBBDDManagerCuandoSePuedeAbrir() {
        BBDDManager manager = new BBDDManager(USER, PASSWORD);

        StringWriter result = manager.run(
                new DataBaseTask[]{},
                new String[]{},
                true
        );

        assertEquals("[fin]", result.toString());
    }

    @Test
    void comprobandoBBDDManagerCuandoNoSePuedeAbrir() {
        BBDDManager manager = new BBDDManager("usuario_mal", "password_mal");

        assertThrows(NullPointerException.class, () -> {
            manager.run(
                    new DataBaseTask[]{},
                    new String[]{},
                    true
            );
        });
    }

    @Test
    void comprobandoBBDDManagerCuandoTaskLanzaBBDDException() {
        BBDDManager manager = new BBDDManager(USER, PASSWORD);

        DataBaseTask task = (conn, data) -> {
            throw new BBDDException(new Exception("fallo interno"), "momento-test");
        };

        StringWriter result = manager.run(
                new DataBaseTask[]{task},
                new String[]{"data"},
                true
        );

        assertTrue(result.toString().contains("Task:momento-test;"));
        assertTrue(result.toString().contains("fallo interno"));
        assertTrue(result.toString().contains("fin"));
    }

    @Test
    void comprobandoBBDDManagerCuandoTaskLanzaSQLException() {
        BBDDManager manager = new BBDDManager(USER, PASSWORD);

        DataBaseTask task = (conn, data) -> {
            throw new SQLException("fallo sql");
        };

        StringWriter result = manager.run(
                new DataBaseTask[]{task},
                new String[]{"data"},
                true
        );

        assertTrue(result.toString().contains("SQL:fallo sql;"));
        assertTrue(result.toString().contains("fin"));
    }

    @Test
    void comprobandoBBDDManagerCuandoTaskLanzaRuntimeException() {
        BBDDManager manager = new BBDDManager(USER, PASSWORD);

        DataBaseTask task = (conn, data) -> {
            throw new RuntimeException("fallo runtime");
        };

        StringWriter result = manager.run(
                new DataBaseTask[]{task},
                new String[]{"data"},
                true
        );

        assertTrue(result.toString().contains("Otro:fallo runtime;"));
        assertTrue(result.toString().contains("fin"));
    }

    @Test
    void comprobandoCommitTrasTareaCorrectaConAutoCommitFalse() throws SQLException {
        BBDDManager manager = new BBDDManager(USER, PASSWORD);

        DataBaseTask insertar = (conn, data) -> {
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO junit_bbddmanager_test(id, texto) VALUES (?, ?)"
            )) {
                ps.setInt(1, 1);
                ps.setString(2, data);
                ps.executeUpdate();
            }
        };

        StringWriter result = manager.run(
                new DataBaseTask[]{insertar},
                new String[]{"ok"},
                false
        );

        assertEquals("[fin]", result.toString());

        // Con tu BBDDManager actual, NO se hace commit tras una tarea correcta.
        assertEquals(0, contarFilas());
    }

    @Test
    void comprobandoCommitTrasBBDDExceptionConAutoCommitFalse() throws SQLException {
        BBDDManager manager = new BBDDManager(USER, PASSWORD);

        DataBaseTask insertarYLanzarBBDDException = (conn, data) -> {
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO junit_bbddmanager_test(id, texto) VALUES (?, ?)"
            )) {
                ps.setInt(1, 1);
                ps.setString(2, data);
                ps.executeUpdate();
            }

            throw new BBDDException(new Exception("fallo despues de insertar"), "insertar");
        };

        StringWriter result = manager.run(
                new DataBaseTask[]{insertarYLanzarBBDDException},
                new String[]{"commit"},
                false
        );

        assertTrue(result.toString().contains("Task:insertar;"));
        assertTrue(result.toString().contains("fallo despues de insertar"));
        assertTrue(result.toString().contains("fin"));

        assertEquals(1, contarFilas());
    }

    @Test
    void comprobandoRollbackTrasSQLExceptionConAutoCommitFalse() throws SQLException {
        BBDDManager manager = new BBDDManager(USER, PASSWORD);

        DataBaseTask insertarYLanzarSQLException = (conn, data) -> {
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO junit_bbddmanager_test(id, texto) VALUES (?, ?)"
            )) {
                ps.setInt(1, 1);
                ps.setString(2, data);
                ps.executeUpdate();
            }

            throw new SQLException("fallo despues de insertar");
        };

        StringWriter result = manager.run(
                new DataBaseTask[]{insertarYLanzarSQLException},
                new String[]{"rollback"},
                false
        );

        assertTrue(result.toString().contains("SQL:fallo despues de insertar;"));
        assertTrue(result.toString().contains("fin"));

        assertEquals(0, contarFilas());
    }
}