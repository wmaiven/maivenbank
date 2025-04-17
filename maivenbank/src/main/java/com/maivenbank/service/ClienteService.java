package com.maivenbank.service;

import com.maivenbank.dto.ClienteDTO;

import java.util.List;

public interface ClienteService {
    
    List<ClienteDTO> listarTodos();
    
    ClienteDTO buscarPorId(Long id);
    
    ClienteDTO buscarPorCpf(String cpf);
    
    ClienteDTO buscarPorEmail(String email);
    
    ClienteDTO criar(ClienteDTO clienteDTO);
    
    ClienteDTO atualizar(Long id, ClienteDTO clienteDTO);
    
    void deletar(Long id);
    
    boolean existeCpf(String cpf);
    
    boolean existeEmail(String email);
} 