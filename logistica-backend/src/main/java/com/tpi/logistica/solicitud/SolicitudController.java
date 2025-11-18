package com.tpi.logistica.solicitud;

import com.tpi.logistica.dto.SolicitudDto;
import com.tpi.logistica.dto.RutaDto;
import com.tpi.logistica.dto.mapper.RutaMapper;
import com.tpi.logistica.dto.solicitud.SolicitudCreateDto;
import com.tpi.logistica.dto.mapper.SolicitudMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/solicitudes")
@RequiredArgsConstructor
public class SolicitudController {

    private final SolicitudService solicitudService;

    @PostMapping
    public SolicitudDto crear(@RequestBody SolicitudCreateDto dto) {
        return SolicitudMapper.toDto(solicitudService.crearSolicitud(dto));
    }

    @GetMapping
    public List<SolicitudDto> findAll() {
        return solicitudService.findAll()
                .stream()
                .map(SolicitudMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public SolicitudDto findById(@PathVariable Long id) {
        return SolicitudMapper.toDto(solicitudService.findById(id));
    }

    @PostMapping("/{id}/ruta")
    public RutaDto asignarRuta(@PathVariable Long id) {
        return RutaMapper.toDto(solicitudService.asignarRuta(id));
    }
}
