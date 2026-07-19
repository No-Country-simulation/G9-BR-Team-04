package com.g9team04.techmind.conteudo.internal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConteudoRepository extends JpaRepository<ConteudoEntity, Long> {

    Optional<ConteudoEntity> findByTituloAndTextoHash(String titulo, String textoHash);
    Page<ConteudoEntity> findByTituloContainingIgnoreCase(String titulo, Pageable pageable);
    Page<ConteudoEntity> findByCategoriaContainingIgnoreCase(String categoria, Pageable pageable);
    Page<ConteudoEntity> findByCategoriaAndIdNot( String categoria, Long id,  Pageable pageable);
    boolean existsByCategoriaContainingIgnoreCase(String categoria);

}
