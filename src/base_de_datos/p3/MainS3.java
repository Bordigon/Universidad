package cursos;

public class MainS3 {

    // ============================================================
    //  TESTS INDIVIDUALES
    // ============================================================
    //  Comenta/descomenta llamadas en main() para activarlos.
    //  Antes de cada test recuerda preparar la BD si hace falta:
    //   - AddColumn:    ALTER TABLE edificio DROP COLUMN foto;
    //   - ConsultaConFiltro: necesita imparte poblada
    // ============================================================

    static BBDDManager cm = new BBDDManager("alumno", "bbdd-upm");

    // ----------- AddColumn -----------

    static void testAddColumn_OK() {
        System.out.println("\n=== testAddColumn_OK ===");
        System.out.println("(Antes ejecuta: ALTER TABLE edificio DROP COLUMN foto;)");

        AddColumn ac = new AddColumn();
        DataBaseTask[] tasks = { ac };
        String[] data = { "" };
        StringWriter result = cm.run(tasks, data, true);
        System.out.println("Salida: " + result);
        System.out.println("Esperado: [fin]");
    }

    static void testAddColumn_yaExiste() {
        System.out.println("\n=== testAddColumn_yaExiste ===");
        System.out.println("(Ejecutalo dos veces seguidas sin dropear la columna)");

        AddColumn ac = new AddColumn();
        DataBaseTask[] tasks = { ac };
        String[] data = { "" };
        StringWriter result = cm.run(tasks, data, true);
        System.out.println("Salida: " + result);
        System.out.println("Esperado: [SQL:Duplicate column name 'foto';fin]");
    }

    // ----------- ConsultaSimple -----------

    static void testConsultaSimple_ASC() {
        System.out.println("\n=== testConsultaSimple_ASC ===");

        ConsultaSimple cs = new ConsultaSimple();
        DataBaseTask[] tasks = { cs };
        String[] data = { "ASC" };
        StringWriter result = cm.run(tasks, data, true);
        System.out.println("Salida: " + result);
        System.out.println("Esperado: [fin]");

        System.out.println("Profesores (ordenados ASC por apellido1):");
        printResultado(cs);
        System.out.println("Esperado: 8 filas, primero Garcia, ultimo Torres");
    }

    static void testConsultaSimple_DESC() {
        System.out.println("\n=== testConsultaSimple_DESC ===");

        ConsultaSimple cs = new ConsultaSimple();
        DataBaseTask[] tasks = { cs };
        String[] data = { "DESC" };
        StringWriter result = cm.run(tasks, data, true);
        System.out.println("Salida: " + result);

        System.out.println("Profesores (ordenados DESC por apellido1):");
        printResultado(cs);
        System.out.println("Esperado: 8 filas, primero Torres, ultimo Garcia");
    }

    static void testConsultaSimple_caseInsensitive() {
        System.out.println("\n=== testConsultaSimple_caseInsensitive ===");

        ConsultaSimple cs = new ConsultaSimple();
        DataBaseTask[] tasks = { cs };
        String[] data = { "asc" };  // minusculas
        StringWriter result = cm.run(tasks, data, true);
        System.out.println("Salida: " + result);

        System.out.println("Profesores:");
        printResultado(cs);
        System.out.println("Esperado: igual que ASC, 8 filas");
    }

    static void testConsultaSimple_dataInvalida() {
        System.out.println("\n=== testConsultaSimple_dataInvalida ===");

        ConsultaSimple cs = new ConsultaSimple();
        DataBaseTask[] tasks = { cs };
        String[] data = { "loquesea" };
        StringWriter result = cm.run(tasks, data, true);
        System.out.println("Salida: " + result);
        System.out.println("Esperado: [Task:ordenando;...;fin]");

        System.out.println("Profesores:");
        printResultado(cs);
        System.out.println("Esperado: lista vacia o null (no se ejecuto la consulta)");
    }

    // ----------- ConsultaConFiltro -----------

