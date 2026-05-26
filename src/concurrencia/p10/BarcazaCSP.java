import org.jcsp.lang.*;
//import es.upm.babel.cclib.ConcIO;

// CTAD Barcaza
public class BarcazaCSP implements Barcaza, CSProcess {

    // Interfaz Barcaza
    
    // tipo Nacionalidad = Truhan | Bribon
    // public static final int NACIONES = 2;
    
    // public static final int TRUHAN = 0;
    // public static final int BRIBON = 1;

    // tipo Estado = Embarcando | Navegando | Desembarcando
    // public static final int EMBARCANDO    = 0;
    // public static final int NAVEGANDO     = 1;
    // public static final int DESEMBARCANDO = 2;
    
    // canales para comunicación con el servidor
    // un canal de embarcar por cada nacionalidad
    // (replicación de canales)
    private Any2OneChannel[] chEmbarcar;
    // un solo canal para desembarcar
    private Any2OneChannel   chDesembarcar;
    // zarpar
    private One2OneChannel   chZarpar;
    // amarrar
    private One2OneChannel   chAmarrar;
    
    // constructor
    public BarcazaCSP() {
	// OJO: estado del recurso al servidor
	// solo creamos los canales:
	chDesembarcar = Channel.any2one();
	chZarpar      = Channel.one2one();
	chAmarrar     = Channel.one2one();
	chEmbarcar    =
	    new Any2OneChannel[Barcaza.NACIONES];
	chEmbarcar[Barcaza.TRUHAN] = Channel.any2one();
	chEmbarcar[Barcaza.BRIBON] = Channel.any2one();
    } 

    public void embarcar (int nacion) {
	// replicación de canales
	chEmbarcar[nacion].out().write(null);
    }

    public void desembarcar (int nacion) {
	chDesembarcar.out().write(nacion);
    }

    public void zarpar () {
	chZarpar.out().write(null);
    }

    public void amarrar () {
	chAmarrar.out().write(null);
    }

    // //////////////////////////////
    // SERVIDOR
    public void run() {
	// Estado del recurso aquí:	
	// 
	//
	//
	//

	// Entradas de la select
	AltingChannelInput[] entradas =
	    new AltingChannelInput[Barcaza.NACIONES+3];

	// Nombres simbólicos:
	final int EMB_TRUHAN  = 0; // =Barcaza.TRUHAN
	final int EMB_BRIBON  = 1; // =Barcaza.BRIBON
	final int DESEMBARCAR = 2;
	final int ZARPAR      = 3;
	final int AMARRAR     = 4;

	entradas[EMB_TRUHAN] =
	    chEmbarcar[Barcaza.TRUHAN].in();
	entradas[EMB_BRIBON] =
	    chEmbarcar[Barcaza.BRIBON].in();
	entradas[DESEMBARCAR] = chDesembarcar.in();
	entradas[ZARPAR]      = chZarpar.in();
	entradas[AMARRAR]     = chAmarrar.in();

	// recepción alternativa
	Alternative servicios = new Alternative(entradas);
	// sincronización condicional en la select
	boolean[] sincCond = new boolean[Barcaza.NACIONES+3];
	sincCond[AMARRAR] = true;
	
	// bucle de servicio
	while (true) {
	    // sincronización condicional
	    // embarcar(nacion)
	    // CPRE: self = (Embarcando,o)     /\
	    //       o(Bribon) + o(Truhan) < 4 /\
	    //       o(Bribon) + o(Truhan) = 3 => o(nacion) mod 2 = 1    
	    // 
	    // 
	    // 
	    // 
	    // 
	    // 
	    // 
	    // 
	    // 
	    // desembarcar
	    // CPRE: self = (Desembarcando,_)
	    // 
	    // zarpar
	    // CPRE: self = (Embarcando,{Bribon->b,Truhan->t}) /\ b+t = 4
	    // 
	    // 
	    //
	    // amarrar
	    // CPRE: Cierto
	    // 

	    // la select
	    switch (servicios.fairSelect(sincCond)) {
	    case EMB_TRUHAN:
		// 
		// 
		break;
	    case EMB_BRIBON:
		// 
		// 
		break;
	    case DESEMBARCAR:
		// 
		// 
		// 
		//
		break;
	    case ZARPAR:
		//
		// 
		break;
	    case AMARRAR:
		//
		//
		break;
	    }//switch 
	}//bucle de servicio
    }//run servidor
}//