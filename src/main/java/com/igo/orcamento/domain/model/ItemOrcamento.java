package com.igo.orcamento.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "itens_orcamento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemOrcamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "orcamento_id", nullable = false)
    private Orcamento orcamento;

    @Column(name = "descricao", nullable = false, length = 150)
    private String descricao;

    @Column(name = "quantidade", nullable = false, precision = 15, scale = 2)
    private BigDecimal quantidade;

    @Column(name = "valor_unitario", nullable = false, precision = 15, scale = 2)
    private BigDecimal valorUnitario;

    @Column(name = "valor_total", nullable = false, precision = 15, scale = 2)
    private BigDecimal valorTotal;

    @Column(name = "quantidade_acumulada", nullable = false, precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal quantidadeAcumulada = BigDecimal.ZERO;

    public void recalcularValorTotal() {
        this.valorTotal = this.quantidade.multiply(this.valorUnitario);
    }
}
