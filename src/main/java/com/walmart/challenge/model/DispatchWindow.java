package com.walmart.challenge.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

/**
 * Entidad que representa una ventana de despacho logistica.
 * Incluye formato personalizado para la serializacion de horas a HH:mm.
 * * @author Mauricio Gomez
 * @version 1.2
 */
@Entity
@Data
public class DispatchWindow {

    /** Identificador unico de la ventana (ej: w-20260128-1) */
    @Id
    private String id;

    /** Fecha programada para el despacho */
    private LocalDate date;

    /** * Hora de inicio del bloque horario.
     * Formato de salida: HH:mm (ej: 09:00)
     */
    @JsonFormat(pattern = "HH:mm")
    private LocalTime start;

    /** * Hora de termino del bloque horario.
     * Formato de salida: HH:mm (ej: 11:00)
     * Se renombra la columna a 'end_time' para evitar conflictos SQL.
     */
    @Column(name = "end_time")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime end;

    /** Capacidad maxima total de la ventana */
    private Integer capacityTotal;

    /** * Mapa que contiene la capacidad disponible desglosada por zona.
     */
    @ElementCollection
    @CollectionTable(name = "window_capacities", joinColumns = @JoinColumn(name = "window_id"))
    @MapKeyColumn(name = "zone_name")
    @Column(name = "capacity")
    private Map<String, Integer> capacityByZone;
}