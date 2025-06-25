package com.base.tesis_backend.Dtos.AdminDTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

//Esto es un DTO para enviarle al frontend una lista resumida de todos los usuario
//que hay en la aplicacion
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSummaryDTO {
    private Long id;
    private String username;
    private String email;
    private LocalDateTime creationDate;
}
