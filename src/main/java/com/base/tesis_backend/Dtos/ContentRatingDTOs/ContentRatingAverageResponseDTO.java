package com.base.tesis_backend.Dtos.ContentRatingDTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

//DTO para enviar el promedio de rating de un contenido audiovisual y el total
//de votos que tiene un contenido
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContentRatingAverageResponseDTO {

    private Long contentId;
    private BigDecimal averageRating;
    private Long totalRatings;
}
