package com.base.tesis_backend.Dtos.AdminDTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//DTO para crear las llamadas query en los repositories para la logica de estadisitcas
//de contenidos audiovisuales
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContentAvgRatingDTO {
    private String title;
    private Double averageRating;
}
