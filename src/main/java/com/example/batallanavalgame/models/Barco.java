package com.example.batallanavalgame.models;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

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


    /**
     * Method for Build ships by type
     * ever ship has a dimensions, by default one position have a 20 x 20.
     * @author Daniel Bolivar
     * @version 1.0.0
     * @since 13-12-2025
     */
    public Pane buildShip(String type, int orientation){
        int valueOfBox = 20;
        int dimensions = 0;

        switch (type){
            case "PORTAAVIONES":
                dimensions = 4;
            break;
            case "SUBMARINO":
                dimensions = 3;
            break;
            case "DESTRUCTOR":
                dimensions = 2;
            break;
            case "FRAGATA":
                dimensions = 1;
            break;
        }

        Pane canva = new Pane();

        int width = valueOfBox;
        int height = dimensions * valueOfBox;

        // build elements for ship
        // En esta seccion podemos completar el diseño del barco por ahora es un cuadrado simple.
        Rectangle body = new Rectangle(width, height);
        int valueOfOrientation = orientation % 4;
        switch(orientation){
            case 1:
                canva.setPrefWidth(width);
                canva.setPrefHeight(height);
                body.setRotate(0);
                canva.setRotate(0);
            break;
            case 2:
                canva.setPrefWidth(width);
                canva.setPrefHeight(height);
                body.setRotate(90);
                canva.setRotate(90);
            break;
            case 3:
                canva.setPrefWidth(width);
                canva.setPrefHeight(height);
                body.setRotate(180);
                canva.setRotate(180);
            break;
            case 4:
                canva.setPrefWidth(width);
                canva.setPrefHeight(height);
                body.setRotate(270);
                canva.setRotate(270);
            break;
        }

        canva.getChildren().add(body);
        return canva;
    }
}
