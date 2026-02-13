package com.igo.orcamento.infra.repository;

import com.igo.orcamento.domain.model.ItemMedicao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface ItemMedicaoRepository extends JpaRepository<ItemMedicao, Long> {

    @Query("""
           SELECT COALESCE(SUM(im.quantidadeMedida), 0)
           FROM ItemMedicao im
           WHERE im.itemOrcamento.id = :itemId
           AND im.medicao.status = 'VALIDADA'
           """)
    BigDecimal somarQuantidadeValidadaPorItem(Long itemId);

    List<ItemMedicao> findByMedicaoId(Long medicaoId);
}
