package pe.edu.vallegrande.mybackend.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pe.edu.vallegrande.mybackend.model.Agrochemical;
import pe.edu.vallegrande.mybackend.model.Agrochemical.AgrochemicalCategory;
import pe.edu.vallegrande.mybackend.repository.AgrochemicalRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Seed inicial de agroquímicos.
 * Solo inserta datos si la tabla está vacía (idempotente).
 */
@Configuration
public class AgrochemicalDataSeeder {

    @Bean
    CommandLineRunner seedAgrochemicals(AgrochemicalRepository repo) {
        return args -> {
            if (repo.count() > 0) {
                System.out.println("Agrochemicals ya tienen datos — seed omitido.");
                return;
            }

            System.out.println(" Insertando datos de ejemplo para Agroquímicos...");

            List<Agrochemical> seeds = List.of(

                // 1 — Insecticida activo
                build("Confidor 350 SC",
                      "Imidacloprid",
                      AgrochemicalCategory.INSECTICIDE,
                      "SENASA-001-2024",
                      LocalDate.of(2027, 6, 30),
                      new BigDecimal("0.5000"), 7,
                      "Bayer CropScience",
                      true, null),

                // 2 — Fungicida activo
                build("Mancozeb 80 WP",
                      "Mancozeb",
                      AgrochemicalCategory.FUNGICIDE,
                      "SENASA-002-2024",
                      LocalDate.of(2027, 12, 31),
                      new BigDecimal("2.0000"), 14,
                      "Dow AgroSciences",
                      true, null),

                // 3 — Herbicida activo
                build("Roundup 480 SL",
                      "Glifosato",
                      AgrochemicalCategory.HERBICIDE,
                      "SENASA-003-2024",
                      LocalDate.of(2028, 8, 15),
                      new BigDecimal("3.0000"), 0,
                      "Monsanto",
                      true, null),

                // 4 — Insecticida activo
                build("Karate 2.5 WG",
                      "Lambda-cihalotrina",
                      AgrochemicalCategory.INSECTICIDE,
                      "SENASA-004-2024",
                      LocalDate.of(2027, 3, 20),
                      new BigDecimal("0.3000"), 7,
                      "Syngenta",
                      true, null),

                // 5 — Fertilizante activo
                build("Nitrofoska Foliar",
                      "NPK 12-4-6",
                      AgrochemicalCategory.FERTILIZER,
                      "SENASA-005-2024",
                      LocalDate.of(2028, 1, 1),
                      new BigDecimal("5.0000"), 0,
                      "COMPO Expert",
                      true, null),

                // 6 — Fungicida activo (vence pronto)
                build("Score 250 EC",
                      "Difenoconazol",
                      AgrochemicalCategory.FUNGICIDE,
                      "SENASA-006-2024",
                      LocalDate.of(2026, 6, 5),
                      new BigDecimal("0.5000"), 14,
                      "Syngenta",
                      true, null),

                // 7 — Bioestimulante activo
                build("Bioestimulante Stoller",
                      "Aminoácidos + Micronutrientes",
                      AgrochemicalCategory.BIOSTIMULANT,
                      "SENASA-007-2024",
                      LocalDate.of(2028, 6, 30),
                      new BigDecimal("3.0000"), 0,
                      "Stoller",
                      true, null),

                // 8 — Fungicida activo
                build("Dithane M-45",
                      "Mancozeb",
                      AgrochemicalCategory.FUNGICIDE,
                      "SENASA-008-2024",
                      LocalDate.of(2027, 9, 30),
                      new BigDecimal("2.5000"), 14,
                      "Dow AgroSciences",
                      true, null),

                // 9 — Insecticida INACTIVO (eliminado lógicamente)
                build("Actara 25 WG",
                      "Tiametoxam",
                      AgrochemicalCategory.INSECTICIDE,
                      "SENASA-009-2022",
                      LocalDate.of(2025, 12, 31),
                      new BigDecimal("0.2000"), 7,
                      "Syngenta",
                      false, LocalDateTime.now().minusDays(30)),

                // 10 — Insecticida INACTIVO (registro vencido)
                build("Vydate 24 SL",
                      "Oxamil",
                      AgrochemicalCategory.INSECTICIDE,
                      "SENASA-010-2021",
                      LocalDate.of(2024, 6, 30),
                      new BigDecimal("1.0000"), 21,
                      "DuPont",
                      false, LocalDateTime.now().minusDays(60))
            );

            repo.saveAll(seeds);
            System.out.println(" " + seeds.size() + " agroquímicos insertados (8 activos, 2 inactivos).");
        };
    }

    private Agrochemical build(
            String commercialName,
            String activeIngredient,
            AgrochemicalCategory category,
            String senasaNumber,
            LocalDate expiry,
            BigDecimal maxDose,
            int waitingDays,
            String manufacturer,
            boolean active,
            LocalDateTime deletedAt) {

        Agrochemical a = new Agrochemical();
        a.setCommercialName(commercialName);
        a.setActiveIngredient(activeIngredient);
        a.setCategory(category);
        a.setSenasaRegistrationNumber(senasaNumber);
        a.setRegistrationExpiry(expiry);
        a.setMaxDose(maxDose);
        a.setWaitingPeriodDays(waitingDays);
        a.setManufacturer(manufacturer);
        a.setActive(active);
        a.setDeletedAt(deletedAt);
        // createdAt se asigna en @PrePersist
        return a;
    }
}
