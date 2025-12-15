package com.example.batallanavalgame.models;

import java.io.Serializable;

/**
 * Representa un barco dentro del juego Batalla Naval
 */
public class Barco implements Serializable {
    private static final long serialVersionUID = 1L;
    /** Tipo del barco */
    private String tipo;
    /** Numero de celdas que ocupa el barco */
    private int size;
    /** Orientación del barco: "HORIZONTAL" o "VERTICAL". */
    private String orientacion;
    /** Fila inicial donde se coloca el barco. */
    private int initialRow;
    /** Columna inicial donde se coloca el barco. */
    private int initialCol;
    /** Cantidad de golpes que ha recibido el barco. */
    private int golpes = 0;

    /**
     * Constructor que crea un barco con todos sus atributos
     * @param tipo tipo del barco
     * @param size tamaño del barco en celdas
     * @param orientacion orientación del barco ("HORIZONTAL" o "VERTICAL")
     * @param initialRow fila inicial en el tablero
     * @param initialCol columna inicial en el tablero
     */
    public Barco(String tipo, int size, String orientacion, int initialRow, int initialCol) {
        this.tipo = tipo;
        this.size = size;
        this.orientacion = orientacion;
        this.initialRow = initialRow;
        this.initialCol = initialCol;
    }
    /**
     * Obtiene el tamaño de un barco según su tipo.
     *
     * @param tipo tipo del barco
     * @return tamaño correspondiente al tipo del barco
     */
    public static int getSizeByType(String tipo) {
        switch (tipo) {
            case "Portaaviones": return 4;
            case "Submarino": return 3;
            case "Destructor": return 2;
            case "Fragata": return 1;
            default: return 0;
        }
    }

    /**
     * Registra golpe recibido a un barco
     */
    public void agregarGolpe() { golpes++; }

    /**
     * Verifica si el barco ha sido hundido.
     * @return true si el numero de golpes es mayor o igual al tamaño del barco, false en caso contrario
     */
    public boolean estaHundido() { return golpes >= size; }

    public String getTipo() { return tipo; }
    public int getSize() { return size; }
    public String getOrientacion() { return orientacion; }
    public int getInitialRow() { return initialRow; }
    public int getInitialCol() { return initialCol; }
}
