package com.walmart.challenge.service;

import com.walmart.challenge.exception.InsufficientCapacityException;
import com.walmart.challenge.exception.ResourceNotFoundException;

/**
 * Interfaz que define el contrato para los servicios de despacho.
 * Permite abstraer la logica de negocio de su implementacion concreta.
 * * @author Mauricio Gomez
 * @version 1.0
 */
public interface IDispatchService {

    /**
     * Procesa la reserva de un cupo para una ventana y zona determinada.
     * * @param windowId Identificador unico de la ventana de despacho
     * @param zoneId Nombre de la zona geografica donde se requiere el cupo
     * @throws ResourceNotFoundException Si el ID de la ventana no existe
     * @throws InsufficientCapacityException Si la zona no tiene cupos disponibles
     */
    void reserveSlot(String windowId, String zoneId);
}