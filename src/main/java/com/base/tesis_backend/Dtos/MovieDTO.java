package com.base.tesis_backend.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

//Esto es un DTO para recibir toda la informacion de una pelicula y pasarsela al front
//asi la muestra en el modal de la pelicula (aca se ve toda la informacion de la
// pelicula seleccionada)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieDTO {
    private Long id;
    private String title;
    private String overview; //descripción
    private String poster_path;
    private String backdrop_path;
    private List<TmdbGenreDTO> genres; //generos = categorias. Es un array de objetos
    private Long budget; //cuanto salio hacerla
    private Long revenue; //cuanto se gano
    private String release_date; //fecha de estreno
    private Integer runtime;
    private Double vote_average;
    private String type; //Esto es para definir si es una movie o tvShow
}
