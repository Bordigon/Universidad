// TODO: importar la clase de los semáforos.
//
package entregas.concurrencia.p5; 
import es.upm.babel.cclib.Semaphore;
// Almacen concurrente para un dato
class Almacen1 {
    // Producto a almacenar
    private int almacenado;
    
    // TODO: declaración e inicialización de los semáforos
    // necesarios
    //
    //
	private static Semaphore vacio = new Semaphore(1);//----------------
	private static Semaphore lleno = new Semaphore();//----------------------
	private static Semaphore acceder = new Semaphore(); //se encargará de sincronizar los otros dos	
	
    public Almacen1() {
		acceder.signal();
    }
    
    public void almacenar(int producto) {
	// TODO: protocolo de acceso a la sección crítica y código de
	// sincronización para poder almacenar.
	//
		vacio.await(); acceder.await();		// -----------------------------

	// Sección crítica
	almacenado = producto;

	// TODO: protocolo de salida de la sección crítica y código de
	// sincronización para poder extraer.
	//
		acceder.signal(); lleno.signal();		//------------------------
    }

    public int extraer() {
	int result;

	// TODO: protocolo de acceso a la sección crítica y código de
	// sincronización para poder extraer.
	// 
		lleno.await();	acceder.await();	//--------------------

	// Sección crítica
	result = almacenado;

	// TODO: protocolo de salida de la sección crítica y código de
	// sincronización para poder almacenar.
	// 
		acceder.signal();	vacio.signal();		//----------------------

	return result;
    }
}