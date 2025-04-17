package com.maivenbank.service;

import com.maivenbank.dto.TransacaoDTO;
import com.maivenbank.model.TipoTransacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface TransacaoService {
    
    List<TransacaoDTO> listarTodas();
    
    List<TransacaoDTO> listarPorConta(Long contaId);
    
    List<TransacaoDTO> listarPorNumeroConta(String numeroConta);
    
    List<TransacaoDTO> listarPorContaETipo(Long contaId, TipoTransacao tipo);
    
    Page<TransacaoDTO> listarPorContaEPeriodo(Long contaId, LocalDateTime inicio, 
                                            LocalDateTime fim, Pageable pageable);
    
    TransacaoDTO buscarPorId(Long id);
    
    TransacaoDTO registrar(TransacaoDTO transacaoDTO);
} 