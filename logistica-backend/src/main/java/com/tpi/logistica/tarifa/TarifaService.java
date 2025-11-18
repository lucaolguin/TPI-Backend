package com.tpi.logistica.tarifa;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TarifaService {

    private final TarifaRepository tarifaRepository;

    public List<Tarifa> findAll() {
        return tarifaRepository.findAll();
    }

    public Tarifa findById(Long id) {
        return tarifaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarifa no encontrada"));
    }

    public Tarifa create(Tarifa tarifa) {
        return tarifaRepository.save(tarifa);
    }

    public Tarifa update(Long id, Tarifa tarifa) {
        Tarifa existente = findById(id);
        tarifa.setId(existente.getId());
        return tarifaRepository.save(tarifa);
    }

    public void delete(Long id) {
        tarifaRepository.deleteById(id);
    }
}
