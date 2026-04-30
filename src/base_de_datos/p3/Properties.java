
package cursos;

public class Properties {

    private String nombre;
    private String apellido1;
    private String apellido2;
    private String data; // Datos adicionales opcionales

    public Properties(String nombre, String apellido1, String apellido2) {
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
    }

    public Properties(String nombre, String apellido1, String apellido2, String data) {
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.data = data;
    }

    @Override
    public String toString() {
        if ( data == null || data.isEmpty() ) {
            return String.format("'%s':'%s':'%s'",
                nombre, apellido1, apellido2);
        }
        return String.format("'%s':'%s':'%s':'%s'",
            nombre, apellido1, apellido2, data);
    }
}