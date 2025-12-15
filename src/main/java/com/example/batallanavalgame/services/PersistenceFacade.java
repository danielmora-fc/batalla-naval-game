package com.example.batallanavalgame.services;

import com.example.batallanavalgame.models.Barco;
import com.example.batallanavalgame.models.Juego;
import com.example.batallanavalgame.models.Jugador;
import com.example.batallanavalgame.models.Tablero;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public class PersistenceFacade {
    private static final Path SAVE_FILE = Paths.get("batalla_save.json");
    private static final Path PLAYER_FILE = Paths.get("player.txt"); // archivo plano
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);

    // ---------- JSON (Juego completo) ----------
    public static void saveGame(Juego juego) throws IOException {
        try {
            // Crear directorio si no existe
            Path dir = SAVE_FILE.getParent();
            if (dir != null) {
                Files.createDirectories(dir);
            }
            
            // Guardado atómico
            Path tmp = Paths.get(SAVE_FILE.toString() + ".tmp");
            objectMapper.writeValue(tmp.toFile(), GameSnapshot.fromJuego(juego));
            Files.move(tmp, SAVE_FILE, 
                StandardCopyOption.REPLACE_EXISTING, 
                StandardCopyOption.ATOMIC_MOVE
            );
            System.out.println("Juego guardado en: " + SAVE_FILE.toAbsolutePath());
        } catch (Exception e) {
            System.err.println("Error al guardar el juego: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public static Juego loadGame() throws IOException {
        try {
            if (!Files.exists(SAVE_FILE)) {
                System.out.println("No se encontró archivo de guardado en: " + SAVE_FILE.toAbsolutePath());
                return null;
            }
            
            GameSnapshot snapshot = objectMapper.readValue(SAVE_FILE.toFile(), GameSnapshot.class);
            Juego juego = snapshot.toJuego();
            System.out.println("Juego cargado exitosamente desde: " + SAVE_FILE.toAbsolutePath());
            return juego;
        } catch (Exception e) {
            System.err.println("Error al cargar el juego: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public static boolean hasSavedGame() {
        return Files.exists(SAVE_FILE);
    }

    // ---------- ARCHIVO PLANO (nickname + hundidos) ----------
    public static void savePlayerData(String nickname, int playerSunk, int machineSunk) throws IOException {
        String safeName = nickname == null ? "" : nickname.trim();
        String content =
                "nickname=" + safeName + "\n" +
                        "hundidosHumano=" + playerSunk + "\n" +
                        "hundidosIA=" + machineSunk + "\n";
        Files.writeString(PLAYER_FILE, content, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    public static PlayerData loadPlayerData() throws IOException {
        if (!Files.exists(PLAYER_FILE)) return new PlayerData("", 0, 0);

        String content = Files.readString(PLAYER_FILE, StandardCharsets.UTF_8);
        String nickname = "";
        int hh = 0, hi = 0;

        for (String line : content.split("\\R")) {
            String[] parts = line.split("=", 2);
            if (parts.length != 2) continue;
            switch (parts[0].trim()) {
                case "nickname" -> nickname = parts[1].trim();
                case "hundidosHumano" -> hh = parseIntSafe(parts[1].trim());
                case "hundidosIA" -> hi = parseIntSafe(parts[1].trim());
            }
        }
        return new PlayerData(nickname, hh, hi);
    }

    private static int parseIntSafe(String s) {
        try { return Integer.parseInt(s); }
        catch (NumberFormatException e) { return 0; }
    }

    public record PlayerData(String nickname, int hundidosHumano, int hundidosIA) {}

    /**
     * Snapshot simple y legible del estado de una partida.
     * Evita serializar directamente el grafo completo del modelo (por ejemplo Celda[][]),
     * guardando únicamente datos primitivos y estructuras simples para facilitar la lectura y la carga.
     */
    public static class GameSnapshot {
        public String nickname;
        public boolean turnoJugador;
        public boolean juegoTerminado;
        public int hundidosHumano;
        public int hundidosIA;
        public PlayerSnapshot humano;
        public PlayerSnapshot maquina;

        /** Constructor por defecto requerido para deserialización. */
        public GameSnapshot() {
        }

        /**
         * Construye un snapshot a partir de una instancia de {@link Juego}.
         * @param juego partida actual
         * @return snapshot listo para persistir en JSON
         */
        static GameSnapshot fromJuego(Juego juego) {
            GameSnapshot s = new GameSnapshot();
            s.nickname = juego.getNickname();
            s.turnoJugador = juego.esTurnoJugador();
            s.juegoTerminado = juego.estaTerminado();
            s.hundidosHumano = juego.getHundidosHumano();
            s.hundidosIA = juego.getHundidosIA();
            s.humano = PlayerSnapshot.fromJugador(juego.getHumano());
            s.maquina = PlayerSnapshot.fromJugador(juego.getMaquina());
            return s;
        }

        /**
         * Reconstruye una instancia de {@link Juego} desde el snapshot.
         * @return juego reconstruido
         */
        Juego toJuego() {
            Juego juego = new Juego();
            juego.setNickname(nickname);
            juego.setTurnoJugador(turnoJugador);
            juego.setJuegoTerminado(juegoTerminado);
            juego.setHundidosHumano(hundidosHumano);
            juego.setHundidosIA(hundidosIA);

            Jugador jHumano = humano != null ? humano.toJugador(true) : new Jugador(true);
            Jugador jMaquina = maquina != null ? maquina.toJugador(false) : new Jugador(false);
            juego.setHumano(jHumano);
            juego.setMaquina(jMaquina);
            return juego;
        }
    }

    /**
     * Snapshot simple del estado de un jugador.
     * Contiene el tablero como matriz de enteros (estados) y la flota con su progreso (golpes).
     */
    public static class PlayerSnapshot {
        public boolean esHumano;
        public int[][] grid;
        public Barco[] flota;

        /** Constructor por defecto requerido para deserialización. */
        public PlayerSnapshot() {
        }

        /**
         * Construye un snapshot a partir de un {@link Jugador}.
         * @param jugador jugador origen
         * @return snapshot del jugador
         */
        static PlayerSnapshot fromJugador(Jugador jugador) {
            PlayerSnapshot s = new PlayerSnapshot();
            s.esHumano = jugador != null && jugador.esHumano();
            if (jugador != null && jugador.getTablero() != null) {
                s.grid = jugador.getTablero().getGrid();
            }
            if (jugador != null && jugador.getFlota() != null) {
                s.flota = jugador.getFlota().toArray(new Barco[0]);
            } else {
                s.flota = new Barco[0];
            }
            return s;
        }

        /**
         * Reconstruye un {@link Jugador} desde el snapshot.
         * @param defaultEsHumano valor por defecto si el snapshot no trae datos suficientes
         * @return jugador reconstruido
         */
        Jugador toJugador(boolean defaultEsHumano) {
            boolean eh = this.esHumano;
            if (this.grid == null && this.flota == null) {
                eh = defaultEsHumano;
            }

            Jugador jugador = new Jugador(eh);
            Tablero tablero = new Tablero();

            if (flota != null) {
                for (Barco b : flota) {
                    if (b == null) continue;
                    tablero.colocarBarco(b);
                }
            }

            if (grid != null) {
                for (int f = 0; f < Math.min(10, grid.length); f++) {
                    for (int c = 0; c < Math.min(10, grid[f].length); c++) {
                        tablero.setEstado(f, c, grid[f][c]);
                    }
                }
            }

            jugador.setTablero(tablero);
            if (flota != null) {
                jugador.setFlota(java.util.Arrays.asList(flota));
            }
            jugador.ensureCreador();
            return jugador;
        }
    }
}
