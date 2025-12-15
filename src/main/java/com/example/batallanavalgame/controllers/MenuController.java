package com.example.batallanavalgame.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import com.example.batallanavalgame.models.Juego;
import com.example.batallanavalgame.models.Jugador;
import com.example.batallanavalgame.services.PersistenceFacade;

import java.io.IOException;
import java.util.Optional;

/**
 * Controlador del menu principal del juego
 * Gestiona las acciones iniciales como iniciar nueva partida, o cargar una partida antigua
 */
public class MenuController {
    /**
     * Carga una partida previamente guardada
     * @param event evento generado al presionar el boton "Cargar Partida"
     */
    @FXML
    void cargarPartida(ActionEvent event) {
        try {
            System.out.println("Intentando cargar partida guardada...");
            Juego juego = PersistenceFacade.loadGame();
            
            if (juego == null) {
                mostrarAlerta("Partida no encontrada", "No se encontró ninguna partida guardada.");
                return;
            }
            
            if (juego.estaTerminado()) {
                mostrarAlerta("Partida Terminada", "La partida guardada ya ha terminado.\nPor favor, inicia una nueva partida.");
                return;
            }

            // Asegurarse de que los creadores estén inicializados
            if (juego.getHumano() != null) {
                juego.getHumano().ensureCreador();
            } else {
                throw new IllegalStateException("El jugador humano no está inicializado");
            }
            
            if (juego.getMaquina() != null) {
                juego.getMaquina().ensureCreador();
            } else {
                throw new IllegalStateException("El jugador máquina no está inicializado");
            }

            // Cargar la vista del juego
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/batallanavalgame/views/juego.fxml"));
            Parent root = loader.load();
            JuegoController controller = loader.getController();
            controller.setJuego(juego);
            
            // Cambiar a la escena del juego
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            
            System.out.println("Partida cargada exitosamente");

        } catch (Exception e) {
            System.err.println("Error al cargar la partida: " + e.getMessage());
            e.printStackTrace();
            mostrarAlerta("Error al cargar partida", "No se pudo cargar la partida guardada.");
        }
    }

    /**
     * Muestra una alerta al usuario
     */
    private void mostrarAlerta(String titulo, String mensaje) {
        try {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(titulo);
            alert.setHeaderText(null);
            alert.setContentText(mensaje);
            alert.showAndWait();
        } catch (Exception e) {
            System.err.println("Error al mostrar alerta: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Inicia una nueva partida
     * @param event evento generado al presionar el boton de "Nueva Partida"
     */

    @FXML
    void iniciarPartida(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/batallanavalgame/views/ubicarflota2.fxml"));
        Parent root = loader.load();
        UbicarFlotaController controller = loader.getController();

        // Crear juego con IA lista, humano coloca manualmente
        Juego juego = new Juego(new Jugador(true), new Jugador(false));

        // Pedir nickname
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Nickname");
        dialog.setHeaderText("Ingresa tu nickname");
        dialog.setContentText("Nickname:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(juego::setNickname);

        // Guardar stats iniciales
        try {
            PersistenceFacade.savePlayerData(juego.getNickname(), 0, 0);
        } catch (IOException ignored) {}


        juego.getMaquina().generarFlotaInicial();
        controller.setJuego(juego);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
    }
}
