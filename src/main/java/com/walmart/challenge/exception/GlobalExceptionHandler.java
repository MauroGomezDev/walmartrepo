package com.walmart.challenge.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

/**
 * Controlador de consejos (Advice) que centraliza el manejo de excepciones de toda la API.
 * Captura las excepciones de negocio y las transforma en respuestas JSON estandarizadas.
 * * @author Mauricio Gomez
 * @version 1.0
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Captura y procesa excepciones de recursos no encontrados.
     * * @param ex La excepcion capturada de tipo ResourceNotFoundException
     * @param request El objeto de la peticion HTTP actual
     * @return ResponseEntity con el objeto ErrorResponse y estatus 404
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Not Found")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * Captura y procesa excepciones de falta de capacidad logistica.
     * * @param ex La excepcion capturada de tipo InsufficientCapacityException
     * @param request El objeto de la peticion HTTP actual
     * @return ResponseEntity con el objeto ErrorResponse y estatus 409
     */
    @ExceptionHandler(InsufficientCapacityException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientCapacity(InsufficientCapacityException ex, HttpServletRequest request) {
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .error("Conflict")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    /**
     * Captura cualquier excepcion no controlada especificamente.
     * Proporciona una respuesta de seguridad para no exponer detalles tecnicos.
     * * @param ex La excepcion generica capturada
     * @param request El objeto de la peticion HTTP actual
     * @return ResponseEntity con el objeto ErrorResponse y estatus 500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, HttpServletRequest request) {
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal Server Error")
                .message("Ocurrio un error inesperado en el servidor")
                .path(request.getRequestURI())
                .build();
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}