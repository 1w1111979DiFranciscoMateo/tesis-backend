package com.base.tesis_backend.Dtos.AdminDTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//DTO para enviarle al frontend todas las Stats de los KPIs para la pagina de
//estadisticas de contenidos audiovisuales
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContentKpiStatsDTO {
    private Long totalContents;
    private Double percentageRated;
    private Double percentageCommented;
    private Double percentageInLists;
}
