package com.maivenbank.config;

import com.maivenbank.exception.RecursoJaExisteException;
import com.maivenbank.exception.RecursoNaoEncontradoException;
import com.maivenbank.exception.SaldoInsuficienteException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ApiError> handleRecursoNaoEncontradoException(RecursoNaoEncontradoException ex) {
        ApiError error = new ApiError(
                HttpStatus.NOT_FOUND.value(),
                "Recurso não encontrado",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(RecursoJaExisteException.class)
    public ResponseEntity<ApiError> handleRecursoJaExisteException(RecursoJaExisteException ex) {
        ApiError error = new ApiError(
                HttpStatus.CONFLICT.value(),
                "Recurso já existe",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(SaldoInsuficienteException.class)
    public ResponseEntity<ApiError> handleSaldoInsuficienteException(SaldoInsuficienteException ex) {
        ApiError error = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                "Saldo insuficiente",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgumentException(IllegalArgumentException ex) {
        ApiError error = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                "Argumento inválido",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiError> handleIllegalStateException(IllegalStateException ex) {
        ApiError error = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                "Estado inválido",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationError> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<String> errorMessages = result.getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        ValidationError error = new ValidationError(
                HttpStatus.BAD_REQUEST.value(),
                "Erro de validação",
                "Campos inválidos na requisição",
                LocalDateTime.now(),
                errorMessages
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(Exception ex) {
        ApiError error = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Erro interno do servidor",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @Getter
    @AllArgsConstructor
    public static class ApiError {
        private int status;
        private String title;
        private String detail;
        private LocalDateTime timestamp;
    }

    @Getter
    public static class ValidationError extends ApiError {
        private final List<String> errors;

        public ValidationError(int status, String title, String detail, LocalDateTime timestamp, List<String> errors) {
            super(status, title, detail, timestamp);
            this.errors = errors;
        }
    }
} 