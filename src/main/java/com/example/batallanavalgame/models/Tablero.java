package com.example.batallanavalgame.models;

import java.io.Serializable;

/**
 * Representa el tablero en el juego batalla naval
 * El tablero esta compuesto por una matriz de 10x10
 */
public class Tablero implements Serializable {
    private static final long serialVersionUID = 1L;

    private Celda[][] grid = new Celda[10][10];

    /**
     * Contructor del tablero
     * Inicializa la matriz de celdas creando una celda vacia en cada posicion
     */
    public Tablero() {
        for(int i = 0; i < 10; i++) {
            for(int j = 0; j < 10; j++) {
                grid[i][j] = new Celda();
            }
        }
    }

    /**
     * Verifica si el barco puede colocarse en una posicion especifica del tablero
     * @param fila fila inicial donde se intenta colocar el barco
     * @param col columna inicial donde se intenta colocar el barco
     * @param size Tamaño del barco a colocar.
     * @param horizontal Indica si el barco se coloca horizontalmente.
     * @return true si el espacio está libre y el barco cabe en el tablero y false en caso contrario
     */
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

    /**
     * Coloca un barco en el tablero en una posición y orientación específicas.
     * @param barco Barco que se va a colocar.
     * @param fila Fila inicial del barco.
     * @param col Columna inicial del barco.
     * @param horizontal Indica si el barco se coloca horizontalmente.
     */
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

    /**
     * Coloca un barco en el tablero utilizando los datos internos del barco.
     *  Antes de colocarlo, verifica que la posición sea válida y no se superponga con otros barcos
     * @param barco Barco que se desea colocar
     * @return true si el baro se coloco correctamente, false si la posicion no es valida
     */
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

    /**
     * Procesa un disparo en la posicion indicada del tablero
     * @param f fila del disparo
     * @param c columa del disparo
     * @return un valor entero que representa el resultado del disparo
     */
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

    /**
     * Devuelve una representación del tablero como una matriz de enteros.
     * @return Matriz 10x10 con los estados del tablero.
     */
    public int[][] getGrid() {
        int[][] gridInt = new int[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                gridInt[i][j] = grid[i][j].getEstado();
            }
        }
        return gridInt;
    }

    /**
     * Asigna manualmente el estado de una celda del tablero.
     * Este método se usa principalmente para restaurar el tablero al cargar una partida guardada.
     * @param f fila
     * @param c columna
     * @param estado estado de la celda
     */
    public void setEstado(int f, int c, int estado) {
        grid[f][c].setEstado(estado);
    }

    /**
     * Obtiene el estado de una celda especifica
     * @param f fila de la celda
     * @param c columna de la celda
     * @return Estado actual de la celda
     */
    public int getEstado(int f, int c) {
        return grid[f][c].getEstado();
    }
}
