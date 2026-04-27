// TODO: importar la clase de los semáforos.
// 
package entregas.concurrencia.p6;
import es.upm.babel.cclib.Semaphore;

// Sauna de dos intensidades con control de acceso

class Sauna {
    // Dos intensidades:
    static final int ALTA  = 0;
    static final int MEDIA = 1;
    // La capacidad es 3, de momento
    static final int CAPAC = 3;
    
    // TODO: declaración e inicialización de los semáforos
    // necesarios
    // 
	private static Semaphore tipoAlto = new Semaphore(CAPAC);
	private static Semaphore tipoMedia = new Semaphore(CAPAC);	  
	private static Semaphore acceder = new Semaphore(1);		  //exclusion mutua para entrar y salir
	private static Semaphore mutuaEx = new Semaphore(1);		  //es como acceder pero para una parte más específica

    // TODO: otras variables de estado
    // 
	private volatile static int cantidad = 0;
    
    public Sauna() {
    }
    
    public void entrar(int tipo) {
	// TODO: protocolo de acceso a la sauna. Debe bloquear si:
	// - Ya hay tres personas en la sauna
	// - Hay personas de un grupo distinto a tipo

	/*
	  Este procedimiento tiene la particularidad de que si en el sauna hay un tipo, 
	  en el momento que llegue alguien de un tipo diferente, todos los canales de entrada se bloquearan
	  hasta que el sauna se haya vaciado por completo y ya pueda entrar el tipo diferente 
	*/


        switch (tipo) {
	case ALTA:
	    // TODO:
	    // 
		//se encarga de que no puedan entrar dos variables tipo ALTA y MEDIA al mismo tiempo
		mutuaEx.await();	

		// este es el semáforo encargado de verificar que no se metan dos de tipos diferentes en el mismo sauna
		tipoAlto.await();
  	
		//acceder para punto crítico del hilo
		acceder.await();    	
		mutuaEx.signal();

		//le quita todos los permisos al otro tipo, para q no puedan entrar
		if(cantidad==0){
		for(int t = 0; t<CAPAC; t++)
			tipoMedia.await();		
		}
		cantidad++;
		acceder.signal();

	    break;
	case MEDIA:
	    // TODO:
	    // 
		mutuaEx.await();	
		tipoMedia.await(); 
		acceder.await();    
		mutuaEx.signal();

		if(cantidad==0){
		for(int t = 0; t<CAPAC; t++)
			tipoAlto.await();		
		}
		cantidad++;
		acceder.signal();


	    break;
	}
    }

    public void salir(int tipo) {
	// TODO: protocolo de salida de la sauna. Debe anotarse que
	// sale alguien y quizá desbloquear a proceso(s) en espera.
	switch (tipo) {
	case ALTA:
	    // TODO:
	    // 
		acceder.await();
		if(cantidad > 0){
			cantidad--;
		}

		//como ya el sauna está vacío se le devuelven los permisos al otro tipo
		if(cantidad==0){
			for(int t = 0; t<CAPAC; t++)	
				tipoMedia.signal();
		}
		tipoAlto.signal();
		acceder.signal();

	    break;
	case MEDIA:
	    // TODO:
	    // 
		acceder.await();
		if(cantidad > 0){
			cantidad--;
		}
		if(cantidad==0){
			for(int t = 0; t<CAPAC; t++)
				tipoAlto.signal();
		}
		tipoMedia.signal();
		acceder.signal();

	    break;
	}
    }
}