package com.g9team04.techmind.conteudo;

import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/conteudo")
public class ControllerConteudo {
    private final ConteudoService conteudoService;

    public ControllerConteudo(ConteudoService conteudoService) {
        this.conteudoService = conteudoService;
    }

    @PostMapping
    public ResponseEntity<ConteudoResponse> processarConteudo(
            @Valid @RequestBody ConteudoRequest request) {
        ConteudoResponse response = conteudoService.processar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ConteudoResponse>> getAllConteudos(Pageable pageable) {
        return ResponseEntity.ok(conteudoService.getAllConteudos(pageable));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<ConteudoResponse>> buscarPorTitulo(
            @RequestParam String titulo,Pageable pageable) {

        return ResponseEntity.ok(conteudoService.buscarPorTitulo(titulo, pageable));
    }
}
