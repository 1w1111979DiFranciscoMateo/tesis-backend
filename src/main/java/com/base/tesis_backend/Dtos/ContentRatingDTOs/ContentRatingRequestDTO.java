package com.base.tesis_backend.Dtos.ContentRatingDTOs;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

//DTO para recibir el rating de un usuario sobre un contenido audiovisual desde el front
@Data
public class ContentRatingRequestDTO {

    @NotNull(message = "Content ID is required")
    private Long contentId;

    @NotNull(message = "Rating is required")
    @DecimalMin(value = "1.0", message = "Rating must be between 1.0 and 10.0")
    @DecimalMax(value = "10.0", message = "Rating must be between 1.0 and 10.0")
    private BigDecimal rating;

    //estos datos son por si el contenido no existe en la base de datos cuando
    //el usuario esta intentando hacer un rating
    private String title;

    private String posterPath;

    private Float ratingTMDB;

    private String type;
}
