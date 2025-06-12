package com.base.tesis_backend.Dtos.SearchDTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchContentDTO {
    private Long id;
    private String title;
    private String posterPath;
    private List<Integer> genreIds;
    private Double voteAverage;
    private Integer voteCount;
    private String overview;
    private String type;
}
