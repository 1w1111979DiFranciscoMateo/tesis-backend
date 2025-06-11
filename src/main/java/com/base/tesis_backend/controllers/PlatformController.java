package com.base.tesis_backend.controllers;

import com.base.tesis_backend.entities.Platform;
import com.base.tesis_backend.repositories.PlatformRepository;
import com.base.tesis_backend.services.PlatformService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/platforms")
@RequiredArgsConstructor
public class PlatformController {
    @Autowired
    private final PlatformService platformService;

    //Endpoint que devuelve todas las plataformas de la base de datos
    @GetMapping("/getAll")
    public ResponseEntity<List<Platform>> getAllPlatforms(){
        return ResponseEntity.ok(platformService.getAll());
    }

    //endpoint que devuelve todas las plataformas en donde se encuentra
    //el contenido audiovisual solicitado
    @GetMapping("/forContent")
    public ResponseEntity<List<Platform>> getContentPlatforms(
            @RequestParam Long id,
            @RequestParam String type,
            Authentication authentication){

        List<Platform> platforms = platformService.getAvailablePlatformsForContent(id, type);
        return ResponseEntity.ok(platforms);
    }

}
