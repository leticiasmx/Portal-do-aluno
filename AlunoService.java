package com.escola.plataforma.service;

import com.escola.plataforma.dto.Dtos.*;
import com.escola.plataforma.exception.ErroHandler.NaoEncontrado;
import com.escola.plataforma.exception.ErroHandler.Regra;
import com.escola.plataforma.model.Aluno;
import com.escola.plataforma.model.Turma;
import com.escola.plataforma.repository.AlunoRepository;
import com.escola.plataforma.repository.TurmaRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AlunoService {
    private final AlunoRepository repo;
    private final TurmaRepository turmas;

    public AlunoService(AlunoRepository repo, TurmaRepository turmas) {
        this.repo = repo;
        this.turmas = turmas;
    }

    private AlunoResponse res(Aluno a) {
        double media = Math.round((a.getNota1() + a.getNota2()) / 2 * 100) / 100.0;
        String s = !a.isAtivo() ? "TRANCADO"
                 : a.getFaltas() > 20 ? "REPROVADO POR FALTA"
                 : media >= 7 ? "APROVADO"
                 : media >= 5 ? "RECUPERAÇÃO" : "REPROVADO";
        return new AlunoResponse(a.getId(), a.getNome(), a.getMatricula(), a.getTurma().getId(), a.getTurma().getNome(),
                a.getNota1(), a.getNota2(), a.getFaltas(), a.isAtivo(), media, s);
    }

    private Aluno entidade(Long id) {
        return repo.findById(id).orElseThrow(() -> new NaoEncontrado("Aluno " + id + " não encontrado"));
    }

    private Aluno ativo(Long id) {
        Aluno a = entidade(id);
        if (!a.isAtivo()) throw new Regra("Matrícula trancada");
        return a;
    }

    private Turma turmaComVaga(Long turmaId) {
        Turma t = turmas.findById(turmaId).orElseThrow(() -> new NaoEncontrado("Turma " + turmaId + " não encontrada"));
        if (repo.countByTurmaId(turmaId) >= t.getCapacidade())
            throw new Regra("Turma lotada (capacidade: " + t.getCapacidade() + ")");
        return t;
    }

    public List<AlunoResponse> listar() { return repo.findAll().stream().map(this::res).toList(); }
    public List<AlunoResponse> porTurma(Long turmaId) { return repo.findByTurmaId(turmaId).stream().map(this::res).toList(); }
    public List<AlunoResponse> porNome(String n) { return repo.findByNomeContainingIgnoreCase(n).stream().map(this::res).toList(); }
    public AlunoResponse buscar(Long id) { return res(entidade(id)); }

    public AlunoResponse matricular(AlunoRequest r) {
        if (repo.existsByMatricula(r.matricula())) throw new Regra("Matrícula já cadastrada");
        Aluno a = new Aluno();
        a.setNome(r.nome());
        a.setMatricula(r.matricula());
        a.setTurma(turmaComVaga(r.turmaId()));
        return res(repo.save(a));
    }

    public AlunoResponse atualizar(Long id, AlunoRequest r) {
        Aluno a = entidade(id);
        if (!a.getTurma().getId().equals(r.turmaId())) a.setTurma(turmaComVaga(r.turmaId()));
        a.setNome(r.nome());
        return res(repo.save(a));
    }

    public void deletar(Long id) { repo.delete(entidade(id)); }

    // ---------- Regras de negócio ----------
    public AlunoResponse lancarNota(Long id, int prova, double valor) {
        Aluno a = ativo(id);
        if (valor < 0 || valor > 10) throw new Regra("Nota deve estar entre 0 e 10");
        if (prova == 1) a.setNota1(valor);
        else if (prova == 2) a.setNota2(valor);
        else throw new Regra("Prova deve ser 1 ou 2");
        return res(repo.save(a));
    }

    public AlunoResponse registrarFalta(Long id) {
        Aluno a = ativo(id);
        a.setFaltas(a.getFaltas() + 1);
        return res(repo.save(a));
    }

    public AlunoResponse trancar(Long id) {
        Aluno a = ativo(id);
        a.setAtivo(false);
        return res(repo.save(a));
    }
}
