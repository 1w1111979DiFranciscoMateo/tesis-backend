package com.base.tesis_backend.Dtos;

import lombok.Data;

//en mi api le llamo categorias pero en la api de TMDB le llaman Genres
@Data
public class TmdbGenreDTO {
    private int id;
    private String name;
}
