package com.igo.orcamento.presentation.dto;

import java.math.BigDecimal;

public record ItemMedicaoRequest(
        Long itemOrcamentoId,
        BigDecimal quantidadeMedida
) {}
