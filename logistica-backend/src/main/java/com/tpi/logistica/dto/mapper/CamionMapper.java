package com.tpi.logistica.dto.mapper;

import com.tpi.logistica.camion.Camion;
import com.tpi.logistica.dto.CamionDto;

public class CamionMapper {

    public static CamionDto toDto(Camion c) {
        CamionDto dto = new CamionDto();
        dto.setId(c.getId());
        dto.setDominio(c.getDominio());
        dto.setNombreTransportista(c.getNombreTransportista());
        dto.setTelefono(c.getTelefono());
        dto.setCapacidadPesoKg(c.getCapacidadPesoKg());
        dto.setCapacidadVolM3(c.getCapacidadVolM3());
        dto.setCostoBaseKm(c.getCostoBaseKm());
        dto.setConsumoLtKm(c.getConsumoLtKm());
        dto.setDisponible(c.getDisponible());
        return dto;
    }
}
