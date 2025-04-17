package com.maivenbank.dto;

import com.maivenbank.model.TipoTransacao;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransacaoDTO {
    
    private Long id;
    
    @NotNull(message = "Tipo de transação é obrigatório")
    private TipoTransacao tipo;
    
    @Positive(message = "Valor deve ser maior que zero")
    private BigDecimal valor;
    
    private LocalDateTime dataHora;
    
    private String descricao;
    
    @NotNull(message = "Conta é obrigatória")
    private Long contaId;
    
    private Long contaDestinoId;
} 