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

-- ==============================
-- Tabela: medicoes
-- ==============================

CREATE TABLE medicoes (
    id BIGSERIAL PRIMARY KEY,
    numero_medicao VARCHAR(50) NOT NULL UNIQUE,
    data_medicao DATE NOT NULL,
    valor_total NUMERIC(15,2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    observacao VARCHAR(500),
    orcamento_id BIGINT NOT NULL,
    CONSTRAINT fk_medicao_orcamento
        FOREIGN KEY (orcamento_id)
        REFERENCES orcamentos(id)
);

-- ==============================
-- Tabela: itens_medicao
-- ==============================

CREATE TABLE itens_medicao (
    id BIGSERIAL PRIMARY KEY,
    quantidade_medida NUMERIC(15,2) NOT NULL,
    valor_total_medido NUMERIC(15,2) NOT NULL,
    medicao_id BIGINT NOT NULL,
    item_orcamento_id BIGINT NOT NULL,
    CONSTRAINT fk_item_medicao_medicao
        FOREIGN KEY (medicao_id)
        REFERENCES medicoes(id),
    CONSTRAINT fk_item_medicao_item
        FOREIGN KEY (item_orcamento_id)
        REFERENCES itens_orcamento(id)
);
