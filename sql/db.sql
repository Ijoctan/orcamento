-- ==============================
-- Script inicial do banco
-- Projeto: Gerenciador de Orçamento
-- ==============================

CREATE DATABASE orcamento_db;

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
