package com.base.tesis_backend.Dtos.CommentDTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

//DTO para recibir los valores del frontend y crear un comentario
@Data
public class CreateCommentDTO {
    @NotNull
    private Long contentId;

    @NotBlank
    @Size(min = 1, max = 1000)
    private String text;

    //Informacion del contenido audiovisual, para crearlo si no existe
    private String title;

    private String posterPath;

    private Float rating;

    private String type;
}
