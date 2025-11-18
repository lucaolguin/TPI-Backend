package com.tpi.logistica.dto.mapper;

import com.tpi.logistica.contenedor.Contenedor;
import com.tpi.logistica.dto.ContenedorDto;

public class ContenedorMapper {

    public static ContenedorDto toDto(Contenedor c) {
        ContenedorDto dto = new ContenedorDto();
        dto.setId(c.getId());
        dto.setClienteId(c.getCliente().getId());
        dto.setPesoKg(c.getPesoKg());
        dto.setVolumenM3(c.getVolumenM3());
        dto.setEstado(c.getEstado());
        return dto;
    }
}
