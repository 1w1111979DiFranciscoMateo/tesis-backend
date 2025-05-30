package com.base.tesis_backend.Dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Este es un dto para recibir los valores del frontend de la lista que se esta por crear
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserListResponseDTO {
    private String name;
    private String description;
    @JsonProperty("isPublic")
    private boolean isPublic;
}
