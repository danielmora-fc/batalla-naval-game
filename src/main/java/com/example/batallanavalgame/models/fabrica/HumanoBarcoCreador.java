package com.example.batallanavalgame.models.fabrica;

import com.example.batallanavalgame.models.Barco;
import com.example.batallanavalgame.models.Tablero;

public class HumanoBarcoCreador extends BarcoCreador {

    @Override
    public Barco crearBarco(String tipo, Tablero tablero) {
        // Lógica para crear barcos para el jugador humano
        int fila = 0;  // Este valor debe ser proporcionado por el jugador
        int columna = 0;  // Este valor debe ser proporcionado por el jugador
        String orientacion = "HORIZONTAL";  // Este valor también debe ser elegido por el jugador

        // Obtener el tamaño del barco usando el tipo
        int size = Barco.getSizeByTipo(tipo);

        // Crear el barco con la información proporcionada
        Barco barco = new Barco(tipo, size, orientacion, fila, columna);

        // Intenta colocar el barco en el tablero
        if (tablero.colocarBarco(barco)) {
            return barco;
        }

        // Si no puede colocarse, podría intentar con otras coordenadas
        return null;
    }
}
