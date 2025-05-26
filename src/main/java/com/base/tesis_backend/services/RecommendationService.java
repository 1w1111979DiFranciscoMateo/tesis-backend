package com.base.tesis_backend.services;

import com.base.tesis_backend.Dtos.RecommendationDTO;
import com.base.tesis_backend.entities.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    //Injectamos otros Services y la RestTemplate para llamar a una api externa
    @Autowired
    private final UserService userService;
    @Autowired
    private final UserCategoryService userCategoryService;
    @Autowired
    private final UserPlatformService userPlatformService;
    @Autowired
    private final RestTemplate restTemplate;

    //llamamos al valor que tenemos guardado en application.properties
    @Value("${tmdb.api.token}")
    private String tmdbReadAccesToken;

    public List<RecommendationDTO> getRecommendationsForUser(String email, boolean filterByPlatforms, int page){
        //Buscamos el usuario por email
        User user = userService.findByEmail(email).orElseThrow(() -> new RuntimeException("User not Found"));

        //obtenemos las categorias favoritas del usuario
        List<Long> categoryIds = userCategoryService.findCategoryIdsByUserIds(user.getId());
        //obtenemos las plataformas disponibles del usuario si corresponde
        //si el filterByPlatforms es true llenamos el List con las plataformas disponibles del usuario
        //si el filterByPlatforms es false devolvemos la List vacia.
        List<Long> platformsIds = filterByPlatforms ? userPlatformService.findPlatformIdsByUserIds(user.getId()) : Collections.emptyList();

        //armamos las URL de las requests a TMDB
        String movieUrl = buildTmdbUrl("movie", categoryIds, platformsIds, page);
        String tvUrl = buildTmdbUrl("tv", categoryIds, platformsIds, page);

        //temporal
        System.out.println("URL Peliculas: " + movieUrl);
        System.out.println("URL Series: " + tvUrl);

        //llamamos a TMDB para Peliculas
        ResponseEntity<String> movieResponse = restTemplate.exchange(
                movieUrl,
                HttpMethod.GET,
                getEntityWithHeaders(),
                String.class
        );

        //Llamamos a TMDB para Series
        ResponseEntity<String> tvResponse = restTemplate.exchange(
                tvUrl,
                HttpMethod.GET,
                getEntityWithHeaders(),
                String.class
        );

        //Parseamos la respuesta de Peliculas a DTOs
        List<RecommendationDTO> movies = parseRecommendationsFromJson(movieResponse.getBody(), "movie");

        //Parseamos la respuesta de Series a DTOs
        List<RecommendationDTO> tvShows = parseRecommendationsFromJson(tvResponse.getBody(), "tv");

        //Combinamos ambos resultados
        List<RecommendationDTO> allRecommendations = new ArrayList<>();
        allRecommendations.addAll(movies);
        allRecommendations.addAll(tvShows);

        //ordenamos la lista de allRecommendations por rating
        allRecommendations.sort(Comparator.comparing(RecommendationDTO::getRating, Comparator.nullsLast(Comparator.reverseOrder())));

        //Devulvemos un array de RecommendationDTO con todas las recomendaciones
        //tanto de peliculas como de tvShows
        return allRecommendations;
    }

    //este metodo es para armar el Header de las request a TMDB
    private HttpEntity<Void> getEntityWithHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + tmdbReadAccesToken);
        headers.add("Accept", "application/json");
        return new HttpEntity<>(headers);
    }

    //metodo para armar el url para la consulta a TMDB
    private String buildTmdbUrl(String type, List<Long> categoryIds, List<Long> platformIds, int page) {
        StringBuilder url = new StringBuilder("https://api.themoviedb.org/3/discover/" + type + "?");

        url.append("language=es-AR"); //lenguaje español argentina
        url.append("&region=AR"); //region argentina
        url.append("&include_adult=false"); //no contenido +18
        url.append("&include_video=false"); //no incuya video
        url.append("&sort_by=vote_average.desc"); //ordenado por rating descendiente
        url.append("&vote_count.gte=1000"); //que el contenido tenga mas de 1000 votos
        url.append("&page=" + page); //pagina
        url.append("&watch_region=AR");//region argentina

        //agregar las categorias favoritas del usuario al url
        if(!categoryIds.isEmpty()){
            String genres = categoryIds.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining("|")); //OR
            url.append("&with_genres=").append(genres);
        }

        //agregar las plataformas disponibles del usuario al url
        //si es que se presiono el boton de "Busqueda inteligente"
        if(!platformIds.isEmpty()){
            String providers = platformIds.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining("|")); //OR
            url.append("&with_watch_providers=").append(providers);
        }

        //devuelvo el url
        return url.toString();
    }

    //esto le agrego al posterPath para que sea el url completo al poster
    private static final String TMDB_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";

    //este metodo es para recibir los datos de la api de TMDB en formato JSON
    //y los voy pasando a un objecto tipo RecommendationDTO a cada
    //contenido audiovisual para luego devolver una List de RecommendationDTO
    private List<RecommendationDTO> parseRecommendationsFromJson(String json, String type){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(json);
            JsonNode results = root.get("results");

            //Si el resultado esta vacio o no es un array devuelvo un emptyList
            if(results == null || !results.isArray()){
                return Collections.emptyList();
            }

            List<JsonNode> nodes = objectMapper.convertValue(
                    results,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, JsonNode.class)
            );
            //voy pasando lo que recibo en el result a el objecto RecommendationDTO
            //y luego retorno esta List de recommendationDTOs
            return nodes.stream()
                    .map(result -> {
                        Long id = result.has("id") ? result.get("id").asLong() : null;
                        String title = result.has("title") ? result.get("title").asText()
                                        : result.has("name") ? result.get("name").asText()
                                        : "Sin titulo";
                        String posterPath = result.has("poster_path") && !result.get("poster_path").isNull()
                                ? TMDB_IMAGE_BASE_URL + result.get("poster_path").asText()
                                : null;
                        List<Integer> genreIds = result.has("genre_ids") && result.get("genre_ids").isArray()
                                ? objectMapper.convertValue(result.get("genre_ids"),
                                  objectMapper.getTypeFactory().constructCollectionType(List.class, Integer.class))
                                : Collections.emptyList();
                        Double rating = result.has("vote_average") ? result.get("vote_average").asDouble() : null;

                        return new RecommendationDTO(id, title, posterPath, genreIds, rating, type);
                    })
                    .collect(Collectors.toList());

        } catch (Exception e){
            throw new RuntimeException("Error parsin TMDB response",e);
        }
    }
}
