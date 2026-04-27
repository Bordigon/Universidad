// Carrera.java
//
// La forma mas elemental de provocar una
// situacion de carrera en Java
//
// Julio Mariño -- 2023

package entregas.concurrencia.p2;

public class CC_02_Carrera{

    // Vamos a provocar una carrera haciendo que varios
    // hilos realicen incrementos o decrementos sobre una variable
    // entera compartida de manera simultánea.

    // La forma mas sencilla de que varios hilos accedan a la misma
    // posición de memoria es declarando una variable _global_, por
    // ejemplo haciéndola de clase:
    static volatile int contador = 0;

    // El "volatile" lo necesitamos para desactivar el _caching_ sobre
    // esta variable y que todos los incrementos/decrementos se
    // vuelquen inmediatamente a memoria RAM.

    // Alternativamente, podemos hacer que dos hilos accedan a las
    // mismas posiciones de memoria creando un objeto con atributos y
    // pasando a dichos hilos referencias a dicho objeto, e.g. en sus
    // constructores. (De hecho, en la práctica este metodo es mas
    // elegante que el de las variables globales, que usamos aquí por
    // concisión.)

    // La idea va a ser la siguiente: para aumentar la probabilidad de
    // carrera lanzaremos _muchos_ hilos, tanto "incrementadores" como
    // "decrementadores", en igual cantidad y cada uno de ellos
    // intentará completar exactamente el mismo número de incrementos
    // o decrementos.
    // Por tanto, de no haber carrera, el valor final del contador
    // debería ser 0.

    // Cuántos incrementadores/decrementadores:
    static final int NUM_THREADS = 1000;
    // Cuántas operaciones por hilo:
    static final int NUM_OPS     = 1000;

    // Los incrementadores incrementan:
    public static class Incrementador extends Thread {
        public void run() {
            for (int i = 0; i < NUM_OPS; i++) {
                contador++;
            }
        }
    }

    // Y los decrementadores decrementan:
    public static class Decrementador extends Thread {
        public void run() {
            for (int i = 0; i < NUM_OPS; i++){
                contador--;
            }
        }
    }

    // El hilo principal creará y lanzará NUM_THREADS Incrementadores,
    // NUM_THREADS Decrementadores, esperará a que terminen y
    // finalmente imprimirá el valor del contador.
    public static void main (String[] args){

	// Usaremos dos vectores para llevar cuenta de los hilos
	// creados:
        Incrementador[] incrementador = new Incrementador[NUM_THREADS];
	Decrementador[] decrementador = new Decrementador[NUM_THREADS];	

	// Creación:
	for (int i = 0; i < NUM_THREADS; i++) {
	    incrementador[i] = new Incrementador();
	    decrementador[i] = new Decrementador();
	}
	
	// Lanzamiento:
	for (int i = 0; i < NUM_THREADS; i++) {
	    incrementador[i].start();
	    decrementador[i].start();
	}

	// Esperamos a que terminen:
	for (int i = 0; i < NUM_THREADS; i++) {
	    try {
		incrementador[i].join();
		decrementador[i].join();
	    } catch(InterruptedException e) {
                e.printStackTrace();
            }
	}

	// Imprimimos resultado final:
	System.out.println ("contador == " + contador);
    }// END main()
}// END Carrera