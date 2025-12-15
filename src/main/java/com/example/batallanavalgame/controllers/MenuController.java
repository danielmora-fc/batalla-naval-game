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

public class MenuController {
    @FXML
    void cargarPartida(ActionEvent event) {
        try {
            Juego juego = PersistenceFacade.loadGame();
            if (juego == null) {
                // Alert: "No hay partida guardada"
                return;
            }
            if (juego.estaTerminado()) {
                // Alert: "La partida guardada ya terminó, inicia una nueva"
                return;
            }

            juego.getHumano().ensureCreador();
            juego.getMaquina().ensureCreador();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/batallanavalgame/views/juego.fxml"));
            Parent root = loader.load();
            JuegoController controller = loader.getController();
            controller.setJuego(juego);
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            // Alert: "Error cargando partida"
        }
    }


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
