package com.igo.orcamento.presentation.controller;

import com.igo.orcamento.app.exception.OrcamentoException;
import com.igo.orcamento.domain.enums.StatusMedicao;
import com.igo.orcamento.domain.enums.StatusOrcamento;
import com.igo.orcamento.domain.model.*;
import com.igo.orcamento.infra.repository.*;
import com.igo.orcamento.presentation.dto.*;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orcamentos/{orcamentoId}/medicoes")
public class MedicaoController {

    private final OrcamentoRepository orcamentoRepository;
    private final MedicaoRepository medicaoRepository;
    private final ItemOrcamentoRepository itemOrcamentoRepository;
    private final ItemMedicaoRepository itemMedicaoRepository;

    public MedicaoController(
            OrcamentoRepository orcamentoRepository,
            MedicaoRepository medicaoRepository,
            ItemOrcamentoRepository itemOrcamentoRepository,
            ItemMedicaoRepository itemMedicaoRepository
    ) {
        this.orcamentoRepository = orcamentoRepository;
        this.medicaoRepository = medicaoRepository;
        this.itemOrcamentoRepository = itemOrcamentoRepository;
        this.itemMedicaoRepository = itemMedicaoRepository;
    }

    @GetMapping
    public List<Medicao> listar(@PathVariable Long orcamentoId) {

        Orcamento orcamento = orcamentoRepository.findById(orcamentoId)
                .orElseThrow(() -> new OrcamentoException("Orçamento não encontrado"));

        return medicaoRepository.findByOrcamentoId(orcamento.getId());
    }

    @GetMapping("/{medicaoId}")
    public Medicao buscarPorId(@PathVariable Long orcamentoId,
                            @PathVariable Long medicaoId) {

        orcamentoRepository.findById(orcamentoId)
                .orElseThrow(() -> new OrcamentoException("Orçamento não encontrado"));

        return medicaoRepository.findById(medicaoId)
                .filter(m -> m.getOrcamento().getId().equals(orcamentoId))
                .orElseThrow(() -> new OrcamentoException("Medição não encontrada"));
    }

    @Transactional
    @PostMapping
    public Medicao criar(@PathVariable Long orcamentoId,
                         @RequestBody MedicaoRequest request) {

        Orcamento orcamento = orcamentoRepository.findById(orcamentoId)
                .orElseThrow(() -> new OrcamentoException("Orçamento não encontrado"));

        if (orcamento.getStatus() == StatusOrcamento.FINALIZADO) {
            throw new OrcamentoException("Não é permitido criar medição para orçamento finalizado");
        }

        if (medicaoRepository.existsByOrcamentoIdAndStatus(orcamentoId, StatusMedicao.ABERTA)) {
            throw new OrcamentoException("Já existe uma medição aberta para este orçamento");
        }

        if (request.itens() == null || request.itens().isEmpty()) {
            throw new OrcamentoException("Medição deve possuir pelo menos um item");
        }

        Medicao medicao = Medicao.builder()
                .numeroMedicao(gerarNumeroMedicao())
                .dataMedicao(LocalDate.now())
                .valorTotal(BigDecimal.ZERO)
                .status(StatusMedicao.ABERTA)
                .observacao(request.observacao())
                .orcamento(orcamento)
                .build();

        medicao = medicaoRepository.save(medicao);

        BigDecimal valorTotalMedicao = BigDecimal.ZERO;

        for (ItemMedicaoRequest itemRequest : request.itens()) {

            ItemOrcamento itemOrcamento = itemOrcamentoRepository
                    .findByIdAndOrcamentoId(itemRequest.itemOrcamentoId(), orcamentoId)
                    .orElseThrow(() -> new OrcamentoException("Item do orçamento não encontrado"));

            if (itemRequest.quantidadeMedida() == null ||
                    itemRequest.quantidadeMedida().compareTo(BigDecimal.ZERO) <= 0) {
                throw new OrcamentoException("Quantidade medida deve ser maior que zero");
            }

            BigDecimal quantidadeJaValidada =
                    itemMedicaoRepository.somarQuantidadeValidadaPorItem(itemOrcamento.getId());

            BigDecimal quantidadeRestante =
                    itemOrcamento.getQuantidade().subtract(quantidadeJaValidada);

            if (itemRequest.quantidadeMedida().compareTo(quantidadeRestante) > 0) {
                throw new OrcamentoException("Quantidade medida ultrapassa o permitido para o item");
            }

            BigDecimal valorTotalItemMedido =
                    itemRequest.quantidadeMedida().multiply(itemOrcamento.getValorUnitario());

            ItemMedicao itemMedicao = ItemMedicao.builder()
                    .medicao(medicao)
                    .itemOrcamento(itemOrcamento)
                    .quantidadeMedida(itemRequest.quantidadeMedida())
                    .valorTotalMedido(valorTotalItemMedido)
                    .build();

            itemMedicaoRepository.save(itemMedicao);

            valorTotalMedicao = valorTotalMedicao.add(valorTotalItemMedido);
        }

        medicao.setValorTotal(valorTotalMedicao);

        return medicaoRepository.save(medicao);
    }

    @PutMapping("/{medicaoId}/validar")
    public Medicao validar(@PathVariable Long orcamentoId,
                        @PathVariable Long medicaoId) {

        Orcamento orcamento = orcamentoRepository.findById(orcamentoId)
                .orElseThrow(() -> new OrcamentoException("Orçamento não encontrado"));

        Medicao medicao = medicaoRepository.findById(medicaoId)
                .filter(m -> m.getOrcamento().getId().equals(orcamentoId))
                .orElseThrow(() -> new OrcamentoException("Medição não encontrada"));

        if (medicao.getStatus() == StatusMedicao.VALIDADA) {
            throw new OrcamentoException("Medição já está validada");
        }

        List<ItemMedicao> itensMedicao =
                itemMedicaoRepository.findByMedicaoId(medicaoId);

        for (ItemMedicao itemMedicao : itensMedicao) {

            ItemOrcamento itemOrcamento = itemMedicao.getItemOrcamento();

            BigDecimal quantidadeAtual = itemOrcamento.getQuantidadeAcumulada();
            if (quantidadeAtual == null) {
                quantidadeAtual = BigDecimal.ZERO;
            }

            BigDecimal novaQuantidade =
                    quantidadeAtual.add(itemMedicao.getQuantidadeMedida());

            if (novaQuantidade.compareTo(itemOrcamento.getQuantidade()) > 0) {
                throw new OrcamentoException(
                        "Validação ultrapassa quantidade total permitida para o item"
                );
            }

            itemOrcamento.setQuantidadeAcumulada(novaQuantidade);
            itemOrcamentoRepository.save(itemOrcamento);
        }

        medicao.setStatus(StatusMedicao.VALIDADA);

        return medicaoRepository.save(medicao);
    }


    private String gerarNumeroMedicao() {
        return "MED-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
