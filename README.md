# 🎓 Plataforma do Aluno

API REST em Spring Boot para gerenciar **turmas** e **alunos** de uma escola, com tela web integrada ao backend e documentação Swagger.

> 🖥️ **Tela (front-end):** https://leticiasmx.github.io/Site-portal-do-aluno/ (depois de rodar o projeto)

## Tecnologias

Java 17+ · Spring Boot 4 · Spring Data JPA · PostgreSQL · Bean Validation · springdoc-openapi (Swagger) · HTML/CSS/JavaScript

## Requisitos

- JDK 17 ou superior
- PostgreSQL
- Maven (ou o `mvnw` que já vem no projeto)

## Como executar

1. Crie o banco de dados no PostgreSQL:

   ```sql
   CREATE DATABASE escola;
   ```

2. Em `src/main/resources/application.properties`, troque `SUA_SENHA` pela senha do seu PostgreSQL (e o `username`, caso não seja `postgres`).

3. Rode o projeto:

   ```bash
   ./mvnw spring-boot:run
   ```

   No Windows: `mvnw.cmd spring-boot:run`. Também é possível executar a classe `PlataformaApplication` pela IDE.

4. As tabelas são criadas automaticamente na primeira execução (`spring.jpa.hibernate.ddl-auto=update`).

## Modelo de dados

`Turma` **1 ── N** `Aluno`

- `Turma` usa `@OneToMany(mappedBy = "turma")`
- `Aluno` usa `@ManyToOne` com a chave estrangeira `turma_id`

## Endpoints

### Turmas (`/turmas`)

| Método | Rota | Descrição |
|---|---|---|
| GET | `/turmas` | Lista as turmas |
| GET | `/turmas/{id}` | Busca turma por id |
| GET | `/turmas/buscar/{nome}` | Filtra turmas por nome |
| POST | `/turmas` | Cria turma |
| PUT | `/turmas/{id}` | Atualiza turma |
| DELETE | `/turmas/{id}` | Exclui turma |

### Alunos (`/alunos`)

| Método | Rota | Descrição |
|---|---|---|
| GET | `/alunos` | Lista os alunos |
| GET | `/alunos/{id}` | Busca aluno por id |
| GET | `/alunos/turma/{turmaId}` | Filtra alunos por turma |
| GET | `/alunos/buscar/{nome}` | Filtra alunos por nome |
| POST | `/alunos` | Matricula aluno |
| PUT | `/alunos/{id}` | Atualiza aluno |
| DELETE | `/alunos/{id}` | Exclui aluno |
| POST | `/alunos/{id}/nota/{prova}/{valor}` | Lança nota (prova 1 ou 2) |
| POST | `/alunos/{id}/falta` | Registra uma falta |
| PUT | `/alunos/{id}/trancar` | Tranca a matrícula |

### Exemplos de corpo (JSON)

Criar turma:

```json
{ "nome": "3A", "capacidade": 30 }
```

Matricular aluno:

```json
{ "nome": "Maria Rios", "matricula": "001", "turmaId": 1 }
```

## Regras de negócio

1. A matrícula do aluno é única.
2. O nome da turma é único.
3. A turma não aceita mais alunos do que a sua capacidade.
4. Turma com alunos não pode ser excluída, nem ter a capacidade reduzida abaixo do número de alunos.
5. As notas devem estar entre 0 e 10.
6. Matrícula trancada não recebe nota nem falta.
7. Média = (N1 + N2) / 2: **≥ 7 aprovado**, **de 5 a 6,99 recuperação**, **< 5 reprovado**.
8. Mais de 20 faltas reprova por falta, independentemente da nota.

## Códigos de status HTTP

| Código | Significado |
|---|---|
| 200 | Sucesso |
| 201 | Recurso criado |
| 204 | Excluído, sem conteúdo |
| 400 | Dados inválidos (validação) |
| 404 | Recurso não encontrado |
| 422 | Regra de negócio violada |

## Estrutura do projeto

```
com.escola.plataforma
 ├─ controller   → recebe as requisições e devolve ResponseEntity
 ├─ service      → regras de negócio
 ├─ repository   → Spring Data JPA (métodos derivados)
 ├─ model        → entidades Turma e Aluno
 ├─ dto          → records de entrada e saída
 └─ exception    → tratamento global de erros (@RestControllerAdvice)

src/main/resources
 ├─ application.properties
 └─ static/index.html   → tela integrada ao backend
```

## Autora

Leticia — Engenharia de Software, UNIFSA
