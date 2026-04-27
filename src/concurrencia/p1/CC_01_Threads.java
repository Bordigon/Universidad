package entregas.concurrencia.p1;

public class CC_01_Threads{



	private static class Hilo extends Thread{
		private int number;
		public Hilo(int number){
			this.number = number;
		}

		public void run(){
			System.out.println("Soy el hilo número"+number);
		}

	}


	private static class Contador extends Thread{
		private int number;
		private int time;
		public Contador(int number, int time){
			this.time = time*1000;
			this.number = number;
		}

		public void run(){
			for(int t = 0; t<number; t++){
				Hilo h = new Hilo(t);
				try{
				Thread.sleep(time);
				}catch(InterruptedException e){}
				h.start();

				try{
					h.join();
				}catch(InterruptedException e){}

			}
		}
	}

	
	public static void main(String[] args){

		Contador c = new Contador(4,2);
		c.start();
		
		try{c.join();}catch(InterruptedException e){}
		
	}

}