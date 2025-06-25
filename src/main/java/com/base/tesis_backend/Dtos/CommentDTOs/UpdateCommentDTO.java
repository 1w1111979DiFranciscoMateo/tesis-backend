package com.base.tesis_backend.Dtos.CommentDTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

//DTO para actualizar un comentario
@Data
public class UpdateCommentDTO {
    @NotBlank
    @Size(min = 1, max = 1000)
    private String text;
}
