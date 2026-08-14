// conteudo/ConteudoService.java
package com.g9team04.techmind.conteudo;

import com.g9team04.techmind.conteudo.internal.ConteudoEntity;
import com.g9team04.techmind.conteudo.internal.ConteudoRepository;
import com.g9team04.techmind.conteudo.internal.HashUtils;
import com.g9team04.techmind.infrastructure.ConteudoNaoEncontradoException;
import org.springframework.cache.annotation.Cacheable;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
        // Agora passa a consultar o método que utiliza o Redis Cache
        return buscarPorHashCacheado(hash)
                .map(this::toResponse)
                .orElseGet(() -> classificarEPersistir(request));
    }
    @Cacheable(value = "conteudoCache", key = "#hash", unless = "#result.isEmpty()")
    protected Optional<ConteudoEntity> buscarPorHashCacheado(String hash) {
        return repository.findByTextoHash(hash);
    }

    public void processarLote(List<ConteudoRequest> requests) {
        // 1. Extrai todos os hashes dos itens que vieram na requisição em lote
        List<String> hashes = requests.stream()
                .map(r -> HashUtils.sha256(r.texto()))
                .toList();

        // 2. Busca todos os registros existentes de uma só vez no banco (Elimina o N+1)
        List<ConteudoEntity> existentes = repository.findByTextoHashIn(hashes);

        // 3. Mapeia os existentes para fácil consulta em memória
        Set<String> hashesExistentes = existentes.stream()
                .map(ConteudoEntity::getTextoHash)
                .collect(Collectors.toSet());

        // 4. Filtra e processa apenas os que ainda não existem
        List<ConteudoEntity> novos = requests.stream()
                .filter(r -> !hashesExistentes.contains(HashUtils.sha256(r.texto())))
                .map(r -> {
                    var entity = new ConteudoEntity(r.titulo(), r.texto());
                    var resultado = classifier.classificar(entity.getTexto());
                    entity.setCategoria(resultado.categoria());
                    entity.setProbabilidade(resultado.probabilidade());
                    entity.setInformacoesAdicionais(resultado.tags());
                    return entity;
                })
                .toList();

        // 5. Salva todos os novos de uma vez
        if (!novos.isEmpty()) {
            repository.saveAll(novos);
        }
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
                .map(this::toResponse)                // reutiliza o conversor
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
