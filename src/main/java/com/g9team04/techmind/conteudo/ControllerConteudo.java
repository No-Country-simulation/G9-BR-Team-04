package com.g9team04.techmind.conteudo;

import com.g9team04.techmind.infrastructure.LoteProcessamentoException;
import com.g9team04.techmind.infrastructure.ValidatorCsv;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("conteudo")
public class ControllerConteudo {
    private final ConteudoService conteudoService;
    private final ValidatorCsv validatorCsv;
    private final LoteProcessor loteProcessor;

    public ControllerConteudo(ConteudoService conteudoService, LoteProcessor loteProcessor, ValidatorCsv validatorCsv, ValidatorCsv validatorCsv1, LoteProcessor loteProcessor1) {
        this.conteudoService = conteudoService;
        this.validatorCsv = validatorCsv1;
        this.loteProcessor = loteProcessor1;
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

    @PostMapping("/lote")
    public ResponseEntity<LoteResponse> processarLote(@RequestParam("arquivo") MultipartFile arquivo) {

        validatorCsv.validarCsv(arquivo);

        try{
            LoteResponse response = loteProcessor.processar(arquivo.getInputStream());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IOException e) {
            throw new LoteProcessamentoException("Erro ao ler o arquivo enviado", e);

        }


    }
}
