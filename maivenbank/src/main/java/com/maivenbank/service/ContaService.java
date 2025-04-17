package com.maivenbank.service;

import com.maivenbank.dto.ContaDTO;

import java.math.BigDecimal;
import java.util.List;

public interface ContaService {
    
    List<ContaDTO> listarTodas();
    
    List<ContaDTO> listarPorCliente(Long clienteId);
    
    List<ContaDTO> listarPorCpf(String cpf);
    
    ContaDTO buscarPorId(Long id);
    
    ContaDTO buscarPorNumero(String numero);
    
    ContaDTO criar(ContaDTO contaDTO);
    
    ContaDTO atualizar(Long id, ContaDTO contaDTO);
    
    void deletar(Long id);
    
    ContaDTO depositar(String numeroConta, BigDecimal valor);
    
    ContaDTO sacar(String numeroConta, BigDecimal valor);
    
    void transferir(String numeroContaOrigem, String numeroContaDestino, BigDecimal valor);
    
    void ativarConta(Long id);
    
    void desativarConta(Long id);
} 