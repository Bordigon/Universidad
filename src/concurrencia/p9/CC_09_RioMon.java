package entregas.concurrencia.p9;
// CC_09_RioMon.java
//
// Cruzando el río.
// Versión "poco realista"

public class CC_09_RioMon {

    static final int NUM_VIAJES_P = 100; // OJO!!
    static final int NUM_VIAJES_B = 10;  // OJO!!
    // PASAJEROS
    // Los pasajeros hacen llamadas a embarcar y desembarcar
    // No llevamos cuenta de en qué orilla está cada uno...
    static class Pasajero extends Thread {
	private int nacionalidad;
	private Barcaza barcaza; // make it generic
	private String str_nacion; // para imprimir

	// constructor
	public Pasajero (int nacionalidad, Barcaza barcaza) {
	    this.nacionalidad = nacionalidad;
	    this.barcaza = barcaza;
	    if (nacionalidad == Barcaza.BRIBON) {
		this.str_nacion = "bribón";
	    } else {
		this.str_nacion = "truhán";
	    }
	}

	public void run () {
	    for (int i = 0; i < NUM_VIAJES_P; i++) {
		barcaza.embarcar(nacionalidad);
		System.out.printf("Soy un %s y he embarcado.\n", this.str_nacion);
		barcaza.desembarcar(nacionalidad);
		System.out.printf("Soy un %s y he desembarcado.\n", this.str_nacion);
	    }
	}
    }

    // Habrá un hilo barquero y se dedica a zarpar en cuanto le dejan
    // y a amarrar en cuanto llega a puerto
    static class Barquero extends Thread {

	private Barcaza barcaza; // generic
	private int     orilla;
	private String[] str_pais = new String[2]; // !!

	public Barquero (Barcaza barcaza) {
	    this.barcaza = barcaza;
	    // comenzamos jornada en Bribonia
	    this.orilla = Barcaza.BRIBON;
	    // nombres de las orillas
	    str_pais[Barcaza.BRIBON] = "Bribonia";
	    str_pais[Barcaza.TRUHAN] = "Truhania";
	}
	
	public void run () {
	    for (int i = 0; i < NUM_VIAJES_B; i++) {
		// System.out.printf("Barcaza amarrada en %s lista para embarcar.\n", this.str_pais);
		barcaza.zarpar();
		System.out.printf("Zarpamos de %s rumbo a %s.\n",
				  this.str_pais[this.orilla],
				  this.str_pais[1-this.orilla]); // OJO!!
		try {Thread.sleep(200);} catch (Exception e) {} // OJO!!
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		try {Thread.sleep(600);} catch (Exception e) {} // OJO!!
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.out.printf("Divisamos la costa de %s.\n",
				  this.str_pais[1-this.orilla]); // OJO!!
		try {Thread.sleep(600);} catch (Exception e) {} // OJO!!
		    this.orilla = 1 - this.orilla;
		System.out.printf("Amarramos en %s. Listos para desembarcar\n",
				  this.str_pais[this.orilla]);
		barcaza.amarrar();
		// IMPRIMIR MENSAJE
	    }
	}
    }

    public static void main(String[] args)
    throws InterruptedException {

	// el recurso compartido
	Barcaza barcaza = new BarcazaMon(); // OJO MONITORES!!
	
	// pasajeros: 5 bribones y 5 truhanes
	Pasajero[] truhan = new Pasajero[5];
	Pasajero[] bribon = new Pasajero[5];
	
	for (int i = 0; i < 5; i++) {
	    truhan[i] = new Pasajero(Barcaza.TRUHAN,barcaza);
	    truhan[i].start();
	    bribon[i] = new Pasajero(Barcaza.BRIBON,barcaza);
	    bribon[i].start();
	}

	// el barquero
	Barquero barquero = new Barquero(barcaza);
	barquero.start();

	// básicamente, no tratamos terminación :(
	// TO DO
    }// main

}// CC_09_RioMon
