package com.tpi.logistica.dto.mapper;

import com.tpi.logistica.deposito.Deposito;
import com.tpi.logistica.dto.DepositoDto;

public class DepositoMapper {

    public static DepositoDto toDto(Deposito d) {
        DepositoDto dto = new DepositoDto();
        dto.setId(d.getId());
        dto.setNombre(d.getNombre());
        dto.setDireccion(d.getDireccion());
        dto.setLat(d.getLat());
        dto.setLon(d.getLon());
        dto.setCostoEstadiaDiario(d.getCostoEstadiaDiario());
        return dto;
    }
}
