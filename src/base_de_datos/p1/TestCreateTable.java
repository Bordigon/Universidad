package tests;

import entregas.base_de_datos.p1.*;
import entregas.Env;
import org.junit.jupiter.api.*;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestCreateTable {

    private static final String USER = Env.getUser();
    private static final String PASSWORD = Env.getPass();
    private static final String URL = "jdbc:mysql://localhost:3306/cursos_db";

    @BeforeEach
    void preparar() throws SQLException {
        limpiarTodo();
        crearTablasNecesarias();
    }

    @AfterEach
    void limpiar() throws SQLException {
        limpiarTodo();
    }

    private void limpiarTodo() throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement st = conn.createStatement()) {

            st.executeUpdate("DROP TABLE IF EXISTS imparte");
            st.executeUpdate("DROP TABLE IF EXISTS modulo");
            st.executeUpdate("DROP TABLE IF EXISTS profesor");
            st.executeUpdate("DROP TABLE IF EXISTS aula");
        }
    }

    private void crearTablasNecesarias() throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement st = conn.createStatement()) {

            st.executeUpdate(
                    "CREATE TABLE profesor (" +
                    "id INT PRIMARY KEY" +
                    ")"
            );

            st.executeUpdate(
                    "CREATE TABLE aula (" +
                    "id INT PRIMARY KEY" +
                    ")"
            );

            st.executeUpdate(
                    "CREATE TABLE modulo (" +
                    "curso_id INT NOT NULL, " +
                    "n_modulo INT NOT NULL, " +
                    "PRIMARY KEY (curso_id, n_modulo)" +
                    ")"
            );
        }
    }

    @Test
    void comprobandoEjecucionCreateTable() {
        BBDDManager manager = new BBDDManager(USER, PASSWORD);

        StringWriter result = manager.run(
                new DataBaseTask[]{new CreateTable()},
                new String[]{""},
                true
        );

        assertEquals("[fin]", result.toString());
    }

    @Test
    void comprobandoElResultadoDeCreateTable() throws SQLException {
        BBDDManager manager = new BBDDManager(USER, PASSWORD);

        StringWriter result = manager.run(
                new DataBaseTask[]{new CreateTable()},
                new String[]{""},
                true
        );

        assertEquals("[fin]", result.toString());

        assertTrue(existeTabla("imparte"));

        assertTrue(existeColumna("imparte", "profesor_id"));
        assertTrue(existeColumna("imparte", "curso_id"));
        assertTrue(existeColumna("imparte", "n_modulo"));
        assertTrue(existeColumna("imparte", "aula_id"));
        assertTrue(existeColumna("imparte", "fecha"));

        assertTrue(existePrimaryKey("imparte", "profesor_id"));
        assertTrue(existePrimaryKey("imparte", "curso_id"));
        assertTrue(existePrimaryKey("imparte", "n_modulo"));
        assertTrue(existePrimaryKey("imparte", "aula_id"));
        assertTrue(existePrimaryKey("imparte", "fecha"));

        assertEquals(3, contarForeignKeys("imparte"));
    }

    private boolean existeTabla(String tabla) throws SQLException {
        String sql =
                "SELECT COUNT(*) " +
                "FROM information_schema.tables " +
                "WHERE table_schema = DATABASE() " +
                "AND table_name = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tabla);

            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1) == 1;
            }
        }
    }

    private boolean existeColumna(String tabla, String columna) throws SQLException {
        String sql =
                "SELECT COUNT(*) " +
                "FROM information_schema.columns " +
                "WHERE table_schema = DATABASE() " +
                "AND table_name = ? " +
                "AND column_name = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tabla);
            ps.setString(2, columna);

            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1) == 1;
            }
        }
    }

    private boolean existePrimaryKey(String tabla, String columna) throws SQLException {
        String sql =
                "SELECT COUNT(*) " +
                "FROM information_schema.key_column_usage " +
                "WHERE table_schema = DATABASE() " +
                "AND table_name = ? " +
                "AND column_name = ? " +
                "AND constraint_name = 'PRIMARY'";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tabla);
            ps.setString(2, columna);

            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1) == 1;
            }
        }
    }

    private int contarForeignKeys(String tabla) throws SQLException {
        String sql =
                "SELECT COUNT(*) " +
                "FROM information_schema.table_constraints " +
                "WHERE table_schema = DATABASE() " +
                "AND table_name = ? " +
                "AND constraint_type = 'FOREIGN KEY'";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tabla);

            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }
}