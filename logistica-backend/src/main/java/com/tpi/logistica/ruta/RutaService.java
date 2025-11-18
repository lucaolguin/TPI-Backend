package com.tpi.logistica.ruta;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RutaService {

    private final RutaRepository rutaRepository;

    public List<Ruta> findAll() {
        return rutaRepository.findAll();
    }

    public Ruta findById(Long id) {
        return rutaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ruta no encontrada"));
    }

    public Ruta create(Ruta ruta) {
        return rutaRepository.save(ruta);
    }

    public Ruta update(Long id, Ruta ruta) {
        Ruta existente = findById(id);
        ruta.setId(existente.getId());
        return rutaRepository.save(ruta);
    }

    public void delete(Long id) {
        rutaRepository.deleteById(id);
    }
}
