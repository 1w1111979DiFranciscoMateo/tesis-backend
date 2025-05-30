package com.base.tesis_backend.services;

import com.base.tesis_backend.Dtos.UserListDTO;
import com.base.tesis_backend.Dtos.UserListResponseDTO;
import com.base.tesis_backend.entities.User;
import com.base.tesis_backend.entities.UserList;
import com.base.tesis_backend.repositories.UserListRepository;
import com.base.tesis_backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserListService {

    @Autowired
    private UserListRepository userListRepository;

    @Autowired
    private UserRepository userRepository;

    //metodo para generar las listas por defecto (Favoritos, Ver más tarde, Vistos)
    // cuando se registra un usuario nuevo
    public void createDefaultLists(User user){
        String[] defaultNames = {"Favoritos", "Vistos", "Ver más Tarde"};

        for (String name : defaultNames) {
            UserList list = new UserList();
            list.setUser(user);
            list.setName(name);
            list.setDescription("Lista por defecto: " + name);
            list.setPublic(false);
            list.setCreationDate(LocalDateTime.now());

            userListRepository.save(list);
        }
    }

    //metodo para Buscar todas las listas pertenecientes a un usuario y devolverlas
    public List<UserListDTO> getUserLists(String email){
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if(optionalUser.isEmpty()){
            throw new UsernameNotFoundException("Usuario no encontrado con email: " + email);
        }

        User user = optionalUser.get();
        List<UserList> lists = userListRepository.findByUser(user);

        return lists.stream()
                .map(list -> new UserListDTO(
                        list.getId(),
                        list.getName(),
                        list.getDescription(),
                        list.isPublic(),
                        list.getCreationDate()
                ))
                .toList();
    }

    //metodo para crear una nueva userList y devolver esta lista creada
    public UserListDTO createUserList(String email, UserListResponseDTO request){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

        //Validacion si ya existe una lista con ese nombre para el usuario logueado
        if(userListRepository.existsByNameAndUser(request.getName(), user)){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe una lista con ese nombre");
        }

        UserList list = new UserList();
        list.setUser(user);
        list.setName(request.getName());
        list.setDescription(request.getDescription());
        list.setPublic(request.isPublic());
        list.setCreationDate(LocalDateTime.now());

        UserList savedList = userListRepository.save(list);

        return new UserListDTO(
                savedList.getId(),
                savedList.getName(),
                savedList.getDescription(),
                savedList.isPublic(),
                savedList.getCreationDate()
        );
    }

    //metodo que verifica que una lista no se cree con el mismo nombre de una lista ya
    //existente (por usuario)
    public boolean listNameExistForUser(String name, String email) {
        return userListRepository.existsByNameIgnoreCaseAndUserEmail(name, email);
    }
}
