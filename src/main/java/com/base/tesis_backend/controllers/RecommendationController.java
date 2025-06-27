package com.base.tesis_backend.controllers;

import com.base.tesis_backend.Dtos.RecommendationDTO;
import com.base.tesis_backend.config.JwtUtil;
import com.base.tesis_backend.services.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequestMapping("/recommendations")
@RequiredArgsConstructor
public class RecommendationController {
    //injects
    @Autowired
    private final RecommendationService recommendationService;
    @Autowired
    private final JwtUtil jwtUtil;

    //devolvemos las recomendaciones de un usuario en especifico
    @GetMapping("/getPage")
    public ResponseEntity<List<RecommendationDTO>> getRecommendations(
            @RequestParam(name = "filterByPlatforms", defaultValue = "false") boolean filterByPlataforms,
            @RequestParam(name = "page", defaultValue = "1") int page,
            Authentication authentication) {

        //Extraemos el email del token
        String email = authentication.getName();

        //LLamamos al servicio y devolvemos las recomendaciones
        List<RecommendationDTO> recommendations = recommendationService.getRecommendationsForUser(email, filterByPlataforms, page);
        return ResponseEntity.ok(recommendations);
    }

    //endpoint para buscar las Peliculas en Tendencia
    @GetMapping("/trending/movies")
    public ResponseEntity<List<RecommendationDTO>> getTrendingMovies(){
        List<RecommendationDTO> trendingMovies = recommendationService.getTrendingContent("movie");
        return ResponseEntity.ok(trendingMovies);
    }

    //endpoint para buscar los TV Shows en Tendencia
    @GetMapping("/trending/tv")
    public ResponseEntity<List<RecommendationDTO>> getTrendingTVShows(){
        List<RecommendationDTO> trendingTVShows = recommendationService.getTrendingContent("tv");
        return ResponseEntity.ok(trendingTVShows);
    }
}
