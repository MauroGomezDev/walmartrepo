package com.walmart.challenge.exception;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * Estructura estandarizada para las respuestas de error de la API.
 * * @author Mauricio Gomez
 * @version 1.0
 */
@Data
@Builder
public class ErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
}