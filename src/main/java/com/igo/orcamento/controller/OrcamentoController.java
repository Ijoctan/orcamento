package com.igo.orcamento.presentation.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrcamentoController {

    @GetMapping("/orcamentos")
    public List<String> listar() {
        return List.of("Orçamento 1", "Orçamento 2");
    }
}
