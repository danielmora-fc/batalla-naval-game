package com.example.batallanavalgame.models;

import com.example.batallanavalgame.models.fabrica.BarcoCreador;
import com.example.batallanavalgame.models.fabrica.HumanoBarcoCreador;
import com.example.batallanavalgame.models.fabrica.IABarcoCreador;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class Jugador implements Serializable {
    private static final long serialVersionUID = 1L;
    private boolean esHumano;
    private Tablero tablero;
    private List<Barco> flota;
    private transient BarcoCreador creador;

    public Jugador(boolean esHumano) {
        this.esHumano = esHumano;
        this.tablero = new Tablero();
        this.flota = new ArrayList<>();
        ensureCreador();
    }
    private void agregar(String tipo) {
        Barco barco = creador.crearBarco(tipo, tablero);
        if (barco != null) flota.add(barco);
    }

    public void generarFlotaInicial() {
        agregar("Portaaviones");
        agregar("Submarino");
        agregar("Submarino");
        agregar("Destructor");
        agregar("Destructor");
        agregar("Destructor");
        agregar("Fragata");
        agregar("Fragata");
        agregar("Fragata");
        agregar("Fragata");
    }

    public int dispararA(Jugador enemigo, int fila, int col){
        return enemigo.tablero.disparar(fila, col);
    }

    public boolean todosHundidos(){
        for(Barco barco : flota){
            if(!barco.estaHundido()) {
                return false;
            }
        }
        return true;
    }

    public boolean esHumano() { return esHumano; }
    public Tablero getTablero() { return tablero; }
    public List<Barco> getFlota() { return flota; }

    public boolean colocarBarco(String tipo, int fila, int col, boolean horizontal) {
        ensureCreador();
        int size = Barco.getSizeByType(tipo);
        if (tablero.estaLibre(fila, col, size, horizontal)) {
            String orientacion = horizontal ? "HORIZONTAL" : "VERTICAL";
            Barco barco = new Barco(tipo, size, orientacion, fila, col);
            tablero.colocarBarco(barco, fila, col, horizontal);
            flota.add(barco);
            return true;
        }
        return false;
    }

    public void ensureCreador() {
        if (this.creador == null) {
            if (esHumano) {
                this.creador = new HumanoBarcoCreador();
            } else {
                this.creador = new IABarcoCreador();
            }
        }
    }
}
