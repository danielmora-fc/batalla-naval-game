package com.example.batallanavalgame.models.fabrica;

import com.example.batallanavalgame.models.Barco;
import com.example.batallanavalgame.models.Tablero;

import java.util.Random;

public class IABarcoCreador extends BarcoCreador {
    private Random random = new Random();

    @Override
    public Barco crearBarco(String tipo, Tablero tablero) {
        while (true) {
            int fila = random.nextInt(10);  // Genera una fila aleatoria entre 0 y 9
            int columna = random.nextInt(10);  // Genera una columna aleatoria entre 0 y 9
            String orientacion = random.nextBoolean() ? "VERTICAL" : "HORIZONTAL";  // Orientación aleatoria

            // Obtener el tamaño del barco usando el tipo
            int size = Barco.getSizeByTipo(tipo);

            // Crear el barco con la orientación y las coordenadas generadas
            Barco barco = new Barco(tipo, size, orientacion, fila, columna);

            // Intenta colocar el barco en el tablero
            if (tablero.colocarBarco(barco)) {
                return barco;  // Si el barco se coloca correctamente, lo retornamos
            }
        }
    }
}
