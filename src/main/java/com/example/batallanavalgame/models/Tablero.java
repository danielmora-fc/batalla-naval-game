package com.example.batallanavalgame.models;

import java.io.Serializable;

public class Tablero implements Serializable {
    private static final long serialVersionUID = 1L;

    private Celda[][] grid = new Celda[10][10];

    public Tablero() {
        for(int i = 0; i < 10; i++) {
            for(int j = 0; j < 10; j++) {
                grid[i][j] = new Celda();
            }
        }
    }

    public boolean estaLibre(int fila, int col, int size, boolean horizontal) {
        for(int i = 0; i < size; i++) {
            int f = fila;
            int c = col;
            if(horizontal) {
                c = col + i;
            } else {
                f = fila + i;
            }
            if(f < 0 || f > 9 || c < 0 || c > 9) {
                return false;
            }
            if(grid[f][c].getEstado() != 0) {
                return false;
            }
        }
        return true;
    }

    public void colocarBarco(Barco barco, int fila, int col, boolean horizontal) {
        int size = barco.getSize();
        for(int i = 0; i < size; i++) {
            int f = fila;
            int c = col;
            if(horizontal) {
                c = col + i;
            } else {
                f = fila + i;
            }
            grid[f][c].setEstado(1);
            grid[f][c].setBarco(barco);
        }
    }

    public boolean colocarBarco(Barco barco) {
        int size = barco.getSize();
        int fila = barco.getInitialRow();
        int col = barco.getInitialCol();
        String orientacion = barco.getOrientacion();


        //1.) Verificar que el barco si se pueda colocar
        for(int i = 0; i < size; i++) {
            int f = fila;
            int c = col;
            //Si el barco esta vertical que aumente la fila
            if(orientacion.equals("VERTICAL")) {
                f = fila + i;
            }
            //Si el barco esta horizontal que aumente la columna
            if(orientacion.equals("HORIZONTAL")) {
                c = col + i;
            }
            //Validar si se sale del tablero
            if(f < 0 || f > 9 || c < 0 || c > 9) {
                return false;
            }
            //Validar que no se sobreponga con otros barcos
            if(grid[f][c].getEstado() == 1) {
                return false;
            }

            //Si pasa todos esos condicionales significa que si se puede colocar
        }

        //2.) Si pasa la validacion entonces ahora si se coloca el barco
        for(int i = 0; i < size; i++) {
            int f = fila;
            int c = col;
            if(orientacion.equals("VERTICAL")) {
                f = fila + i;
            }
            else {
                c = col + i;
            }
            grid[f][c].setEstado(1);
            grid[f][c].setBarco(barco);
        }
        return true;
    }

    public int disparar(int f, int c) {
        Celda celda = grid[f][c];
        int estado = celda.getEstado();
        //AGUA
        if (estado == 0) {
            celda.setEstado(2);
            return 2;
        }
        //TOCADO
        if(estado == 1) {
            Barco barco = celda.getBarco();
            barco.agregarGolpe();
            //Verificar si se hundio
            if(barco.estaHundido()) {
                //Aqui va la logica de asignar como hundidas todas las celdas del barco
                for(int i = 0; i < barco.getSize(); i++) {
                    int fila = barco.getInitialRow();
                    int col = barco.getInitialCol();
                    if(barco.getOrientacion().equals("VERTICAL")) {
                        fila = barco.getInitialRow() + i;
                    } else {
                        col = barco.getInitialCol() + i;
                    }
                    grid[fila][col].setEstado(4);
                }
                return 4;
            }
            //Si no se hundio y solo fue tocado
            celda.setEstado(3);
            return 3;
        }
        //Si el usuario selecciona una celda que ya se haya presionado simplemente la devuelve
        return estado;

    }

    public int[][] getGrid() {
        int[][] gridInt = new int[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                gridInt[i][j] = grid[i][j].getEstado();
            }
        }
        return gridInt;
    }

    public int getEstado(int f, int c) {
        return grid[f][c].getEstado();
    }
}
