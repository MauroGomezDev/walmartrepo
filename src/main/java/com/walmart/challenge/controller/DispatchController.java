package com.walmart.challenge.controller;

import com.walmart.challenge.model.DispatchWindow;
import com.walmart.challenge.repository.DispatchWindowRepository;
import com.walmart.challenge.service.DispatchServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controlador REST para la gestion de ventanas de despacho.
 * * @author Mauricio Gomez
 * @version 1.0
 */
@RestController
@RequestMapping("/api/v1/dispatch")
@CrossOrigin(origins = "http://localhost:3000") // Permite que React se conecte
public class DispatchController {

    @Autowired
    private DispatchWindowRepository windowRepository;

    @Autowired
    private DispatchServiceImpl dispatchService;

    /**
     * Obtiene el listado completo de ventanas de despacho y sus capacidades.
     * * @return Lista de ventanas en formato JSON
     */
    @GetMapping("/windows")
    public List<DispatchWindow> getAllWindows() {
        return windowRepository.findAll();
    }

    /**
     * Realiza una reserva mediante el ID de ventana y zona.
     * El manejo de errores es delegado al GlobalExceptionHandler.
     * * @param windowId ID de la ventana
     * @param zoneId Nombre de la zona
     * @return ResponseEntity con mensaje de exito
     */
    @PostMapping("/reserve/{windowId}")
    public ResponseEntity<String> reserve(@PathVariable String windowId, @RequestParam String zoneId) {
        dispatchService.reserveSlot(windowId, zoneId);
        return ResponseEntity.ok("Reserva realizada con exito en " + zoneId);
    }
}