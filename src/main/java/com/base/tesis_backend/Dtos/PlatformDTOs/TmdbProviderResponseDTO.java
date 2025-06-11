package com.base.tesis_backend.Dtos.PlatformDTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

//Esto es un DTO para recibir la respuesda de TMDB cuando le preguntamos
//en que plataformas se encuentra un contenido audiovisual
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TmdbProviderResponseDTO {
    private Long id;
    private Map<String, TmdbCountryProviderDTO> results;
}
