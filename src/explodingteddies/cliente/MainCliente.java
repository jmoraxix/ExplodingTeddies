/*
 * Programacion Concurrente Cliente Servidor
 * 
 * Melanie Benvides
 * Jose Mora Loria
 * Thomas White
 * 
 * Exploding Teddies
 */
package explodingteddies.cliente;

import explodingteddies.cliente.vista.MenuClienteController;
import explodingteddies.cliente.vista.TableroController;
import explodingteddies.modelo.Dificultad;
import explodingteddies.modelo.Jugador;
import explodingteddies.modelo.Partida;
import explodingteddies.util.Util;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * MainCliente Application. This class handles navigation and user session.
 */
public class MainCliente extends Application {

    private Stage stage;
    private final double WINDOW_WIDTH = Util.WIDTH;
    private final double WINDOW_HEIGHT = Util.HEIGHT;

    //Variables del cliente TCP
    private Cliente cliente;

    // Variables del juego
    private TableroController tablero;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Application.launch(MainCliente.class, (java.lang.String[]) null);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            stage = primaryStage;
            stage.setTitle("Exploding Teddies");
            stage.setMinWidth(WINDOW_WIDTH);
            stage.setMinHeight(WINDOW_HEIGHT);
            gotoMenu();
            primaryStage.show();
        } catch (Exception ex) {
            Logger.getLogger(MainCliente.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Iniciando cliente TCP
        this.cliente = new Cliente(this);
        this.cliente.start();
    }

    public void gotoMenu() {
        try {
            MenuClienteController menu = (MenuClienteController) replaceSceneContent("MenuCliente.fxml");
            menu.setApp(this);
        } catch (Exception ex) {
            Logger.getLogger(MainCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void gotoTablero(Dificultad dificultad, Jugador jugador) {
        try {
            tablero = (TableroController) replaceSceneContent("Tablero.fxml");
            tablero.setApp(this, cliente, dificultad, jugador);
        } catch (Exception ex) {
            Logger.getLogger(MainCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Initializable replaceSceneContent(String fxml) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        InputStream in = MainCliente.class.getResourceAsStream("vista/" + fxml);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(MainCliente.class.getResource("vista/" + fxml));
        AnchorPane page;
        try {
            page = (AnchorPane) loader.load(in);
        } finally {
            in.close();
        }
        Scene scene = new Scene(page, WINDOW_WIDTH, WINDOW_HEIGHT);
        stage.setScene(scene);
        stage.sizeToScene();
        return (Initializable) loader.getController();
    }

    // Metodos de TCP
    public void entraUsuario(Dificultad dificultad, String usuario) {
        cliente.entraUsuario(dificultad, usuario);
    }

    // Inicializamos las varibles con la partida generada por el servidor
    public void crearPartida(Partida partidaNueva) {
        tablero.crearPartida(partidaNueva);
    }

    // Inicializamos las variables con el nuevo jugador e iniciamos el juego
    public void cerrarPartida(Partida partidaNueva) {
        tablero.cerrarPartida(partidaNueva);
    }
    
    // Recibimos una nueva matriz y la mapeamos con el tablero actual
    public void recibeJugada(Jugador jugador, Partida partidaNueva){
        tablero.recibeJugada(jugador, partidaNueva);
    }

    //Getters & Setters
    public Cliente getCliente() {
        return cliente;
    }

}
