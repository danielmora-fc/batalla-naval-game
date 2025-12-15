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

/**
 * Controlador principal de la escena del juego
 * Gestiona la interaccion entre el jugador humano y la maquina
 * Controla los disparos, cambio de turnos, actualizacion visual
 */
public class JuegoController {
    @FXML private GridPane gridMaquina; // tablero principal (donde dispara humano)
    @FXML private GridPane gridHumano;  // tablero de posición (donde dispara IA)
    @FXML private Label lblNickname;
    @FXML private Label lblStats;
    /** Instancia del juego actual */
    private Juego juego;
    /** Estrategia utilizada por la IA para realizar disparos */
    private final DisparoStrategy iaStrategy = new RandomDisparoStrategy();

    /**
     * Asigna el juego activo al controlador e inicializa los tableros.
     *
     * @param juego instancia del juego cargado o recién creado
     */
    public void setJuego(Juego juego) {
        this.juego = juego;
        buildGrid(gridMaquina);
        buildGrid(gridHumano);
        drawBoards();
        updateUI();
    }

    /**
     * Inicializa los eventos de disparo del jugador humano.
     * Se ejecuta automáticamente al cargar la vista FXML.
     */
    @FXML
    public void initialize() {
        // Eventos de disparo del jugador
        gridMaquina.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            if (juego == null || juego.estaTerminado() || !juego.esTurnoJugador()) return;
            int fila = (int) (e.getY() / getCellSize(gridMaquina));
            int col = (int) (e.getX() / getCellSize(gridMaquina));
            if (fila < 0 || fila > 9 || col < 0 || col > 9) return;
            int res = juego.realizarDisparo(fila, col);
            updateEnemyCell(fila, col, res);
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

    /**
     * Ejecuta el turno de la IA utilizando una estrategia de disparo.
     * La IA continúa disparando mientras acierte.
     */
    private void machineTurn() {
        while (!juego.esTurnoJugador() && !juego.estaTerminado()) {
            int[] shot = iaStrategy.nextShot(juego.getHumano().getTablero().getGrid());
            int fila = shot[0], col = shot[1];
            int res = juego.realizarDisparo(fila, col);
            updateHumanCell(fila, col, res);
            saveAll();
            updateUI();
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

    /**
     * Dibuja el tablero del jugador humano mostrando sus barcos.
     */
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

    /**
     * Actualiza visualmente una celda del tablero enemigo.
     *
     * @param f fila disparada
     * @param c columna disparada
     * @param estado resultado del disparo
     */
    private void updateEnemyCell(int f, int c, int estado) {
        if (estado == 2) colorCell(gridMaquina, f, c, Color.BLUE);
        else if (estado == 3) colorCell(gridMaquina, f, c, Color.RED);
        else if (estado == 4) colorCell(gridMaquina, f, c, Color.BLACK);
    }
    /**
     * Actualiza visualmente una celda del tablero del jugador humano.
     *
     * @param f fila disparada
     * @param c columna disparada
     * @param estado resultado del disparo
     */
    private void updateHumanCell(int f, int c, int estado) {
        if (estado == 2) colorCell(gridHumano, f, c, Color.BLUE);
        else if (estado == 3) colorCell(gridHumano, f, c, Color.RED);
        else if (estado == 4) colorCell(gridHumano, f, c, Color.BLACK);
    }

    /**
     * Cambia el color de una celda específica del tablero.
     * @param grid tablero a modificar
     * @param f fila
     * @param c columna
     * @param color color a aplicar
     */
    private void colorCell(GridPane grid, int f, int c, Color color) {
        int idx = f * 10 + c;
        Rectangle r = (Rectangle) grid.getChildren().get(idx);
        r.setFill(color.deriveColor(0,1,1,0.6));
    }

    /**
     * Construye visualmente un tablero 10x10 dentro del gridpane
     * @param grid GridPane en el que se construirá el tablero
     */
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

    /**
     * Calcula el tamaño de cada celda del tablero en función del ancho preferido del GridPane.
     *
     * @param grid GridPane del cual se obtiene el tamaño
     * @return tamaño de cada celda en píxeles
     */
    private double getCellSize(GridPane grid) {
        return Math.floor(grid.getPrefWidth() / 10.0);
    }
    /**
     * Guarda el estado actual del juego y las estadísticas.
     */
    private void saveAll() {
        try {
            PersistenceFacade.saveGame(juego);
            PersistenceFacade.saveStats(juego.getHundidosHumano(), juego.getHundidosIA());
        } catch (Exception ignored) {}
    }

    /**
     * Actualiza la información visual de la interfaz del juego, incluyendo el nickname del jugador y las estadísticas de barcos hundidos.
     */
    private void updateUI() {
        if (lblNickname != null) {
            lblNickname.setText("Jugador: " + (juego.getNickname() != null ? juego.getNickname() : "Anónimo"));
        }
        if (lblStats != null) {
            lblStats.setText("Barcos Hundidos - Tú: " + juego.getHundidosHumano() + " | IA: " + juego.getHundidosIA());
        }
    }

    /**
     * Muestra un diálogo informativo cuando la partida termina, indicando el ganador.
     */
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
}
