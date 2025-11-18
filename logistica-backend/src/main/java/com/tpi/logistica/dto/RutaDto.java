package com.tpi.logistica.dto;

import lombok.Data;

@Data
public class RutaDto {
    private Long id;
    private Long solicitudId;
    private Integer cantidadTramos;
    private Integer cantidadDepositos;
}
