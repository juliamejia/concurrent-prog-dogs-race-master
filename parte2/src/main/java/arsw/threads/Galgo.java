package arsw.threads;

/**
 * Un galgo que puede correr en un carril
 *
 * author rlopez
 *
 */
public class Galgo extends Thread {
	private int paso;
	private Carril carril;
	RegistroLlegada regl;
	private boolean parar = false;

	public Galgo(Carril carril, String name, RegistroLlegada reg) {
		super(name);
		this.carril = carril;
		paso = 0;
		this.regl = reg;
	}

	public void corra() throws InterruptedException {
		while (paso < carril.size()) {
			Thread.sleep(100); // Hace que el galgo espere un tiempo simulando el paso
			carril.setPasoOn(paso++);
			carril.displayPasos(paso);

			synchronized (this) {
				while (parar == true) { // Espera si se llama al método 'dormirse'
					this.wait();
				}
			}

			if (paso == carril.size()) {
				carril.finish(); // Marca el carril como terminado
				int ubicacion;
				synchronized (regl) {
					ubicacion = regl.getUltimaPosicionAlcanzada();
					regl.setUltimaPosicionAlcanzada(ubicacion + 1);
					System.out.println("El galgo " + this.getName() + " llego en la posicion " + ubicacion);
				}
				if (ubicacion == 1) {
					regl.setGanador(this.getName()); // Establece al ganador si llega primero
				}
			}
		}
	}

	@Override
	public void run() {
		try {
			corra();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void dormirse() {
		parar = true; // Pone al galgo en estado de pausa
	}

	public void despertarse() {
		synchronized (this) {
			parar = false; // Reanuda al galgo si estaba en pausa
			notifyAll(); // Notifica a todos los hilos que esperaban por 'wait'
		}
	}
}
