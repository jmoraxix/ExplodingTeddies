package explodingteddies.Logic;


public class Cronometro extends Thread {

    private boolean running;
    private BuscaMinas bm;
    private boolean keepRunning;
    
    //TODO Logica
    // Crea thread para el cronometro
    public Cronometro(String name, BuscaMinas b) {
        //super(name);
        running = false;
        keepRunning = true;
        bm = b;
    }

    public void iniciar() {
        running = true;
    }

    public void stopRunning() {
        keepRunning = false;
    }

    @Override
    public void run() {
        while (keepRunning) {
            try {
                // We sleep to give the "running" variable a chance to reflect its changed state
                sleep(1000);
            } catch (InterruptedException ex) {
                System.err.println("Exception: " + ex.getMessage());
            }
            if (running) {
                bm.aumentarTiempo();
            }
        }
    }

    public void detener() {
        running = false;
    }
}