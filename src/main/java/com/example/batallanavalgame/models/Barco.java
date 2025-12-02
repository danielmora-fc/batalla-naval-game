package com.example.batallanavalgame.models;

import java.util.ArrayList;
import java.util.List;

public class Barco {
    private String tipo;
    private int size;//Si es de 1,2,3,4 celdas
    private String orientacion;
    private int initialRow;
    private int initialCol;
    private int golpes = 0;
    //Faltaria un atributo para las posiciones y tambien un metodo para asignarle posiciones
    private List<int[]> posiciones = new ArrayList<int[]>();

    public Barco(String tipo, int size, String orientacion, int initialRow, int initialCol) {this.tipo = tipo;
        this.size = getSize(tipo);
        this.orientacion = orientacion;
        this.initialRow = initialRow;
        this.initialCol = initialCol;
    }

    private int getSize(String tipo) {
        switch (tipo) {
            case "FRAGATA" -> {
                return 1;
            }
            case "DESTRUCTOR" -> {
                return 2;
            }
            case "SUBMARINO" -> {
                return 3;
            }
            case "PORTAAVIONES" -> {
                return 4;
            }
            default ->  {
                return -1;
            }
        }
    }

    public void agregarGolpe() { golpes++; }
    public boolean estaHundido() { return golpes >= size; }

    //getters
    public String getTipo() { return tipo; }
    public int getSize() { return size; }
    public String getOrientacion() { return orientacion; }
    public int getInitialRow() { return initialRow; }
    public int getInitialCol() { return initialCol; }


    public void setPosiciones(List<int[]> posiciones) {
        this.posiciones = posiciones;
    }
}
