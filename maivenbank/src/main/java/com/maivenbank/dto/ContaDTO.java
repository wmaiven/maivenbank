package com.maivenbank.dto;

import com.maivenbank.model.TipoConta;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContaDTO {
    
    private Long id;
    
    private String numero;
    
    @NotBlank(message = "Agência é obrigatória")
    private String agencia;
    
    @NotNull(message = "Tipo de conta é obrigatório")
    private TipoConta tipo;
    
    private BigDecimal saldo;
    
    private boolean ativa;
    
    @NotNull(message = "Cliente é obrigatório")
    private Long clienteId;
} 