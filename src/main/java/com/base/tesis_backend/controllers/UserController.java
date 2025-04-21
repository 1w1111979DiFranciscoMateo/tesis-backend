package com.base.tesis_backend.controllers;

import com.base.tesis_backend.entities.User;
import com.base.tesis_backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

//Esto es un controller, aca se escuchan las llamadas a la api
@RestController
@RequestMapping("/user") //esto marca que todos los endpoints son /users
public class UserController {

    @Autowired //conexion al service
    private UserService userService;

    //endpoint para registrar un nuevo usuario
    @PostMapping("/add")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        try {
            //intentamos buscar el usuario ingresado por su email
            Optional<User> exist = userService.findByEmail(user.getEmail());

            if (exist.isPresent()) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            //si no entra el if de arriba, quiere decir que no existe un usuario con ese
            //Email, por lo tanto lo guardo al usuario y devuelvo un CREATED.
            User newUser = userService.addUser(user);
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);

        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //endpoint para verificar si un usuario existe o no
    @GetMapping("/exist")
    public ResponseEntity<Boolean> exist(@RequestParam String email) {
        try {
            //Busco si el email existe, si existe el boolean es true y devuelvo ese true.
            //Si no existe ese boolean es false y lo devuelvo tambien
            boolean exist = userService.findByEmail(email).isPresent();
            return new ResponseEntity<>(exist, HttpStatus.OK);

        } catch (Exception ex){
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
