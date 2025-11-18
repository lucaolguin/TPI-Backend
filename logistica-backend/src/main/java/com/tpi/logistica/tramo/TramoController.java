package com.tpi.logistica.tramo;

import com.tpi.logistica.dto.TramoDto;
import com.tpi.logistica.dto.mapper.TramoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tramos")
@RequiredArgsConstructor
public class TramoController {

    private final TramoService tramoService;

    @GetMapping
    public List<TramoDto> findAll() {
        return tramoService.findAll()
                .stream()
                .map(TramoMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public TramoDto findById(@PathVariable Long id) {
        return TramoMapper.toDto(tramoService.findById(id));
    }

    @PostMapping
    public TramoDto create(@RequestBody Tramo tramo) {
        return TramoMapper.toDto(tramoService.create(tramo));
    }

    @PutMapping("/{id}")
    public TramoDto update(@PathVariable Long id, @RequestBody Tramo tramo) {
        return TramoMapper.toDto(tramoService.update(id, tramo));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        tramoService.delete(id);
    }
}
