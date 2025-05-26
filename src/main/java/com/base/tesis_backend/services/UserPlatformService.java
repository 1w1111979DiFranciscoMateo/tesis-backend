package com.base.tesis_backend.services;

import com.base.tesis_backend.entities.Platform;
import com.base.tesis_backend.entities.User;
import com.base.tesis_backend.entities.UserPlatform;
import com.base.tesis_backend.repositories.UserCategoryRepository;
import com.base.tesis_backend.repositories.UserPlatformRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserPlatformService {
    @Autowired
    private UserPlatformRepository userPlatformRepository;
    @Autowired
    private UserCategoryRepository userCategoryRepository;

    //metodo para guardar que un usuario tiene acceso a una plataforma especifica
    public void addUserPlatform(User user, Platform platform) {
        UserPlatform userPlatform = new UserPlatform();
        userPlatform.setUser(user);
        userPlatform.setPlatform(platform);
        userPlatformRepository.save(userPlatform);
    }

    //esto es para usar el metodo del repository para encontrar los ids
    //de las plataformas que un usuario tiene como disponibles.
    public List<Long> findPlatformIdsByUserIds(Long userId){
        return userPlatformRepository.findPlatformIdsByUserId(userId);
    }

    //este metodo es para encontrar las plataformas disponibles de un usuario y
    //devolverlas con todos los datos.
    public List<Platform> getPlatformsByUser(User user){
        return userPlatformRepository.findByUser(user)
                .stream()
                .map(UserPlatform::getPlatform)
                .collect(Collectors.toList());
    }

    //este metodo es para actualizar las plataformas disponibles de un usuario
    public void updateUserPlatforms(User user, List<Platform> platforms){
        //Borramos las plataformas anteriores.
        userPlatformRepository.deleteByUser(user);
        List<UserPlatform> newAssociations = platforms.stream()
                .map(platform -> {
                    UserPlatform up = new UserPlatform();
                    up.setUser(user);
                    up.setPlatform(platform);
                    return up;
                }).toList();
        userPlatformRepository.saveAll(newAssociations);
    }
}
