import es.upm.babel.cclib.Monitor;

// CTAD Barcaza
public class BarcazaMon implements Barcaza {

    // estado del recurso
	int estado;
	int numTru;
	int numBri;
	

    // declaración de monitores y conditions
    //
    // 
    // 
    //

    public BarcazaMon() {
	// 
	//
	//
	//
	//
    }

    public void embarcar (int nacion) {
	// 
	// CPRE: self = (Embarcando,o)     /\
	//       o(Bribon) + o(Truhan) < 4 /\
	//       o(Bribon) + o(Truhan) = 3 => o(nacion) mod 2 = 0
	// 
	// 
	// 
	// 
	// 
	// 
	// POST: self_pre = (Embarcando,o) /\
	//       self     = (Embarcando,o(+){nacion -> o(nacion) + 1}
	// 
	// 
	// 
    }

    public void desembarcar (int nacion) {
	// 
	// PRE : self = (_,o) /\ o(nacion) > 0
	// dejad el tratamiento de las PREs para lo último
	// CPRE: self = (Desembarcando,_)
	// 
	// 
	// 
	// POST: self_pre = (Desembarcando,o)                 /\
	//       self = (s,o(+){nacion -> o(nacion) - 1}      /\
	//       o(Bribon)+o(Truhan) = 1 => s = Embarcando    /\
	//       o(Bribon)+o(Truhan) > 1 => s = Desembarcando /\ 
	// 
	// 
	// 
	// 
	// 
	// 
	// 
	// 
    }

    public void zarpar () {
	//
	// CPRE: self = (Embarcando,{Bribon->b,Truhan->t}) /\ b+t = 4
	//
	//
	//
	//
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
	// POST: self_pre = (Navegando,o) /\ self = (Desembarcando,o)
	// 
	//
	//
    }
    
    private void desbloqueo_generico () {
	// 
    	// 
	// 
	// 
	// 
	// 
	// 
	// 
	// 
	// 
	// 
	// 
	// 
	// 
	// 
	// 
	// 
	// 
	// 
	// 
	// 
	// 
	// 
	// 
	// 
	// 
	// 
	// 
    }

} 
