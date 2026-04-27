// Sauna de dos intensidades
// *** NO MODIFICAR, salvo número de usuarios, etc. ***
package entregas.concurrencia.p6;

class CC_06_Sauna {

    // Probad distintos valores para la simulación
    static final int NUM_ITERS = 100;
    static final int NUM_ALTA  = 2;
    static final int NUM_MEDIA = 4;
    static final int TIEMPO_CLASE = 3000; // ms.
    static final int TIEMPO_SAUNA = 500;  // ms.

    // CLASE USUARIO
    static class Usuario extends Thread {
	// vamos a escribir números en una cola,
	// de dos en dos, a partir de uno inicial
	// constructor: pasamos referencia a objeto compartido
	
	int tipo; // el tipo de usuario de sauna:
	          // temperatura ALTA o MEDIA
	Sauna sauna; // referencia a la sauna compartida
	
	Usuario (int tipo, Sauna sauna) {
	    this.tipo = tipo;
	    this.sauna = sauna;
	}

	// Los profes dan clase y luego van a la sauna
	// :-O
	public void run() {
	    for (int i = 0; i < NUM_ITERS; i++) {
		// dar_clase();
		try {Thread.sleep(TIEMPO_CLASE);} catch (Exception e) {}
		this.sauna.entrar(this.tipo);
		System.out.printf("Usuario de tipo %s entrando \n",
				  (this.tipo == Sauna.ALTA ? "caliente" : "templado"));
		try {Thread.sleep(TIEMPO_SAUNA);} catch (Exception e) {}
		System.out.printf("Usuario de tipo %s saliendo \n",
				  (this.tipo == Sauna.ALTA ? "caliente" : "templado"));
		this.sauna.salir(this.tipo);
	    }
	}
    }// END CLASS USUARIO

    // PRINCIPAL
    public static final void main(final String[] args)
       throws InterruptedException {

        // Sauna compartida
        Sauna sauna = new Sauna();

        // Usuarios "HOT"
        Usuario[] usuarios_alta = new Usuario[NUM_ALTA];

        // Usuarios "MILD"
	Usuario[] usuarios_media = new Usuario[NUM_MEDIA];

        // Creación y arranque de los distintos usuarios
        for (int i = 0; i < NUM_ALTA; i++) {
            usuarios_alta[i] = new Usuario(Sauna.ALTA, sauna);
	    usuarios_alta[i].start();
        }

	for (int i = 0; i < NUM_MEDIA; i++) {
            usuarios_media[i] = new Usuario(Sauna.MEDIA, sauna);
	    usuarios_media[i].start();
        }

        // Espera hasta la terminación de los usuarios
        try {
	    for (int i = 0; i < NUM_ALTA; i++) {
		usuarios_alta[i].join();
	    }
	    for (int i = 0; i < NUM_MEDIA; i++) {
		usuarios_media[i].join();
	    }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit (-1);
        }

	System.exit(0);
    }
}