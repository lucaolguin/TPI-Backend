package com.tpi.logistica.camion;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CamionService {

    private final CamionRepository camionRepository;

    public List<Camion> findAll() {
        return camionRepository.findAll();
    }

    public Camion findById(Long id) {
        return camionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Camión no encontrado"));
    }

    public Camion create(Camion camion) {
        return camionRepository.save(camion);
    }

    public Camion update(Long id, Camion camion) {
        Camion existente = findById(id);
        camion.setId(existente.getId());
        return camionRepository.save(camion);
    }

    public void delete(Long id) {
        camionRepository.deleteById(id);
    }
}
