package com.walmart.challenge.config;

import com.walmart.challenge.model.DispatchWindow;
import com.walmart.challenge.repository.DispatchWindowRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

/**
 * Configuracion para la carga inicial automatizada de datos de prueba.
 * Genera un set de datos completo para multiples dias facilitando la visualizacion
 * en el frontend y la realizacion de pruebas de capacidad.
 * * @author Mauricio Gomez
 * @version 1.1
 */
@Configuration
public class DataSeeder {

    /**
     * Metodo que inicializa la base de datos con un ciclo de ventanas para 7 dias.
     * Crea bloques horarios de mañana y tarde para cada fecha.
     * * @param windowRepo Repositorio de ventanas de despacho para persistencia
     * @return CommandLineRunner ejecutado al inicio de la aplicacion
     */
    @Bean
    CommandLineRunner initDatabase(DispatchWindowRepository windowRepo) {
        return args -> {
            LocalDate today = LocalDate.now();

            // Generamos ventanas para los proximos 7 dias
            for (int i = 0; i < 7; i++) {
                LocalDate date = today.plusDays(i);
                String dateStr = date.toString().replace("-", "");

                // Ventana Bloque Mañana (09:00 - 11:00)
                DispatchWindow morning = new DispatchWindow();
                morning.setId("w-" + dateStr + "-1");
                morning.setDate(date);
                morning.setStart(LocalTime.of(9, 0));
                morning.setEnd(LocalTime.of(11, 0));
                morning.setCapacityTotal(10);
                morning.setCapacityByZone(Map.of(
                        "zone-1", 5,
                        "zone-2", 3,
                        "zone-3", 2
                ));
                windowRepo.save(morning);

                // Ventana Bloque Tarde (14:00 - 16:00)
                DispatchWindow afternoon = new DispatchWindow();
                afternoon.setId("w-" + dateStr + "-2");
                afternoon.setDate(date);
                afternoon.setStart(LocalTime.of(14, 0));
                afternoon.setEnd(LocalTime.of(16, 0));
                afternoon.setCapacityTotal(8);
                afternoon.setCapacityByZone(Map.of(
                        "zone-1", 4,
                        "zone-2", 2,
                        "zone-3", 2
                ));
                windowRepo.save(afternoon);
            }

            // Ejemplo de una ventana agotada para pruebas de validacion (para el dia 7)
            LocalDate lastDay = today.plusDays(7);
            DispatchWindow soldOut = new DispatchWindow();
            soldOut.setId("w-" + lastDay.toString().replace("-", "") + "-1");
            soldOut.setDate(lastDay);
            soldOut.setStart(LocalTime.of(11, 0));
            soldOut.setEnd(LocalTime.of(13, 0));
            soldOut.setCapacityTotal(0);
            soldOut.setCapacityByZone(Map.of("zone-1", 0, "zone-2", 0));
            windowRepo.save(soldOut);
        };
    }
}