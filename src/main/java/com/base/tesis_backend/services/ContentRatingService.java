package com.base.tesis_backend.services;

import com.base.tesis_backend.Dtos.ContentRatingDTOs.ContentRatingAverageResponseDTO;
import com.base.tesis_backend.Dtos.ContentRatingDTOs.ContentRatingRequestDTO;
import com.base.tesis_backend.Dtos.ContentRatingDTOs.ContentRatingResponseDTO;
import com.base.tesis_backend.entities.AudioVisualContent;
import com.base.tesis_backend.entities.ContentRating;
import com.base.tesis_backend.entities.User;
import com.base.tesis_backend.repositories.AudioVisualContentRepository;
import com.base.tesis_backend.repositories.ContentRatingRepository;
import com.base.tesis_backend.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContentRatingService {

    //Inyects
    private final ContentRatingRepository contentRatingRepository;
    private final UserRepository userRepository;
    private final AudioVisualContentRepository audioVisualContentRepository;

    //metodo para recibir el puntaje de un contenido y guardarlo o actualizar un puntaje
    //ya existente (por usuario)
    @Transactional
    public ContentRatingResponseDTO createOrUpdateRating(String email, ContentRatingRequestDTO request) {
        //Verificamos que el usuario existe por email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        //Verificamos que el contenido existe por id
        AudioVisualContent content = audioVisualContentRepository.findById(request.getContentId())
                .orElseGet(() -> {
                    //si el contenido no existe en la bd lo creo y guardo
                    AudioVisualContent newContent = new AudioVisualContent();
                    newContent.setId(request.getContentId());
                    newContent.setTitle(request.getTitle());
                    newContent.setPosterPath(request.getPosterPath());
                    newContent.setRating(request.getRatingTMDB());
                    newContent.setType(request.getType());
                    return audioVisualContentRepository.save(newContent);
                });

        //Buscamos si ya existe un rating del usuario para este contenido
        Optional<ContentRating> existingRating = contentRatingRepository
                .findByUserIdAndContentId(user.getId(), request.getContentId());

        ContentRating contentRating;

        if(existingRating.isPresent()){
            //si el usuario ya tiene un rating, actualizamos su valor
            contentRating = existingRating.get();
            contentRating.setRating(request.getRating());
        } else {
            //el usuario no tiene un rating entonces creamos uno
            contentRating = new ContentRating();
            contentRating.setUser(user);
            contentRating.setContent(content);
            contentRating.setRating(request.getRating());
        }

        //guardamos el update / nuevo rating
        ContentRating savedRating = contentRatingRepository.save(contentRating);

        return mapToResponse(savedRating);
    }

    //metodo para devolver el puntaje que un usuario le puso a un contenido audiovisual
    @Transactional
    public ContentRatingResponseDTO getUserRatingForContent(String email, Long contentId) {
        //Obtenemos el usuario por email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        //Busco el rating de un usuario sobre un contenido
        ContentRating rating = contentRatingRepository.findByUserIdAndContentId(user.getId(), contentId)
                .orElseThrow(() -> new RuntimeException("Rating not found for user " + email + " and content " + contentId));

        return mapToResponse(rating);
    }

    //metodo para devolver el puntaje final promedio de un contenido audiovisual y la
    //cantidad de votos que tiene este contenido
    @Transactional
    public ContentRatingAverageResponseDTO getContentAverageRating(Long contentId){
        //Verificamos que el contenido exista
        audioVisualContentRepository.findById(contentId)
                .orElseThrow(() -> new RuntimeException("Audio visual content not found"));

        //Obtenemos estadisticas por separado
        BigDecimal avgRating = contentRatingRepository.getAverageRatingByContentId(contentId);
        Long totalRatings = contentRatingRepository.getTotalRatingsByContentId(contentId);

        //Manejar null y redondear a 1 decimal
        BigDecimal averageRating = avgRating != null ?
                avgRating.setScale(1, RoundingMode.HALF_UP) :
                BigDecimal.ZERO;

        return new ContentRatingAverageResponseDTO(contentId, averageRating, totalRatings);
    }

    //metodo para devolver el puntaje final promedio de un contenido audiovisual
    @Transactional
    public BigDecimal getContentAverageRatingValue(Long contentId){
        //obtenemos el valor
        BigDecimal average = contentRatingRepository.getAverageRatingByContentId(contentId);

        if(average == null){
            return BigDecimal.ZERO;
        }

        //redondeamos a 1 decimal y devolvemos
        return average.setScale(1, RoundingMode.HALF_UP);
    }

    //metodo para eliminar el rating de un usuario a un contenido audiovisual
    @Transactional
    public void deleteRating(String email, Long contentId) {
        //Obtenemos el usuario por email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        //Obtenemos el rating del usuario a ese contenido seleccionado
        ContentRating rating = contentRatingRepository.findByUserIdAndContentId(user.getId(), contentId)
                .orElseThrow(() -> new RuntimeException("Rating not found for user " + email + " and content " + contentId));

        contentRatingRepository.delete(rating);
    }

    //metodo para saber si un usuario le puso un rating a un contenido audiovisual
    @Transactional
    public boolean hasUserRatedContent(String email, Long contentId) {
        //Obtenemos el usuario por email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        //devolvemos un true o false
        return contentRatingRepository.existsByUserIdAndContentId(user.getId(), contentId);
    }

    //metodo para las devoluciones mapToResponse (ayuda a no escribir siempre lo mismo
    // en cada return de los metodos en donde se usa)
    private ContentRatingResponseDTO mapToResponse(ContentRating rating) {
        return new ContentRatingResponseDTO(
                rating.getId(),
                rating.getUser().getId(),
                rating.getContent().getId(),
                rating.getRating(),
                rating.getCreationDate()
        );
    }
}
