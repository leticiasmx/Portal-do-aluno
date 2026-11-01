package com.escola.plataforma.controller;

import com.escola.plataforma.dto.Dtos.*;
import com.escola.plataforma.service.AlunoService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/alunos")
public class AlunoController {
    private final AlunoService s;

    public AlunoController(AlunoService s) { this.s = s; }

    @GetMapping
    public ResponseEntity<List<AlunoResponse>> listar() { return ResponseEntity.ok(s.listar()); }

    @GetMapping("/{id}")
    public ResponseEntity<AlunoResponse> buscar(@PathVariable("id") Long id) { return ResponseEntity.ok(s.buscar(id)); }

    @GetMapping("/turma/{turmaId}")
    public ResponseEntity<List<AlunoResponse>> turma(@PathVariable("turmaId") Long turmaId) { return ResponseEntity.ok(s.porTurma(turmaId)); }

    @GetMapping("/buscar/{nome}")
    public ResponseEntity<List<AlunoResponse>> nome(@PathVariable("nome") String nome) { return ResponseEntity.ok(s.porNome(nome)); }

    @PostMapping
    public ResponseEntity<AlunoResponse> criar(@Valid @RequestBody AlunoRequest r) { return ResponseEntity.status(201).body(s.matricular(r)); }

    @PutMapping("/{id}")
    public ResponseEntity<AlunoResponse> atualizar(@PathVariable("id") Long id, @Valid @RequestBody AlunoRequest r) {
        return ResponseEntity.ok(s.atualizar(id, r));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable("id") Long id) {
        s.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/nota/{prova}/{valor}")
    public ResponseEntity<AlunoResponse> nota(@PathVariable("id") Long id, @PathVariable("prova") int prova, @PathVariable("valor") double valor) {
        return ResponseEntity.ok(s.lancarNota(id, prova, valor));
    }

    @PostMapping("/{id}/falta")
    public ResponseEntity<AlunoResponse> falta(@PathVariable("id") Long id) { return ResponseEntity.ok(s.registrarFalta(id)); }

    @PutMapping("/{id}/trancar")
    public ResponseEntity<AlunoResponse> trancar(@PathVariable("id") Long id) { return ResponseEntity.ok(s.trancar(id)); }
}
