package com.maivenbank.controller;

import com.maivenbank.dto.ContaDTO;
import com.maivenbank.service.ContaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/contas")
@RequiredArgsConstructor
public class ContaController {
    
    private final ContaService contaService;
    
    @GetMapping
    public ResponseEntity<List<ContaDTO>> listarTodas() {
        return ResponseEntity.ok(contaService.listarTodas());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ContaDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(contaService.buscarPorId(id));
    }
    
    @GetMapping("/numero/{numero}")
    public ResponseEntity<ContaDTO> buscarPorNumero(@PathVariable String numero) {
        return ResponseEntity.ok(contaService.buscarPorNumero(numero));
    }
    
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<ContaDTO>> listarPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(contaService.listarPorCliente(clienteId));
    }
    
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<List<ContaDTO>> listarPorCpf(@PathVariable String cpf) {
        return ResponseEntity.ok(contaService.listarPorCpf(cpf));
    }
    
    @PostMapping
    public ResponseEntity<ContaDTO> criar(@Valid @RequestBody ContaDTO contaDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(contaService.criar(contaDTO));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ContaDTO> atualizar(@PathVariable Long id, @Valid @RequestBody ContaDTO contaDTO) {
        return ResponseEntity.ok(contaService.atualizar(id, contaDTO));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        contaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/{numero}/deposito")
    public ResponseEntity<ContaDTO> depositar(
            @PathVariable String numero, 
            @RequestParam BigDecimal valor) {
        return ResponseEntity.ok(contaService.depositar(numero, valor));
    }
    
    @PostMapping("/{numero}/saque")
    public ResponseEntity<ContaDTO> sacar(
            @PathVariable String numero, 
            @RequestParam BigDecimal valor) {
        return ResponseEntity.ok(contaService.sacar(numero, valor));
    }
    
    @PostMapping("/transferencia")
    public ResponseEntity<Void> transferir(
            @RequestParam String numeroOrigem, 
            @RequestParam String numeroDestino, 
            @RequestParam BigDecimal valor) {
        contaService.transferir(numeroOrigem, numeroDestino, valor);
        return ResponseEntity.ok().build();
    }
    
    @PatchMapping("/{id}/ativar")
    public ResponseEntity<Void> ativar(@PathVariable Long id) {
        contaService.ativarConta(id);
        return ResponseEntity.ok().build();
    }
    
    @PatchMapping("/{id}/desativar")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        contaService.desativarConta(id);
        return ResponseEntity.ok().build();
    }
} 