    static void testConsultaConFiltro_OK() {
        System.out.println("\n=== testConsultaConFiltro_OK (filtro='SQL') ===");

        ConsultaConFiltro cf = new ConsultaConFiltro();
        DataBaseTask[] tasks = { cf };
        String[] data = { "SQL" };
        StringWriter result = cm.run(tasks, data, true);
        System.out.println("Salida: " + result);
        System.out.println("Esperado: [fin]");

        System.out.println("Profesores que imparten modulos con 'SQL' en el titulo:");
        printResultado(cf);
        System.out.println("(Cada fila lleva 'curso_id-titulo' como dato adicional)");
    }

    static void testConsultaConFiltro_dataVacia() {
        System.out.println("\n=== testConsultaConFiltro_dataVacia ===");

        ConsultaConFiltro cf = new ConsultaConFiltro();
        DataBaseTask[] tasks = { cf };
        String[] data = { "" };
        StringWriter result = cm.run(tasks, data, true);
        System.out.println("Salida: " + result);
        System.out.println("Esperado: [Task:filtro vacio;...;fin]");
    }

    static void testConsultaConFiltro_sinResultados() {
        System.out.println("\n=== testConsultaConFiltro_sinResultados ===");

        ConsultaConFiltro cf = new ConsultaConFiltro();
        DataBaseTask[] tasks = { cf };
        String[] data = { "ZZZ_no_existe" };
        StringWriter result = cm.run(tasks, data, true);
        System.out.println("Salida: " + result);
        System.out.println("Esperado: [fin]");

        System.out.println("Profesores:");
        printResultado(cf);
        System.out.println("Esperado: lista vacia (no nula)");
    }

    static void testConsultaConFiltro_sqlInjection() {
        System.out.println("\n=== testConsultaConFiltro_sqlInjection ===");
        System.out.println("Probando que PreparedStatement protege contra inyeccion");

        ConsultaConFiltro cf = new ConsultaConFiltro();
        DataBaseTask[] tasks = { cf };
        // Si concatenara el data en la SQL, esto daria error de sintaxis SQL.
        // Con PreparedStatement, lo trata como un literal y simplemente
        // busca un titulo que contenga ese texto raro (no encontrara nada).
        String[] data = { "x'; DROP TABLE imparte; --" };
        StringWriter result = cm.run(tasks, data, true);
        System.out.println("Salida: " + result);
        System.out.println("Esperado: [fin], y la tabla imparte sigue intacta");
    }

    // ----------- Combinado -----------

    static void testCombinado() {
        System.out.println("\n=== testCombinado: las 3 tareas seguidas ===");
        System.out.println("(Antes ejecuta: ALTER TABLE edificio DROP COLUMN foto;)");

        AddColumn add = new AddColumn();
        ConsultaSimple cs = new ConsultaSimple();
        ConsultaConFiltro cf = new ConsultaConFiltro();

        DataBaseTask[] tasks = { add, cs, cf };
        String[] data = { "", "ASC", "SQL" };
        StringWriter result = cm.run(tasks, data, true);
        System.out.println("Salida: " + result);

        System.out.println("\nConsultaSimple:");
        printResultado(cs);

        System.out.println("\nConsultaConFiltro:");
        printResultado(cf);
    }

    // ============================================================
    //  HELPERS
    // ============================================================

    static void printResultado(ConsultaConResultado<Properties> consulta) {
        java.util.ArrayList<Properties> lista = consulta.get();
        if (lista == null) {
            System.out.println("  (resultado es null)");
            return;
        }
        if (lista.isEmpty()) {
            System.out.println("  (lista vacia)");
            return;
        }
        for (Properties p : lista) {
            System.out.println("  " + p);
        }
    }

    // ============================================================
    //  MAIN — descomenta los tests que quieras correr
    // ============================================================

    public static void main(String[] args) {
        // --- AddColumn ---
        //testAddColumn_OK();
        //testAddColumn_yaExiste();

        // --- ConsultaSimple ---
        //testConsultaSimple_ASC();
        // testConsultaSimple_DESC();
        // testConsultaSimple_caseInsensitive();
        // testConsultaSimple_dataInvalida();

        // --- ConsultaConFiltro ---
        // testConsultaConFiltro_OK();
        // testConsultaConFiltro_dataVacia();
        // testConsultaConFiltro_sinResultados();
        // testConsultaConFiltro_sqlInjection();

        // --- Combinado ---
        // testCombinado();
    }
}

