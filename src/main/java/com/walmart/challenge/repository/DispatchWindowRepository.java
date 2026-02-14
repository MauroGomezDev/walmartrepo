package com.walmart.challenge.repository;

import com.walmart.challenge.model.DispatchWindow;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Interfaz de persistencia para DispatchWindow con soporte para concurrencia.
 * * @author Mauricio Gomez
 * @version 1.0
 */
@Repository
public interface DispatchWindowRepository extends JpaRepository<DispatchWindow, String> {

    /**
     * Busca una ventana y bloquea la fila de forma pesimista para evitar
     * que otros hilos modifiquen la capacidad simultaneamente.
     * * @param id Identificador de la ventana
     * @return La ventana con bloqueo aplicado
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT w FROM DispatchWindow w WHERE w.id = :id")
    Optional<DispatchWindow> findByIdWithLock(String id);
}