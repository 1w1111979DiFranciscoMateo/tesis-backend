package com.base.tesis_backend.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

//Esto es un DTO para recibir toda la informacion de un tvShow y pasarle al front
//asi la muestra en el modal del tvShow (aca se ve toda la informacion del
// tvShow seleccionada)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TvShowDTO {
    private Long id;
    private String name;
    private String overview; //descripción
    private String poster_path;
    private String backdrop_path;
    private List<TmdbGenreDTO> genres; //generos = categorias. Es un array de objetos
    private String first_air_date; //primer episodio salio en
    private Integer number_of_seasons;
    private Integer number_of_episodes;
    private boolean in_production;
    private Double vote_average;
    private String type; //Esto es para definir si es una movie o tvShow
}
