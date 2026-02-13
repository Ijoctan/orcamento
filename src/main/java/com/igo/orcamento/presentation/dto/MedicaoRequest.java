package com.igo.orcamento.presentation.dto;

import java.util.List;

public record MedicaoRequest(
        String observacao,
        List<ItemMedicaoRequest> itens
) {}
