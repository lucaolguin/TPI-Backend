package com.tpi.logistica.dto.mapper;

import com.tpi.logistica.dto.TramoDto;
import com.tpi.logistica.tramo.Tramo;

public class TramoMapper {

    public static TramoDto toDto(Tramo t) {
        TramoDto dto = new TramoDto();
        dto.setId(t.getId());
        dto.setRutaId(t.getRuta().getId());
        dto.setCamionId(t.getCamion().getId());
        dto.setDepositoOrigenId(t.getDepositoOrigen().getId());
        dto.setDepositoDestinoId(t.getDepositoDestino().getId());
        dto.setTarifaId(t.getTarifa().getId());
        dto.setTipo(t.getTipo());
        dto.setEstado(t.getEstado());
        dto.setCostoAprox(t.getCostoAprox());
        dto.setCostoReal(t.getCostoReal());
        return dto;
    }
}
