package com.base.tesis_backend.controllers;

import com.base.tesis_backend.Dtos.UserListDTO;
import com.base.tesis_backend.Dtos.UserListResponseDTO;
import com.base.tesis_backend.services.UserListService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/userLists")
public class UserListController {

    @Autowired
    private UserListService userListService;

    //en este endpoint devuelvo una lista de UserListDTO para el frontend
    //devuelvo todas las listas del usuario logueado.
    @GetMapping("/getAll")
    public ResponseEntity<List<UserListDTO>> getLists(Authentication authentication) {
        //extraemos el email del token
        String email = authentication.getName();

        List<UserListDTO> userLists = userListService.getUserLists(email);

        return new ResponseEntity<>(userLists, HttpStatus.OK);
    }

    //este es un endpoint que va a recibir del frontend los valores de una nueva
    //lista a crear y va a devolverle al frontend la Lista ya creada
    @PostMapping("/create")
    public ResponseEntity<UserListDTO> createUserList(@RequestBody UserListResponseDTO request, Authentication authentication) {
        //extraemos el email del token
        String email = authentication.getName();
        UserListDTO createdList = userListService.createUserList(email, request);
        return new ResponseEntity<>(createdList, HttpStatus.CREATED);
    }

    //este es un endpoint para que funcione el async validator del frontend que
    //valida que una lista no se pueda crear con el nombre de una lista ya existente
    @GetMapping("/exist")
    public ResponseEntity<Boolean> checkIfListNameExist(@RequestParam String name, Authentication authentication) {
        //extraemos el email del token
        String email = authentication.getName();

        boolean exists = userListService.listNameExistForUser(name, email);
        return ResponseEntity.ok(exists);
    }

}
