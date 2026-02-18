package com.igo.orcamento.domain.model;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tipo_orcamento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoOrcamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codigo;
    private String descricao;
    private Boolean ativo;
}
