package com.tpi.logistica.tramo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TramoService {

    private final TramoRepository tramoRepository;

    public List<Tramo> findAll() {
        return tramoRepository.findAll();
    }

    public Tramo findById(Long id) {
        return tramoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tramo no encontrado"));
    }

    public Tramo create(Tramo tramo) {
        return tramoRepository.save(tramo);
    }

    public Tramo update(Long id, Tramo tramo) {
        Tramo existente = findById(id);
        tramo.setId(existente.getId());
        return tramoRepository.save(tramo);
    }

    public void delete(Long id) {
        tramoRepository.deleteById(id);
    }
}
