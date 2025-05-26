package com.base.tesis_backend.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecommendationDTO {
    private Long id;
    private String title;
    private String posterUrl;
    private List<Integer> categories;
    private Double rating;
    private String type; //Esto es para definir si es una movie o tvShow
}
