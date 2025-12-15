package com.example.batallanavalgame.models.fabrica;

import com.example.batallanavalgame.models.Barco;
import com.example.batallanavalgame.models.Tablero;

/**
 * Clase abstracta que implementa el patron de diseño creacional
 * Factory Method para la creacion de objetos (Barco)
 */
public abstract class BarcoCreador {
    // Factory method

    /**
     *  Factory Method encargado de crear un barco segun el tipo
     * @param tipo tipo de barco a crear
     * @param tablero tablero donde se intentara colocar el barco
     * @return un barco creado y colocado correctamente
     */
    public abstract Barco crearBarco(String tipo, Tablero tablero);
}
