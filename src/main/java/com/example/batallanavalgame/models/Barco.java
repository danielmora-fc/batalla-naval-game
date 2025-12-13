package com.example.batallanavalgame.models;

public class Barco {
    private String tipo;
    private int size;
    private String orientacion;
    private int initialRow;
    private int initialCol;
    private int golpes = 0;

    // Constructor actualizado: ahora incluye el tamaño
    public Barco(String tipo, int size, String orientacion, int initialRow, int initialCol) {
        this.tipo = tipo;
        this.size = size;  // Recibe el tamaño como un parámetro
        this.orientacion = orientacion;
        this.initialRow = initialRow;
        this.initialCol = initialCol;
    }

    // Método para obtener el tamaño según el tipo
    public static int getSizeByTipo(String tipo) {
        switch (tipo) {
            case "PORTAAVIONES": return 4;
            case "SUBMARINO": return 3;
            case "DESTRUCTOR": return 2;
            case "FRAGATA": return 1;
            default: return 0;
        }
    }

    public void agregarGolpe() {
        golpes++;
    }

    public boolean estaHundido() {
        return golpes >= size;
    }

    // Getters y Setters
    public String getTipo() { return tipo; }
    public int getSize() { return size; }
    public String getOrientacion() { return orientacion; }
    public int getInitialRow() { return initialRow; }
    public int getInitialCol() { return initialCol; }
}
