package entregas.concurrencia.p4;
import java.util.Queue;
import java.util.ArrayDeque;
import es.upm.babel.cclib.Semaphore;

public class CC_04_ColaSem_solution{
	
	static final int P=6;
	static final int PRODUCCIONES=20;
	static final int CAPACIDAD=50;
	 
	static Queue<Integer> cola= new ArrayDeque<>();
	 
	static Semaphore seccionCritica= new Semaphore(1);
	static Semaphore numProducciones= new Semaphore(0);
	static Semaphore espacio= new Semaphore(CAPACIDAD);
	 
	public static void main(String[] args){
		 
		//Creación de P productores
		for(int i=0;i<P;i++){
			new Thread(new Productor(i)).start();
		}
		 
		 //Creación del Consumidor
		 new Thread(new Consumidor()).start();
	 }
	 
	 static class Productor implements Runnable{
		 private int id;
		 
		 public Productor(int id){
			 this.id=id;
		 }
		 
		 public void run(){
			for(int k=0;k<PRODUCCIONES;k++){
				int valor= k*P+id;
	
				espacio.await(); //¿Hay espacio para colocar la producción?--> Sino hay uqe esperar hasta que haya.
				seccionCritica.await(); //¿Hay algún productor (o está el consumidor) en su sección crítica? 
										// En caso afirmativo esperar hasta que no haya productores, ni tampoco este el Consumidor ejecutando su sección crítica.
				
		/************************Sección crítica*********************************/				
			
				cola.add(valor); //Adición de una producción
				System.out.println("Producido: "+valor+" por productor "+id);

		/************************Sección crítica*********************************/				
				seccionCritica.signal(); // Tras la ejecución de la sección critica se libera el semáforo para permitir la ejecución a otro productor o al consumidor.
				numProducciones.signal(); // Se notifica que existe una producción disponible.
			 }
		 }
	 }
	 
	  static class Consumidor implements Runnable{
		 public void run(){
			 int totAConsumir=P*PRODUCCIONES;
			 
			 for(int i=0;i<totAConsumir;i++){
				
				numProducciones.await(); //¿Hay alguna producción?--> Sino hay que esperar hasta que haya.
				seccionCritica.await(); //¿Hay algún productor en su sección crítica? 
										// En caso afirmativo esperar hasta que no haya productores ejecutando su sección crítica.

		/**********************Sección crítica***********************************/					

				int valor=cola.poll(); //Tras consumición, se elimina la producción.
				System.out.println("Consumido: "+valor);

		/************************Sección crítica********************************/	
	
				seccionCritica.signal(); // Tras la ejecución de la sección critica se libera el semáforo para permitir la ejecución a los productores.
				espacio.signal(); // Se notifica que la producción fue consumida.
			 }
			 
			 System.out.println("Consumidor terminado");
		 }
	 }
}