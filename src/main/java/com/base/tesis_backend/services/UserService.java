package com.base.tesis_backend.services;

import com.base.tesis_backend.entities.User;
import com.base.tesis_backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

//Esto es un Service, aca va la logica de la aplicacion
@Service
public class UserService {

    @Autowired //conexion al repository
    private UserRepository userRepository;

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
}
