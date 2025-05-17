package com.base.tesis_backend.Dtos;

import lombok.Data;

import java.util.List;

@Data
public class TmdbPlatformResponseDTO {
    private List<TmdbPlatformDTO> results;
}

// IMPORTANTE:
// El nombre del atributo tiene que coincidir con la clave del JSON.
// En el caso de TMDB para /watch/providers, normalmente la clave es "results"
