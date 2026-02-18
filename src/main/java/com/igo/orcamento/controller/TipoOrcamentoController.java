package com.igo.orcamento.controller;

import com.igo.orcamento.domain.model.TipoOrcamento;
import com.igo.orcamento.infra.repository.TipoOrcamentoRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tipo-orcamento")
public class TipoOrcamentoController {

    private final TipoOrcamentoRepository repository;

    public TipoOrcamentoController(TipoOrcamentoRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<TipoOrcamento> listar() {
        return repository.findAll();
    }
}
