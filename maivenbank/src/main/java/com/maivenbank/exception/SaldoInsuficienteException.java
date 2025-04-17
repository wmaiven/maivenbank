package com.maivenbank.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SaldoInsuficienteException extends RuntimeException {
    
    public SaldoInsuficienteException(String mensagem) {
        super(mensagem);
    }
    
    public SaldoInsuficienteException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
} 