package com.tpi.logistica.ruta;

import com.tpi.logistica.dto.RutaDto;
import com.tpi.logistica.dto.mapper.RutaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rutas")
@RequiredArgsConstructor
public class RutaController {

    private final RutaService rutaService;

    @GetMapping
    public List<RutaDto> findAll() {
        return rutaService.findAll()
                .stream()
                .map(RutaMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public RutaDto findById(@PathVariable Long id) {
        return RutaMapper.toDto(rutaService.findById(id));
    }

    @PostMapping
    public RutaDto create(@RequestBody Ruta ruta) {
        return RutaMapper.toDto(rutaService.create(ruta));
    }

    @PutMapping("/{id}")
    public RutaDto update(@PathVariable Long id, @RequestBody Ruta ruta) {
        return RutaMapper.toDto(rutaService.update(id, ruta));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        rutaService.delete(id);
    }
}
