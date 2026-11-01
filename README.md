# 🎓 Plataforma do Aluno

API REST em Spring Boot para gerenciar **turmas** e **alunos** de uma escola, com tela web integrada e documentação Swagger.

## Tecnologias
Java 17+ · Spring Boot 4 · Spring Data JPA · PostgreSQL · Bean Validation · springdoc-openapi (Swagger) · HTML/CSS/JS

## Requisitos
- JDK 17 ou superior
- PostgreSQL
- Maven (ou o `mvnw` do projeto)

## Como executar
1. Crie o banco no PostgreSQL:
   ```sql
   CREATE DATABASE escola;
   ```
2. Em `src/main/resources/application.properties`, troque `SUA_SENHA` pela senha do seu PostgreSQL (e o `username`, se não for `postgres`).
3. Rode o projeto:
   ```bash
   ./mvnw spring-boot:run
   ```
   (no Windows: `mvnw.cmd spring-boot:run`) ou execute a classe `PlataformaApplication` pela IDE.
4. As tabelas são criadas automaticamente (`ddl-auto=update`).

## Acessos
| O quê | URL |
|---|---|
| Tela web | http://localhost:8081 |
| Swagger | http://localhost:8081/swagger-ui.html |

## Modelo de dados
`Turma` **1 ── N** `Aluno` (`@OneToMany` em Turma, `@ManyToOne` em Aluno).

## Endpoints

### Turmas (`/turmas`)
| Método | Rota | Descrição |
|---|---|---|
| GET | `/turmas` | Lista turmas |
| GET | `/turmas/{id}` | Busca por id |
| GET | `/turmas/buscar/{nome}` | Filtra por nome |
| POST | `/turmas` | Cria turma |
| PUT | `/turmas/{id}` | Atualiza turma |
| DELETE | `/turmas/{id}` | Exclui turma |

### Alunos (`/alunos`)
| Método | Rota | Descrição |
|---|---|---|
| GET | `/alunos` | Lista alunos |
| GET | `/alunos/{id}` | Busca por id |
| GET | `/alunos/turma/{turmaId}` | Filtra por turma |
| GET | `/alunos/buscar/{nome}` | Filtra por nome |
| POST | `/alunos` | Matricula aluno |
| PUT | `/alunos/{id}` | Atualiza aluno |
| DELETE | `/alunos/{id}` | Exclui aluno |
| POST | `/alunos/{id}/nota/{prova}/{valor}` | Lança nota (prova 1 ou 2) |
| POST | `/alunos/{id}/falta` | Registra falta |
| PUT | `/alunos/{id}/trancar` | Tranca matrícula |

## Regras de negócio
1. Matrícula do aluno é única.
2. Nome da turma é único.
3. Turma não aceita mais alunos que sua capacidade.
4. Turma com alunos não pode ser excluída (nem ter a capacidade reduzida abaixo do nº de alunos).
5. Notas devem estar entre 0 e 10.
6. Matrícula trancada não recebe nota nem falta.
7. Média = (N1 + N2) / 2: **≥ 7 aprovado**, **5 a 6,99 recuperação**, **< 5 reprovado**.
8. Mais de 20 faltas reprova por falta, independentemente da nota.

## Status HTTP
`200` OK · `201` criado · `204` sem conteúdo · `400` dados inválidos · `404` não encontrado · `422` regra de negócio violada

## Estrutura
```
com.escola.plataforma
 ├─ controller   (recebe requisições, devolve ResponseEntity)
 ├─ service      (regras de negócio)
 ├─ repository   (Spring Data JPA, métodos derivados)
 ├─ model        (entidades Turma e Aluno)
 ├─ dto          (records de entrada e saída)
 └─ exception    (tratamento global de erros)
```

## Autora
Leticia — Engenharia de Software, UNIFSA
