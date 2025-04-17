package com.maivenbank.service.impl;

import com.maivenbank.dto.ClienteDTO;
import com.maivenbank.exception.RecursoJaExisteException;
import com.maivenbank.exception.RecursoNaoEncontradoException;
import com.maivenbank.model.Cliente;
import com.maivenbank.repository.ClienteRepository;
import com.maivenbank.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    @Override
    public List<ClienteDTO> listarTodos() {
        return clienteRepository.findAll().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ClienteDTO buscarPorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado com o ID: " + id));
        return converterParaDTO(cliente);
    }

    @Override
    public ClienteDTO buscarPorCpf(String cpf) {
        Cliente cliente = clienteRepository.findByCpf(cpf)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado com o CPF: " + cpf));
        return converterParaDTO(cliente);
    }

    @Override
    public ClienteDTO buscarPorEmail(String email) {
        Cliente cliente = clienteRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado com o email: " + email));
        return converterParaDTO(cliente);
    }

    @Override
    @Transactional
    public ClienteDTO criar(ClienteDTO clienteDTO) {
        // Verifica se já existe cliente com o mesmo CPF
        if (clienteRepository.existsByCpf(clienteDTO.getCpf())) {
            throw new RecursoJaExisteException("Já existe um cliente com o CPF: " + clienteDTO.getCpf());
        }
        
        // Verifica se já existe cliente com o mesmo email
        if (clienteRepository.existsByEmail(clienteDTO.getEmail())) {
            throw new RecursoJaExisteException("Já existe um cliente com o email: " + clienteDTO.getEmail());
        }
        
        Cliente cliente = converterParaEntidade(clienteDTO);
        cliente = clienteRepository.save(cliente);
        return converterParaDTO(cliente);
    }

    @Override
    @Transactional
    public ClienteDTO atualizar(Long id, ClienteDTO clienteDTO) {
        // Verifica se o cliente existe
        Cliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado com o ID: " + id));
        
        // Verifica se já existe outro cliente com o mesmo CPF
        if (!clienteExistente.getCpf().equals(clienteDTO.getCpf()) && 
                clienteRepository.existsByCpf(clienteDTO.getCpf())) {
            throw new RecursoJaExisteException("Já existe um cliente com o CPF: " + clienteDTO.getCpf());
        }
        
        // Verifica se já existe outro cliente com o mesmo email
        if (!clienteExistente.getEmail().equals(clienteDTO.getEmail()) && 
                clienteRepository.existsByEmail(clienteDTO.getEmail())) {
            throw new RecursoJaExisteException("Já existe um cliente com o email: " + clienteDTO.getEmail());
        }
        
        // Atualiza os dados do cliente
        clienteExistente.setNome(clienteDTO.getNome());
        clienteExistente.setCpf(clienteDTO.getCpf());
        clienteExistente.setEmail(clienteDTO.getEmail());
        clienteExistente.setTelefone(clienteDTO.getTelefone());
        
        clienteExistente = clienteRepository.save(clienteExistente);
        return converterParaDTO(clienteExistente);
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Cliente não encontrado com o ID: " + id);
        }
        clienteRepository.deleteById(id);
    }

    @Override
    public boolean existeCpf(String cpf) {
        return clienteRepository.existsByCpf(cpf);
    }

    @Override
    public boolean existeEmail(String email) {
        return clienteRepository.existsByEmail(email);
    }
    
    // Métodos auxiliares de conversão entre entidade e DTO
    private ClienteDTO converterParaDTO(Cliente cliente) {
        ClienteDTO dto = new ClienteDTO();
        dto.setId(cliente.getId());
        dto.setNome(cliente.getNome());
        dto.setCpf(cliente.getCpf());
        dto.setEmail(cliente.getEmail());
        dto.setTelefone(cliente.getTelefone());
        return dto;
    }
    
    private Cliente converterParaEntidade(ClienteDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setId(dto.getId());
        cliente.setNome(dto.getNome());
        cliente.setCpf(dto.getCpf());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefone(dto.getTelefone());
        return cliente;
    }
} 