package com.tpi.logistica.deposito;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepositoService {

    private final DepositoRepository depositoRepository;

    public List<Deposito> findAll() {
        return depositoRepository.findAll();
    }

    public Deposito findById(Long id) {
        return depositoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Depósito no encontrado"));
    }

    public Deposito create(Deposito deposito) {
        return depositoRepository.save(deposito);
    }

    public Deposito update(Long id, Deposito deposito) {
        Deposito existente = findById(id);
        deposito.setId(existente.getId());
        return depositoRepository.save(deposito);
    }

    public void delete(Long id) {
        depositoRepository.deleteById(id);
    }
}
