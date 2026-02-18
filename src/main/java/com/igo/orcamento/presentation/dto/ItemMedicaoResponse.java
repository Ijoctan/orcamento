package com.igo.orcamento.presentation.dto;

import java.math.BigDecimal;

public record ItemMedicaoResponse(
        Long id,
        BigDecimal quantidadeMedida,
        BigDecimal valorTotalMedido,
        ItemOrcamentoDetalhado itemOrcamento
) {}
