package com.igo.orcamento.presentation.handler;

import com.igo.orcamento.app.exception.OrcamentoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OrcamentoException.class)
    public ResponseEntity<String> tratarRegraNegocio(OrcamentoException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }
}
