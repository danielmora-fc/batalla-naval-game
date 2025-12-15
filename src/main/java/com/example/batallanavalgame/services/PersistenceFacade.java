package com.example.batallanavalgame.services;

import com.example.batallanavalgame.models.Juego;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public class PersistenceFacade {
    private static final Path SAVE_FILE = Paths.get("batalla_save.ser");
    private static final Path PLAYER_FILE = Paths.get("player.txt"); // archivo plano

    // ---------- SERIALIZABLE (Juego completo) ----------
    public static void saveGame(Juego juego) throws IOException {
        // Guardado atómico (evita archivo corrupto si se cierra a mitad)
        Path tmp = Paths.get(SAVE_FILE.toString() + ".tmp");
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(tmp))) {
            oos.writeObject(juego);
        }
        Files.move(tmp, SAVE_FILE, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
    }

    public static Juego loadGame() throws IOException, ClassNotFoundException {
        if (!Files.exists(SAVE_FILE)) return null;
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(SAVE_FILE))) {
            return (Juego) ois.readObject();
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
}
