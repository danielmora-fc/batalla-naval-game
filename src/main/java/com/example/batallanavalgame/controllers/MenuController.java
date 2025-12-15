package com.example.batallanavalgame.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
            Juego juego = PersistenceFacade.loadGame();
            if (juego == null) return;
            // Asegurar creadores de jugadores tras deserializar
            juego.getHumano().ensureCreador();
            juego.getMaquina().ensureCreador();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/batallanavalgame/views/juego.fxml"));
            Parent root = loader.load();
            JuegoController controller = loader.getController();
            controller.setJuego(juego);
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Inicia una nueva partida
     * @param event evento generado al presionar el boton de "Nueva Partida"
     * @throws IOException si ocurre un error al cargar la vista
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
        try { PersistenceFacade.saveStats(0, 0); } catch (Exception ignored) {}

        juego.getMaquina().generarFlotaInicial();
        controller.setJuego(juego);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
    }
}
