package pe.edu.vallegrande.mybackend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private T data;
    private String message;
    private boolean success;
    private LocalDateTime timestamp = LocalDateTime.now();

    public ApiResponse(T data, String message, boolean success) {
        this.data = data;
        this.message = message;
        this.success = success;
    }
}
