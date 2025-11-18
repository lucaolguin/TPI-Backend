package com.tpi.logistica.dto;

import lombok.Data;

@Data
public class TramoDto {
    private Long id;
    private Long rutaId;
    private Long camionId;
    private Long depositoOrigenId;
    private Long depositoDestinoId;
    private Long tarifaId;
    private String tipo;
    private String estado;
    private Double costoAprox;
    private Double costoReal;
}
