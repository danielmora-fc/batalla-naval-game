package com.example.batallanavalgame.controllers;

import com.example.batallanavalgame.models.Juego;
import com.example.batallanavalgame.models.Jugador;
import com.example.batallanavalgame.models.Barco;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.Modality;

import java.io.IOException;
import java.util.List;

/**
 * Controlador encargado de la escena de ubicar la flota del jugador humano
 * Permite al usuario colocar sus barcos en el tablero mediante interaccion con el mouse
 */
public class UbicarFlotaController {
    @FXML private GridPane gridPane;
    @FXML private Label lblBarcoActual;
    @FXML private Button btnRotar;
    @FXML private Button btnComenzar;
    @FXML private Label lblOrientacion;
    /** Referencia al juego actual */
    private Juego juego;
    /** Jugador humano que coloca la flota. */
    private Jugador jugador;
    /** Indica si el barco actual se coloca horizontalmente. */
    private boolean horizontal = true;
    /** Indice del barco actual */
    private int barcoActualIndex = 0;
    /** Lista ordenada de los barcos que deben colocarse según las reglas del juego. */
    private final List<String> barcosAcolocar = List.of("Portaaviones", "Submarino", "Submarino", "Destructor", "Destructor", "Destructor", "Fragata", "Fragata", "Fragata", "Fragata");
    /**
     * Inicializa el controlador con el juego actual.
     *
     * @param juego instancia del juego en curso
     */
    public void setJuego(Juego juego) {
        this.juego = juego;
        this.jugador = juego.getJugador();
        inicializarTablero();
        actualizarLabel();
        actualizarOrientacion();
    }
    /**
     * Crea visualmente el tablero de 10x10 para la colocación de la flota.
     * Asigna eventos de mouse a cada celda.
     */
    private void inicializarTablero() {
        gridPane.getChildren().clear();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Rectangle rect = new Rectangle(40, 40);
                rect.setStroke(Color.BLACK);
                rect.setFill(Color.LIGHTBLUE);
                rect.setOnMouseClicked(this::handleCeldaClick);
                rect.setOnMouseEntered(this::handleMouseEntered);
                rect.setOnMouseExited(this::handleMouseExited);
                gridPane.add(rect, j, i);
            }
        }
    }
    /**
     * Cambia la orientación del barco actual entre horizontal y vertical.
     */
    @FXML
    private void handleRotar() {
        horizontal = !horizontal;
    }
    /**
     * Maneja el evento de clic sobre una celda del tablero.
     * Click izquierdo: intenta colocar el barco.
     * Click derecho: rota el barco y actualiza la previsualización.
     *
     * @param event evento del mouse
     */
    @FXML
    private void handleCeldaClick(MouseEvent event) {
        if (barcoActualIndex >= barcosAcolocar.size()) return;


        if (event.getButton() == MouseButton.SECONDARY) {
            horizontal = !horizontal;

            actualizarOrientacion();
            Rectangle rect = (Rectangle) event.getSource();
            int col = GridPane.getColumnIndex(rect);
            int row = GridPane.getRowIndex(rect);
            showPreview(row, col);
            return;
        }

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
    /**
     * Actualiza la visualización del tablero según el estado del modelo.
     */
    private void actualizarTablero() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Rectangle rect = (Rectangle) gridPane.getChildren().get(i * 10 + j);
                if (jugador.getTablero().getEstado(i, j) == 1) {
                    rect.setFill(Color.GRAY);
                } else {
                    rect.setFill(Color.LIGHTBLUE);
                }
            }
        }
    }

    /**
     * Muestra la previsualización del barco cuando el mouse entra en una celda.
     *
     * @param event evento del mouse
     */
    @FXML
    private void handleMouseEntered(MouseEvent event) {
        if (barcoActualIndex >= barcosAcolocar.size()) return;
        
        Rectangle rect = (Rectangle) event.getSource();
        int col = GridPane.getColumnIndex(rect);
        int row = GridPane.getRowIndex(rect);
        
        showPreview(row, col);
    }

    /**
     * Restaura la vista del tablero cuando el mouse sale de una celda.
     *
     * @param event evento del mouse
     */
    @FXML
    private void handleMouseExited(MouseEvent event) {
        if (barcoActualIndex >= barcosAcolocar.size()) return;
        
        actualizarTablero();
    }
    /**
     * Muestra una previsualización del barco indicando si puede colocarse correctamente o no.
     * @param row fila inicial
     * @param col columna inicial
     */
    private void showPreview(int row, int col) {
        if (barcoActualIndex >= barcosAcolocar.size()) return;
        
        String tipoBarco = barcosAcolocar.get(barcoActualIndex);
        int size = Barco.getSizeByType(tipoBarco);
        
        boolean canPlace = true;
        

        for (int i = 0; i < size; i++) {
            int r = row;
            int c = col;
            
            if (horizontal) {
                c = col + i;
            } else {
                r = row + i;
            }
            
            if (r < 0 || r > 9 || c < 0 || c > 9 || jugador.getTablero().getEstado(r, c) != 0) {
                canPlace = false;
                break;
            }
        }
        

        for (int i = 0; i < size; i++) {
            int r = row;
            int c = col;
            
            if (horizontal) {
                c = col + i;
            } else {
                r = row + i;
            }
            
            if (r >= 0 && r <= 9 && c >= 0 && c <= 9) {
                Rectangle rect = (Rectangle) gridPane.getChildren().get(r * 10 + c);
                if (canPlace) {
                    rect.setFill(Color.LIGHTGREEN);
                } else {
                    rect.setFill(Color.LIGHTCORAL);
                }
            }
        }
    }

    /**
     * Actualiza el texto que indica el barco que debe colocarse.
     */
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

    /**
     * Actualiza el texto que indica la orientación actual del barco.
     */
    private void actualizarOrientacion() {
        if (lblOrientacion != null) {
            String texto;
            if (horizontal) {
                texto = "Horizontal";
            } else {
                texto = "Vertical";
            }

            lblOrientacion.setText("Orientación: " + texto + " --> (Haz click derecho para rotar el barco)");
        }
    }
    /**
     * Cambia a la escena principal del juego una vez finalizada
     * la colocación de la flota.
     *
     * @throws IOException si ocurre un error al cargar la vista
     */
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

    /**
     * Se complementa con el metodo handleComenzar()
     * @throws IOException si ocurre un error al cargar la vista
     */
    @FXML
    private void continuar() throws IOException {
        handleComenzar();
    }
    /**
     * Muestra una ventana con la ubicación de la flota enemiga como ayuda visual antes de iniciar la partida.
     */
    @FXML
    private void verFlotaEnemiga() {
        Stage stage = new Stage();
        stage.setTitle("Flota Enemiga");
        stage.initModality(Modality.APPLICATION_MODAL);
        
        VBox root = new VBox(10);
        root.setStyle("-fx-padding: 20; -fx-alignment: center;");
        
        Label title = new Label("Mapa de la Flota Enemiga");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Rectangle[][] enemyCells = new Rectangle[10][10];
        GridPane enemyGrid = new GridPane();
        enemyGrid.setGridLinesVisible(true);
        enemyGrid.setStyle("-fx-border-color: black; -fx-border-width: 2;");
        

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Rectangle rect = new Rectangle(30, 30);
                rect.setStroke(Color.BLACK);
                rect.setFill(Color.LIGHTBLUE);
                enemyCells[i][j] = rect;
                enemyGrid.add(rect, j, i);
            }
        }
        

        Jugador maquina = juego.getMaquina();
        for (Barco barco : maquina.getFlota()) {
            int row = barco.getInitialRow();
            int col = barco.getInitialCol();
            String orientacion = barco.getOrientacion();
            
            for (int i = 0; i < barco.getSize(); i++) {
                int r = row;
                int c = col;
                
                if (orientacion.equals("VERTICAL")) {
                    r = row + i;
                } else {
                    c = col + i;
                }
                
                if (r >= 0 && r <= 9 && c >= 0 && c <= 9) {
                    enemyCells[r][c].setFill(Color.DARKGRAY);
                }
            }
        }
        
        Button closeButton = new Button("Cerrar");
        closeButton.setOnAction(e -> stage.close());
        
        root.getChildren().addAll(title, enemyGrid, closeButton);
        
        Scene scene = new Scene(root, 400, 450);
        stage.setScene(scene);
        stage.showAndWait();
    }
}