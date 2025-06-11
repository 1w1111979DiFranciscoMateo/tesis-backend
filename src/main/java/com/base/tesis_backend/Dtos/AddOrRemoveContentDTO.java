package com.base.tesis_backend.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//DTO para recibir la solicitud de agregar/quitar un contenido de una lista
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddOrRemoveContentDTO {
    private Long listId;
    private Long contentId;
    private String title;
    private String posterPath;
    private Float rating;
    private String type;
}
