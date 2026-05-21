package pe.edu.vallegrande.mybackend.service;

<<<<<<< HEAD
import pe.edu.vallegrande.mybackend.model.DashboardMetrics;

public interface DashboardService {
    DashboardMetrics getMetrics();
=======
import pe.edu.vallegrande.mybackend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * US-06: Panel de control para supervisores (dashboard)
 * Datos en tiempo real sin recargar la página
 */
@Service
public class DashboardService {

    @Autowired private ProducerRepository producerRepo;
    @Autowired private FieldApplicationRepository appRepo;
    @Autowired private AgrochemicalRepository agroRepo;
    @Autowired private AlertRepository alertRepo;

    public Map<String, Object> getMetrics() {
        Map<String, Object> metrics = new HashMap<>();

        // US-06: Active producers
        long totalActive = producerRepo.findByActive(true).size();
        metrics.put("totalActiveProducers", totalActive);

        // US-06: Applications last 7 days
        long last7Days = appRepo.countApplicationsSince(LocalDate.now().minusDays(7));
        long today     = appRepo.countApplicationsSince(LocalDate.now());
        metrics.put("applicationsLast7Days", last7Days);
        metrics.put("applicationsToday", today);

        // US-03: SENASA expiring in 30 days — use registrationExpiry field
        LocalDate cutoff = LocalDate.now().plusDays(30);
        long expiring = agroRepo.findByActiveTrueAndRegistrationExpiryBefore(cutoff).size();
        long expired  = agroRepo.findByActiveTrueAndRegistrationExpiryBefore(LocalDate.now()).size();
        metrics.put("expiringProducts", expiring);
        metrics.put("expiredProducts", expired);

        // US-09: Pending alerts
        long pendingAlerts = alertRepo.countPending();
        metrics.put("totalPendingAlerts", pendingAlerts);
        metrics.put("lowStockAlerts", 0); // Placeholder — implement with inventory thresholds

        // US-06: Producers with pending records (no applications in 7 days)
        long pendingProducers = appRepo.findFieldIdsWithNoApplicationsSince(
                LocalDate.now().minusDays(7)).size();
        metrics.put("producersWithPendingRecords", pendingProducers);

        return metrics;
    }
>>>>>>> c68b25f (feact(backend): general)
}
