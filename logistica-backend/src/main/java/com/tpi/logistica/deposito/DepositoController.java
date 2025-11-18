package com.tpi.logistica.deposito;

import com.tpi.logistica.dto.DepositoDto;
import com.tpi.logistica.dto.mapper.DepositoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/depositos")
@RequiredArgsConstructor
public class DepositoController {

    private final DepositoService depositoService;

    @GetMapping
    public List<DepositoDto> findAll() {
        return depositoService.findAll()
                .stream()
                .map(DepositoMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public DepositoDto findById(@PathVariable Long id) {
        return DepositoMapper.toDto(depositoService.findById(id));
    }

    @PostMapping
    public DepositoDto create(@RequestBody Deposito deposito) {
        return DepositoMapper.toDto(depositoService.create(deposito));
    }

    @PutMapping("/{id}")
    public DepositoDto update(@PathVariable Long id, @RequestBody Deposito deposito) {
        return DepositoMapper.toDto(depositoService.update(id, deposito));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        depositoService.delete(id);
    }
}
