package com.escola.plataforma.dto;

import jakarta.validation.constraints.*;

public class Dtos {
    public record TurmaRequest(
        @NotBlank(message = "Nome da turma é obrigatório") String nome,
        @Min(value = 1, message = "Capacidade mínima é 1") int capacidade) {}

    public record TurmaResponse(Long id, String nome, int capacidade, long totalAlunos) {}

    public record AlunoRequest(
        @NotBlank(message = "Nome é obrigatório") String nome,
        @NotBlank(message = "Matrícula é obrigatória") String matricula,
        @NotNull(message = "Turma é obrigatória") Long turmaId) {}

    public record AlunoResponse(Long id, String nome, String matricula, Long turmaId, String turma,
        double nota1, double nota2, int faltas, boolean ativo, double media, String situacao) {}
}
