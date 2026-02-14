package com.walmart.challenge.exception;

/**
 * Excepcion lanzada cuando se intenta realizar una reserva en una zona sin cupos.
 * * @author Mauricio Gomez
 * @version 1.0
 */
public class InsufficientCapacityException extends RuntimeException {

    /**
     * Constructor que recibe el mensaje de error.
     * * @param message Detalle del error de capacidad
     */
    public InsufficientCapacityException(String message) {
        super(message);
    }
}