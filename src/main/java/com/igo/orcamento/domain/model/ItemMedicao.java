package com.igo.orcamento.domain.model;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "itens_medicao")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemMedicao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal quantidadeMedida;

    @Column(nullable = false)
    private BigDecimal valorTotalMedido;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "medicao_id")
    private Medicao medicao;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "item_orcamento_id")
    private ItemOrcamento itemOrcamento;
}
