package com.tpi.logistica.cliente;

import com.tpi.logistica.dto.ClienteDto;
import com.tpi.logistica.dto.mapper.ClienteMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping
    public List<ClienteDto> findAll() {
        return clienteService.findAll()
                .stream()
                .map(ClienteMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ClienteDto findById(@PathVariable Long id) {
        return ClienteMapper.toDto(clienteService.findById(id));
    }

    @PostMapping
    public ClienteDto create(@RequestBody Cliente cliente) {
        return ClienteMapper.toDto(clienteService.create(cliente));
    }

    @PutMapping("/{id}")
    public ClienteDto update(@PathVariable Long id, @RequestBody Cliente cliente) {
        return ClienteMapper.toDto(clienteService.update(id, cliente));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        clienteService.delete(id);
    }
}
