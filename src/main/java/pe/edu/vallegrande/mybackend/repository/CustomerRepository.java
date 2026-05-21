package pe.edu.vallegrande.mybackend.repository;

import pe.edu.vallegrande.mybackend.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("SELECT c FROM Customer c WHERE " +
           "(:razonSocial IS NULL OR LOWER(c.razonSocial) LIKE LOWER(CONCAT('%', :razonSocial, '%'))) AND " +
           "(:pais IS NULL OR LOWER(c.pais) LIKE LOWER(CONCAT('%', :pais, '%'))) AND " +
           "(:tipoCliente IS NULL OR LOWER(c.tipoCliente) = LOWER(:tipoCliente)) AND " +
           "(:active IS NULL OR c.active = :active)")
    Page<Customer> buscarConFiltros(
        @Param("razonSocial") String razonSocial,
        @Param("pais") String pais,
        @Param("tipoCliente") String tipoCliente,
        @Param("active") Boolean active,
        Pageable pageable
    );

    List<Customer> findByActive(Boolean active);
}
