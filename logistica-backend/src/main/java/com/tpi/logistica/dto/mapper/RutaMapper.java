package com.tpi.logistica.dto.mapper;

import com.tpi.logistica.dto.RutaDto;
import com.tpi.logistica.ruta.Ruta;

public class RutaMapper {

    public static RutaDto toDto(Ruta r) {
        RutaDto dto = new RutaDto();
        dto.setId(r.getId());
        dto.setSolicitudId(r.getSolicitud().getId());
        dto.setCantidadTramos(r.getCantidadTramos());
        dto.setCantidadDepositos(r.getCantidadDepositos());
        return dto;
    }
}
