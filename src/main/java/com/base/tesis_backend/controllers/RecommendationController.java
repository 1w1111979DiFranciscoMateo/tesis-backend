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
    @GetMapping("/getPage1")
    public ResponseEntity<List<RecommendationDTO>> getRecommendations(
            // @RequestHeader("Authorization") String authHeader,
            @RequestParam(name = "filterByPlatforms", defaultValue = "false") boolean filterByPlataforms,
                Authentication authentication) {

        // extraemos el token del header ("Bearer ...")
        // validamos que el header no este vacio y que tenga el "Bearer "
        // if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        //    return ResponseEntity.status(401).build();
        // }

        // le quitamos el "Bearer " al token
        // String token = authHeader.substring(7);

        //Validamos el token
        // if (!jwtUtil.isTokenValid(token)) {
        //     return ResponseEntity.status(401).build();
        // }

        //Extraemos el email del token
        String email = authentication.getName();

        //LLamamos al servicio y devolvemos las recomendaciones
        List<RecommendationDTO> recommendations = recommendationService.getRecommendationsForUser(email, filterByPlataforms);
        return ResponseEntity.ok(recommendations);
    }
}
