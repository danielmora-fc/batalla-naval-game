package com.example.batallanavalgame.services;

import com.example.batallanavalgame.models.Juego;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PersistenceFacade {
    private static final String SAVE_FILE = "batalla_save.ser";
    private static final String NICKNAME_FILE = "nickname.txt";
    private static final String STATS_FILE = "stats.txt";

    public static void saveGame(Juego juego) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
            oos.writeObject(juego);
        } catch (IOException e) {
            System.err.println("Error saving game: " + e.getMessage());
        }
    }

    public static Juego loadGame() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SAVE_FILE))) {
            return (Juego) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading game: " + e.getMessage());
            return null;
        }
    }

    public static void saveNickname(String nickname) {
        try {
            Files.write(Paths.get(NICKNAME_FILE), nickname.getBytes());
        } catch (IOException e) {
            System.err.println("Error saving nickname: " + e.getMessage());
        }
    }

    public static String loadNickname() {
        try {
            return Files.readString(Paths.get(NICKNAME_FILE));
        } catch (IOException e) {
            return "";
        }
    }

    public static void saveStats(int playerSunk, int machineSunk) {
        try {
            String stats = playerSunk + "\n" + machineSunk + "\n";
            Files.write(Paths.get(STATS_FILE), stats.getBytes());
        } catch (IOException e) {
            System.err.println("Error saving stats: " + e.getMessage());
        }
    }

    public static int[] loadStats() {
        try {
            String content = Files.readString(Paths.get(STATS_FILE));
            String[] lines = content.trim().split("\n");
            return new int[]{Integer.parseInt(lines[0]), Integer.parseInt(lines[1])};
        } catch (IOException | ArrayIndexOutOfBoundsException | NumberFormatException e) {
            return new int[]{0, 0};
        }
    }
}