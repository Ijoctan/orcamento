# Gerenciador de Orçamento - Backend

Projeto desenvolvido em Spring Boot para gerenciamento de Orçamentos e Medições.

## Regras de negócio

- Não é permitido editar orçamento com status FINALIZADO.
- Orçamento pode ser finalizado via endpoint PUT /orcamentos/{id}/finalizar.

## Tecnologias Utilizadas

- Java 21
- Spring Boot 3
- Spring Data JPA
- PostgreSQL
- Swagger (OpenAPI)
- Lombok

---

## Como Executar o Projeto

### 1. Pré-requisitos

- Java 21 instalado
- Maven instalado
- PostgreSQL instalado

---

## Configuração do Banco de Dados

### Criar banco manualmente:

Entrar no PostgreSQL:

```bash
psql -U postgres
