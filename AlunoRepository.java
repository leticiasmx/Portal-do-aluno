package com.escola.plataforma.repository;

import com.escola.plataforma.model.Aluno;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    List<Aluno> findByTurmaId(Long turmaId);
    List<Aluno> findByNomeContainingIgnoreCase(String nome);
    boolean existsByMatricula(String matricula);
    long countByTurmaId(Long turmaId);
}
