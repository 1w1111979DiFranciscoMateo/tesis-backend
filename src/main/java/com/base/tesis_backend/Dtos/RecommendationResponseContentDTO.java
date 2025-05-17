package com.base.tesis_backend.Dtos;

import lombok.Data;

import java.util.List;

@Data
public class RecommendationResponseContentDTO {
    private Long id;
    private String title;
    private String poster_path;
    private List<Integer> genre_ids;
    private Double vote_average;
}
