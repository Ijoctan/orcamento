package com.igo.orcamento.controller;

import com.igo.orcamento.domain.enums.StatusOrcamento;
import com.igo.orcamento.domain.model.Orcamento;
import com.igo.orcamento.domain.model.ItemOrcamento;
import com.igo.orcamento.domain.model.TipoOrcamento;
import com.igo.orcamento.infra.repository.OrcamentoRepository;
import com.igo.orcamento.infra.repository.ItemOrcamentoRepository;
import com.igo.orcamento.infra.repository.TipoOrcamentoRepository;
import com.igo.orcamento.presentation.dto.OrcamentoRequest;
import com.igo.orcamento.app.exception.OrcamentoException;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;


import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/orcamentos")
public class OrcamentoController {

    private final OrcamentoRepository repository;
    private final ItemOrcamentoRepository itemRepository;
    private final TipoOrcamentoRepository tipoOrcamentoRepository;

    public OrcamentoController(OrcamentoRepository repository,
                               ItemOrcamentoRepository itemRepository,
                               TipoOrcamentoRepository tipoOrcamentoRepository) {
        this.repository = repository;
        this.itemRepository = itemRepository;
        this.tipoOrcamentoRepository = tipoOrcamentoRepository;
    }

    @PostMapping
    public Orcamento criar(@RequestBody OrcamentoRequest request) {

        TipoOrcamento tipo = tipoOrcamentoRepository.findById(request.tipoOrcamentoId())
            .orElseThrow(() -> new OrcamentoException("Tipo de orçamento inválido"));

        Orcamento orcamento = Orcamento.builder()
                .tipoOrcamento(tipo)    
                .valorTotal(request.valorTotal())
                .dataCriacao(LocalDate.now())
                .status(StatusOrcamento.ABERTO)
                .build();

        Orcamento salvo = repository.save(orcamento);

        String anoMes = LocalDate.now().getYear() + "-" +
                String.format("%02d", LocalDate.now().getMonthValue());

        String protocolo = salvo.getId() + "/" + anoMes;
        salvo.setNumeroProtocolo(protocolo);

        return repository.save(salvo);
    }

    @GetMapping
    public List<Orcamento> listar() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Orcamento buscarPorId(@PathVariable Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new OrcamentoException("Orçamento não encontrado"));
    }

    @PutMapping("/{id}/finalizar")
    public Orcamento finalizar(@PathVariable Long id) {
        
        Orcamento orcamento = repository.findById(id)
            .orElseThrow(() -> new OrcamentoException("Orçamento não encontrado"));

        validarFinalizacao(orcamento);

        orcamento.setStatus(StatusOrcamento.FINALIZADO);

        return repository.save(orcamento);
    }

    @PutMapping("/{id}")
    public Orcamento editar(@PathVariable Long id,
                            @RequestBody OrcamentoRequest request) {

        Orcamento orcamento = repository.findById(id)
                .orElseThrow(() -> new OrcamentoException("Orçamento não encontrado"));

        if (orcamento.getStatus() == StatusOrcamento.FINALIZADO) {
            throw new OrcamentoException("Não é permitido editar orçamento finalizado");
        }

        TipoOrcamento tipo = tipoOrcamentoRepository.findById(request.tipoOrcamentoId())
            .orElseThrow(() -> new OrcamentoException("Tipo de orçamento inválido"));

        orcamento.setTipoOrcamento(tipo);

        orcamento.setValorTotal(request.valorTotal());

        return repository.save(orcamento);
    }

    private void validarFinalizacao(Orcamento orcamento) {

        if (orcamento.getStatus() == StatusOrcamento.FINALIZADO) {
            throw new OrcamentoException("Orçamento já está finalizado");
        }

        List<ItemOrcamento> itens =
                itemRepository.findByOrcamentoId(orcamento.getId());

        if (itens.isEmpty()) {
            throw new OrcamentoException("Não é possível finalizar orçamento sem itens");
        }

        BigDecimal somaItens =
                itemRepository.somarValorTotalPorOrcamento(orcamento.getId());

        if (somaItens == null) somaItens = BigDecimal.ZERO;

        if (somaItens.compareTo(orcamento.getValorTotal()) != 0) {
            throw new OrcamentoException(
                    "A soma dos itens deve ser exatamente igual ao valor total do orçamento"
            );
        }
        
    }

}


