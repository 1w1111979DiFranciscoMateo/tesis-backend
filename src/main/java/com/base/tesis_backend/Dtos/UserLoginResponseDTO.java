package com.base.tesis_backend.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data //Genera los getters y setters
@NoArgsConstructor //genera un constructor sin argumentos
@AllArgsConstructor //genera un constructor con todos los argumentos
public class UserLoginResponseDTO {
    private String message;
    private String token;
}

