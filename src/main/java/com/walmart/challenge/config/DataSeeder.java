package com.walmart.challenge.config;

import com.walmart.challenge.model.DispatchWindow;
import com.walmart.challenge.repository.DispatchWindowRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuración para la carga inicial automatizada de datos de prueba.
 * Versión optimizada para pruebas de capacidad y costos por bloque.
 * * @author Mauricio Gomez
 * @version 1.2
 */
@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner initDatabase(DispatchWindowRepository windowRepo) {
        return args -> {
            windowRepo.deleteAll(); // Limpiamos para evitar duplicados al reiniciar
            LocalDate today = LocalDate.now();

            // Generamos 3 franjas horarias por día para los próximos 7 días
            for (int i = 0; i < 7; i++) {
                LocalDate date = today.plusDays(i);

                // Bloque Mañana - Costo menor
                createWindow(windowRepo, date, "1", LocalTime.of(9, 0), LocalTime.of(11, 0), 2500.0);

                // Bloque Mediodía - Costo estándar
                createWindow(windowRepo, date, "2", LocalTime.of(12, 0), LocalTime.of(14, 0), 3500.0);

                // Bloque Tarde - Costo prime
                createWindow(windowRepo, date, "3", LocalTime.of(16, 0), LocalTime.of(18, 0), 4500.0);
            }
        };
    }

    /**
     * Método auxiliar para crear y guardar una ventana con capacidad limitada y costo.
     */
    private void createWindow(DispatchWindowRepository repo, LocalDate date, String blockId,
                              LocalTime start, LocalTime end, double cost) {

        String dateStr = date.toString().replace("-", "");
        DispatchWindow window = new DispatchWindow();

        window.setId("w-" + dateStr + "-" + blockId);
        window.setDate(date);
        window.setStart(start);
        window.setEnd(end);
        window.setCost(cost); // Atributo solicitado en el requerimiento

        // CAPACIDAD: Solo 3 cupos por zona para facilitar la prueba de "Agotado"
        Map<String, Integer> capacities = new HashMap<>();
        capacities.put("zone-1", 3);
        capacities.put("zone-2", 3);
        capacities.put("zone-3", 1); // Una zona casi agotada por defecto

        window.setCapacityByZone(capacities);
        window.setCapacityTotal(7); // Suma de las capacidades por zona

        repo.save(window);
    }
}