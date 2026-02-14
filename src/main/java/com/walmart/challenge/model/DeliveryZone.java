package com.walmart.challenge.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class DeliveryZone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // Ejemplo: "Santiago Centro", "Las Condes"
    private String city; // Ejemplo: "Santiago"

    // El código de área o comuna para validar cobertura
    private String zoneCode;
}