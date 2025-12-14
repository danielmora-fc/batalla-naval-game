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

public class UbicarFlotaController {
    @FXML private GridPane gridPane;
    @FXML private Label lblBarcoActual;
    @FXML private Button btnRotar;
    @FXML private Button btnComenzar;
    @FXML private Label lblOrientacion;

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
        actualizarOrientacion();
    }

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

    @FXML
    private void handleRotar() {
        horizontal = !horizontal;
    }

    @FXML
    private void handleCeldaClick(MouseEvent event) {
        if (barcoActualIndex >= barcosAcolocar.size()) return;

        // Right click to rotate
        if (event.getButton() == MouseButton.SECONDARY) {
            horizontal = !horizontal;
            // Update preview after rotation
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

    @FXML
    private void handleMouseEntered(MouseEvent event) {
        if (barcoActualIndex >= barcosAcolocar.size()) return;
        
        Rectangle rect = (Rectangle) event.getSource();
        int col = GridPane.getColumnIndex(rect);
        int row = GridPane.getRowIndex(rect);
        
        showPreview(row, col);
    }

    @FXML
    private void handleMouseExited(MouseEvent event) {
        if (barcoActualIndex >= barcosAcolocar.size()) return;
        
        actualizarTablero();
    }

    private void showPreview(int row, int col) {
        if (barcoActualIndex >= barcosAcolocar.size()) return;
        
        String tipoBarco = barcosAcolocar.get(barcoActualIndex);
        int size = Barco.getSizeByType(tipoBarco);
        
        boolean canPlace = true;
        
        // Check if ship can be placed
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
        
        // Show preview
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
        
        // Create 10x10 grid for enemy fleet
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Rectangle rect = new Rectangle(30, 30);
                rect.setStroke(Color.BLACK);
                rect.setFill(Color.LIGHTBLUE);
                enemyCells[i][j] = rect;
                enemyGrid.add(rect, j, i);
            }
        }
        
        // Draw enemy ships on the grid
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
                    /*
                    Rectangle rect = (Rectangle) enemyGrid.getChildren().get(r * 10 + c);
                    rect.setFill(Color.DARKGRAY);
                     */
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