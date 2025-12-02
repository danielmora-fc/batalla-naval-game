package com.example.batallanavalgame.models.fabrica;

import com.example.batallanavalgame.models.Barco;
import com.example.batallanavalgame.models.Tablero;

import java.util.Random;

public class IABarcoCreador extends BarcoCreador {
    private Random random = new Random();

    @Override
    public Barco crearBarco(String tipo, Tablero tablero) {

        while (true) {
            int fila = random.nextInt(10);
            int columna = random.nextInt(10);
            String orientacion;
            if(random.nextBoolean()){
                orientacion = "VERTICAL";
            } else {
                orientacion = "HORIZONTAL";
            }
            //Crea el barco teniendo la orientacion y coordenadas definidas
            Barco barco = new Barco(tipo, 0, orientacion, fila, columna);
            //Intenta colocar el barco en el tablero, si no puede prueba con otras coordenadas
            if(tablero.colocarBarco(barco)){
                return barco;
            }
        }
    }
}
