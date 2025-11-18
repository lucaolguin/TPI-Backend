package com.tpi.logistica.dto;

import lombok.Data;

@Data
public class SolicitudDto {
    private Long id;
    private Long clienteId;
    private Long contenedorId;
    private String estado;
    private Double costoEstimado;
    private Integer tiempoEstimadoMin;
    private Double costoFinal;
    private Integer tiempoRealMin;
}
