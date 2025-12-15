package com.example.batallanavalgame.controllers;

import com.example.batallanavalgame.models.Juego;
import com.example.batallanavalgame.models.Tablero;
import com.example.batallanavalgame.services.PersistenceFacade;
import com.example.batallanavalgame.services.strategy.DisparoStrategy;
import com.example.batallanavalgame.services.strategy.RandomDisparoStrategy;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class JuegoController {
    @FXML private GridPane gridMaquina; // tablero principal (donde dispara humano)
    @FXML private GridPane gridHumano;  // tablero de posición (donde dispara IA)
    @FXML private Label lblNickname;
    @FXML private Label lblStats;

    private Juego juego;
    private final DisparoStrategy iaStrategy = new RandomDisparoStrategy();

    public void setJuego(Juego juego) {
        this.juego = juego;

        buildGrid(gridMaquina);
        buildGrid(gridHumano);

        redrawAllBoards();
        updateUI();

        if (!juego.esTurnoJugador() && !juego.estaTerminado()) {
            machineTurn();
        }

        if (juego.estaTerminado()) {
            showGameEndDialog();
        }
    }


    @FXML
    public void initialize() {
        // Eventos de disparo del jugador
        gridMaquina.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            if (juego == null || juego.estaTerminado() || !juego.esTurnoJugador()) return;
            int fila = (int) (e.getY() / getCellSize(gridMaquina));
            int col = (int) (e.getX() / getCellSize(gridMaquina));
            if (fila < 0 || fila > 9 || col < 0 || col > 9) return;
            int res = juego.realizarDisparo(fila, col);

            if (res == 4) {
                // Hundido: repintar todo el tablero enemigo
                redrawEnemyBoard();
            } else {
                // Agua o tocado: solo una celda
                updateEnemyCell(fila, col, res);
            }

            saveAll();
            updateUI();

            if (res == 2) { // agua -> turno IA
                juego.cambiarTurno();
                machineTurn();
            } else {
                if (juego.estaTerminado()) {
                    showGameEndDialog();
                }
            }
        });
    }

    private void machineTurn() {
        while (!juego.esTurnoJugador() && !juego.estaTerminado()) {
            int[] shot = iaStrategy.nextShot(juego.getHumano().getTablero().getGrid());
            int fila = shot[0], col = shot[1];
            int res = juego.realizarDisparo(fila, col);

            if (res == 4) {
                redrawHumanBoard();
            } else {
                updateHumanCell(fila, col, res);
            }

            if (res == 2) { // agua -> cambia turno a humano
                juego.cambiarTurno();
            }
            // If hit (res != 2), AI continues shooting
        }
        
        // Check if game ended after AI's turn
        if (juego.estaTerminado()) {
            showGameEndDialog();
        }
    }

    private void drawBoards() {
        // pintar tablero humano con barcos
        Tablero th = juego.getHumano().getTablero();
        for (int f = 0; f < 10; f++) {
            for (int c = 0; c < 10; c++) {
                int st = th.getEstado(f, c);
                if (st == 1) colorCell(gridHumano, f, c, Color.GRAY);
            }
        }
    }

    private void updateEnemyCell(int f, int c, int estado) {
        if (estado == 2) colorCell(gridMaquina, f, c, Color.BLUE);
        else if (estado == 3) colorCell(gridMaquina, f, c, Color.RED);
        else if (estado == 4) colorCell(gridMaquina, f, c, Color.BLACK);
    }

    private void updateHumanCell(int f, int c, int estado) {
        if (estado == 2) colorCell(gridHumano, f, c, Color.BLUE);
        else if (estado == 3) colorCell(gridHumano, f, c, Color.RED);
        else if (estado == 4) colorCell(gridHumano, f, c, Color.BLACK);
    }

    private void colorCell(GridPane grid, int f, int c, Color color) {
        int idx = f * 10 + c;
        Rectangle r = (Rectangle) grid.getChildren().get(idx);
        r.setFill(color.deriveColor(0,1,1,0.6));
    }

    private void buildGrid(GridPane grid) {
        grid.getChildren().clear();
        double size = getCellSize(grid);
        for (int f = 0; f < 10; f++) {
            for (int c = 0; c < 10; c++) {
                Rectangle r = new Rectangle(size, size);
                r.setFill(Color.TRANSPARENT);
                r.setStroke(Color.LIGHTGRAY);
                grid.add(r, c, f);
            }
        }
    }

    private double getCellSize(GridPane grid) {
        return Math.floor(grid.getPrefWidth() / 10.0);
    }

    private void saveAll() {
        try {
            PersistenceFacade.saveGame(juego);
            PersistenceFacade.savePlayerData(
                    juego.getNickname(),
                    juego.getHundidosHumano(),
                    juego.getHundidosIA()
            );
        } catch (Exception e) {
            System.err.println("Error guardando partida: " + e.getMessage());
        }
    }


    private void updateUI() {
        if (lblNickname != null) {
            lblNickname.setText("Jugador: " + (juego.getNickname() != null ? juego.getNickname() : "Anónimo"));
        }
        if (lblStats != null) {
            lblStats.setText("Barcos Hundidos - Tú: " + juego.getHundidosHumano() + " | IA: " + juego.getHundidosIA());
        }
    }

    private void showGameEndDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Juego Terminado");
        alert.setHeaderText(null);
        
        String message;
        String winnerName;
        
        if (juego.getGanador() == juego.getHumano()) {
            winnerName = juego.getNickname() != null ? juego.getNickname() : "Jugador";
            message = "¡Felicidades " + winnerName + "! Has ganado la batalla naval.";
        } else {
            message = "La IA ha ganado. Mejor suerte la próxima vez.";
        }
        
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void redrawAllBoards() {
        // 1) Limpia ambos grids a “vacío”
        clearGrid(gridHumano);
        clearGrid(gridMaquina);

        // 2) Dibuja tablero humano: barcos + disparos de la IA
        Tablero th = juego.getHumano().getTablero();
        for (int f = 0; f < 10; f++) {
            for (int c = 0; c < 10; c++) {
                int st = th.getEstado(f, c);
                if (st == 1) colorCell(gridHumano, f, c, Color.GRAY);     // barcos humanos visibles
                else if (st == 2) colorCell(gridHumano, f, c, Color.BLUE); // agua
                else if (st == 3) colorCell(gridHumano, f, c, Color.RED);  // tocado
                else if (st == 4) colorCell(gridHumano, f, c, Color.BLACK);// hundido
            }
        }

        // 3) Dibuja tablero máquina: SOLO lo que el humano ya disparó (2/3/4)
        Tablero tm = juego.getMaquina().getTablero();
        for (int f = 0; f < 10; f++) {
            for (int c = 0; c < 10; c++) {
                int st = tm.getEstado(f, c);
                if (st == 2) colorCell(gridMaquina, f, c, Color.BLUE);
                else if (st == 3) colorCell(gridMaquina, f, c, Color.RED);
                else if (st == 4) colorCell(gridMaquina, f, c, Color.BLACK);
                // st == 1 (barco de la máquina) NO se pinta para no hacer trampa
            }
        }
    }

    private void clearGrid(GridPane grid) {
        // OJO: esto asume que los hijos son Rectangles y que buildGrid ya los creó
        for (var node : grid.getChildren()) {
            Rectangle r = (Rectangle) node;
            r.setFill(Color.TRANSPARENT);
            r.setStroke(Color.LIGHTGRAY);
        }
    }
    private void redrawEnemyBoard() {
        Tablero tm = juego.getMaquina().getTablero();
        for (int f = 0; f < 10; f++) {
            for (int c = 0; c < 10; c++) {
                int st = tm.getEstado(f, c);
                if (st == 2) colorCell(gridMaquina, f, c, Color.BLUE);
                else if (st == 3) colorCell(gridMaquina, f, c, Color.RED);
                else if (st == 4) colorCell(gridMaquina, f, c, Color.BLACK);
                // NO pintes st == 1 (barcos vivos de la máquina)
            }
        }
    }

    private void redrawHumanBoard() {
        Tablero th = juego.getHumano().getTablero();
        for (int f = 0; f < 10; f++) {
            for (int c = 0; c < 10; c++) {
                int st = th.getEstado(f, c);
                if (st == 1) colorCell(gridHumano, f, c, Color.GRAY);
                else if (st == 2) colorCell(gridHumano, f, c, Color.BLUE);
                else if (st == 3) colorCell(gridHumano, f, c, Color.RED);
                else if (st == 4) colorCell(gridHumano, f, c, Color.BLACK);
            }
        }
    }

}
