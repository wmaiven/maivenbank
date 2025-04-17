package com.maivenbank.controller;

import com.maivenbank.dto.TransacaoDTO;
import com.maivenbank.model.TipoTransacao;
import com.maivenbank.service.TransacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transacoes")
@RequiredArgsConstructor
public class TransacaoController {
    
    private final TransacaoService transacaoService;
    
    @GetMapping
    public ResponseEntity<List<TransacaoDTO>> listarTodas() {
        return ResponseEntity.ok(transacaoService.listarTodas());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TransacaoDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(transacaoService.buscarPorId(id));
    }
    
    @GetMapping("/conta/{contaId}")
    public ResponseEntity<List<TransacaoDTO>> listarPorConta(@PathVariable Long contaId) {
        return ResponseEntity.ok(transacaoService.listarPorConta(contaId));
    }
    
    @GetMapping("/conta/numero/{numeroConta}")
    public ResponseEntity<List<TransacaoDTO>> listarPorNumeroConta(@PathVariable String numeroConta) {
        return ResponseEntity.ok(transacaoService.listarPorNumeroConta(numeroConta));
    }
    
    @GetMapping("/conta/{contaId}/tipo/{tipo}")
    public ResponseEntity<List<TransacaoDTO>> listarPorContaETipo(
            @PathVariable Long contaId,
            @PathVariable TipoTransacao tipo) {
        return ResponseEntity.ok(transacaoService.listarPorContaETipo(contaId, tipo));
    }
    
    @GetMapping("/conta/{contaId}/periodo")
    public ResponseEntity<Page<TransacaoDTO>> listarPorContaEPeriodo(
            @PathVariable Long contaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim,
            Pageable pageable) {
        return ResponseEntity.ok(transacaoService.listarPorContaEPeriodo(contaId, inicio, fim, pageable));
    }
    
    @PostMapping
    public ResponseEntity<TransacaoDTO> registrar(@Valid @RequestBody TransacaoDTO transacaoDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transacaoService.registrar(transacaoDTO));
    }
} 