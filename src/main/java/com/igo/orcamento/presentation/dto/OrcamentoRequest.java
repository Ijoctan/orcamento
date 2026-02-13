package com.igo.orcamento.presentation.dto;

import java.math.BigDecimal;

public record OrcamentoRequest(
        String tipoOrcamento,
        BigDecimal valorTotal
) {}
