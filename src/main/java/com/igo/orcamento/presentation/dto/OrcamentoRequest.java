package com.igo.orcamento.presentation.dto;

import java.math.BigDecimal;

public record OrcamentoRequest(
        Long tipoOrcamentoId,
        BigDecimal valorTotal
) {}
