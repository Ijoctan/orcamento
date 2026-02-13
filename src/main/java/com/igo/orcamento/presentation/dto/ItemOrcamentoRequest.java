package com.igo.orcamento.presentation.dto;

import java.math.BigDecimal;

public record ItemOrcamentoRequest(
        String descricao,
        BigDecimal quantidade,
        BigDecimal valorUnitario
){}
