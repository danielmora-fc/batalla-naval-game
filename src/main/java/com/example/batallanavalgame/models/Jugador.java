package com.example.batallanavalgame.models;

import com.example.batallanavalgame.models.fabrica.BarcoCreador;
import com.example.batallanavalgame.models.fabrica.HumanoBarcoCreador;
import com.example.batallanavalgame.models.fabrica.IABarcoCreador;

import java.util.ArrayList;
import java.util.List;

public class Jugador {
    private boolean esHumano;
    private Tablero tablero;
    private List<Barco> flota;
    private BarcoCreador creador;

    public Jugador(boolean esHumano) {
        this.esHumano = esHumano;
        this.tablero = new Tablero();
        this.flota = new ArrayList<>();

        if(esHumano){
            this.creador = new HumanoBarcoCreador();
        } else {
            this.creador = new IABarcoCreador();
        }
    }
    //Metodo auxiliar para evitar esribir mucho codigo
    private void agregar(String tipo) {
        Barco barco = creador.crearBarco(tipo, tablero);
        flota.add(barco);
    }

    public void generarFlotaInicial() {
        agregar("PORTAAVIONES");
        agregar("SUBMARINO");
        agregar("SUBMARINO");
        agregar("DESTRUCTOR");
        agregar("DESTRUCTOR");
        agregar("DESTRUCTOR");
        agregar("FRAGATA");
        agregar("FRAGATA");
        agregar("FRAGATA");
        agregar("FRAGATA");
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

    //Getters
    public boolean esHumano() { return esHumano; }
    public Tablero getTablero() { return tablero; }
    public List<Barco> getFlota() { return flota; }
}
