package com.igo.orcamento.presentation.dto;

import com.igo.orcamento.domain.enums.StatusMedicao;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record MedicaoResponse(
        Long id,
        String numeroMedicao,
        LocalDate dataMedicao,
        BigDecimal valorTotal,
        StatusMedicao status,
        String observacao,
        List<ItemMedicaoResponse> itens
) {}
