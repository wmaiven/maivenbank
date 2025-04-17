package com.maivenbank.repository;

import com.maivenbank.model.Transacao;
import com.maivenbank.model.TipoTransacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long> {
    
    List<Transacao> findByContaId(Long contaId);
    
    List<Transacao> findByContaNumero(String numeroConta);
    
    Page<Transacao> findByContaIdAndDataHoraBetween(
            Long contaId, 
            LocalDateTime inicio, 
            LocalDateTime fim, 
            Pageable pageable);
    
    List<Transacao> findByContaIdAndTipo(Long contaId, TipoTransacao tipo);
} 