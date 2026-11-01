package com.escola.plataforma.service;

import com.escola.plataforma.dto.Dtos.*;
import com.escola.plataforma.exception.ErroHandler.NaoEncontrado;
import com.escola.plataforma.exception.ErroHandler.Regra;
import com.escola.plataforma.model.Turma;
import com.escola.plataforma.repository.AlunoRepository;
import com.escola.plataforma.repository.TurmaRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TurmaService {
    private final TurmaRepository repo;
    private final AlunoRepository alunos;

    public TurmaService(TurmaRepository repo, AlunoRepository alunos) {
        this.repo = repo;
        this.alunos = alunos;
    }

    private TurmaResponse res(Turma t) {
        return new TurmaResponse(t.getId(), t.getNome(), t.getCapacidade(), alunos.countByTurmaId(t.getId()));
    }

    private Turma entidade(Long id) {
        return repo.findById(id).orElseThrow(() -> new NaoEncontrado("Turma " + id + " não encontrada"));
    }

    public List<TurmaResponse> listar() { return repo.findAll().stream().map(this::res).toList(); }
    public List<TurmaResponse> porNome(String n) { return repo.findByNomeContainingIgnoreCase(n).stream().map(this::res).toList(); }
    public TurmaResponse buscar(Long id) { return res(entidade(id)); }

    public TurmaResponse criar(TurmaRequest r) {
        if (repo.existsByNomeIgnoreCase(r.nome())) throw new Regra("Já existe turma com esse nome");
        Turma t = new Turma();
        t.setNome(r.nome());
        t.setCapacidade(r.capacidade());
        return res(repo.save(t));
    }

    public TurmaResponse atualizar(Long id, TurmaRequest r) {
        Turma t = entidade(id);
        if (!t.getNome().equalsIgnoreCase(r.nome()) && repo.existsByNomeIgnoreCase(r.nome()))
            throw new Regra("Já existe turma com esse nome");
        if (r.capacidade() < alunos.countByTurmaId(id))
            throw new Regra("Capacidade menor que o número de alunos da turma");
        t.setNome(r.nome());
        t.setCapacidade(r.capacidade());
        return res(repo.save(t));
    }

    public void deletar(Long id) {
        Turma t = entidade(id);
        if (alunos.countByTurmaId(id) > 0) throw new Regra("Turma possui alunos e não pode ser excluída");
        repo.delete(t);
    }
}
