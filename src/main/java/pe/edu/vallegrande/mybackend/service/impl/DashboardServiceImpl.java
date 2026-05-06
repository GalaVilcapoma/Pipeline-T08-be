package pe.edu.vallegrande.mybackend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.mybackend.model.DashboardMetrics;
import pe.edu.vallegrande.mybackend.model.DashboardMetrics.*;
import pe.edu.vallegrande.mybackend.repository.CustomerRepository;
import pe.edu.vallegrande.mybackend.repository.ProductorRepository;
import pe.edu.vallegrande.mybackend.repository.AgrochemicalRepository;
import pe.edu.vallegrande.mybackend.service.DashboardService;

import java.util.Arrays;
import java.util.List;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ProductorRepository productorRepository;
    @Autowired
    private AgrochemicalRepository agrochemicalRepository;

    @Override
    public DashboardMetrics getMetrics() {
        DashboardMetrics m = new DashboardMetrics();

        // KPI counts — use real DB counts if available, else demo values
        long producers = productorRepository.count();
        m.setTotalActiveProducers(producers > 0 ? producers : 34);
        m.setProducersWithPendingRecords(5);
        m.setApplicationsLast7Days(47);
        m.setApplicationsToday(8);
        m.setLowStockAlerts(3);
        m.setExpiringProducts(2);
        m.setExpiredProducts(1);
        m.setTotalPendingAlerts(6);

        // Charts — demo data
        m.setApplicationsByDay(Arrays.asList(
                new ApplicationByDay("Mon", 5),
                new ApplicationByDay("Tue", 9),
                new ApplicationByDay("Wed", 7),
                new ApplicationByDay("Thu", 11),
                new ApplicationByDay("Fri", 8),
                new ApplicationByDay("Sat", 4),
                new ApplicationByDay("Sun", 3)
        ));

        m.setApplicationsByCrop(Arrays.asList(
                new ApplicationByCrop("Asparagus", 18, 38),
                new ApplicationByCrop("Avocado", 14, 30),
                new ApplicationByCrop("Blueberry", 10, 21),
                new ApplicationByCrop("Grape", 5, 11)
        ));

        m.setTopAgrochemicals(Arrays.asList(
                new TopAgrochemical(1, "Confidor", 12, 26),
                new TopAgrochemical(2, "Mancozeb", 10, 21),
                new TopAgrochemical(3, "Roundup", 8, 17),
                new TopAgrochemical(4, "Karate", 7, 15)
        ));

        m.setAlertsByType(Arrays.asList(
                new AlertByType("No Application", 3, "HIGH"),
                new AlertByType("Dose Exceeded", 1, "HIGH"),
                new AlertByType("Low Stock", 3, "MEDIUM"),
                new AlertByType("SENASA Expiry", 2, "MEDIUM")
        ));

        return m;
    }
}
