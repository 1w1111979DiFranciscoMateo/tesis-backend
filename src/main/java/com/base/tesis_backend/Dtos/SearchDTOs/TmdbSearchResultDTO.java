package com.base.tesis_backend.Dtos.SearchDTOs;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

import java.util.List;

//DTO del objeto que devuelve la busqueda de peliculas o series por nombre
@Data
public class TmdbSearchResultDTO {
    private Long id;
    @JsonAlias({"title", "name"}) //peliculas tienen title, series tienen name
    private String title;
    @JsonAlias("poster_path")
    private String posterPath;
    @JsonAlias("genre_ids")
    private List<Integer> genreIds;
    @JsonAlias("vote_average")
    private Double voteAverage;
    @JsonAlias("vote_count")
    private Integer voteCount;
    private String overview;
}
