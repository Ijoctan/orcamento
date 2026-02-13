package com.igo.orcamento.domain.model;
import com.fasterxml.jackson.annotation.JsonIgnore;

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

    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "medicao_id")
    private Medicao medicao;

    @ManyToOne(optional = false)
    @JoinColumn(name = "item_orcamento_id")
    private ItemOrcamento itemOrcamento;
}
