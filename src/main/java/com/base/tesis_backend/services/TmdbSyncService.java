package com.base.tesis_backend.services;

import com.base.tesis_backend.Dtos.TmdbGenreDTO;
import com.base.tesis_backend.Dtos.TmdbGenreResponseDTO;
import com.base.tesis_backend.Dtos.TmdbPlatformDTO;
import com.base.tesis_backend.Dtos.TmdbPlatformResponseDTO;
import com.base.tesis_backend.entities.Category;
import com.base.tesis_backend.entities.Platform;
import com.base.tesis_backend.repositories.CategoryRepository;
import com.base.tesis_backend.repositories.PlatformRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

//Este es un Service para Sincronizar las categorias y plataformas de mi api
//con los generos y plataformas de la api de TMDB (solo lo ejecuto manualmente cuando yo quiero)
@Service
@RequiredArgsConstructor
public class TmdbSyncService {

    //Injectamos otros Services y la RestTemplate para llamar a una api externa
    @Autowired
    private final RestTemplate restTemplate;
    @Autowired
    private final CategoryRepository categoryRepository;
    @Autowired
    private final PlatformRepository platformRepository;

    //llamamos al valor que tenemos guardado en application.properties
    @Value("${tmdb.api.token}")
    private String tmdbReadAccesToken;

    //Metodo para sincronizar mis categorias con las categorias de TMDB
    //PELICULAS
    public void syncCategoriesMovies(){
        //url al endpoint de categorias de peliculas
        String url = "https://api.themoviedb.org/3/genre/movie/list?language=es-AR";

        ResponseEntity<TmdbGenreResponseDTO> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                getEntityWithHeaders(),
                TmdbGenreResponseDTO.class
        );

        if(response.getBody() != null && response.getBody().getGenres() != null){
            for(TmdbGenreDTO genreDTO : response.getBody().getGenres()){
                boolean exist = categoryRepository.existsById((long) genreDTO.getId());
                if(!exist){
                    Category category = new Category();
                    category.setId((long) genreDTO.getId()); //Casteamos int -> long
                    category.setName(genreDTO.getName());
                    //temporal
                    System.out.println("Categoría nueva: " + genreDTO.getName());
                    categoryRepository.save(category);
                }
            }
        }
    }

    //Metodo para sincronizar mis categorias con las categorias de TMDB
    //TV SHOWS
    public void syncCategoriesSeries(){
        String url = "https://api.themoviedb.org/3/genre/tv/list?language=es-AR";

        //aca hago X
        ResponseEntity<TmdbGenreResponseDTO> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                getEntityWithHeaders(),
                TmdbGenreResponseDTO.class
        );

        if (response.getBody() != null && response.getBody().getGenres() != null){
            for (TmdbGenreDTO genreDTO : response.getBody().getGenres()){
                boolean exist = categoryRepository.existsById((long) genreDTO.getId());
                if (!exist){
                    Category category = new Category();
                    category.setId((long) genreDTO.getId());
                    category.setName(genreDTO.getName());
                    //temporal
                    System.out.println("Categoría nueva: " + genreDTO.getName());
                    categoryRepository.save(category);
                }
            }
        }
    }


    //Metodo para sincronizar mis plataformas con las plataformas de TMDB
    //PELICULAS
    public void syncPlatformsMovies(){
        String url = "https://api.themoviedb.org/3/watch/providers/movie?language=es-AR&watch_region=AR";

        ResponseEntity<TmdbPlatformResponseDTO> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                getEntityWithHeaders(),
                TmdbPlatformResponseDTO.class
        );

        if(response.getBody() != null && response.getBody().getResults() != null){
            for (TmdbPlatformDTO platformDTO : response.getBody().getResults()){
                boolean exist = platformRepository.existsById((long) platformDTO.getProviderId());
                if(!exist){
                    Platform platform = new Platform();
                    platform.setId((long) platformDTO.getProviderId());
                    platform.setName(platformDTO.getProviderName());
                    platform.setLogoPath(platformDTO.getLogoPath());
                    platformRepository.save(platform);
                }
            }
        }
    }

    //Metodo para sincronizar mis plataformas con las plataformas de TMDB
    //TV SHOWS
    public void syncPlatformsSeries(){
        String url = "https://api.themoviedb.org/3/watch/providers/tv?language=es-AR&watch_region=AR";

        ResponseEntity<TmdbPlatformResponseDTO> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                getEntityWithHeaders(),
                TmdbPlatformResponseDTO.class
        );

        if(response.getBody() != null && response.getBody().getResults() != null){
            for(TmdbPlatformDTO platformDTO : response.getBody().getResults()){
                boolean exist = platformRepository.existsById((long) platformDTO.getProviderId());
                if(!exist){
                    Platform platform = new Platform();
                    platform.setId((long) platformDTO.getProviderId());
                    platform.setName(platformDTO.getProviderName());
                    platform.setLogoPath(platformDTO.getLogoPath());
                    platformRepository.save(platform);
                }
            }
        }
    }

    //este metodo es para armar el Header de las request a TMDB
    private HttpEntity<Void> getEntityWithHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + tmdbReadAccesToken);
        headers.add("Accept", "application/json");
        return new HttpEntity<>(headers);
    }
}
