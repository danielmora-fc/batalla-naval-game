package com.example.batallanavalgame.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import com.example.batallanavalgame.models.Barco;
import com.example.batallanavalgame.models.Tablero;

public class UbicarFlotaController {

    @FXML
    private GridPane gridPane;

    private Tablero tablero;  // Asegúrate de que el tablero esté inicializado

    public void initialize() {
        // Inicializa el tablero (puede ser un tablero vacío o con barcos precolocados)
        this.tablero = new Tablero();

        // Añadir el evento de mouse a las celdas del grid
        gridPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // Obtener la fila y columna donde se hizo clic
                int fila = (int) event.getY() / 39;  // Asumiendo que cada celda tiene 39px
                int columna = (int) event.getX() / 39;

                // Imprimir la fila y columna para verificar
                System.out.println("Fila: " + fila + ", Columna: " + columna);

                // Lógica para colocar el barco en la celda seleccionada
                // Este ejemplo usa un barco de tipo "SUBMARINO"
                Barco barco = new Barco("SUBMARINO", 3, "HORIZONTAL", fila, columna);

                // Intentar colocar el barco en el tablero
                if (tablero.colocarBarco(barco)) {
                    System.out.println("Barco colocado en Fila: " + fila + ", Columna: " + columna);
                } else {
                    System.out.println("No se puede colocar el barco en esta posición.");
                }
            }
        });
    }

    @FXML
    public void continuar(ActionEvent event) {
        // Continuar con el juego después de colocar los barcos
    }

    @FXML
    public void verFlotaEnemiga(ActionEvent event) {
        // Mostrar la flota del enemigo
    }
}
