package com.base.tesis_backend.services;

import com.base.tesis_backend.Dtos.PlatformDTOs.TmdbCountryProviderDTO;
import com.base.tesis_backend.Dtos.PlatformDTOs.TmdbPlatformDTO;
import com.base.tesis_backend.Dtos.PlatformDTOs.TmdbProviderResponseDTO;
import com.base.tesis_backend.controllers.PlatformController;
import com.base.tesis_backend.entities.Platform;
import com.base.tesis_backend.repositories.PlatformRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.Provider;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlatformService {
    @Autowired
    private final PlatformRepository platformRepository;
    @Autowired
    private RestTemplate restTemplate;

    //llamamos al valor que tenemos guardado en application.properties
    @Value("${tmdb.api.token}")
    private String tmdbReadAccesToken;

    //metodo que obtiene todas las plataformas de la base de datos
    public List<Platform> getAll() {
        return platformRepository.findAll();
    }

    //metodo para buscar todas las plataformas en la que un contenido audiovisual
    //en especifico esta disponible (para la region AR)
    public List<Platform> getAvailablePlatformsForContent(Long contentId, String type){
        //url de TMDB
        String url;
        if("movie".equalsIgnoreCase(type)){
            url = "https://api.themoviedb.org/3/movie/"+ contentId +"/watch/providers";
        } else if("tv".equalsIgnoreCase(type)){
            url = "https://api.themoviedb.org/3/tv/" + contentId +"/watch/providers";
        } else {
            throw new IllegalArgumentException("Tipo Invalido, debe ser movie o tv");
        }

        //LLamamos a TMDB y recibimos la respuesta
        ResponseEntity<TmdbProviderResponseDTO> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                getEntityWithHeaders(),
                TmdbProviderResponseDTO.class
        );

        //la reponse si o si tiene que tener algo
        TmdbProviderResponseDTO body = response.getBody();
        if(body == null || body.getResults() == null){
            return Collections.emptyList();
        }

        //pais a consultar
        String countryCode = "AR";
        TmdbCountryProviderDTO countryData = body.getResults().get(countryCode);

        //si el resultado no tiene una plataforma con un countryCode = AR
        //devuelve una list vacia
        if(countryData == null || countryData.getFlatrate() == null){
            return Collections.emptyList();
        }

        //Extraemos los IDs unicos
        Set<Long> providerIds = countryData.getFlatrate().stream()
                .map(TmdbPlatformDTO::getProviderId)
                .collect(Collectors.toSet());

        //Buscamos en nuestra Base de Datos todas las plataformas que cumplan con los
        //filtros
        List<Platform> platforms = platformRepository.findAllById(providerIds);

        //prefijo base de TMDB para los logos
        String tmdbImagePrefix = "https://image.tmdb.org/t/p/w45";

        //Para cada plataforma le agregamos el prefijo a su logopath
        for(Platform platform : platforms){
            if(platform.getLogoPath() != null && !platform.getLogoPath().startsWith("http")){
                platform.setLogoPath(tmdbImagePrefix + platform.getLogoPath());
                //temporal
                System.out.println("Logo final: " + platform.getLogoPath());
            }
        }

        return platforms;
    }

    //este metodo es para armar el Header de las request a TMDB
    private HttpEntity<Void> getEntityWithHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + tmdbReadAccesToken);
        headers.add("Accept", "application/json");
        return new HttpEntity<>(headers);
    }

}
