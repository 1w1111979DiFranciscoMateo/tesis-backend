package com.base.tesis_backend.controllers;

import com.base.tesis_backend.services.TmdbSyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tmdb-sync")
@RequiredArgsConstructor
public class TmdbSyncController {
    private final TmdbSyncService tmdbSyncService;

    @PostMapping("/all")
    public ResponseEntity<String> syncAll() {
        tmdbSyncService.syncCategoriesMovies();
        tmdbSyncService.syncCategoriesSeries();
        tmdbSyncService.syncPlatformsMovies();
        tmdbSyncService.syncPlatformsSeries();

        return ResponseEntity.ok("Sincronizacion completa de peliculas y series.");
    }

    @PostMapping("/categories/movies")
    public ResponseEntity<String> syncCategoriesMovies() {
        tmdbSyncService.syncCategoriesMovies();
        return ResponseEntity.ok("Categorias de Peliculas sincronizadas.");
    }

    @PostMapping("/categories/series")
    public ResponseEntity<String> syncCategoriesSeries() {
        tmdbSyncService.syncCategoriesSeries();
        return ResponseEntity.ok("Categorias de Series sincronizadas.");
    }

    @PostMapping("/platforms/movies")
    public ResponseEntity<String> syncPlatformsMovies() {
        tmdbSyncService.syncPlatformsMovies();
        return ResponseEntity.ok("Plataformas de Peliculas sincronizadas.");
    }

    @PostMapping("/platforms/series")
    public ResponseEntity<String> syncPlatformsSeries() {
        tmdbSyncService.syncPlatformsSeries();
        return ResponseEntity.ok("Plataformas de Series sincronizadas.");
    }




}
