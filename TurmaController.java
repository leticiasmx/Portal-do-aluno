package com.escola.plataforma.controller;

import com.escola.plataforma.dto.Dtos.TurmaRequest;
import com.escola.plataforma.dto.Dtos.TurmaResponse;
import com.escola.plataforma.service.TurmaService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/turmas")
public class TurmaController {
    private final TurmaService s;

    public TurmaController(TurmaService s) { this.s = s; }

    @GetMapping
    public ResponseEntity<List<TurmaResponse>> listar() {
        return ResponseEntity.ok(s.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TurmaResponse> buscar(@PathVariable("id") Long id) {
        return ResponseEntity.ok(s.buscar(id));
    }

    @GetMapping("/buscar/{nome}")
    public ResponseEntity<List<TurmaResponse>> nome(@PathVariable("nome") String nome) {
        return ResponseEntity.ok(s.porNome(nome));
    }

    @PostMapping
    public ResponseEntity<TurmaResponse> criar(@Valid @RequestBody TurmaRequest r) {
        return ResponseEntity.status(201).body(s.criar(r));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TurmaResponse> atualizar(@PathVariable("id") Long id, @Valid @RequestBody TurmaRequest r) {
        return ResponseEntity.ok(s.atualizar(id, r));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable("id") Long id) {
        s.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
