package com.example.batallanavalgame.models;

import java.io.Serializable;

/**
 * Representa una celda en el tablero de batalla naval
 */
public class Celda implements Serializable {
    private static final long serialVersionUID = 1L;
    /** Estado de la celda */
    private int estado;
    /** Referencia al barco que ocupa la celda */
    private Barco barco;
    /** Constructor que inicializa la celda como vacio y sin barco asignado. */
    public Celda() {
        this.estado = 0;
        this.barco = null;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public Barco getBarco() {
        return barco;
    }

    public void setBarco(Barco barco) {
        this.barco = barco;
    }
}