package com.base.tesis_backend.Dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

//Este es un DTO para devolverle al frontend la lista del usuario logueado
// (asi no le paso el objeto User user)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserListDTO {
    private Long id;
    private String name;
    private String description;
    @JsonProperty("isPublic")
    private boolean isPublic;
    private LocalDateTime creationDate;
}
