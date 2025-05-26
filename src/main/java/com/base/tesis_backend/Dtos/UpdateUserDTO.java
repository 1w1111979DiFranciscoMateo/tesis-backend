package com.base.tesis_backend.Dtos;

import com.base.tesis_backend.entities.Category;
import com.base.tesis_backend.entities.Platform;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

//Este es un dto para actualizar los datos de un usuario
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDTO {
    private String username;
    private String password;
    private boolean publicProfile;
    private List<Category> userCategories;
    private List<Platform> userPlatforms;
}
