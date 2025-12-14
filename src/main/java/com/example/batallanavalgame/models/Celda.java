package com.example.batallanavalgame.models;

import java.io.Serializable;

public class Celda implements Serializable {
    private static final long serialVersionUID = 1L;
    private int estado;
    private Barco barco;

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