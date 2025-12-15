package com.example.batallanavalgame.models.fabrica;

import com.example.batallanavalgame.models.Barco;
import com.example.batallanavalgame.models.Tablero;

import java.util.Random;

/**
 * Implementacion del patron factory method para la creacion de barcos por la maquina
 */
public class IABarcoCreador extends BarcoCreador {
    private Random random = new Random();

    /**
     * Crea un barco para la maquina generado aleatoriamente
     * @param tipo tipo de barco a crear
     * @param tablero tablero donde se intentara colocar el barco
     * @return el barco creado y colocado correctamente
     */
    @Override
    public Barco crearBarco(String tipo, Tablero tablero) {
        while (true) {
            int fila = random.nextInt(10);  // Genera una fila aleatoria entre 0 y 9
            int columna = random.nextInt(10);  // Genera una columna aleatoria entre 0 y 9
            String orientacion = random.nextBoolean() ? "VERTICAL" : "HORIZONTAL";  // Orientación aleatoria

            // Obtener el tamaño del barco usando el tipo
            int size = Barco.getSizeByType(tipo);

            // Crear el barco con la orientación y las coordenadas generadas
            Barco barco = new Barco(tipo, size, orientacion, fila, columna);

            // Intenta colocar el barco en el tablero
            if (tablero.colocarBarco(barco)) {
                return barco;  // Si el barco se coloca correctamente, lo retornamos
            }
        }
    }
}
