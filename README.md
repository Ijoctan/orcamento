# Gerenciador de Orçamento - Backend

Projeto desenvolvido em Spring Boot para gerenciamento de Orçamentos e Medições.

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
```

Executar script de criação das tabelas:

```bash
psql -U postgres -d orcamento_db -f sql/db.sql
```

Rodar aplicação:

```bash
mvn clean install
mvn spring-boot:run
```

Acessar documentação Swagger:

```bash
http://localhost:8080/swagger-ui/index.html
```