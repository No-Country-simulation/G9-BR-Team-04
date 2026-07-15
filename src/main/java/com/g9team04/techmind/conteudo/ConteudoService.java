// conteudo/ConteudoService.java
package com.g9team04.techmind.conteudo;

import com.g9team04.techmind.conteudo.internal.ConteudoEntity;
import com.g9team04.techmind.conteudo.internal.ConteudoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

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
        // Cache: verifica se já existe conteúdo igual
        return repository.findByTituloAndTexto(request.titulo(), request.texto())
                .map(entity -> new ConteudoResponse(
                        entity.getCategoria(),
                        entity.getProbabilidade(),
                        entity.getInformacoesAdicionais()
                ))
                .orElseGet(() -> classificarEPersistir(request));
    }

    private ConteudoResponse classificarEPersistir(ConteudoRequest request) {
        var entity = new ConteudoEntity(request.titulo(), request.texto());

        // Chama o motor de IA
        var resultado = classifier.classificar(entity.getTexto());
        entity.setCategoria(resultado.categoria());
        entity.setProbabilidade(resultado.probabilidade());
        entity.setInformacoesAdicionais(resultado.tags());

        // Persiste o novo resultado
        repository.save(entity);

        return new ConteudoResponse(
                entity.getCategoria(),
                entity.getProbabilidade(),
                entity.getInformacoesAdicionais()
        );
    }
}