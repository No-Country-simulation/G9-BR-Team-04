package com.g9team04.techmind.conteudo;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("conteudo")
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


    @GetMapping("/titulo")
    public ResponseEntity<Page<ConteudoResponse>> buscarPorTitulo(
            @RequestParam(required = false) String titulo,
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(conteudoService.findByTituloContainingIgnoreCase(titulo, pageable));
    }

    @GetMapping("/categoria")
    public ResponseEntity<Page<ConteudoResponse>> categoria(
            @RequestParam String categoria,
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(conteudoService.buscarPorCategoria(categoria, pageable));
    }

    @GetMapping("relacionados/{id}")
    public ResponseEntity<Page<ConteudoResponse>> buscarRelacionados(
            @PathVariable Long id,
            @PageableDefault(size = 5, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(conteudoService.buscarRelacionados(id, pageable));
    }
}
