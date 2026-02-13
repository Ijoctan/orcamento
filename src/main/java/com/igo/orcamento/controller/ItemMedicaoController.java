package com.igo.orcamento.presentation.controller;

import com.igo.orcamento.app.exception.OrcamentoException;
import com.igo.orcamento.domain.model.*;
import com.igo.orcamento.infra.repository.*;
import com.igo.orcamento.presentation.dto.*;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orcamentos/{orcamentoId}/medicoes/{medicaoId}/itens")
public class ItemMedicaoController {

    private final MedicaoRepository medicaoRepository;
    private final ItemMedicaoRepository itemMedicaoRepository;

    public ItemMedicaoController(MedicaoRepository medicaoRepository,
                                 ItemMedicaoRepository itemMedicaoRepository) {
        this.medicaoRepository = medicaoRepository;
        this.itemMedicaoRepository = itemMedicaoRepository;
    }

    @GetMapping
    public List<ItemMedicao> listar(@PathVariable Long orcamentoId,
                                    @PathVariable Long medicaoId) {

        Medicao medicao = medicaoRepository.findById(medicaoId)
                .filter(m -> m.getOrcamento().getId().equals(orcamentoId))
                .orElseThrow(() -> new OrcamentoException("Medição não encontrada"));

        return itemMedicaoRepository.findByMedicaoId(medicao.getId());
    }

    @GetMapping("/{itemMedicaoId}")
    public ItemMedicao buscarPorId(@PathVariable Long orcamentoId,
                                   @PathVariable Long medicaoId,
                                   @PathVariable Long itemMedicaoId) {

        medicaoRepository.findById(medicaoId)
                .filter(m -> m.getOrcamento().getId().equals(orcamentoId))
                .orElseThrow(() -> new OrcamentoException("Medição não encontrada"));

        return itemMedicaoRepository.findById(itemMedicaoId)
                .orElseThrow(() -> new OrcamentoException("Item da medição não encontrado"));
    }
}
