package com.base.tesis_backend.Dtos.ContentRatingDTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

//DTO para devolver toda la informacion de un rating de un usuario
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContentRatingResponseDTO {

    private Long id;
    private Long userId;
    private Long contentId;
    private BigDecimal rating;
    private LocalDateTime creationDate;
}
