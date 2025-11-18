package com.tpi.logistica.camion;

import com.tpi.logistica.dto.CamionDto;
import com.tpi.logistica.dto.mapper.CamionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/camiones")
@RequiredArgsConstructor
public class CamionController {

    private final CamionService camionService;

    @GetMapping
    public List<CamionDto> findAll() {
        return camionService.findAll()
                .stream()
                .map(CamionMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public CamionDto findById(@PathVariable Long id) {
        return CamionMapper.toDto(camionService.findById(id));
    }

    @PostMapping
    public CamionDto create(@RequestBody Camion camion) {
        return CamionMapper.toDto(camionService.create(camion));
    }

    @PutMapping("/{id}")
    public CamionDto update(@PathVariable Long id, @RequestBody Camion camion) {
        return CamionMapper.toDto(camionService.update(id, camion));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        camionService.delete(id);
    }
}
