-- ==============================
-- Script inicial do banco
-- Projeto: Gerenciador de Orçamento
-- ==============================

--CREATE DATABASE orcamento_db;

-- ==============================
-- Tabela: orcamentos
-- ==============================

CREATE TABLE IF NOT EXISTS orcamentos (
    id BIGSERIAL PRIMARY KEY,
    numero_protocolo VARCHAR(30) UNIQUE,
    tipo_orcamento VARCHAR(100) NOT NULL,
    valor_total NUMERIC(15,2) NOT NULL,
    data_criacao DATE NOT NULL,
    status VARCHAR(20) NOT NULL
);

-- ==============================
-- Tabela: itens_orcamento
-- ==============================

CREATE TABLE IF NOT EXISTS itens_orcamento (
    id BIGSERIAL PRIMARY KEY,
    orcamento_id BIGINT NOT NULL,
    descricao VARCHAR(150) NOT NULL,
    quantidade NUMERIC(15,2) NOT NULL,
    valor_unitario NUMERIC(15,2) NOT NULL,
    valor_total NUMERIC(15,2) NOT NULL,
    quantidade_acumulada NUMERIC(15,2) NOT NULL DEFAULT 0,

    CONSTRAINT fk_itens_orcamento_orcamento
        FOREIGN KEY (orcamento_id)
        REFERENCES orcamentos(id)
        ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_itens_orcamento_orcamento_id
    ON itens_orcamento (orcamento_id);
