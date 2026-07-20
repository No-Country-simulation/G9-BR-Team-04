// conteudo/ConteudoService.java
package com.g9team04.techmind.conteudo;

import com.g9team04.techmind.conteudo.internal.ConteudoEntity;
import com.g9team04.techmind.conteudo.internal.ConteudoRepository;
import com.g9team04.techmind.conteudo.internal.HashUtils;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
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


    public List<ConteudoResponse> getAllConteudos(Pageable pageable) {
        return repository.findAll(pageable)
                .map(entity -> new ConteudoResponse(
                        entity.getId(),
                        entity.getTitulo(),
                        entity.getCategoria(),
                        entity.getProbabilidade(),
                        entity.getInformacoesAdicionais()
                ))
                .getContent();
    }

    public List<ConteudoResponse> buscarPorTitulo(String titulo, Pageable pageable) {
        return repository.findByTitulo(titulo, pageable)
                .map(entity -> new ConteudoResponse(
                        entity.getId(),
                        entity.getTitulo(),
                        entity.getCategoria(),
                        entity.getProbabilidade(),
                        entity.getInformacoesAdicionais()
                ))
                .getContent();
    }
}