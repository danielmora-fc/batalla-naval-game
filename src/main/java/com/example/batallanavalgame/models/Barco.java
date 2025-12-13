package com.example.batallanavalgame.models;

import java.io.Serializable;

public class Barco implements Serializable {
    private static final long serialVersionUID = 1L;

    private String tipo;
    private int size;
    private String orientacion; // "HORIZONTAL" o "VERTICAL"
    private int initialRow;
    private int initialCol;
    private int golpes = 0;

    public Barco(String tipo, int size, String orientacion, int initialRow, int initialCol) {
        this.tipo = tipo;
        this.size = size;
        this.orientacion = orientacion;
        this.initialRow = initialRow;
        this.initialCol = initialCol;
    }

    public static int getSizeByType(String tipo) {
        switch (tipo) {
            case "Portaaviones": return 4;
            case "Submarino": return 3;
            case "Destructor": return 2;
            case "Fragata": return 1;
            default: return 0;
        }
    }

    public void agregarGolpe() { golpes++; }

    public boolean estaHundido() { return golpes >= size; }

    public String getTipo() { return tipo; }
    public int getSize() { return size; }
    public String getOrientacion() { return orientacion; }
    public int getInitialRow() { return initialRow; }
    public int getInitialCol() { return initialCol; }
}
