package entregas.concurrencia.p4;
import java.util.ArrayList;
import es.upm.babel.cclib.Semaphore;


public class CC_04_ColaSem{
    static int P = 5;     // número de hilos PRODUCTORES
	volatile static int contador = 0;
	static Semaphore fabrica = new Semaphore(1);
	static Semaphore stock = new Semaphore(1);

    private static class Productor extends Thread {
        private int number; 
        private ArrayList<Integer> cola;

        
        public Productor(int number, ArrayList<Integer> cola) {
            this.number = number; 
            this.cola = cola; 

        }

        public void run() {
            for (int i = 0; i < 13; i++) {
                int valor = i * P + number;
		contador++;
		fabrica.await(); 
		// --------------> seccion critica <-------------
                cola.add(valor);
		//System.out.println(valor +" producido por " + number + " contador total "+ contador);
		fabrica.signal(); // para que otro hilo pueda producir
		stock.signal(); // para que el consumidor sepa que ya puedo 
            }
        }
    }

    private static class Consumidor extends Thread {
        private ArrayList<Integer> cola; 
        private boolean productoresVivos = true;

        public Consumidor(ArrayList<Integer> cola) {
            this.cola = cola; 
        }

        public void terminar() {
            productoresVivos = false;
        }

        public void run() { 
            while (productoresVivos || !cola.isEmpty()) {
                Integer x = null; 
		stock.await();
                if (!cola.isEmpty()) {
                    x = cola.remove(0);
                }

                if (x != null) {
			contador++;
                    //System.out.println("Consumido: " + x);
                }
            }
        }
    }

    public static void main (String[] args) {
		ArrayList<Integer> cola = new ArrayList<>();
        Productor[] productores = new Productor[P];
        Consumidor consumidor = new Consumidor(cola);
		consumidor.start();

        for (int i = 0; i < P; i++) {
            productores[i] = new Productor(i, cola);
            productores[i].start();
        }

        for (int i = 0; i < P; i++) {
            try {
                productores[i].join();
            } catch(InterruptedException e) {
                System.out.println("Un productor fue interrumpido");
            }
        }

        consumidor.terminar();

        try {
            consumidor.join();
        } catch (InterruptedException e) {
            System.out.println("El consumidor fue interrumpido");
        }

        System.out.println("FIN DEL PROGRAMA " + contador);
    }
}