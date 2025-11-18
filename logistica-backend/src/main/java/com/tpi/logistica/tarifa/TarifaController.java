package com.tpi.logistica.tarifa;

import com.tpi.logistica.dto.TarifaDto;
import com.tpi.logistica.dto.mapper.TarifaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tarifas")
@RequiredArgsConstructor
public class TarifaController {

    private final TarifaService tarifaService;

    @GetMapping
    public List<TarifaDto> findAll() {
        return tarifaService.findAll()
                .stream()
                .map(TarifaMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public TarifaDto findById(@PathVariable Long id) {
        return TarifaMapper.toDto(tarifaService.findById(id));
    }

    @PostMapping
    public TarifaDto create(@RequestBody Tarifa tarifa) {
        return TarifaMapper.toDto(tarifaService.create(tarifa));
    }

    @PutMapping("/{id}")
    public TarifaDto update(@PathVariable Long id, @RequestBody Tarifa tarifa) {
        return TarifaMapper.toDto(tarifaService.update(id, tarifa));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        tarifaService.delete(id);
    }
}
