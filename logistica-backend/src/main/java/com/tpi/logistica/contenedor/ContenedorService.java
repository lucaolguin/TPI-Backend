package com.tpi.logistica.contenedor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContenedorService {

    private final ContenedorRepository contenedorRepository;

    public List<Contenedor> findAll() {
        return contenedorRepository.findAll();
    }

    public Contenedor findById(Long id) {
        return contenedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contenedor no encontrado"));
    }

    public Contenedor create(Contenedor c) {
        return contenedorRepository.save(c);
    }

    public Contenedor update(Long id, Contenedor c) {
        Contenedor existente = findById(id);
        c.setId(existente.getId());
        return contenedorRepository.save(c);
    }

    public void delete(Long id) {
        contenedorRepository.deleteById(id);
    }
}
