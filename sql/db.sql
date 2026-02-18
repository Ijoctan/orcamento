-- ==============================
-- Script inicial do banco
-- Projeto: Gerenciador de Orçamento
-- ==============================

-- OBS: Este script foi escrito para ser executado via psql (cliente CLI do PostgreSQL).
-- A checagem de existência do banco utiliza um comando que depende do psql (\gexec).
-- Se você executar em outro cliente, crie o banco manualmente antes de rodar o script.

-- Cria o banco apenas se não existir (psql meta-command \gexec)
SELECT 'CREATE DATABASE orcamento_db'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'orcamento_db')\gexec

\connect orcamento_db

-- ==============================
-- Tabela: tipo_orcamento
-- ==============================

CREATE TABLE IF NOT EXISTS tipo_orcamento (
    id BIGSERIAL PRIMARY KEY,
    codigo VARCHAR(50) UNIQUE NOT NULL,
    descricao VARCHAR(150) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE
);


-- Insere tipos padrão (somente se não existirem)
INSERT INTO tipo_orcamento (codigo, descricao)
SELECT v.codigo, v.descricao FROM (VALUES
    ('OUTROS', 'Outros'),
    ('OBRA_EDIFICAO', 'Obra de Edificação'),
    ('OBRA_RODOVIARIAS', 'Obra de Rodovias'),
    ('OBRA_PARTICULAR', 'Obra Particular'),
    ('OBRA_PUBLICA', 'Obra Publica')
) AS v(codigo, descricao)
WHERE NOT EXISTS (
    SELECT 1 FROM tipo_orcamento t WHERE t.codigo = v.codigo
);


-- ==============================
-- Tabela: orcamentos
-- ==============================

CREATE TABLE IF NOT EXISTS orcamentos (
    id BIGSERIAL PRIMARY KEY,
    numero_protocolo VARCHAR(30) UNIQUE,
    tipo_orcamento_id BIGINT NOT NULL,
    valor_total NUMERIC(15,2) NOT NULL,
    data_criacao DATE NOT NULL,
    status VARCHAR(20) NOT NULL,

    CONSTRAINT fk_orcamento_tipo
        FOREIGN KEY (tipo_orcamento_id)
        REFERENCES tipo_orcamento(id)
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

CREATE TABLE IF NOT EXISTS medicoes (
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

CREATE INDEX IF NOT EXISTS idx_medicoes_orcamento_id
    ON medicoes (orcamento_id);


-- ==============================
-- Tabela: itens_medicao
-- ==============================

CREATE TABLE IF NOT EXISTS itens_medicao (
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

CREATE INDEX IF NOT EXISTS idx_itens_medicao_medicao_id
    ON itens_medicao (medicao_id);

CREATE INDEX IF NOT EXISTS idx_itens_medicao_item_orcamento_id
    ON itens_medicao (item_orcamento_id);
