package cursos;
import entregas.Env;


public class MainS1 {

    // Comprobar
    public static void test() {
        // Crea el gestor de conexiones
        BBDDManager cm = new BBDDManager(Env.getUser(),Env.getPass());

        // Crear las tareas
        DataBaseTask[] tasks = {
            new CreateTable("hola")
        };
        String[] data = { "" };

        // Llamar a run:
        StringWriter result = cm.run(tasks, data, true);
        System.out.println(result);
    }

    public static void main(String[] args) {
       // test();
    }
}


