package com.base.tesis_backend.Dtos.SearchDTOs;

import lombok.Data;

import java.util.List;

//DTO para el objeto de respuesta de TMDB cuando se busca una pelicula o serie por nombre
@Data
public class TmdbSearchResponseDTO {
    private List<TmdbSearchResultDTO> results;
}
