package com.base.tesis_backend.Dtos.AdminDTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

//Esto es un DTO para enviarle al frontend todos los datos necesarios para
//hacer la pagina de Estadisticas de Listas
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListStatisticsFullDTO {
    private Double percentageUsersWithMoreThanThreeLists;
    private Double averageContentPerList;
    private Double averageCustomListsPerUser;
    private Map<String, Long> defaultListRanking;
    private Map<String, Long> listSizeDistribution;
}
