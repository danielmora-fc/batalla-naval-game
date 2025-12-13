package com.example.batallanavalgame.models.fabrica;

import com.example.batallanavalgame.models.Barco;
import com.example.batallanavalgame.models.Tablero;

public abstract class BarcoCreador {
    // Factory method
    public abstract Barco crearBarco(String tipo, Tablero tablero);
}
