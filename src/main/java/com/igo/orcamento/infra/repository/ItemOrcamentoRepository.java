package com.igo.orcamento.infra.repository;

import com.igo.orcamento.domain.model.ItemOrcamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ItemOrcamentoRepository extends JpaRepository<ItemOrcamento, Long> {

    List<ItemOrcamento> findByOrcamentoId(Long orcamentoId);

    Optional<ItemOrcamento> findByIdAndOrcamentoId(Long id, Long orcamentoId);

    @Query("""
            select coalesce(sum(i.valorTotal), 0.0)
            from ItemOrcamento i
            where i.orcamento.id = :orcamentoId
            """)
    BigDecimal somarValorTotalPorOrcamento(Long orcamentoId);

}
