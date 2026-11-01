package com.escola.plataforma.repository;

import com.escola.plataforma.model.Turma;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TurmaRepository extends JpaRepository<Turma, Long> {
    boolean existsByNomeIgnoreCase(String nome);
    List<Turma> findByNomeContainingIgnoreCase(String nome);
}
