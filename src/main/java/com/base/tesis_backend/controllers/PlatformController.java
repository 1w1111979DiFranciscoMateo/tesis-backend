package com.base.tesis_backend.controllers;

import com.base.tesis_backend.entities.Platform;
import com.base.tesis_backend.services.PlatformService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/platforms")
@RequiredArgsConstructor
public class PlatformController {
    private final PlatformService platformService;

    //Endpoint que devuelve todas las plataformas de la base de datos
    @GetMapping("/getAll")
    public ResponseEntity<List<Platform>> getAllPlatforms(){
        return ResponseEntity.ok(platformService.getAll());
    }

}
