package arsw.threads;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;

public class MainCanodromo {

    private static Galgo[] galgos;
    private static Canodromo can;
    private static RegistroLlegada reg = new RegistroLlegada();

    public static void main(String[] args) {
        // Crear una instancia del Canódromo con 17 carriles y una longitud de 100
        can = new Canodromo(17, 100);
        galgos = new Galgo[can.getNumCarriles()];
        can.setVisible(true);

        // Acción del botón "Start"
        can.setStartAction(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                // Como acción, se crea un nuevo hilo que crea los hilos 'galgos', los pone a correr,
                // y luego muestra los resultados.
                // La acción del botón se realiza en un hilo aparte para evitar bloquear la interfaz gráfica.
                ((JButton) e.getSource()).setEnabled(false);
                new Thread() {
                    public void run() {
                        for (int i = 0; i < can.getNumCarriles(); i++) {
                            // Crea los hilos 'galgos'
                            galgos[i] = new Galgo(can.getCarril(i), "" + i, reg);
                            // Inicia los hilos
                            galgos[i].start();
                        }
                        for (Galgo galgo : galgos) {
                            try {
                                galgo.join(); // Espera a que todos los galgos terminen la carrera
                            } catch (InterruptedException ex) {
                                Logger.getLogger(MainCanodromo.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                        can.winnerDialog(reg.getGanador(), reg.getUltimaPosicionAlcanzada() - 1);
                        System.out.println("El ganador fue:" + reg.getGanador());
                    }
                }.start();
            }
        });

        // Acción del botón "Stop"
        can.setStopAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Carrera pausada!");
                for (Galgo galgo : galgos) {
                    galgo.dormirse(); // Pone a todos los galgos en pausa
                }
            }
        });

        // Acción del botón "Continue"
        can.setContinueAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Carrera reanudada!");
                for (Galgo galgo : galgos) {
                    galgo.despertarse(); // Reanuda a todos los galgos que estaban en pausa
                }
            }
        });
    }
}
