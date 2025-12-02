package com.example.batallanavalgame.models.fabrica;

import com.example.batallanavalgame.models.Barco;
import com.example.batallanavalgame.models.Tablero;

public class HumanoBarcoCreador extends BarcoCreador {
    @Override
    public Barco crearBarco(String tipo,  Tablero tablero) {
        return new Barco(tipo,0,"HORIZONTAL", -1,-1);
    }
}
