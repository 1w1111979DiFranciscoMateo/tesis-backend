package com.base.tesis_backend.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Esto es un DTO para crear un array del contenido audiovisual que pertenece a una Lista
//de un usuario
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AudioVisualContentDTO {
    private Long id;
    private String title;
    private String posterPath;
    private Float rating;
    private String type;
}
