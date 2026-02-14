package com.walmart.challenge.exception;

/**
 * Excepcion lanzada cuando un recurso solicitado no existe en el sistema.
 * * @author Mauricio Gomez
 * @version 1.0
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructor que recibe el mensaje de error.
     * * @param message Detalle del error para el usuario
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}