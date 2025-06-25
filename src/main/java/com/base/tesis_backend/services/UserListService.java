package com.base.tesis_backend.services;

import com.base.tesis_backend.Dtos.*;
import com.base.tesis_backend.Dtos.AdminDTOs.ListStatisticsFullDTO;
import com.base.tesis_backend.entities.AudioVisualContent;
import com.base.tesis_backend.entities.User;
import com.base.tesis_backend.entities.UserList;
import com.base.tesis_backend.entities.UserListContent;
import com.base.tesis_backend.repositories.AudioVisualContentRepository;
import com.base.tesis_backend.repositories.UserListContentRepository;
import com.base.tesis_backend.repositories.UserListRepository;
import com.base.tesis_backend.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserListService {

    @Autowired
    private UserListRepository userListRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserListContentRepository userListContentRepository;
    @Autowired
    private AudioVisualContentRepository audioVisualContentRepository;

    //metodo para generar las listas por defecto (Favoritos, Ver más tarde, Vistos)
    // cuando se registra un usuario nuevo
    public void createDefaultLists(User user){
        String[] defaultNames = {"Favoritos", "Vistos", "Ver más Tarde"};

        for (String name : defaultNames) {
            UserList list = new UserList();
            list.setUser(user);
            list.setName(name);
            list.setDescription("Lista por defecto de " + name);
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

    //metodo para buscar toda la informacion de una lista especifica y devolverla.
    public UserListWithContentsDTO getUserListDetail(Long listId, String email){
        //Busco la lista solicitada del usuario logueado
        UserList list = userListRepository.findByIdAndUserEmail(listId, email)
                .orElseThrow( () -> new RuntimeException("Lista no encontrada o no pertenece al Usuario"));

        //Buscamos los contenidos audiovisuales pertenecientes a la lista
        List<UserListContent> userListContents = userListContentRepository.findByList(list);

        //Genero el array de AudioVisualContentDTO de la lista solicitada
        List<AudioVisualContentDTO> contents = userListContents.stream()
                .map(ulc -> {
                    AudioVisualContent content = ulc.getContent();
                    return new AudioVisualContentDTO(
                            content.getId(),
                            content.getTitle(),
                            content.getPosterPath(),
                            content.getRating(),
                            content.getType()
                    );
                }).toList();

        //genero el objeto UserListWithContentDTO que le voy a devolver al front
        return new UserListWithContentsDTO(
                list.getId(),
                list.getName(),
                list.getDescription(),
                list.isPublic(),
                list.getCreationDate(),
                contents
        );
    }

    //metodo para agregar o quitar el un contenido de una Lista (y para guardar el
    // contenido en la base de datos)
    @Transactional
    public void toggleContentInList(AddOrRemoveContentDTO dto, String email){
        //Valido que la lista pertenece al usuario logueado
        UserList list = userListRepository.findByIdAndUserEmail(dto.getListId(), email)
                .orElseThrow(() -> new RuntimeException("Lista no encontrada o no pertenece al usuario"));

        //Buscamos si el contenido ya esta en la base de datos
        AudioVisualContent content = audioVisualContentRepository.findById(dto.getContentId())
                .orElseGet(() -> {
                    //Si no existe lo creo y lo guardo
                    AudioVisualContent newContent = new AudioVisualContent();
                    newContent.setId(dto.getContentId());
                    newContent.setTitle(dto.getTitle());
                    newContent.setPosterPath(dto.getPosterPath());
                    newContent.setRating(dto.getRating());
                    newContent.setType(dto.getType());
                    return audioVisualContentRepository.save(newContent);
                });

        //Verificamos si ya existe la relacion (si el contenido ya esta agregado a la lista)
        Optional<UserListContent> existing = userListContentRepository.findByListAndContent(list, content);

        if(existing.isPresent()){
            //Si ya existe lo quito de la lista
            userListContentRepository.delete(existing.get());
        } else {
            //Si no existe lo agrego
            UserListContent relation = new UserListContent();
            relation.setList(list);
            relation.setContent(content);
            userListContentRepository.save(relation);
        }
    }

    //Metodo para obtener los IDs de las Listas que contienen un Contenido Audiovisual
    //Especifico
    public List<Long> getListsContainingContent(Long contentId, String email){
        //Obtenemos todas las listas del usuario
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

        //Busco el contenido audiovisual si existe
        Optional<AudioVisualContent> contentOpt = audioVisualContentRepository.findById(contentId);

        if(contentOpt.isEmpty()){
            //Si el contenido no existe en nuestra Base de Datos, no esta en ninguna lista
            return List.of();
        }

        AudioVisualContent content = contentOpt.get();

        //Busco todas las relaciones UserListContent que contengan este contenido audiovisual
        //y que las listas pertenezcan al usuario logueado
        List<UserListContent> relations = userListContentRepository.findByContent(content);

        //Filtro solo las listas que pertenezcan al usuario logueado y extraigo los IDs
        return relations.stream()
                .filter(relation -> relation.getList().getUser().equals(user))
                .map(relation -> relation.getList().getId())
                .collect(Collectors.toList());
    }

    //Metodo para editar los valores base de una lista (nombre, descripcion, visibilidad)
    //exceptuando las listas por defecto (Favoritos, Ver mas Tarde, Vistos)
    public UserListDTO updateUserList(Long listId, String email, UserListResponseDTO request){
        //busco la lista del usuario logueado
        UserList list = userListRepository.findByIdAndUserEmail(listId, email)
                .orElseThrow(() -> new RuntimeException("Lista no encontrado o no pertenece al usuario."));

        //Verifico que no sea una lista por defecto
        String[] defaultListNames = {"Favoritos", "Vistos", "Ver más Tarde"};
        for(String defaultName : defaultListNames){
            if(list.getName().equals(defaultName)){
                System.out.println("COINCIDENCIA ENCONTRADA - Rechazando edición");
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                        "No se pueden editar las listas por defecto");
            }
        }

        //Verifico que el nuevo nombre no exista ya para este usuario (solo si cambió el nombre)
        if(userListRepository.existsByNameAndUserAndIdNot(request.getName(), list.getUser(), listId)){
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Ya existe una lista con ese nombre");
        }

        //Actualizo los campos
        list.setName(request.getName());
        list.setDescription(request.getDescription());
        list.setPublic(request.isPublic());

        //Guardo los cambios
        UserList updatedList = userListRepository.save(list);

        //Devuelvo el DTO de la lista actualizado
        return new UserListDTO(
                updatedList.getId(),
                updatedList.getName(),
                updatedList.getDescription(),
                updatedList.isPublic(),
                updatedList.getCreationDate()
        );
    }

    //metodo para eliminar una lista
    //Exceptuando las listas por defecto (Favoritos, Vistos, Ver más Tarde)
    @Transactional
    public void deleteUserList(Long listId, String email){
        //Busco la lista del usuario logueado
        UserList list = userListRepository.findByIdAndUserEmail(listId, email)
                .orElseThrow(() -> new RuntimeException("Lista no encontrada o no pertenece al usuario"));

        //Verificamos que no sea un lista por defecto
        String[] defaultListNames = {"Favoritos", "Vistos", "Ver más Tarde"};
        for(String defaultName : defaultListNames){
            if(list.getName().equals(defaultName)){
                System.out.println("COINCIDENCIA ENCONTRADA - Rechazando edición");
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                        "No se pueden editar las listas por defecto");
            }
        }

        //Primero elimino todas las relaciones UserListContent de esta lista
        //para evitar problemas de integridad referencial
        userListContentRepository.deleteByList(list);

        //Luego elimino la lista
        userListRepository.delete(list);
    }

    //*****************************************************************
    //Aca comienzan los metodos para hacer Las Estadisticas de Listas
    //*****************************************************************

    //metodo para devolverle al front todas las estadisticas necesarias
    //para la pagina de Estadisticas de Listas
    public ListStatisticsFullDTO getCompleteListStatistics(){
        List<User> allUsers = userRepository.findAll();
        List<UserList> allLists = userListRepository.findAll();
        List<UserListContent> allListContents = userListContentRepository.findAll();

        // === KPI 1: % de usuarios con más de 3 listas ===
        Map<Long, Long> listsPerUser = allLists.stream()
                .collect(Collectors.groupingBy(l -> l.getUser().getId(), Collectors.counting()));

        long usersWithMoreThan3Lists = listsPerUser.values().stream().filter(count -> count > 3).count();
        long usersWithAtLeast1List = listsPerUser.size();

        double percentage = usersWithAtLeast1List == 0 ? 0.0 : ((double) usersWithMoreThan3Lists / usersWithAtLeast1List) * 100;

        // === KPI 2: promedio de contenidos por lista ===
        double avgContent = allLists.isEmpty() ? 0.0 : ((double) allListContents.size() / allLists.size());

        // === KPI 3: promedio de listas personalizadas por usuarios ===
        Map<Long, Long> listCountPerUser = allLists.stream()
                .collect(Collectors.groupingBy(l -> l.getUser().getId(), Collectors.counting()));

        long usersWithLists = listCountPerUser.size();

        long totalCustomLists = allLists.stream()
                .filter(l -> !List.of("Favoritos", "Ver más Tarde", "Vistos").contains(l.getName()))
                .count();

        double averageCustomListsPerUser = usersWithLists == 0 ? 0.0 : (double) totalCustomLists / usersWithLists;

        // === Gráfico 1: ranking de listas por defecto ===
        String[] defaultNames = {"Favoritos", "Ver más Tarde", "Vistos"};
        Map<String, Long> defaultRanking = Arrays.stream(defaultNames)
                .collect(Collectors.toMap(name -> name, name -> {
                    return allListContents.stream()
                            .filter(ulc -> ulc.getList().getName().equals(name))
                            .count();
                }));

        // === Gráfico 2: distribución por cantidad de contenido ===
        Map<String, Long> sizeDistribution = new HashMap<>();
        sizeDistribution.put("0", 0L);
        sizeDistribution.put("1-5", 0L);
        sizeDistribution.put("6-10", 0L);
        sizeDistribution.put("11-20", 0L);
        sizeDistribution.put("+20", 0L);

        Map<Long, Long> contentCountPerList = allListContents.stream()
                .collect(Collectors.groupingBy(c -> c.getList().getId(), Collectors.counting()));

        for (UserList list : allLists){
            long count = contentCountPerList.getOrDefault(list.getId(), 0L);

            if (count == 0) sizeDistribution.put("0", sizeDistribution.get("0") + 1);
            else if (count <= 5) sizeDistribution.put("1-5", sizeDistribution.get("1-5") + 1);
            else if (count <= 10) sizeDistribution.put("6-10", sizeDistribution.get("6-10") + 1);
            else if (count <= 20) sizeDistribution.put("11-20", sizeDistribution.get("11-20") + 1);
            else sizeDistribution.put("+20", sizeDistribution.get("+20") + 1);
        }

        return new ListStatisticsFullDTO(percentage, avgContent, averageCustomListsPerUser, defaultRanking, sizeDistribution);
    }


}
