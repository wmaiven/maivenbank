package com.maivenbank.repository;

import com.maivenbank.model.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {
    
    Optional<Conta> findByNumero(String numero);
    
    List<Conta> findByClienteId(Long clienteId);
    
    List<Conta> findByClienteCpf(String cpf);
    
    boolean existsByNumero(String numero);
} 