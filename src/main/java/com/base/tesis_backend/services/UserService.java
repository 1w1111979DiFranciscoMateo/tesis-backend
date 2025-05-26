package com.base.tesis_backend.services;

import com.base.tesis_backend.Dtos.UpdateUserDTO;
import com.base.tesis_backend.Dtos.UserProfileDTO;
import com.base.tesis_backend.config.JwtUtil;
import com.base.tesis_backend.entities.Category;
import com.base.tesis_backend.entities.Platform;
import com.base.tesis_backend.entities.User;
import com.base.tesis_backend.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

//Esto es un Service, aca va la logica de la aplicacion
@Service
public class UserService {

    @Autowired //conexion al repository
    private UserRepository userRepository;
    @Autowired
    private UserCategoryService userCategoryService;
    @Autowired
    private UserPlatformService userPlatformService;
    @Autowired
    private JwtUtil jwtUtil;

    //metodo para encontrar un usuario por Email
    public Optional<User> findByEmail(String email) {
        //temporal
        System.out.println(("Email recibido en el servicio: " + email));
        return userRepository.findByEmail(email);
    }

    //metodo para guardar un nuevo usuario registrado
    public User addUser(User user) {
        return userRepository.save(user);
    }

    //metodo que dado un email, devolvemos todos los datos del usuario
    //para mostrarlos en la pantalla de "Mi Perfil".
    //Tambien uso este metodo para cargar el formulario de "Modificar Perfil" con
    //los datos que ya tiene el usuario
    public UserProfileDTO getUserProfile(String email) {
        //Buscamos el usuario por email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("El usuario no Encontrado"));

        //Obtenemos las categorias y plataformas associadas de este usuario
        List<Category> userCategories = userCategoryService.getCategoriesByUser(user);
        List<Platform> userPlatforms = userPlatformService.getPlatformsByUser(user);

        //Arrmamos y retornamos el dto
        return new UserProfileDTO(
                user.getRealUsername(),
                user.getEmail(),
                user.getPassword(),
                user.isPremium(),
                user.isPublicProfile(),
                user.getCreationDate(),
                userCategories,
                userPlatforms
        );
    }

    //este es un metodo para actualizar los datos de un usuario
    //usa Transactional porq hace varios remove o pudate y Spring necesita
    //como agrupar todos los cambios en un paquete. Con esto le dice que aca comienza
    @Transactional
    public void updateUser(String email, UpdateUserDTO dto){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("Usuario no Encontrado"));

        //Actualizamos los datos
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword()); //Sin hashing por ahora
        user.setPublicProfile(dto.isPublicProfile());

        //Actualizamos categorias
        userCategoryService.updateUserCategories(user, dto.getUserCategories());

        //Actualizamos Plataformas
        userPlatformService.updateUserPlatforms(user, dto.getUserPlatforms());

        userRepository.save(user);

    }
}
