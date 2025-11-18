package com.tpi.logistica.dto;

import lombok.Data;

@Data
public class ContenedorDto {
    private Long id;
    private Long clienteId;
    private Double pesoKg;
    private Double volumenM3;
    private String estado;
}
