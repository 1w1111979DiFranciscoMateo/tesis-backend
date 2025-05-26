package com.base.tesis_backend.Dtos;

import com.base.tesis_backend.entities.Category;
import com.base.tesis_backend.entities.Platform;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

//DTO para devolverle al frontend todos los datos que se van a
//mostrar en la pantalla "Mi Perfil".
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDTO {
    private String username;
    private String email;
    private String password;
    private boolean isPremium;
    private boolean publicProfile;
    private LocalDateTime creationDate;
    private List<Category> userCategories;
    private List<Platform> userPlatforms;
}
