package com.example.batallanavalgame.models;

public class Juego {
    private Jugador humano;
    private Jugador maquina;
    private boolean turnoJugador;
    private boolean juegoTerminado;

    public Juego(){
        humano = new Jugador(true);
        maquina = new Jugador(false);
        //El humano inicia
        turnoJugador = true;
        juegoTerminado = false;
    }
    public void iniciarPartida() {
        //Crear flota del humano
        humano.generarFlotaInicial();
        //Crear flota de la IA, aqui coloca los barcos en el tablero
        maquina.generarFlotaInicial();
        // reiniciar el juego
        juegoTerminado = false;
        turnoJugador = true;
    }

    public void cambiarTurno() {
        //Si era true, lo volvera false
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

        //Despues de disparar se verifica si alguien gano
        verificarEstadoPartida();

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

    // getters
    public boolean esTurnoJugador() { return turnoJugador; }
    public boolean estaTerminado() { return juegoTerminado; }
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
}
