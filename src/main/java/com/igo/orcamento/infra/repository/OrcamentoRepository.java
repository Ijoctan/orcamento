package com.igo.orcamento.infra.repository;

import com.igo.orcamento.domain.model.Orcamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrcamentoRepository extends JpaRepository<Orcamento, Long> {

    boolean existsByNumeroProtocolo(String numeroProtocolo);
}
