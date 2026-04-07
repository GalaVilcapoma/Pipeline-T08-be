package pe.edu.vallegrande.mybackend.model;

import lombok.Data;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Table;

@Entity
@Data                      
@Table(name = "customer")  
public class Customer {

    @Id
    @Column(name = "id")          
    @GeneratedValue(strategy = GenerationType.IDENTITY)  
    private Long id;              

    @Column(name = "dni")         
    private String dni;           

    @Column(name = "cellphone")   
    private String cellPhone;     

    @Column(name = "first_name")  
    private String firstName;     

    @Column(name = "last_name")   
    private String lastName;      

    @Column(name = "active")
    private Boolean active;       

    //CAMPOS DE AUDITORIA

    @Column(name = "created_at")       
    private LocalDateTime createdAt;  

    @Column(name = "updated_at")      
    private LocalDateTime updatedAt;   

    @Column(name = "deleted_at")       
    private LocalDateTime deletedAt;   

    @Column(name = "restored_at")      
    private LocalDateTime restoredAt;  

}