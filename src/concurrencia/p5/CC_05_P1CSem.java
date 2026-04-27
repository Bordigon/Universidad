// Productor-Consumidor con un almacén para un dato
// *** NO MODIFICAR SALVO Nº DE PRODUCTORES O CONSUMIDORES ***
// GRACEFUL DEATH EDITION
package entregas.concurrencia.p5;
class CC_05_P1CSem {

    // longitud de las secuencias de cada productor
    static final int NUM_ITERS = 100;
    // número de productores
    static final int N_PRODS = 3;

    // CLASE PRODUCTOR
    static class Productor extends Thread {
	// vamos a escribir números en una cola,
	// de NUM_PRODS en NUM_PRODS, a partir de uno inicial
	// constructor: pasamos referencia a objeto compartido
	
	int      cont; // valor a escribir
	Almacen1 alm;  // el sitio donde escribimos
	
	Productor (int ini, Almacen1 alm) {
	    this.cont = ini;
	    this.alm  = alm;
	}

	// escribimos en la cola e incrementamos,
	// y así hasta que nos aburramos.
	public void run() {
	    for (int i = 0; i < NUM_ITERS; i++) {
		this.alm.almacenar(this.cont);
		this.cont += N_PRODS;
	    }
	}
    }// END CLASS PRODUCTOR

    // CLASE CONSUMIDOR
    static class Consumidor extends Thread {

	// tendremos una referencia al objeto compartido
	// del que leemos
	Almacen1 alm;
	int id;

	Consumidor (int id, Almacen1 alm) {
	    this.id = id;
	    this.alm = alm;
	}
	
	public void run() {
	    // leemos sin parar...
	    while (true) {
		int valor = this.alm.extraer();
		// muerte por envenenamiento OJO!!!
		if (valor < 0) System.exit(0);
		// FIN CÓDIGO TÓXICO
		System.out.printf("[%d]: %d \n", this.id, valor);
	    }            
        }
    }// END CLASS CONSUMIDOR

    public static final void main(final String[] args)
       throws InterruptedException {
        // Número de productores y consumidores
        // Va a haber dos productores
	// y un número variable de consumidores
        final int N_CONSS = 2;

        // Almacen compartido
        Almacen1 almac = new Almacen1();

	// Array de productores
	Productor[] productores = new Productor[N_PRODS];
        // Array de consumidores
        Consumidor[] consumidores = new Consumidor[N_CONSS];

        // Creación de los productores
        for (int i = 0; i < N_PRODS; i++) {
            productores[i] = new Productor(i, almac);
        }

        // Creación de los consumidores
        for (int i = 0; i < N_CONSS; i++) {
            consumidores[i] = new Consumidor(i, almac);
        }

        // Lanzamiento de los productores
        for (int i = 0; i < N_PRODS; i++) {
            productores[i].start();
        }

        // Lanzamiento de los consumidores
        for (int i = 0; i < N_CONSS; i++) {
            consumidores[i].start();
        }

        // Espera hasta la terminación de productores
	for (int i = 0; i < N_PRODS; i++) {
	    productores[i].join();
	}

	// Envenenamos el almacén
	for (int i = 0; i < N_CONSS; i++) {
	    almac.almacenar(-666);
	}

	// Comprobamos que los consumidores han muerto
	for (int i = 0; i < N_CONSS; i++) {
	    consumidores[i].join();
	}
	
	//System.exit(0);
    }
}