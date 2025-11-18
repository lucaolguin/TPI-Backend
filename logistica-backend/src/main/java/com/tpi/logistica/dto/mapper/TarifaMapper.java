package com.tpi.logistica.dto.mapper;

import com.tpi.logistica.dto.TarifaDto;
import com.tpi.logistica.tarifa.Tarifa;

public class TarifaMapper {

    public static TarifaDto toDto(Tarifa t) {
        TarifaDto dto = new TarifaDto();
        dto.setId(t.getId());
        dto.setNombre(t.getNombre());
        dto.setValorLitroCombustible(t.getValorLitroCombustible());
        dto.setCostoKmBase(t.getCostoKmBase());
        dto.setCostoGestionTramo(t.getCostoGestionTramo());
        dto.setRecargoVolumenPorM3(t.getRecargoVolumenPorM3());
        dto.setRecargoPesoPorKg(t.getRecargoPesoPorKg());
        return dto;
    }
}
