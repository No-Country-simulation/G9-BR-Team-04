// conteudo/ConteudoService.java
package com.g9team04.techmind.conteudo;

import com.g9team04.techmind.conteudo.internal.ConteudoEntity;
import com.g9team04.techmind.conteudo.internal.ConteudoRepository;
import com.g9team04.techmind.conteudo.internal.HashUtils;
import com.g9team04.techmind.infrastructure.ConteudoNaoEncontradoException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ConteudoService {

    private final ConteudoRepository repository;
    private final ClassifierService classifier;

    public ConteudoService(ConteudoRepository repository, ClassifierService classifier) {
        this.repository = repository;
        this.classifier = classifier;
    }

    @Transactional
    public ConteudoResponse processar(ConteudoRequest request) {
        var hash = HashUtils.sha256(request.texto());
        // Cache: verifica se já existe conteúdo igual
        return repository.findByTituloAndTextoHash(request.titulo(),hash)
                .map(entity -> new ConteudoResponse(
                        entity.getId(),
                        entity.getTitulo(),
                        entity.getCategoria(),
                        entity.getProbabilidade(),
                        entity.getInformacoesAdicionais()
                ))
                .orElseGet(() -> classificarEPersistir(request));
    }

    private ConteudoResponse classificarEPersistir(ConteudoRequest request) {
        return Optional.of(new ConteudoEntity(request.titulo(), request.texto()))
                .map(entity -> {
                    // Chama o motor de IA e preenche os resultados
                    var resultado = classifier.classificar(entity.getTexto());
                    entity.setCategoria(resultado.categoria());
                    entity.setProbabilidade(resultado.probabilidade());
                    entity.setInformacoesAdicionais(resultado.tags());
                    return entity;
                })
                .map(repository::save)                // persiste a entidade
                .map(entity -> new ConteudoResponse(
                        entity.getId(),
                        entity.getTitulo(),
                        entity.getCategoria(),
                        entity.getProbabilidade(),
                        entity.getInformacoesAdicionais()
                ))
                .orElseThrow(() -> new IllegalStateException("Erro inesperado ao classificar e persistir"));
    }
    public Page<ConteudoResponse> findByTituloContainingIgnoreCase(String titulo, Pageable pageable) {
        return repository.findByTituloContainingIgnoreCase(titulo, pageable)
                .map(this::toResponse);
    }

    // ===== BUSCA POR CATEGORIA (lança 404 se não existir) =====
    public Page<ConteudoResponse> buscarPorCategoria(String categoria, Pageable pageable) {
        Optional.of(categoria)
                .filter(repository::existsByCategoriaContainingIgnoreCase)
                .orElseThrow(() -> new ConteudoNaoEncontradoException("Categoria: " + categoria));

        return repository.findByCategoriaContainingIgnoreCase(categoria, pageable)
                .map(this::toResponse);
    }

    // ===== RECOMENDAÇÃO (lança 404 se o ID não existir) =====
    public Page<ConteudoResponse> buscarRelacionados(Long id, Pageable pageable) {
        ConteudoEntity entity = repository.findById(id)
                .orElseThrow(() -> new ConteudoNaoEncontradoException(id));
        return repository.findByCategoriaAndIdNot(entity.getCategoria(), id, pageable)
                .map(this::toResponse);
    }

    public ConteudoResponse toResponse(ConteudoEntity entity) {
        return new ConteudoResponse(
                entity.getId(),
                entity.getTitulo(),
                entity.getCategoria(),
                entity.getProbabilidade(),
                entity.getInformacoesAdicionais()
        );

    }


}
