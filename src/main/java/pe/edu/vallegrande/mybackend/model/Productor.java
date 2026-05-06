package pe.edu.vallegrande.mybackend.model;

import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "productores")
public class Productor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_completo")
    private String nombreCompleto;

    private String dni;
    private String telefono;
    private String correo;
    private String ubicacion;
    private String distrito;

    @Column(name = "estado")
    private Boolean estado = true;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @Column(name = "fecha_eliminacion")
    private LocalDateTime fechaEliminacion;

    @Column(name = "fecha_restauracion")
    private LocalDateTime fechaRestauracion;

    @PrePersist
    protected void onCreate() { this.fechaCreacion = LocalDateTime.now(); }

    @PreUpdate
    protected void onUpdate() { this.fechaActualizacion = LocalDateTime.now(); }
}
