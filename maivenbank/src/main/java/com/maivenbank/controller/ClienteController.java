package com.maivenbank.controller;

import com.maivenbank.dto.ClienteDTO;
import com.maivenbank.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {
    
    private final ClienteService clienteService;
    
    @GetMapping
    public ResponseEntity<List<ClienteDTO>> listarTodos() {
        return ResponseEntity.ok(clienteService.listarTodos());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.buscarPorId(id));
    }
    
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<ClienteDTO> buscarPorCpf(@PathVariable String cpf) {
        return ResponseEntity.ok(clienteService.buscarPorCpf(cpf));
    }
    
    @GetMapping("/email/{email}")
    public ResponseEntity<ClienteDTO> buscarPorEmail(@PathVariable String email) {
        return ResponseEntity.ok(clienteService.buscarPorEmail(email));
    }
    
    @PostMapping
    public ResponseEntity<ClienteDTO> criar(@Valid @RequestBody ClienteDTO clienteDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.criar(clienteDTO));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> atualizar(@PathVariable Long id, @Valid @RequestBody ClienteDTO clienteDTO) {
        return ResponseEntity.ok(clienteService.atualizar(id, clienteDTO));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        clienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/existe/cpf/{cpf}")
    public ResponseEntity<Boolean> existeCpf(@PathVariable String cpf) {
        return ResponseEntity.ok(clienteService.existeCpf(cpf));
    }
    
    @GetMapping("/existe/email/{email}")
    public ResponseEntity<Boolean> existeEmail(@PathVariable String email) {
        return ResponseEntity.ok(clienteService.existeEmail(email));
    }
} 