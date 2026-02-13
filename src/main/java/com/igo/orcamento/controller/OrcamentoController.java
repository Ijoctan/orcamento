package com.igo.orcamento.presentation.controller;

import com.igo.orcamento.domain.enums.StatusOrcamento;
import com.igo.orcamento.domain.model.Orcamento;
import com.igo.orcamento.infra.repository.OrcamentoRepository;
import com.igo.orcamento.presentation.dto.OrcamentoRequest;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/orcamentos")
public class OrcamentoController {

    private final OrcamentoRepository repository;

    public OrcamentoController(OrcamentoRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public Orcamento criar(@RequestBody OrcamentoRequest request) {
        Orcamento orcamento = Orcamento.builder()
                .tipoOrcamento(request.tipoOrcamento())
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
}
