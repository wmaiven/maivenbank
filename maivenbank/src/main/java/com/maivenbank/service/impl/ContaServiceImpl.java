package com.maivenbank.service.impl;

import com.maivenbank.dto.ContaDTO;
import com.maivenbank.exception.RecursoJaExisteException;
import com.maivenbank.exception.RecursoNaoEncontradoException;
import com.maivenbank.exception.SaldoInsuficienteException;
import com.maivenbank.model.Cliente;
import com.maivenbank.model.Conta;
import com.maivenbank.model.TipoTransacao;
import com.maivenbank.model.Transacao;
import com.maivenbank.repository.ClienteRepository;
import com.maivenbank.repository.ContaRepository;
import com.maivenbank.repository.TransacaoRepository;
import com.maivenbank.service.ContaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContaServiceImpl implements ContaService {

    private final ContaRepository contaRepository;
    private final ClienteRepository clienteRepository;
    private final TransacaoRepository transacaoRepository;
    private final Random random = new Random();

    @Override
    public List<ContaDTO> listarTodas() {
        return contaRepository.findAll().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ContaDTO> listarPorCliente(Long clienteId) {
        return contaRepository.findByClienteId(clienteId).stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ContaDTO> listarPorCpf(String cpf) {
        return contaRepository.findByClienteCpf(cpf).stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ContaDTO buscarPorId(Long id) {
        Conta conta = contaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Conta não encontrada com o ID: " + id));
        return converterParaDTO(conta);
    }

    @Override
    public ContaDTO buscarPorNumero(String numero) {
        Conta conta = contaRepository.findByNumero(numero)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Conta não encontrada com o número: " + numero));
        return converterParaDTO(conta);
    }

    @Override
    @Transactional
    public ContaDTO criar(ContaDTO contaDTO) {
        // Verifica se o cliente existe
        Cliente cliente = clienteRepository.findById(contaDTO.getClienteId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado com o ID: " + contaDTO.getClienteId()));
        
        // Gera número único para a conta
        String numeroConta;
        do {
            numeroConta = gerarNumeroConta();
        } while (contaRepository.existsByNumero(numeroConta));
        
        // Cria a conta
        Conta conta = converterParaEntidade(contaDTO);
        conta.setNumero(numeroConta);
        conta.setCliente(cliente);
        conta.setSaldo(BigDecimal.ZERO);
        conta.setDataCriacao(LocalDateTime.now());
        conta.setAtiva(true);
        
        conta = contaRepository.save(conta);
        return converterParaDTO(conta);
    }

    @Override
    @Transactional
    public ContaDTO atualizar(Long id, ContaDTO contaDTO) {
        // Verifica se a conta existe
        Conta contaExistente = contaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Conta não encontrada com o ID: " + id));
        
        // Não permite alterar o cliente da conta
        if (!contaExistente.getCliente().getId().equals(contaDTO.getClienteId())) {
            throw new IllegalArgumentException("Não é permitido alterar o cliente da conta");
        }
        
        // Atualiza os dados da conta
        contaExistente.setAgencia(contaDTO.getAgencia());
        contaExistente.setTipo(contaDTO.getTipo());
        contaExistente.setAtiva(contaDTO.isAtiva());
        
        contaExistente = contaRepository.save(contaExistente);
        return converterParaDTO(contaExistente);
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        // Verifica se a conta existe
        Conta conta = contaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Conta não encontrada com o ID: " + id));
        
        // Verifica se a conta tem saldo
        if (conta.getSaldo().compareTo(BigDecimal.ZERO) > 0) {
            throw new IllegalStateException("Não é possível excluir uma conta com saldo. Saldo atual: " + conta.getSaldo());
        }
        
        contaRepository.deleteById(id);
    }

    @Override
    @Transactional
    public ContaDTO depositar(String numeroConta, BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor do depósito deve ser maior que zero");
        }
        
        // Localiza a conta
        Conta conta = contaRepository.findByNumero(numeroConta)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Conta não encontrada com o número: " + numeroConta));
        
        // Verifica se a conta está ativa
        if (!conta.isAtiva()) {
            throw new IllegalStateException("Não é possível realizar depósito em uma conta inativa");
        }
        
        // Atualiza o saldo
        conta.setSaldo(conta.getSaldo().add(valor));
        conta = contaRepository.save(conta);
        
        // Registra a transação
        Transacao transacao = new Transacao();
        transacao.setTipo(TipoTransacao.DEPOSITO);
        transacao.setValor(valor);
        transacao.setDataHora(LocalDateTime.now());
        transacao.setDescricao("Depósito em conta");
        transacao.setConta(conta);
        
        transacaoRepository.save(transacao);
        
        return converterParaDTO(conta);
    }

    @Override
    @Transactional
    public ContaDTO sacar(String numeroConta, BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor do saque deve ser maior que zero");
        }
        
        // Localiza a conta
        Conta conta = contaRepository.findByNumero(numeroConta)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Conta não encontrada com o número: " + numeroConta));
        
        // Verifica se a conta está ativa
        if (!conta.isAtiva()) {
            throw new IllegalStateException("Não é possível realizar saque em uma conta inativa");
        }
        
        // Verifica se há saldo suficiente
        if (conta.getSaldo().compareTo(valor) < 0) {
            throw new SaldoInsuficienteException("Saldo insuficiente para realizar o saque. Saldo atual: " + conta.getSaldo());
        }
        
        // Atualiza o saldo
        conta.setSaldo(conta.getSaldo().subtract(valor));
        conta = contaRepository.save(conta);
        
        // Registra a transação
        Transacao transacao = new Transacao();
        transacao.setTipo(TipoTransacao.SAQUE);
        transacao.setValor(valor);
        transacao.setDataHora(LocalDateTime.now());
        transacao.setDescricao("Saque em conta");
        transacao.setConta(conta);
        
        transacaoRepository.save(transacao);
        
        return converterParaDTO(conta);
    }

    @Override
    @Transactional
    public void transferir(String numeroContaOrigem, String numeroContaDestino, BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor da transferência deve ser maior que zero");
        }
        
        if (numeroContaOrigem.equals(numeroContaDestino)) {
            throw new IllegalArgumentException("Não é possível transferir para a mesma conta");
        }
        
        // Localiza as contas
        Conta contaOrigem = contaRepository.findByNumero(numeroContaOrigem)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Conta de origem não encontrada com o número: " + numeroContaOrigem));
        
        Conta contaDestino = contaRepository.findByNumero(numeroContaDestino)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Conta de destino não encontrada com o número: " + numeroContaDestino));
        
        // Verifica se as contas estão ativas
        if (!contaOrigem.isAtiva()) {
            throw new IllegalStateException("Não é possível realizar transferência de uma conta inativa");
        }
        
        if (!contaDestino.isAtiva()) {
            throw new IllegalStateException("Não é possível realizar transferência para uma conta inativa");
        }
        
        // Verifica se há saldo suficiente
        if (contaOrigem.getSaldo().compareTo(valor) < 0) {
            throw new SaldoInsuficienteException("Saldo insuficiente para realizar a transferência. Saldo atual: " + contaOrigem.getSaldo());
        }
        
        // Atualiza os saldos
        contaOrigem.setSaldo(contaOrigem.getSaldo().subtract(valor));
        contaDestino.setSaldo(contaDestino.getSaldo().add(valor));
        
        contaRepository.save(contaOrigem);
        contaRepository.save(contaDestino);
        
        // Registra a transação de saída
        Transacao transacaoSaida = new Transacao();
        transacaoSaida.setTipo(TipoTransacao.TRANSFERENCIA);
        transacaoSaida.setValor(valor);
        transacaoSaida.setDataHora(LocalDateTime.now());
        transacaoSaida.setDescricao("Transferência para conta " + numeroContaDestino);
        transacaoSaida.setConta(contaOrigem);
        transacaoSaida.setContaDestino(contaDestino);
        
        transacaoRepository.save(transacaoSaida);
    }

    @Override
    @Transactional
    public void ativarConta(Long id) {
        Conta conta = contaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Conta não encontrada com o ID: " + id));
        
        conta.setAtiva(true);
        contaRepository.save(conta);
    }

    @Override
    @Transactional
    public void desativarConta(Long id) {
        Conta conta = contaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Conta não encontrada com o ID: " + id));
        
        conta.setAtiva(false);
        contaRepository.save(conta);
    }
    
    // Métodos auxiliares
    
    private String gerarNumeroConta() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
    
    private ContaDTO converterParaDTO(Conta conta) {
        ContaDTO dto = new ContaDTO();
        dto.setId(conta.getId());
        dto.setNumero(conta.getNumero());
        dto.setAgencia(conta.getAgencia());
        dto.setTipo(conta.getTipo());
        dto.setSaldo(conta.getSaldo());
        dto.setAtiva(conta.isAtiva());
        dto.setClienteId(conta.getCliente().getId());
        return dto;
    }
    
    private Conta converterParaEntidade(ContaDTO dto) {
        Conta conta = new Conta();
        conta.setId(dto.getId());
        conta.setNumero(dto.getNumero());
        conta.setAgencia(dto.getAgencia());
        conta.setTipo(dto.getTipo());
        conta.setSaldo(dto.getSaldo());
        conta.setAtiva(dto.isAtiva());
        
        if (dto.getClienteId() != null) {
            Cliente cliente = new Cliente();
            cliente.setId(dto.getClienteId());
            conta.setCliente(cliente);
        }
        
        return conta;
    }
} 