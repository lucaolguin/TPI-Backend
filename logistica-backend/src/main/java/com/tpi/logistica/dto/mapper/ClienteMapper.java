package com.tpi.logistica.dto.mapper;

import com.tpi.logistica.cliente.Cliente;
import com.tpi.logistica.dto.ClienteDto;

public class ClienteMapper {

    public static ClienteDto toDto(Cliente c) {
        ClienteDto dto = new ClienteDto();
        dto.setId(c.getId());
        dto.setNombre(c.getNombre());
        dto.setEmail(c.getEmail());
        dto.setTelefono(c.getTelefono());
        dto.setDocumento(c.getDocumento());
        return dto;
    }
}
