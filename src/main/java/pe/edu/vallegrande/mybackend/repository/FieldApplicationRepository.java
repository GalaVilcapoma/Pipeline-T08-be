package pe.edu.vallegrande.mybackend.repository;

import pe.edu.vallegrande.mybackend.model.FieldApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface FieldApplicationRepository extends JpaRepository<FieldApplication, Long> {

    List<FieldApplication> findByProducerId(Long producerId);

    List<FieldApplication> findByFieldId(Long fieldId);

    List<FieldApplication> findByLotId(Long lotId);

    List<FieldApplication> findByApplicationDateBetween(LocalDate from, LocalDate to);

    // US-05: Traceability by lot
    @Query("SELECT fa FROM FieldApplication fa WHERE fa.lot.id = :lotId ORDER BY fa.applicationDate")
    List<FieldApplication> findByLotForTraceability(@Param("lotId") Long lotId);

    // US-09: Fields with no applications in last X days
    @Query(value = """
        SELECT DISTINCT f.id FROM fields f
        WHERE f.active = 1
        AND f.id NOT IN (
            SELECT DISTINCT fa.field_id FROM field_applications fa
            WHERE fa.application_date >= :sinceDate
        )
        """, nativeQuery = true)
    List<Long> findFieldIdsWithNoApplicationsSince(@Param("sinceDate") LocalDate sinceDate);

    // US-06: Count applications in last 7 days
    @Query("SELECT COUNT(fa) FROM FieldApplication fa WHERE fa.applicationDate >= :since")
    Long countApplicationsSince(@Param("since") LocalDate since);
}
