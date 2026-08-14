package com.g9team04.techmind.conteudo.internal;

import aj.org.objectweb.asm.commons.Remapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ConteudoRepository extends JpaRepository<ConteudoEntity, Long> {

    List<ConteudoEntity> findByTextoHashIn(Collection<String> textosHashes);
    Optional<ConteudoEntity> findByTextoHash(String textoHash);
    Page<ConteudoEntity> findByTituloContainingIgnoreCase(String titulo, Pageable pageable);
    Page<ConteudoEntity> findByCategoriaContainingIgnoreCase(String categoria, Pageable pageable);
    Page<ConteudoEntity> findByCategoriaAndIdNot( String categoria, Long id,  Pageable pageable);
    boolean existsByCategoriaContainingIgnoreCase(String categoria);

}
