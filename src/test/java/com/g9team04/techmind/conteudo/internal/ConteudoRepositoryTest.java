package com.g9team04.techmind.conteudo.internal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ConteudoRepositoryTest {

    @Autowired
    private ConteudoRepository repository;

    @BeforeEach
    void cleanUp() {
        repository.deleteAll();
        repository.flush();
    }

    @Test
    void deveEncontrarPorTituloETextoHash() {
        var entity = new ConteudoEntity("Spring Boot", "Introdução ao Spring Boot");
        repository.save(entity);

        var resultado = repository.findByTituloAndTextoHash("Spring Boot", entity.getTextoHash());

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getTitulo()).isEqualTo("Spring Boot");
    }

    @Test
    void naoDeveEncontrarQuandoTextoHashNaoBate() {
        var entity = new ConteudoEntity("Spring Boot", "Introdução ao Spring Boot");
        repository.save(entity);

        var resultado = repository.findByTituloAndTextoHash("Spring Boot", "hash-inexistente");

        assertThat(resultado).isEmpty();
    }

    @Test
    void deveEncontrarPorTituloContendoIgnorandoCase() {
        repository.saveAndFlush(new ConteudoEntity("Introdução ao Spring Boot", "Texto 1"));
        repository.saveAndFlush(new ConteudoEntity("Outro assunto qualquer", "Texto 2"));

        var resultado = repository.findByTituloContainingIgnoreCase("spring", PageRequest.of(0, 10));

        assertThat(resultado.getContent()).hasSize(1);
        assertThat(resultado.getContent().get(0).getTitulo()).contains("Spring");
    }

    @Test
    void deveEncontrarPorCategoriaContendoIgnorandoCase() {
        var entity1 = new ConteudoEntity("Titulo 1", "Texto 1");
        entity1.setCategoria("Backend");
        var entity2 = new ConteudoEntity("Titulo 2", "Texto 2");
        entity2.setCategoria("Frontend");

        repository.save(entity1);
        repository.save(entity2);

        var resultado = repository.findByCategoriaContainingIgnoreCase("back", PageRequest.of(0, 10));

        assertThat(resultado.getContent()).hasSize(1);
        assertThat(resultado.getContent().get(0).getCategoria()).isEqualTo("Backend");
    }

    @Test
    void deveEncontrarPorCategoriaExcluindoIdEspecifico() {
        var entity1 = new ConteudoEntity("Titulo 1", "Texto 1");
        entity1.setCategoria("Backend");
        var entity2 = new ConteudoEntity("Titulo 2", "Texto 2");
        entity2.setCategoria("Backend");

        var salvo1 = repository.saveAndFlush(entity1);
        repository.saveAndFlush(entity2);

        var resultado = repository.findByCategoriaAndIdNot("Backend", salvo1.getId(), PageRequest.of(0, 10));

        assertThat(resultado.getContent()).hasSize(1);
        assertThat(resultado.getContent().get(0).getId()).isNotEqualTo(salvo1.getId());
    }

    @Test
    void deveRetornarTrueQuandoCategoriaExiste() {
        var entity = new ConteudoEntity("Titulo", "Texto");
        entity.setCategoria("Backend");
        repository.save(entity);

        var existe = repository.existsByCategoriaContainingIgnoreCase("back");

        assertThat(existe).isTrue();
    }

    @Test
    void deveRetornarFalseQuandoCategoriaNaoExiste() {
        var existe = repository.existsByCategoriaContainingIgnoreCase("Inexistente");

        assertThat(existe).isFalse();
    }
}