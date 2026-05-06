package pe.edu.vallegrande.mybackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardMetrics {
    // Producers
    private long totalActiveProducers;
    private long producersWithPendingRecords;
    // Applications (last 7 days)
    private long applicationsLast7Days;
    private long applicationsToday;
    // Inventory alerts
    private long lowStockAlerts;
    // SENASA alerts
    private long expiringProducts;
    private long expiredProducts;
    // Pending alerts total
    private long totalPendingAlerts;
    // Charts
    private List<ApplicationByDay> applicationsByDay;
    private List<ApplicationByCrop> applicationsByCrop;
    private List<TopAgrochemical> topAgrochemicals;
    private List<AlertByType> alertsByType;

    @Data @NoArgsConstructor @AllArgsConstructor
    public static class ApplicationByDay {
        private String date;
        private int count;
    }

    @Data @NoArgsConstructor @AllArgsConstructor
    public static class ApplicationByCrop {
        private String crop;
        private int count;
        private int percentage;
    }

    @Data @NoArgsConstructor @AllArgsConstructor
    public static class TopAgrochemical {
        private long agrochemicalId;
        private String name;
        private int applications;
        private int percentage;
    }

    @Data @NoArgsConstructor @AllArgsConstructor
    public static class AlertByType {
        private String type;
        private int count;
        private String severity;
    }
}
