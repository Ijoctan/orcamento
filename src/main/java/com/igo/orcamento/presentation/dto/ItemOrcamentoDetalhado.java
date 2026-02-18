package com.igo.orcamento.presentation.dto;

import java.math.BigDecimal;

public record ItemOrcamentoDetalhado(
        Long id,
        String descricao,
        BigDecimal quantidade,
        BigDecimal valorUnitario,
        BigDecimal valorTotal,
        BigDecimal quantidadeAcumulada
) {}
