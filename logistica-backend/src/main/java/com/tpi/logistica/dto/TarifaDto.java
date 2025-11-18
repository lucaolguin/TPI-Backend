package com.tpi.logistica.dto;

import lombok.Data;

@Data
public class TarifaDto {
    private Long id;
    private String nombre;
    private Double valorLitroCombustible;
    private Double costoKmBase;
    private Double costoGestionTramo;
    private Double recargoVolumenPorM3;
    private Double recargoPesoPorKg;
}
