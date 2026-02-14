package com.walmart.challenge.repository;

import com.walmart.challenge.model.DeliveryZone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryZoneRepository extends JpaRepository<DeliveryZone, Long> {
    // Aquí podrías agregar métodos de búsqueda personalizados más adelante,
    // como buscar por zoneCode si fuera necesario.
}