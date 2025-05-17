package com.base.tesis_backend.Dtos;

import lombok.Data;

import java.util.List;
//en mi api le llamo categorias pero en la api de TMDB le llaman Genres
@Data
public class TmdbGenreResponseDTO {
    private List<TmdbGenreDTO> genres;
}

// IMPORTANTE:
// El nombre del atributo tiene que coincidir con la clave del JSON.
// En el caso de TMDB para /watch/providers, normalmente la clave es "results"

