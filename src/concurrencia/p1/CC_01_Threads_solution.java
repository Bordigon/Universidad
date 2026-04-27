package entregas.concurrencia.p1;

public class CC_01_Threads_solution{
	 public static void main(String args[]){ 
		int max=100;
		Thread hilo[]= new Thread[max];
		
		for(int i=0;i<max;i++){
			hilo[i]= new Thread(new Hilo(i));
			hilo[i].start(); 
		}
		
		for(int i=1;i<max;i++){
			try{
				hilo[i].join();
			}
			catch(InterruptedException e){
				return;
			}
		}
		
		System.out.println("Todos mis hijos han muerto");
	}
} 

class Hilo implements Runnable{
	
	private int num;
	
	public Hilo(int num){
		this.num=num;
	}
	
	public void run() { 
        System.out.println("Hola soy el hilo "+num);
		
		try{
				Thread.sleep(100);
			}
		catch(InterruptedException e){
			return;
		}
		
		System.out.println("Hola soy el hilo "+num+" y me muero"); 
    } 
}