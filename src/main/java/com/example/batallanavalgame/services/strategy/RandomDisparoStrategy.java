package com.example.batallanavalgame.services.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomDisparoStrategy implements DisparoStrategy {
    private final Random random = new Random();
    private final List<int[]> availableShots = new ArrayList<>();

    public RandomDisparoStrategy() {
        // Initialize all possible positions
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                availableShots.add(new int[]{i, j});
            }
        }
    }

    @Override
    public int[] nextShot(int[][] board) {
        // Remove already shot positions from available shots
        // Board states: 0=empty, 1=ship, 2=miss, 3=hit, 4=sunk
        availableShots.removeIf(shot -> {
            int state = board[shot[0]][shot[1]];
            return state == 2 || state == 3 || state == 4; // Remove positions already shot
        });
        
        // If no available shots, return a random position (shouldn't happen in normal game)
        if (availableShots.isEmpty()) {
            return new int[]{random.nextInt(10), random.nextInt(10)};
        }
        
        // Pick a random available shot
        int index = random.nextInt(availableShots.size());
        return availableShots.get(index);
    }
}