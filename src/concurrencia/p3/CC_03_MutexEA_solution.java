package entregas.concurrencia.p3;

class CC_03_MutexEA_solution {
    static final int NUM_OPS = 10000;

    // Generador de números aleatorios para simular tiempos de
    // ejecución
    // static final java.util.Random RNG = new java.util.Random(0);

    // Variable compartida
    volatile static int n = 0;

    // Variables compartidas para asegurar exclusión mutua
	volatile static boolean pedirDecremento = false; 
	volatile static boolean pedirIncremento = false; 
	volatile static char turno = ' '; // Si pone i se cede el turno al incrementados, si pone d al decrementador.
    // ****************************************************************
    
    
    // Sección no crítica
    static void no_sc() {
	// Podéis jugar con prints, retardos, etc.
	// System.out.println("No SC");
	// try {
	//    // No más de 2ms
	//    Thread.sleep(RNG.nextInt(3));
	// }
	// catch (Exception e) {
	//    e.printStackTrace();
	// }
    }

    // Secciones críticas
    static void sc_inc() {
	// System.out.println("Incrementando");
		n++;
    }
    
    static void sc_dec() {
	// System.out.println("Decrementando");
		n--;
    }

    // La labor del proceso incrementador es ejecutar no_sc() y luego
    // sc_inc() NUM_OPS veces.
    static class Incrementador extends Thread {
		public void run () {
			for (int i = 0; i < NUM_OPS; i++) {
			// Sección no crítica
			no_sc(); // ** NO TOCAR **
			
			// Protocolo de acceso a la sección crítica
			pedirIncremento=true; // Petición del incrementador para poder realizar su función.
			turno='d'; // Se cede el turno al decrementador
			
			while (pedirDecremento && turno=='d') {} 
			// *****************************************
			
			// Sección crítica
			sc_inc(); // ** NO TOCAR **
			
			// Protocolo de salida de la sección crítica
			pedirIncremento = false;
			// *****************************************
			}
		}
    }

    // La labor del proceso incrementador es ejecutar no_sc() y luego
    // sc_dec() durante NUM_OPS asegurando exclusión mutua sobre
    // sc_dec().
    static class Decrementador extends Thread {
	public void run () {
	    for (int i = 0; i < NUM_OPS; i++) {
		// Sección no crítica
		no_sc(); // ** NO TOCAR **
		
		// Protocolo de acceso a la sección crítica
		pedirDecremento=true; // Petición del decrementador para poder realizar su función.
		turno='i'; // Se cede el turno al incrementador
		
		while (pedirIncremento && turno=='i') {} 
		// ****************************************
		
		// Sección crítica
		sc_dec(); // ** NO TOCAR **
		
		// Protocolo de salida de la sección crítica
		pedirDecremento=false;
		// *****************************************
	    }
	}
    }
	
    public static final void main(final String[] args)
	throws InterruptedException {
	// Creamos las tareas
	Thread t1 = new Incrementador();
	Thread t2 = new Decrementador();
	
	// Las ponemos en marcha
	t1.start();
	t2.start();
	
	// Esperamos a que terminen
	t1.join();
	t2.join();
	
	// Simplemente se muestra el valor final de la variable:
	System.out.println(n);
    }
}