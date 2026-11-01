package com.escola.plataforma.exception;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class ErroHandler {

    public static class NaoEncontrado extends RuntimeException {
        public NaoEncontrado(String m) { super(m); }
    }

    public static class Regra extends RuntimeException {
        public Regra(String m) { super(m); }
    }

    @ExceptionHandler(NaoEncontrado.class)
    public ResponseEntity<Map<String, String>> naoEncontrado(NaoEncontrado e) {
        return ResponseEntity.status(404).body(Map.of("erro", e.getMessage()));
    }

    @ExceptionHandler(Regra.class)
    public ResponseEntity<Map<String, String>> regra(Regra e) {
        return ResponseEntity.status(422).body(Map.of("erro", e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> validacao(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        return ResponseEntity.badRequest().body(Map.of("erro", msg));
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<Map<String, String>> requisicaoInvalida(Exception e) {
        return ResponseEntity.badRequest().body(Map.of("erro", "Requisição inválida: confira os valores enviados"));
    }
}
