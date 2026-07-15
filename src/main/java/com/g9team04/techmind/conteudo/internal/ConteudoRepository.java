package com.g9team04.techmind.conteudo.internal;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConteudoRepository extends JpaRepository<ConteudoEntity, Long> {
    Optional<ConteudoEntity> findByTituloAndTexto(String titulo, String texto);
}
