package com.igo.orcamento.controller;

import com.igo.orcamento.domain.enums.StatusOrcamento;
import com.igo.orcamento.domain.model.ItemOrcamento;
import com.igo.orcamento.domain.model.Orcamento;
import com.igo.orcamento.infra.repository.ItemOrcamentoRepository;
import com.igo.orcamento.infra.repository.OrcamentoRepository;
import com.igo.orcamento.presentation.dto.ItemOrcamentoRequest;
import com.igo.orcamento.app.exception.OrcamentoException;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/orcamentos/{orcamentoId}/itens")
public class ItemOrcamentoController {

    private final OrcamentoRepository orcamentoRepository;
    private final ItemOrcamentoRepository itemRepository;

    public ItemOrcamentoController(OrcamentoRepository orcamentoRepository,
                                   ItemOrcamentoRepository itemRepository) {
        this.orcamentoRepository = orcamentoRepository;
        this.itemRepository = itemRepository;
    }

    @PostMapping
    public ItemOrcamento criar(@PathVariable Long orcamentoId,
                               @RequestBody ItemOrcamentoRequest request) {

        validarRequest(request);

        Orcamento orcamento = buscarOrcamentoOuFalhar(orcamentoId);
        validarOrcamentoAberto(orcamento);

        ItemOrcamento item = ItemOrcamento.builder()
                .orcamento(orcamento)
                .descricao(request.descricao().trim())
                .quantidade(request.quantidade())
                .valorUnitario(request.valorUnitario())
                .quantidadeAcumulada(BigDecimal.ZERO)
                .build();

        item.recalcularValorTotal();

        validarLimiteDoOrcamento(orcamento, null, item.getValorTotal());

        return itemRepository.save(item);
    }

    @GetMapping
    public List<ItemOrcamento> listar(@PathVariable Long orcamentoId) {
        buscarOrcamentoOuFalhar(orcamentoId);
        return itemRepository.findByOrcamentoId(orcamentoId);
    }

    @GetMapping("/{itemId}")
    public ItemOrcamento buscarPorId(@PathVariable Long orcamentoId,
                                    @PathVariable Long itemId) {

        buscarOrcamentoOuFalhar(orcamentoId);

        return itemRepository.findByIdAndOrcamentoId(itemId, orcamentoId)
                .orElseThrow(() ->
                        new OrcamentoException("Item do orçamento não encontrado"));
    }


    @PutMapping("/{itemId}")
    public ItemOrcamento editar(@PathVariable Long orcamentoId,
                                @PathVariable Long itemId,
                                @RequestBody ItemOrcamentoRequest request) {

        validarRequest(request);

        Orcamento orcamento = buscarOrcamentoOuFalhar(orcamentoId);
        validarOrcamentoAberto(orcamento);

        ItemOrcamento itemExistente = itemRepository.findByIdAndOrcamentoId(itemId, orcamentoId)
                .orElseThrow(() -> new OrcamentoException("Item do orçamento não encontrado"));

        BigDecimal valorAntigo = itemExistente.getValorTotal();

        itemExistente.setDescricao(request.descricao().trim());
        itemExistente.setQuantidade(request.quantidade());
        itemExistente.setValorUnitario(request.valorUnitario());
        itemExistente.recalcularValorTotal();

        validarLimiteDoOrcamento(
                orcamento,
                valorAntigo,
                itemExistente.getValorTotal()
        );

        return itemRepository.save(itemExistente);
    }

    @DeleteMapping("/{itemId}")
    public void excluir(@PathVariable Long orcamentoId,
                        @PathVariable Long itemId) {

        Orcamento orcamento = buscarOrcamentoOuFalhar(orcamentoId);
        validarOrcamentoAberto(orcamento);

        ItemOrcamento item = itemRepository.findByIdAndOrcamentoId(itemId, orcamentoId)
                .orElseThrow(() -> new OrcamentoException("Item do orçamento não encontrado"));

        itemRepository.delete(item);
    }


    private Orcamento buscarOrcamentoOuFalhar(Long orcamentoId) {
        return orcamentoRepository.findById(orcamentoId)
                .orElseThrow(() -> new OrcamentoException("Orçamento não encontrado"));
    }

    private void validarOrcamentoAberto(Orcamento orcamento) {
        if (orcamento.getStatus() == StatusOrcamento.FINALIZADO) {
            throw new OrcamentoException("Não é permitido incluir/editar itens de um orçamento finalizado");
        }
    }

    private void validarRequest(ItemOrcamentoRequest request) {
        if (request.descricao() == null || request.descricao().trim().isEmpty()) {
            throw new OrcamentoException("Descrição do item é obrigatória");
        }
        if (request.quantidade() == null || request.quantidade().compareTo(BigDecimal.ZERO) <= 0) {
            throw new OrcamentoException("Quantidade deve ser maior que zero");
        }
        if (request.valorUnitario() == null || request.valorUnitario().compareTo(BigDecimal.ZERO) <= 0) {
            throw new OrcamentoException("Valor unitário deve ser maior que zero");
        }
    }

    private void validarLimiteDoOrcamento(Orcamento orcamento,
                                      BigDecimal valorAntigoItem,
                                      BigDecimal novoValorTotalItem) {

        BigDecimal somaAtual = itemRepository.somarValorTotalPorOrcamento(orcamento.getId());

        if (somaAtual == null) {
            somaAtual = BigDecimal.ZERO;
        }

        if (valorAntigoItem != null) {
            somaAtual = somaAtual.subtract(valorAntigoItem);
        }

        BigDecimal novaSoma = somaAtual.add(novoValorTotalItem);

        novaSoma = novaSoma.setScale(2, RoundingMode.HALF_UP);
        BigDecimal limite = orcamento.getValorTotal().setScale(2, RoundingMode.HALF_UP);

        if (novaSoma.compareTo(limite) > 0) {
            throw new OrcamentoException(
                    "A soma dos itens (" + novaSoma +
                    ") ultrapassa o valor total do orçamento (" +
                    limite + ")"
            );
        }
    }
}
