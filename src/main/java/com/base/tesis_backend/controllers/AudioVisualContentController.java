package com.base.tesis_backend.controllers;

import com.base.tesis_backend.services.AudioVisualContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/audioVisualContent")
@RequiredArgsConstructor
public class AudioVisualContentController {
    @Autowired
    private final AudioVisualContentService audioVisualContentService;

    //endpoint que recibe el id y el type de un contenido audiovisual que el usuario
    //esta solicitando y devuelve todos los detalles de este contenido.
    @GetMapping("/getContent/{type}/{id}")
    public ResponseEntity<?> getAudioVisualContentDetails(
            @PathVariable String type,
            @PathVariable Long id,
            Authentication authentication
    ){
        try {
            Object content = audioVisualContentService.getAudioVisualContentDetails(type, id);
            return ResponseEntity.ok(content);
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e){
            return ResponseEntity.internalServerError().body("Ocurrio un error al obtener los datos de TMDB: " + e.getMessage());
        }
    }
}
