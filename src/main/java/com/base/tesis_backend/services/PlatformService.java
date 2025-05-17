package com.base.tesis_backend.services;

import com.base.tesis_backend.entities.Platform;
import com.base.tesis_backend.repositories.PlatformRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlatformService {
    @Autowired
    private final PlatformRepository platformRepository;

    //metodo que obtiene todas las plataformas de la base de datos
    public List<Platform> getAll() {
        return platformRepository.findAll();
    }
}
