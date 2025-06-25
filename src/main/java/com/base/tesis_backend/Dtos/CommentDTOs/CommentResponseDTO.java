package com.base.tesis_backend.Dtos.CommentDTOs;

import com.base.tesis_backend.entities.User;
import lombok.Data;

import java.time.LocalDateTime;

//DTO para ver toda la informacion de un comentario en especifico
@Data
public class CommentResponseDTO {
    private Long id;
    private User user;
    private Long contentId;
    private String text;
    private Integer netReactions;
    private LocalDateTime creationDate;
    private LocalDateTime updatedDate;
    private boolean isEdited;
}
