// BarcoNoValidoException.java
package com.example.batallanavalgame.models;

/**
 * Excepción personalizada para cuando un barco no pueda ser colocado en el tablero.
 */
public class BarcoNoValidoException extends Exception {
    public BarcoNoValidoException(String mensaje) {
        super(mensaje);
    }
}
