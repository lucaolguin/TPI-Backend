package com.tpi.logistica.dto;

import lombok.Data;

@Data
public class DepositoDto {
    private Long id;
    private String nombre;
    private String direccion;
    private Double lat;
    private Double lon;
    private Double costoEstadiaDiario;
}
