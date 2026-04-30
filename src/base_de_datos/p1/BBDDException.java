package cursos;

public class BBDDException extends Exception {

    private String when;

    /**
     * Permite construir una BBDDException a partir de
     * cualquier otra Exception, pero indicando un dato
     * adicional, when, a la Exception que se habia producido.
     *
     * @param e, la Exception original
     * @param when, el momento en que se ha producido
      */
    public BBDDException (Exception e, String when) {
        super(e);
        this.when = when;
    }

    /**
     * Devuelve when, el dato que se indica adicionalmente
     * a la Exception original.
     * @return when
     */
    public String when() {
        return when;
    }

}
