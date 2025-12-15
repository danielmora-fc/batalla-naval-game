package com.example.batallanavalgame.models;

import com.example.batallanavalgame.models.fabrica.BarcoCreador;
import com.example.batallanavalgame.models.fabrica.HumanoBarcoCreador;
import com.example.batallanavalgame.models.fabrica.IABarcoCreador;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

/**
 * Representa un jugador dentro del juego de batalla naval
 */
public class Jugador implements Serializable {
    private static final long serialVersionUID = 1L;
    /** Indica si el jugador es humano o controlado por la IA. */
    private boolean esHumano;
    /** Tablero asociado al jugador */
    private Tablero tablero;
    /** Lista de barcos que conforman la flota del jugador. */
    private List<Barco> flota;
    private transient BarcoCreador creador;

    /**
     * Constructor por defecto.
     * Se utiliza principalmente para reconstrucción de objetos desde persistencia (por ejemplo JSON).
     */
    public Jugador() {
        this(true);
    }

    /**
     * Contructor que crea un jugador humano o maquina
     * @param esHumano true si el jugador es humano, false si es Ia
     */
    public Jugador(boolean esHumano, Tablero tablero, List<Barco> flota) {
        this.esHumano = esHumano;
        this.tablero = tablero != null ? tablero : new Tablero();
        this.flota = flota != null ? flota : new ArrayList<>();
        ensureCreador();
    }

    public Jugador(boolean esHumano) {
        this(esHumano, null, null);
    }
    /**
     * Agrega un barco a la flota del jugador usando el creador correspondiente.
     *
     * @param tipo tipo del barco a crear
     */
    private void agregar(String tipo) {
        Barco barco = creador.crearBarco(tipo, tablero);
        if (barco != null) flota.add(barco);
    }
    /**
     * Genera la flota inicial del jugador con la cantidad y tipos
     * definidos por las reglas del juego.
     */
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
    /**
     * Realiza un disparo sobre el tablero del jugador enemigo.
     *
     * @param enemigo jugador objetivo del disparo
     * @param fila fila seleccionada
     * @param col columna seleccionada
     * @return resultado del disparo según el estado de la celda
     */
    public int dispararA(Jugador enemigo, int fila, int col){
        return enemigo.tablero.disparar(fila, col);
    }

    /**
     * Verifica si todos los barcos han sido hundidos.
     * @return true si toda la flota esta hundida, false en caso contrario
     */
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

    /**
     * Define si el jugador es humano o controlado por la IA.
     * Este setter re-inicializa el creador de barcos si es necesario.
     * @param esHumano true si es humano, false si es IA
     */
    public void setEsHumano(boolean esHumano) {
        this.esHumano = esHumano;
        ensureCreador();
    }

    /**
     * Asigna el tablero del jugador.
     * Si el tablero es null, se inicializa un tablero nuevo.
     * @param tablero tablero a asignar
     */
    public void setTablero(Tablero tablero) {
        this.tablero = tablero != null ? tablero : new Tablero();
    }

    /**
     * Asigna la flota del jugador.
     * Si la flota es null, se inicializa una lista vacía.
     * @param flota lista de barcos
     */
    public void setFlota(List<Barco> flota) {
        this.flota = flota != null ? flota : new ArrayList<>();
    }

    /**
     * Intenta colocar un barco en el tablero del jugador
     * @param tipo tipo del barco
     * @param fila fila inicial
     * @param col columna inicial
     * @param horizontal true si el barco es horizontal y false si es vertical
     * @return true si el barco fue colocado correctamente, false si la posicion no es valida
     */
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

    /**
     * Asegura que el creador de barcos este inicializado
     */
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
