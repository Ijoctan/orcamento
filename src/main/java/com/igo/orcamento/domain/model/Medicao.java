package com.igo.orcamento.domain.model;

import com.igo.orcamento.domain.enums.StatusMedicao;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@Entity
@Table(name = "medicoes")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Medicao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numeroMedicao;

    @Column(nullable = false)
    private LocalDate dataMedicao;

    @Column(nullable = false)
    private BigDecimal valorTotal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusMedicao status;

    @Column(length = 500)
    private String observacao;

    @ManyToOne(optional = false)
    @JoinColumn(name = "orcamento_id")
    private Orcamento orcamento;

    @OneToMany(mappedBy = "medicao", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ItemMedicao> itens;
}
