package com.base.tesis_backend.services;

import com.base.tesis_backend.entities.Platform;
import com.base.tesis_backend.entities.User;
import com.base.tesis_backend.entities.UserPlatform;
import com.base.tesis_backend.repositories.UserPlatformRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserPlatformService {
    @Autowired
    private UserPlatformRepository userPlatformRepository;

    //metodo para guardar que un usuario tiene acceso a una plataforma especifica
    public void addUserPlatform(User user, Platform platform) {
        UserPlatform userPlatform = new UserPlatform();
        userPlatform.setUser(user);
        userPlatform.setPlatform(platform);
        userPlatformRepository.save(userPlatform);
    }
}
