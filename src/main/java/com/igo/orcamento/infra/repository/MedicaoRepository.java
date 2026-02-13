package com.igo.orcamento.infra.repository;

import com.igo.orcamento.domain.enums.StatusMedicao;
import com.igo.orcamento.domain.model.Medicao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface MedicaoRepository extends JpaRepository<Medicao, Long> {

    boolean existsByOrcamentoIdAndStatus(Long orcamentoId, StatusMedicao status);

    Optional<Medicao> findByOrcamentoIdAndStatus(Long orcamentoId, StatusMedicao status);

    List<Medicao> findByOrcamentoId(Long orcamentoId);

}
