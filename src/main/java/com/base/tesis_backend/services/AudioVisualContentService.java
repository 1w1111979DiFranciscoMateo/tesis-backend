package com.base.tesis_backend.services;

import com.base.tesis_backend.Dtos.MovieDTO;
import com.base.tesis_backend.Dtos.TmdbGenreDTO;
import com.base.tesis_backend.Dtos.TvShowDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

//Service que se encarga de la conexion con TMDB para recibir toda la informacion
//de una movie o tvShow solicitado
@Service
public class AudioVisualContentService {

    //llamamos al valor que tenemos guardado en application.properties
    @Value("${tmdb.api.token}")
    private String tmdbReadAccesToken;

    private final RestTemplate restTemplate = new RestTemplate();

    //metodo para arman los headers con Authorization para la llamada a TMDB
    private HttpEntity<Void> getEntityWithHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + tmdbReadAccesToken);
        headers.add("Accept", "application/json");
        return new HttpEntity<>(headers);
    }

    //metodo para hacer la llamada a TMDB y recibir toda la informacion de un
    //audioVisualContent solicitado
    public Object getAudioVisualContentDetails(String type, Long id){
        String url;

        //si el type es movie voy a hacer una llamada al url de movie, Si el
        //type es tv voy a hacer una llamada a la url de tv
        if("movie".equalsIgnoreCase(type)){
            //url de TMDB a donde voy a llamar
            url = String.format("https://api.themoviedb.org/3/movie/%d?language=es-AR", id);
            //respuesta de TMDB
            ResponseEntity<String> movieResponse = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    getEntityWithHeaders(),
                    String.class
            );

            //le paso lo que recibo de la llamada al metodo parse de movie
            //para que arme el dto
            MovieDTO movie = parseMovieFromJson(movieResponse.getBody(), type);
            //devuelvo la pelicula ya armada en forma de MovieDTO
            return movie;

        } else if("tv".equalsIgnoreCase(type)){
            //url de TMDB a donde voy a llamar
            url = String.format("https://api.themoviedb.org/3/tv/%d?language=es-AR", id);
            //respuesta de TMDB
            ResponseEntity<String> tvResponse = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    getEntityWithHeaders(),
                    String.class
            );

            //le paso lo que recibo de la llamada al metodo parse de tvShow
            //para que arme el dto
            TvShowDTO tv = parseTvShowFromJson(tvResponse.getBody(), type);
            //devuelvo el tvShow ya armado en forma de TvShowDTO
            return tv;
        } else {
            throw new IllegalArgumentException("Tipo de contenido no encontrado. Usa 'movie' o 'tv'");
        }
    }

    //esto le agrego al posterPath o backDropPath para que sea el url completo al poster
    private static final String TMDB_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";

    //metodo para recibir la respuesta de TMDB del url de movies y mapearla a un MovieDTO
    private MovieDTO parseMovieFromJson(String json, String type){
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(json);

            MovieDTO movie = new MovieDTO();

            //mapeo los campos basicos
            movie.setId(rootNode.get("id").asLong());
            movie.setTitle(rootNode.get("title").asText());
            movie.setOverview(rootNode.get("overview").asText());
            movie.setType(type);

            //Mapeo poster_path y backdrop_path agregando la URL base
            String posterPath = rootNode.get("poster_path").asText();
            if (posterPath != null && !posterPath.equals("null") && !posterPath.isEmpty()){
                movie.setPoster_path(TMDB_IMAGE_BASE_URL + posterPath);
            }

            String backdropPath = rootNode.get("backdrop_path").asText();
            if (backdropPath != null && !backdropPath.equals("null") && !backdropPath.isEmpty()){
                movie.setBackdrop_path(TMDB_IMAGE_BASE_URL + backdropPath);
            }

            //Mapeo Generos (categorias)
            List<TmdbGenreDTO> genres = new ArrayList<>();
            JsonNode genresNode = rootNode.get("genres");
            if (genresNode != null && genresNode.isArray()){
                for (JsonNode genreNode : genresNode){
                    TmdbGenreDTO genre = new TmdbGenreDTO();
                    genre.setId(genreNode.get("id").asInt());
                    genre.setName(genreNode.get("name").asText());
                    genres.add(genre);
                }
            }
            movie.setGenres(genres);

            //mapeo campos especificos de movie con validacion si vienen vacios
            movie.setBudget(rootNode.get("budget").asLong());
            movie.setRevenue(rootNode.get("revenue").asLong());
            movie.setRelease_date(rootNode.get("release_date").asText());
            movie.setRuntime(rootNode.get("runtime").asInt());
            movie.setVote_average(rootNode.get("vote_average").asDouble());

            return movie;

        } catch (Exception e){
            throw new RuntimeException("Error parseando respuesta de Movie desde TMDB: " + e.getMessage(), e);
        }
    }

    //metodo que parsea la respuesta de la llamada a TMDB de tv a un TvShowDTO
    private TvShowDTO parseTvShowFromJson(String json, String type){
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(json);

            TvShowDTO tvShow = new TvShowDTO();

            //Mapeo campos basicos
            tvShow.setId(rootNode.get("id").asLong());
            tvShow.setName(rootNode.get("name").asText());
            tvShow.setOverview(rootNode.get("overview").asText());
            tvShow.setType(type);

            //Mapeo poster_path y backdrop_path agregando la URL base
            String posterPath = rootNode.get("poster_path").asText();
            if (posterPath != null && !posterPath.equals("null") && !posterPath.isEmpty()){
                tvShow.setPoster_path(TMDB_IMAGE_BASE_URL + posterPath);
            }

            String backdropPath = rootNode.get("backdrop_path").asText();
            if (backdropPath != null && !backdropPath.equals("null") && !backdropPath.isEmpty()){
                tvShow.setBackdrop_path(TMDB_IMAGE_BASE_URL + backdropPath);
            }

            //Mapeo generos (categorias)
            List<TmdbGenreDTO> genres = new ArrayList<>();
            JsonNode genresNode = rootNode.get("genres");
            if (genresNode != null && genresNode.isArray()){
                for (JsonNode genreNode : genresNode){
                    TmdbGenreDTO genre = new TmdbGenreDTO();
                    genre.setId(genreNode.get("id").asInt());
                    genre.setName(genreNode.get("name").asText());
                    genres.add(genre);
                }
            }
            tvShow.setGenres(genres);

            //Mapeo campos especificos de TvShow
            tvShow.setFirst_air_date(rootNode.get("first_air_date").asText());
            tvShow.setNumber_of_seasons(rootNode.get("number_of_seasons").asInt());
            tvShow.setNumber_of_episodes(rootNode.get("number_of_episodes").asInt());
            tvShow.setIn_production(rootNode.get("in_production").asBoolean());
            tvShow.setVote_average(rootNode.get("vote_average").asDouble());

            return tvShow;

        } catch (Exception e){
            throw new RuntimeException("Error parseando respuesta de TvShow desde TMDB: " + e.getMessage(), e);
        }
    }
}
