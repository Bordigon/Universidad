package entregas.concurrencia.p9;
import es.upm.babel.cclib.Monitor;

// CTAD Barcaza
public class BarcazaMon implements Barcaza {

    // estado del recurso
	private int estado;
	private int numTru;
	private int numBri;
	

    // declaración de monitores y conditions
	private Monitor control = new Monitor();
	//Si el barco tiene 4 personas no pueden haber 1 y 3 de diferentes nacionalidades

	private final Monitor.Cond puedeEmbarcarTru = control.newCond();
	private final Monitor.Cond puedeEmbarcarBri = control.newCond();
	private final Monitor.Cond puedeZarpar = control.newCond();
	private final Monitor.Cond puedeDesembarcarTru = control.newCond();
	private final Monitor.Cond puedeDesembarcarBri = control.newCond();	


    public BarcazaMon() {
	this.estado = 0;
	this.numTru = 0;
	this.numBri = 0;
    }

	
	private boolean puedeEmbarcar(int nacion){
		boolean result = false;
		if(estado != 0)
			result = false;
		else if(nacion!=0 && nacion!=1)
			result = false;
		else if(numTru+numBri == 4)
			result = false;
		else if(numTru+numBri == 3){
			if(nacion == 0 && numTru%2==1)
				result = true;
			else if(nacion == 1 && numBri%2 == 1){
				result = true;
			}		
		}else result = true;
		return result;
	}

    public void embarcar (int nacion) {
	// 
	// CPRE: self = (Embarcando,o)     /\
	//       o(Bribon) + o(Truhan) < 4 /\
	//       o(Bribon) + o(Truhan) = 3 => o(nacion) mod 2 = 0
	control.enter();

		while(!puedeEmbarcar(nacion)){
		if(nacion == 0)
			puedeEmbarcarTru.await();
		else if(nacion == 1)
			puedeEmbarcarBri.await();
		}
		
		if(nacion == 0){
			numTru++;
		} else if(nacion == 1){
			numBri++;
		}

		desbloqueo_generico();

	control.leave();

	// 
	// POST: self_pre = (Embarcando,o) /\
	//       self     = (Embarcando,o(+){nacion -> o(nacion) + 1}
	// 
	// 
	// 
    }



	private boolean puedeDesembarcarMetodo(int nacion){
		boolean result = false;
		
		if(estado != 2)
			result = false;
		else if(nacion == 0){
			if(numTru > 0)
				result = true;
		}
		else if(nacion == 1){
			if(numBri > 0)
				result = true;
		}		
		
		return result;
	}


    public void desembarcar (int nacion) {
	// 
	// PRE : self = (_,o) /\ o(nacion) > 0
	// dejad el tratamiento de las PREs para lo último
	// CPRE: self = (Desembarcando,_)

	control.enter();

		if(nacion == 0 && numTru == 0 || nacion == 1 && numBri == 0){
			control.leave();
			throw new IllegalArgumentException("No hay viajeros de esa nacionalidad");
		}	
		while(!puedeDesembarcarMetodo(nacion)){
			if(nacion == 0)
				puedeDesembarcarTru.await();
			else if(nacion == 1)
				puedeDesembarcarBri.await();
		}
		
		if(nacion == 0){
			numTru--;
		}else if(nacion == 1){
			numBri--;
		}

		if(numTru+numBri == 0)
			estado = 0;
		desbloqueo_generico();

	control.leave();	

	// POST: self_pre = (Desembarcando,o)                 /\
	//       self = (s,o(+){nacion -> o(nacion) - 1}      /\
	//       o(Bribon)+o(Truhan) = 1 => s = Embarcando    /\
	//       o(Bribon)+o(Truhan) > 1 => s = Desembarcando /\ 
	// 

    }



    public void zarpar () {
	//
	// CPRE: self = (Embarcando,{Bribon->b,Truhan->t}) /\ b+t = 4
	//

	control.enter();
		while(!(estado == 0 && (numBri + numTru == 4))){
			puedeZarpar.await();
		}
		estado = 1;
		desbloqueo_generico();

	control.leave();

	// POST: self_pre = (_,o) /\ self = (Navegando,o)
	// 
	// 
	//
    }

    public void amarrar () {
	// 
	// PRE : self = (Navegando,_)
	// TO DO: tratamiento de excepciones
	// CPRE: Cierto
	control.enter();
		if(estado!=1){
			control.leave();
			throw new IllegalArgumentException("El barco no está navegando");
		}estado = 2;
		desbloqueo_generico();

	control.leave();
	// POST: self_pre = (Navegando,o) /\ self = (Desembarcando,o)
	// 
	//
	//
    }
    
    private void desbloqueo_generico () {
		if(puedeEmbarcarTru.waiting()>0 && puedeEmbarcar(0)){
			puedeEmbarcarTru.signal();
		}
		else if(puedeEmbarcarBri.waiting()>0 && puedeEmbarcar(1)){
			puedeEmbarcarBri.signal();
		}
		else if(puedeDesembarcarTru.waiting()>0 && puedeDesembarcarMetodo(0)){
			puedeDesembarcarTru.signal();
		}
		else if(puedeDesembarcarBri.waiting()>0 && puedeDesembarcarMetodo(1)){
			puedeDesembarcarBri.signal();
		}
		else if(puedeZarpar.waiting()>0){
			if(estado == 0 && (numBri + numTru == 4))
				puedeZarpar.signal();
		}
	    }

	
} 
