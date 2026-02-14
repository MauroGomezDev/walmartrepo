package com.walmart.challenge.service;

import com.walmart.challenge.exception.InsufficientCapacityException;
import com.walmart.challenge.exception.ResourceNotFoundException;
import com.walmart.challenge.model.DispatchWindow;
import com.walmart.challenge.repository.DispatchWindowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementacion concreta del servicio de despacho para el desafio de Walmart.
 * Esta clase gestiona la logica de negocio, validaciones de capacidad
 * y asegura la integridad de los datos mediante bloqueos de base de datos.
 * * @author Mauricio Gomez
 * @version 1.2
 */
@Service
public class DispatchServiceImpl implements IDispatchService {

    @Autowired
    private DispatchWindowRepository windowRepository;

    /**
     * Ejecuta la reserva de un cupo en una ventana de despacho y zona especifica.
     * Implementa un bloqueo pesimista (Pessimistic Write) para garantizar que
     * no existan sobre-reservas en escenarios de alta concurrencia.
     * * @param windowId Identificador unico de la ventana de despacho (ej: w-20260128-1)
     * @param zoneId Nombre de la zona donde se solicita el cupo (ej: zone-1)
     * @throws ResourceNotFoundException Si el identificador de la ventana no existe
     * @throws InsufficientCapacityException Si la zona no tiene disponibilidad o no existe
     */
    @Override
    @Transactional
    public void reserveSlot(String windowId, String zoneId) {
        // 1. Obtencion de la ventana con bloqueo de escritura en base de datos
        DispatchWindow window = windowRepository.findByIdWithLock(windowId)
                .orElseThrow(() -> new ResourceNotFoundException("La ventana con ID " + windowId + " no existe."));

        Map<String, Integer> capacities = window.getCapacityByZone();
        Integer available = capacities.get(zoneId);

        // 2. Verificacion de existencia de la zona dentro de la ventana seleccionada
        if (available == null) {
            throw new InsufficientCapacityException("La zona '" + zoneId + "' no esta configurada para esta ventana.");
        }

        // 3. Validacion de disponibilidad de cupos
        if (available <= 0) {
            throw new InsufficientCapacityException("No hay capacidad disponible para la zona: " + zoneId);
        }

        // 4. Actualizacion de capacidad de forma segura
        Map<String, Integer> updatedCapacities = new HashMap<>(capacities);
        updatedCapacities.put(zoneId, available - 1);

        window.setCapacityByZone(updatedCapacities);

        // 5. Persistencia de los cambios dentro de la transaccion activa
        windowRepository.save(window);
    }
}