package com.example.batallanavalgame.models;

import java.io.Serializable;

public class Juego implements Serializable {
    private static final long serialVersionUID = 1L;

    private Jugador humano;
    private Jugador maquina;
    private boolean turnoJugador;
    private boolean juegoTerminado;
    private int hundidosHumano; // barcos hundidos por el humano
    private int hundidosIA;     // barcos hundidos por la IA
    private String nickname;

    public Juego(){
        this(new Jugador(true), new Jugador(false));
    }

    public Juego(Jugador humano, Jugador maquina){
        this.humano = humano;
        this.maquina = maquina;
        this.turnoJugador = true;
        this.juegoTerminado = false;
        this.hundidosHumano = 0;
        this.hundidosIA = 0;
    }

    public void iniciarPartida() {
        humano.generarFlotaInicial();
        maquina.generarFlotaInicial();
        juegoTerminado = false;
        turnoJugador = true;
        hundidosHumano = 0;
        hundidosIA = 0;
    }

    public void cambiarTurno() {
        turnoJugador = !turnoJugador;
    }

    public int realizarDisparo(int fila, int columna) {
        int resultado;
        if(juegoTerminado){
            return -1;
        }
        if(turnoJugador){
            resultado = humano.dispararA(maquina,fila,columna);
        } else {
            resultado = maquina.dispararA(humano, fila, columna);
        }

        verificarEstadoPartida();

        if (resultado == 4) {
            if (turnoJugador) hundidosHumano++;
            else hundidosIA++;
        }

        return resultado;
    }

    private void verificarEstadoPartida(){
        if(humano.todosHundidos()){
            juegoTerminado = true;
        }
        if(maquina.todosHundidos()){
            juegoTerminado = true;
        }
    }

    public boolean esTurnoJugador() { return turnoJugador; }
    public boolean estaTerminado() { return juegoTerminado; }
    public Jugador getJugador() { return humano; }
    public Jugador getHumano() { return humano; }
    public Jugador getMaquina() { return maquina; }
    public Jugador getGanador() {
        if(!juegoTerminado){
            return null;
        }
        if(humano.todosHundidos()) {
            return maquina;
        }
        if(maquina.todosHundidos()) {
            return humano;
        }
        return null;
    }

    public int getHundidosHumano() { return hundidosHumano; }
    public int getHundidosIA() { return hundidosIA; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
}
