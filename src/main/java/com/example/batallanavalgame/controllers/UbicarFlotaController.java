package com.example.batallanavalgame.controllers;

import com.example.batallanavalgame.models.Juego;
import com.example.batallanavalgame.models.Jugador;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class UbicarFlotaController {
    @FXML private GridPane gridPane;
    @FXML private Label lblBarcoActual;
    @FXML private Button btnRotar;
    @FXML private Button btnComenzar;

    private Juego juego;
    private Jugador jugador;
    private boolean horizontal = true;
    private int barcoActualIndex = 0;
    private final List<String> barcosAcolocar = List.of("Portaaviones", "Submarino", "Submarino", "Destructor", "Destructor", "Destructor", "Fragata", "Fragata", "Fragata", "Fragata");

    public void setJuego(Juego juego) {
        this.juego = juego;
        this.jugador = juego.getJugador();
        inicializarTablero();
        actualizarLabel();
    }

    private void inicializarTablero() {
        gridPane.getChildren().clear();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Rectangle rect = new Rectangle(40, 40);
                rect.setStroke(Color.BLACK);
                rect.setFill(Color.LIGHTBLUE);
                rect.setOnMouseClicked(this::handleCeldaClick);
                gridPane.add(rect, j, i);
            }
        }
    }

    @FXML
    private void handleRotar() {
        horizontal = !horizontal;
    }

    @FXML
    private void handleCeldaClick(MouseEvent event) {
        if (barcoActualIndex >= barcosAcolocar.size()) return;

        Rectangle rect = (Rectangle) event.getSource();
        int col = GridPane.getColumnIndex(rect);
        int row = GridPane.getRowIndex(rect);

        String tipoBarco = barcosAcolocar.get(barcoActualIndex);

        if (jugador.colocarBarco(tipoBarco, row, col, horizontal)) {
            actualizarTablero();
            barcoActualIndex++;
            actualizarLabel();
        }
    }

    private void actualizarTablero() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Rectangle rect = (Rectangle) gridPane.getChildren().get(i * 10 + j);
                if (jugador.getTablero().getEstado(i, j) == 1) {
                    rect.setFill(Color.GRAY);
                }
            }
        }
    }

    private void actualizarLabel() {
        if (lblBarcoActual != null) {
            if (barcoActualIndex < barcosAcolocar.size()) {
                lblBarcoActual.setText("Colocar: " + barcosAcolocar.get(barcoActualIndex));
            } else {
                lblBarcoActual.setText("Flota completa");
                if (btnComenzar != null) {
                    btnComenzar.setDisable(false);
                }
            }
        }
    }

    @FXML
    private void handleComenzar() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/batallanavalgame/views/juego.fxml"));
        Parent root = loader.load();
        JuegoController controller = loader.getController();
        controller.setJuego(juego);

        Stage stage = (Stage) gridPane.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void continuar() throws IOException {
        handleComenzar();
    }

    @FXML
    private void verFlotaEnemiga() {
        // TODO: Implement enemy fleet preview
    }
}