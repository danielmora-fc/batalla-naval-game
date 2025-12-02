package com.example.batallanavalgame.models;

public class Celda {
    private int estado; // 0 = vacio, 1 = barco, 2 = agua; 3 = tocado, 4 = hundido;
    private Barco barco;

    public Celda() {
        estado = 0; //0 significa que esta vacio;
    }

    public int getEstado() { return estado; }
    public void setEstado(int estado) { this.estado = estado; }
    public Barco getBarco() { return barco; }
    public void setBarco(Barco barco) { this.barco = barco; }
}
