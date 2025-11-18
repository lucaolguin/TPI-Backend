package com.tpi.logistica.dto;

import lombok.Data;

@Data
public class CamionDto {
    private Long id;
    private String dominio;
    private String nombreTransportista;
    private String telefono;
    private Double capacidadPesoKg;
    private Double capacidadVolM3;
    private Double costoBaseKm;
    private Double consumoLtKm;
    private Boolean disponible;
}
