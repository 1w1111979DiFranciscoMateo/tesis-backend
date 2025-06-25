package com.base.tesis_backend.controllers;

import com.base.tesis_backend.Dtos.ContentRatingDTOs.ContentRatingAverageResponseDTO;
import com.base.tesis_backend.Dtos.ContentRatingDTOs.ContentRatingRequestDTO;
import com.base.tesis_backend.Dtos.ContentRatingDTOs.ContentRatingResponseDTO;
import com.base.tesis_backend.entities.ContentRating;
import com.base.tesis_backend.services.ContentRatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/rating")
@RequiredArgsConstructor
public class ContentRatingController {

    //Inyects
    private final ContentRatingService contentRatingService;

    //endpoint para crear un rating de un usuario de un contenido o para actualizar
    @PostMapping("/createOrUpdate")
    public ResponseEntity<ContentRatingResponseDTO> createOrUpdateRating(
            @RequestBody ContentRatingRequestDTO request,
            Authentication authentication){

        //Obtenemos el email a traves del JWT
        String email = authentication.getName();

        ContentRatingResponseDTO response = contentRatingService.createOrUpdateRating(email, request);
        return ResponseEntity.ok(response);
    }

    //endpoint para saber el rating que un usuario le dio a un contenido audiovisual
    @GetMapping("/content/{contentId}")
    public ResponseEntity<ContentRatingResponseDTO> getUserRatingForContent(
            @PathVariable Long contentId,
            Authentication authentication){

        //Obtenemos el email a traves del JWT
        String email = authentication.getName();

        ContentRatingResponseDTO response = contentRatingService.getUserRatingForContent(email, contentId);
        return ResponseEntity.ok(response);
    }

    //endpoint para obtener el rating average de un contenido audiovisual y
    //el total de votos
    @GetMapping("/content/{contentId}/averageAndVotes")
    public ResponseEntity<ContentRatingAverageResponseDTO> getContentRatingAverageAndVotes(
            @PathVariable Long contentId){

        ContentRatingAverageResponseDTO response = contentRatingService.getContentAverageRating(contentId);
        return ResponseEntity.ok(response);
    }

    //endpoint para obtener el rating average de un contenido audiovisual
    @GetMapping("/content/{contentId}/average")
    public ResponseEntity<BigDecimal> getContentRatingAverage(
            @PathVariable Long contentId){

        BigDecimal response = contentRatingService.getContentAverageRatingValue(contentId);
        return ResponseEntity.ok(response);
    }

    //endpoint para saber si el usuario logueado le puso un voto a un contenido
    @GetMapping("/content/{contentId}/exists")
    public ResponseEntity<Boolean> hasUserRatedContent(
            @PathVariable Long contentId,
            Authentication authentication){

        //Obtenemos el email por el JWT
        String email = authentication.getName();

        boolean response = contentRatingService.hasUserRatedContent(email, contentId);
        return ResponseEntity.ok(response);
    }

    //endpoint para eliminar el voto de un usuario sobre un contenido audiovisual
    @DeleteMapping("/deleteRating/{contentId}")
    public ResponseEntity<Void> deleteRating(
            @PathVariable Long contentId,
            Authentication authentication){

        //Obtenemos el email por el JWT
        String email = authentication.getName();

        contentRatingService.deleteRating(email, contentId);
        return ResponseEntity.noContent().build();
    }
}
