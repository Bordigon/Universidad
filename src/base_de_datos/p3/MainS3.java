package cursos;
import entregas.Env;

public class MainS3 {


    public static void limpieza() {
        // Crea el gestor de conexiones
        BBDDManager cm = new BBDDManager(Env.getUser(),Env.getPass());

        // Crear las tareas
        DataBaseTask[] tasks = {
            new DropTable("edificio")
        };
        String[] data = { "" };

        // Llamar a run:
        StringWriter result = cm.run(tasks, data, true);
        System.out.println(result);
    }




    // Comprobar
    public static void test1() {
        // Crea el gestor de conexiones
        BBDDManager cm = new BBDDManager(Env.getUser(),Env.getPass());

        // Crear las tareas
        DataBaseTask[] tasks = {
            new CreateTable("edificio")
        };
        String[] data = { "" };

        // Llamar a run:
        StringWriter result = cm.run(tasks, data, true);
        System.out.println(result);
    }


    public static void test2() {
        // Crea el gestor de conexiones
        BBDDManager cm = new BBDDManager(Env.getUser(),Env.getPass());

        // Crear las tareas
        DataBaseTask[] tasks = {
            new InsertaUnaFilaImparte(),
            new InsertaImparteDesdeCSV()
        };
        String[] data = {
            "7, 3, 2, 4, 14/03/2025 ",
            "_imparte.csv"
        };

        // Llamar a run:
        StringWriter result = cm.run(tasks, data, true);
        System.out.println(result);
    }

    public static void test3() {
        // Crea el gestor de conexiones
        BBDDManager cm = new BBDDManager(Env.getUser(),Env.getPass());

        // Crear las tareas
        DataBaseTask[] tasks = {
            new AddColumn(),
        };
        String[] data = {""};

        // Llamar a run:
        StringWriter result = cm.run(tasks, data, true);
        System.out.println(result);
    }

    public static void main(String[] args) {
        //test1();
	test3();
    }
}


