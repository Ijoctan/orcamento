package com.igo.orcamento.controller;

import com.igo.orcamento.app.exception.OrcamentoException;
import com.igo.orcamento.domain.model.*;
import com.igo.orcamento.infra.repository.*;
import com.igo.orcamento.presentation.dto.*;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
    public List<ItemMedicaoResponse> listar(@PathVariable Long orcamentoId,
                                    @PathVariable Long medicaoId) {

        Medicao medicao = medicaoRepository.findById(medicaoId)
                .filter(m -> m.getOrcamento().getId().equals(orcamentoId))
                .orElseThrow(() -> new OrcamentoException("Medição não encontrada"));

        return itemMedicaoRepository.findByMedicaoId(medicao.getId())
                .stream()
                .map(this::converterParaResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{itemMedicaoId}")
    public ItemMedicaoResponse buscarPorId(@PathVariable Long orcamentoId,
                                   @PathVariable Long medicaoId,
                                   @PathVariable Long itemMedicaoId) {

        medicaoRepository.findById(medicaoId)
                .filter(m -> m.getOrcamento().getId().equals(orcamentoId))
                .orElseThrow(() -> new OrcamentoException("Medição não encontrada"));

        ItemMedicao itemMedicao = itemMedicaoRepository.findById(itemMedicaoId)
                .orElseThrow(() -> new OrcamentoException("Item da medição não encontrado"));
        
        return converterParaResponse(itemMedicao);
    }

    private ItemMedicaoResponse converterParaResponse(ItemMedicao itemMedicao) {
        ItemOrcamento itemOrcamento = itemMedicao.getItemOrcamento();
        
        ItemOrcamentoDetalhado itemDetalhado = new ItemOrcamentoDetalhado(
                itemOrcamento.getId(),
                itemOrcamento.getDescricao(),
                itemOrcamento.getQuantidade(),
                itemOrcamento.getValorUnitario(),
                itemOrcamento.getValorTotal(),
                itemOrcamento.getQuantidadeAcumulada()
        );

        return new ItemMedicaoResponse(
                itemMedicao.getId(),
                itemMedicao.getQuantidadeMedida(),
                itemMedicao.getValorTotalMedido(),
                itemDetalhado
        );
    }
}
