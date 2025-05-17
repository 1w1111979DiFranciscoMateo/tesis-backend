package com.base.tesis_backend.controllers;

import com.base.tesis_backend.Dtos.UserLoginDTO;
import com.base.tesis_backend.Dtos.UserLoginResponseDTO;
import com.base.tesis_backend.Dtos.UserRegisterDTO;
import com.base.tesis_backend.Dtos.UserRegisterResponseDTO;
import com.base.tesis_backend.config.JwtUtil;
import com.base.tesis_backend.entities.Category;
import com.base.tesis_backend.entities.Platform;
import com.base.tesis_backend.entities.User;
import com.base.tesis_backend.services.UserCategoryService;
import com.base.tesis_backend.services.UserPlatformService;
import com.base.tesis_backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

//Esto es un controller, aca se escuchan las llamadas a la api

@RestController
@RequestMapping("/users") //esto marca que todos los endpoints son /users
public class UserController {

    @Autowired //conexion al service
    private UserService userService;
    @Autowired
    private UserCategoryService userCategoryService;
    @Autowired
    private UserPlatformService userPlatformService;
    @Autowired
    private JwtUtil jwtUtil;

    //endpoint para registrar un nuevo usuario
    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponseDTO> addUser(@RequestBody UserRegisterDTO dto) {
        try {
            //intentamos buscar el usuario ingresado por su email
            Optional<User> exist = userService.findByEmail(dto.email);

            //si entra a este if, quiere decir que existe un usuario con ese email
            if (exist.isPresent()) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }

            //Creamos el nuevo usuario con los datos del dto
            User user = new User();
            user.setUsername(dto.username);
            user.setEmail(dto.email);
            user.setPassword(dto.password);
            user.setPremium(false);
            user.setPublicProfile(false);
            user.setCreationDate(dto.creationDate);

            //Guardamos el usuario nuevo
            User savedUser = userService.addUser(user);

            //asociamos las categorias preferidas del usuario
            for (Category category : dto.userCategories){
                userCategoryService.addUserCategory(savedUser, category);
            }

            //asociamos las plataformas disponibles del usuario
            for (Platform platform : dto.userPlatforms){
                userPlatformService.addUserPlatform(savedUser, platform);
            }

            //Creamos un JWT para el nuevo usuario
            String jwtToken = jwtUtil.generateToken(savedUser.getEmail());

            //Creo un objeto responseDTO de tipo UserRegisterResponseDTO para devolverlo al front
            UserRegisterResponseDTO responseDTO = new UserRegisterResponseDTO(jwtToken, savedUser.getUsername(), savedUser.getEmail());

            //devolvemos el jwt token, el username y email del usuario registrado
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);

        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //endpoint para verificar si un usuario existe o no registrado con un email que recibe
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

    //endpoint para el login de usuario, devuelve un string
    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDTO> login(@RequestBody UserLoginDTO userLogin) {
        try {
            // temporal
            System.out.println("Buscando Email: " + userLogin.getEmail());
            //Buscamos si existe un usuario con el email recibido
            Optional<User> userOptional = userService.findByEmail(userLogin.getEmail());

            //Si el usuario existe...
            if (userOptional.isPresent()){
                User user = userOptional.get();
                //comparamos contraseñas del usuario que existe con el usuario recibido
                if (user.getPassword().equals(userLogin.getPassword())){
                    //generamos el token JWT usando el email del usuario
                    String jwtToken = jwtUtil.generateToken(user.getEmail());
                    //login exitoso, datos correctos
                    return ResponseEntity.ok(new UserLoginResponseDTO("Login Exitoso", jwtToken));
                } else {
                    //le erro de contraseña
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new UserLoginResponseDTO("Contraseña Incorrecta", null));
                }
            } else {
                //no existe el usuario
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new UserLoginResponseDTO("Email no encontrado", null));
            }
        } catch (Exception ex) {
            ex.printStackTrace(); //se imprime el error en consola
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new UserLoginResponseDTO("Error en el servidor: " + ex.getMessage(), null));
        }
    }
}
