package com.walmart.challenge.service;

import com.walmart.challenge.exception.InsufficientCapacityException;
import com.walmart.challenge.exception.ResourceNotFoundException;
import com.walmart.challenge.model.DispatchWindow;
import com.walmart.challenge.repository.DispatchWindowRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

/**
 * Pruebas unitarias para la logica de negocio de despacho.
 * Verifica escenarios de capacidad insuficiente.
 * @author Mauricio Gomez
 */
public class DispatchServiceTest {

    @Mock
    private DispatchWindowRepository repository;

    @InjectMocks
    private DispatchServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenCapacityIsZero_thenThrowException() {
        // GIVEN: Una ventana con capacidad 0 para la zona-1
        String windowId = "w-test-1";
        String zoneId = "zone-1";

        DispatchWindow window = new DispatchWindow();
        window.setId(windowId);
        Map<String, Integer> capacity = new HashMap<>();
        capacity.put(zoneId, 0); // SIN CAPACIDAD
        window.setCapacityByZone(capacity);

        when(repository.findByIdWithLock(windowId)).thenReturn(Optional.of(window));

        // WHEN & THEN: Al intentar reservar, debe lanzar InsufficientCapacityException
        assertThrows(InsufficientCapacityException.class, () -> {
            service.reserveSlot(windowId, zoneId);
        });
    }

    @Test
    void whenCapacityAvailable_thenReserveSuccessfully() {
        String windowId = "w-1";
        String zoneId = "zone-1";

        DispatchWindow window = new DispatchWindow();
        window.setId(windowId);

        // SOLUCIÓN: Creamos el mapa e inicializamos la capacidad
        Map<String, Integer> capacityMap = new HashMap<>();
        capacityMap.put(zoneId, 5);

        // Le pasamos el mapa completo al objeto
        window.setCapacityByZone(capacityMap);

        when(repository.findByIdWithLock(windowId)).thenReturn(Optional.of(window));

        // Ejecución
        service.reserveSlot(windowId, zoneId);

        // Verificación
        assertEquals(4, window.getCapacityByZone().get(zoneId));
        verify(repository).save(window);
    }

    @Test
    void whenWindowNotFound_thenThrowException() {
        String windowId = "no-existe";
        when(repository.findByIdWithLock(windowId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            service.reserveSlot(windowId, "zone-1");
        });
    }
}