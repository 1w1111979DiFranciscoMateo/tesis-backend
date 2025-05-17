package com.base.tesis_backend.Dtos;

import lombok.Data;

import java.util.List;

@Data
public class RecommendationResponseDTO {
    private int page;
    private List<RecommendationResponseContentDTO> results;
}
