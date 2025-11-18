package com.tpi.logistica.contenedor;

import com.tpi.logistica.dto.ContenedorDto;
import com.tpi.logistica.dto.mapper.ContenedorMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contenedores")
@RequiredArgsConstructor
public class ContenedorController {

    private final ContenedorService contenedorService;

    @GetMapping
    public List<ContenedorDto> findAll() {
        return contenedorService.findAll()
                .stream()
                .map(ContenedorMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ContenedorDto findById(@PathVariable Long id) {
        return ContenedorMapper.toDto(contenedorService.findById(id));
    }

    @PostMapping
    public ContenedorDto create(@RequestBody Contenedor contenedor) {
        return ContenedorMapper.toDto(contenedorService.create(contenedor));
    }

    @PutMapping("/{id}")
    public ContenedorDto update(@PathVariable Long id, @RequestBody Contenedor contenedor) {
        return ContenedorMapper.toDto(contenedorService.update(id, contenedor));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        contenedorService.delete(id);
    }
}
