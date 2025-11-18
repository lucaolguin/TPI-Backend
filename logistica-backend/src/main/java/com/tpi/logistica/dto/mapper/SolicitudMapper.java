package com.tpi.logistica.dto.mapper;

import com.tpi.logistica.dto.SolicitudDto;
import com.tpi.logistica.solicitud.Solicitud;

public class SolicitudMapper {

    public static SolicitudDto toDto(Solicitud s) {
        SolicitudDto dto = new SolicitudDto();
        dto.setId(s.getId());
        dto.setClienteId(s.getCliente().getId());
        dto.setContenedorId(s.getContenedor().getId());
        dto.setEstado(s.getEstado());
        dto.setCostoEstimado(s.getCostoEstimado());
        dto.setTiempoEstimadoMin(s.getTiempoEstimadoMin());
        dto.setCostoFinal(s.getCostoFinal());
        dto.setTiempoRealMin(s.getTiempoRealMin());
        return dto;
    }
}
