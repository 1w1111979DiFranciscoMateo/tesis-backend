package com.base.tesis_backend.services;

import com.base.tesis_backend.Dtos.PlatformDTOs.TmdbProviderResponseDTO;
import com.base.tesis_backend.Dtos.SearchDTOs.SearchContentDTO;
import com.base.tesis_backend.Dtos.SearchDTOs.TmdbSearchResponseDTO;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    //Inyects
    @Autowired
    private RestTemplate restTemplate;

    //llamamos al valor que tenemos guardado en application.properties
    @Value("${tmdb.api.token}")
    private String tmdbReadAccesToken;

    // Constante para el umbral mínimo de votos
    private static final int MIN_VOTE_COUNT = 100;

    //metodo para buscar en TMDB una serie o movie por titulo
    public List<SearchContentDTO> getSearchResults(String query){
        //armamos los urls
        String MovieUrl = UriComponentsBuilder.fromHttpUrl("https://api.themoviedb.org/3/search/movie")
                .queryParam("query", query)
                .queryParam("include_adult", false)
                .queryParam("language", "es-AR")
                .queryParam("page", 1)
                .queryParam("region", "AR")
                .build()
                .toUriString();

        String TvUrl = UriComponentsBuilder.fromHttpUrl("https://api.themoviedb.org/3/search/tv")
                .queryParam("query", query)
                .queryParam("include_adult", false)
                .queryParam("language", "es-AR")
                .queryParam("page", 1)
                .queryParam("region", "AR")
                .build()
                .toUriString();

        //LLamamos a TMDB para movies y recibimos la respuesta
        ResponseEntity<TmdbSearchResponseDTO> movieResponse = restTemplate.exchange(
                MovieUrl,
                HttpMethod.GET,
                getEntityWithHeaders(),
                TmdbSearchResponseDTO.class
        );

        //LLamamos a TMDB para movies y recibimos la respuesta
        ResponseEntity<TmdbSearchResponseDTO> tvResponse = restTemplate.exchange(
                TvUrl,
                HttpMethod.GET,
                getEntityWithHeaders(),
                TmdbSearchResponseDTO.class
        );

        //la reponse de Movie si o si tiene que tener algo
        TmdbSearchResponseDTO movieBody = movieResponse.getBody();
        if(movieBody == null || movieBody.getResults() == null){
            return Collections.emptyList();
        }

        //la reponse de Tv si o si tiene que tener algo
        TmdbSearchResponseDTO tvBody = tvResponse.getBody();
        if(tvBody == null || tvBody.getResults() == null){
            return Collections.emptyList();
        }

        //Ahora mapeamos los resultados del MovieBody a una lista
        List<SearchContentDTO> movieResults = movieBody.getResults().stream().map(result ->
                new SearchContentDTO(
                        result.getId(),
                        result.getTitle(),
                        result.getPosterPath(),
                        result.getGenreIds(),
                        result.getVoteAverage(),
                        result.getVoteCount(),
                        result.getOverview(),
                        "movie"
                )).toList();

        //Ahora mapeamos los resultados del TvBody a una lista
        List<SearchContentDTO> tvResults = tvBody.getResults().stream().map((result ->
                new SearchContentDTO(
                        result.getId(),
                        result.getTitle(),
                        result.getPosterPath(),
                        result.getGenreIds(),
                        result.getVoteAverage(),
                        result.getVoteCount(),
                        result.getOverview(),
                        "tv"
                ))).toList();

        //combinamos los arrays en uno
        List<SearchContentDTO> allResults = new ArrayList<>();
        allResults.addAll(movieResults);
        allResults.addAll(tvResults);

        //devolvemos el array aplicando el filtro y ordenamiento mejorado
        return filterAndSortResults(allResults);
    }

    //este metodo es para armar el Header de las request a TMDB
    private HttpEntity<Void> getEntityWithHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + tmdbReadAccesToken);
        headers.add("Accept", "application/json");
        return new HttpEntity<>(headers);
    }

    //metodo para aplicar un filtro de vote_count y ordenamiento al array de resultados
    /**
     * Filtra y ordena los resultados de búsqueda:
     * 1. Separa contenido con suficientes votos (>=50) del que tiene pocos votos (<50)
     * 2. Ordena por vote_average dentro de cada grupo
     * 3. Coloca primero el contenido con suficientes votos, luego el de pocos votos
     */
    private List<SearchContentDTO> filterAndSortResults(List<SearchContentDTO> allResults){
        //separamos los contenidos por cantidad de votos
        List<SearchContentDTO> highVoteCount = new ArrayList<>();
        List<SearchContentDTO> lowVoteCount = new ArrayList<>();

        //agregamos cada contenido a su lista correspondiente
        for(SearchContentDTO content : allResults){
            if(content.getVoteCount() != null && content.getVoteCount() >= MIN_VOTE_COUNT){
                highVoteCount.add(content);
            } else {
                lowVoteCount.add(content);
            }
        }

        //Ordenamos cada grupo por vote_average
        Comparator<SearchContentDTO> voteAverageComparator =
                Comparator.comparing(SearchContentDTO::getVoteAverage,
                        Comparator.nullsLast(Comparator.reverseOrder()));

        highVoteCount.sort(voteAverageComparator);
        lowVoteCount.sort(voteAverageComparator);

        //Combinamos los resutlados, primeros los de alto vote_count y despues el resto
        List<SearchContentDTO> finalResults = new ArrayList<>();
        finalResults.addAll(highVoteCount);
        finalResults.addAll(lowVoteCount);

        return finalResults;
    }
}
