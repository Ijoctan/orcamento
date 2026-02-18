package com.igo.orcamento.infra.repository;

import com.igo.orcamento.domain.model.TipoOrcamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoOrcamentoRepository extends JpaRepository<TipoOrcamento, Long> {
}
