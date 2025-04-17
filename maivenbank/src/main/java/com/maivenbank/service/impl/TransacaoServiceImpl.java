package com.maivenbank.service.impl;

import com.maivenbank.dto.TransacaoDTO;
import com.maivenbank.exception.RecursoNaoEncontradoException;
import com.maivenbank.model.Conta;
import com.maivenbank.model.TipoTransacao;
import com.maivenbank.model.Transacao;
import com.maivenbank.repository.ContaRepository;
import com.maivenbank.repository.TransacaoRepository;
import com.maivenbank.service.TransacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransacaoServiceImpl implements TransacaoService {

    private final TransacaoRepository transacaoRepository;
    private final ContaRepository contaRepository;

    @Override
    public List<TransacaoDTO> listarTodas() {
        return transacaoRepository.findAll().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransacaoDTO> listarPorConta(Long contaId) {
        return transacaoRepository.findByContaId(contaId).stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransacaoDTO> listarPorNumeroConta(String numeroConta) {
        return transacaoRepository.findByContaNumero(numeroConta).stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransacaoDTO> listarPorContaETipo(Long contaId, TipoTransacao tipo) {
        return transacaoRepository.findByContaIdAndTipo(contaId, tipo).stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<TransacaoDTO> listarPorContaEPeriodo(Long contaId, LocalDateTime inicio, 
                                                  LocalDateTime fim, Pageable pageable) {
        Page<Transacao> transacoes = transacaoRepository.findByContaIdAndDataHoraBetween(
                contaId, inicio, fim, pageable);
        
        List<TransacaoDTO> transacoesDTO = transacoes.getContent().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
        
        return new PageImpl<>(transacoesDTO, pageable, transacoes.getTotalElements());
    }

    @Override
    public TransacaoDTO buscarPorId(Long id) {
        Transacao transacao = transacaoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Transação não encontrada com o ID: " + id));
        
        return converterParaDTO(transacao);
    }

    @Override
    @Transactional
    public TransacaoDTO registrar(TransacaoDTO transacaoDTO) {
        // Verifica se a conta existe
        Conta conta = contaRepository.findById(transacaoDTO.getContaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Conta não encontrada com o ID: " + transacaoDTO.getContaId()));
        
        // Verifica conta de destino, se for uma transferência
        Conta contaDestino = null;
        if (transacaoDTO.getTipo() == TipoTransacao.TRANSFERENCIA && transacaoDTO.getContaDestinoId() != null) {
            contaDestino = contaRepository.findById(transacaoDTO.getContaDestinoId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Conta de destino não encontrada com o ID: " + transacaoDTO.getContaDestinoId()));
        }
        
        // Cria a transação
        Transacao transacao = converterParaEntidade(transacaoDTO);
        transacao.setConta(conta);
        transacao.setContaDestino(contaDestino);
        transacao.setDataHora(LocalDateTime.now());
        
        transacao = transacaoRepository.save(transacao);
        return converterParaDTO(transacao);
    }
    
    // Métodos auxiliares
    
    private TransacaoDTO converterParaDTO(Transacao transacao) {
        TransacaoDTO dto = new TransacaoDTO();
        dto.setId(transacao.getId());
        dto.setTipo(transacao.getTipo());
        dto.setValor(transacao.getValor());
        dto.setDataHora(transacao.getDataHora());
        dto.setDescricao(transacao.getDescricao());
        dto.setContaId(transacao.getConta().getId());
        
        if (transacao.getContaDestino() != null) {
            dto.setContaDestinoId(transacao.getContaDestino().getId());
        }
        
        return dto;
    }
    
    private Transacao converterParaEntidade(TransacaoDTO dto) {
        Transacao transacao = new Transacao();
        transacao.setId(dto.getId());
        transacao.setTipo(dto.getTipo());
        transacao.setValor(dto.getValor());
        transacao.setDataHora(dto.getDataHora());
        transacao.setDescricao(dto.getDescricao());
        
        return transacao;
    }
} 