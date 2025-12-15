package com.example.batallanavalgame.models;

import java.io.Serializable;

/**
 * Representa una partida del juego Batalla Naval
 */
public class Juego implements Serializable {

    private static final long serialVersionUID = 1L;
    /** Jugador humano que participa en la partida */
    private Jugador humano;
    /** Jugador controlado por la maquina */
    private Jugador maquina;
    /** Indica si el turno actual es del jugador humano */
    private boolean turnoJugador;
    /** Indica si la partida termino */
    private boolean juegoTerminado;
    /** Numero de barcos hundidos por el jugador humano */
    private int hundidosHumano;
    /** Numero de barcos hundidos por la maquina */
    private int hundidosIA;
    /** Nombre o apodo del humano */
    private String nickname;

    /**
     * Constructor por defecto
     * Crea una nueva partida con un jugador humano y un jugador maquina
     */
    public Juego(){
        this(new Jugador(true), new Jugador(false));
    }

    /**
     * Constructor que permite inicializar la partida con jugadores especificos
     * @param humano Jugador humano
     * @param maquina Jugador maquina
     */
    public Juego(Jugador humano, Jugador maquina){
        this.humano = humano;
        this.maquina = maquina;
        this.turnoJugador = true;
        this.juegoTerminado = false;
        this.hundidosHumano = 0;
        this.hundidosIA = 0;
    }

    /**
     * Inicializa una nueva partida.
     * Genera las flotas iniciales de ambos jugadores y reinicia
     * los contadores y el estado del juego.
     */
    public void iniciarPartida() {
        humano.generarFlotaInicial();
        maquina.generarFlotaInicial();
        juegoTerminado = false;
        turnoJugador = true;
        hundidosHumano = 0;
        hundidosIA = 0;
    }

    /**
     * Cambia el turno actual del juego.
     * Si era el turno del jugador humano, pasa a la máquina y viceversa.
     */
    public void cambiarTurno() {
        turnoJugador = !turnoJugador;
    }

    /**
     * Realiza un disparo en la posicion indicada segun el turno actual
     * @param fila fila del tablero donde se realiza el disparo
     * @param columna columna del tablero donde se realiza el disparo
     * @return un valor entero que representa el resultado del disparo
     */
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

    /**
     * Verifica si alguno de los jugadores no tiene mas barcos
     * Si es asi, la partida termina
     */
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
