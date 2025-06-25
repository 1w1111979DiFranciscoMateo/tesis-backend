package com.base.tesis_backend.controllers;

import com.base.tesis_backend.Dtos.AdminDTOs.ContentKpiStatsDTO;
import com.base.tesis_backend.Dtos.AdminDTOs.ListStatisticsFullDTO;
import com.base.tesis_backend.Dtos.AdminDTOs.UserSummaryDTO;
import com.base.tesis_backend.services.ContentStatisticsService;
import com.base.tesis_backend.services.UserListService;
import com.base.tesis_backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/admin")
public class AdminStatsController {

    //injects
    @Autowired
    private UserService userService;
    @Autowired
    private UserListService userListService;
    @Autowired
    private ContentStatisticsService contentStatisticsService;

    //metodo para verificar el email del usuario asi se ve si es el admin o no
    //Si quiero agregar otro admin, simplemente agregar su email a la lista
    private boolean isAdmin(String email){
        List<String> adminEmails = List.of(
                "mateo@gmail.com"
        );
        return adminEmails.contains(email);
    }

    //******************************************************************
    //ACA Empiezan los endpoints de Estadisticas de Usuario
    //******************************************************************

    //endpoint para obtener la cantidad de usuarios que existen en la aplicacion
    @GetMapping("/total-users")
    public ResponseEntity<Long> getTotalUsers(Authentication authentication){
        //extraemos el email del usuario del JWT
        String email = authentication.getName();
        //Verificamos si es el admin o no
        if(!isAdmin(email)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok(userService.countTotalUsers());
    }

    //endpoint para obtener una lista resumida de todos los usuarios
    @GetMapping("/all-users")
    public ResponseEntity<List<UserSummaryDTO>> getAllUsers(Authentication authentication){
        //extraemos el email del usuario del JWT
        String email = authentication.getName();
        //Verificamos si es el admin o no
        if(!isAdmin(email)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok(userService.getAllUserSumaries());
    }

    //endpoint para obtener todos los usuarios activos
    @GetMapping("/active-percentage")
    public ResponseEntity<Double> getPercentageOfActiveUsers(Authentication authentication){
        //extraemos el email del usuario del JWT
        String email = authentication.getName();
        //Verificamos si es el admin o no
        if(!isAdmin(email)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok(userService.getPercentageOfUsersWithCommentsOrRatings());
    }

    //endpoint para obtener el total de usuarios nuevos en las ultimas 2 semanas
    @GetMapping("/new-users")
    public ResponseEntity<Long> getNewUsersInLastTwoWeeks(Authentication authentication){
        //extraemos el email del usuario del JWT
        String email = authentication.getName();
        //Verificamos si es el admin o no
        if(!isAdmin(email)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok(userService.countUsersCreatedInLastTwoWeeks());
    }

    //endpoint para saber las categorias mas populares
    @GetMapping("/popular-categories")
    public ResponseEntity<Map<String, Long>> getMostPopularCategories(Authentication authentication){
        //extraemos el email del usuario del JWT
        String email = authentication.getName();
        //Verificamos si es el admin o no
        if(!isAdmin(email)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok(userService.getMostPopularCategories());
    }

    //endpoint para saber las plataformas mas usadas/populares
    @GetMapping("/popular-platforms")
    public ResponseEntity<Map<String, Long>> getMostPopularPlatforms(Authentication authentication){
        //extraemos el email del usuario del JWT
        String email = authentication.getName();
        //Verificamos si es el admin o no
        if(!isAdmin(email)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok(userService.getMostPopularPlatforms());
    }

    //******************************************************************
    //ACA Empiezan los endpoints de Estadisticas de Listas
    //******************************************************************

    //endpoint para envierle al frontned todas las estadisticas de listas
    @GetMapping("/lists")
    public ResponseEntity<ListStatisticsFullDTO> getListStats(Authentication authentication){
        //buscamos el email del usuario
        String email = authentication.getName();
        //Verificamos si es el admin o no
        if(!isAdmin(email)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        ListStatisticsFullDTO dto = userListService.getCompleteListStatistics();
        return ResponseEntity.ok(dto);
    }

    //******************************************************************
    //ACA Empiezan los endpoints de Estadisticas de Contenidos
    //******************************************************************

    //endpoint para devolver los contenidos con mayor cantidad de votos
    @GetMapping("/content/most-rated")
    public ResponseEntity<?> getMostRatedContents(Authentication authentication){
        //buscamos el email del usuario
        String email = authentication.getName();
        //Verificamos si es el admin o no
        if(!isAdmin(email)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return ResponseEntity.ok(contentStatisticsService.getMostRatedContents(10));
    }

    //endpoint para devolver los contenidos mas comentados
    @GetMapping("/content/most-commented")
    public ResponseEntity<?> getMostCommentedContents(Authentication authentication) {
        //buscamos el email del usuario
        String email = authentication.getName();
        //Verificamos si es el admin o no
        if(!isAdmin(email)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return ResponseEntity.ok(contentStatisticsService.getMostCommentedContents(10));
    }

    //endpoint para devolver los contenidos con mejor puntuacion
    @GetMapping("/content/top-rated")
    public ResponseEntity<?> getTopRatedContents(Authentication authentication) {
        //buscamos el email del usuario
        String email = authentication.getName();
        //Verificamos si es el admin o no
        if(!isAdmin(email)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return ResponseEntity.ok(contentStatisticsService.getTopRatedContents(10));
    }

    //endpoint para devolver al frontend todos los valores de los kpis de
    //estadisticas de contenidos
    @GetMapping("/content/kpis")
    public ResponseEntity<ContentKpiStatsDTO> getContentKpis(Authentication authentication){
        //buscamos el email del usuario
        String email = authentication.getName();
        //Verificamos si es el admin o no
        if(!isAdmin(email)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return ResponseEntity.ok(contentStatisticsService.getContentKpiStats());
    }


}
