// CTAD Barcaza
public interface Barcaza {

    // tipo Nacionalidad = Truhan | Bribon
    public static final int NACIONES = 2;
    public static final int TRUHAN = 0;
    public static final int BRIBON = 1;

    // tipo Estado = Embarcando | Navegando | Desembarcando
    public static final int EMBARCANDO    = 0;
    public static final int NAVEGANDO     = 1;
    public static final int DESEMBARCANDO = 2;

    public void embarcar (int nacion);

    public void desembarcar (int nacion);

    public void zarpar ();

    public void amarrar ();

} 
