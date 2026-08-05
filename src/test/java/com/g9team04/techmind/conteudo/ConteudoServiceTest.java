package com.g9team04.techmind.conteudo;

import com.g9team04.techmind.conteudo.internal.ClassificacaoResponse;
import com.g9team04.techmind.conteudo.internal.ConteudoEntity;
import com.g9team04.techmind.conteudo.internal.ConteudoRepository;
import com.g9team04.techmind.infrastructure.ConteudoNaoEncontradoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConteudoServiceTest {

    @Mock
    private ConteudoRepository repository;

    @Mock
    private ClassifierService classifier;

    @InjectMocks
    private ConteudoService conteudoService;

    @Test
    void deveRetornarConteudoExistenteQuandoJaFoiProcessadoAntes() {
        var request = new ConteudoRequest("Spring Boot", "Introdução ao Spring Boot", null, null);
        var entityExistente = new ConteudoEntity("Spring Boot", "Introdução ao Spring Boot");
        entityExistente.setCategoria("Backend");
        entityExistente.setProbabilidade(0.89);
        entityExistente.setInformacoesAdicionais(List.of("Java", "Spring Boot"));

        when(repository.findByTituloAndTextoHash(eq(request.titulo()), anyString()))
                .thenReturn(Optional.of(entityExistente));

        var resultado = conteudoService.processar(request);

        assertThat(resultado.categoria()).isEqualTo("Backend");
        verify(classifier, never()).classificar(anyString());
        verify(repository, never()).save(any());
    }

    @Test
    void deveClassificarEPersistirQuandoConteudoForNovo() {
        var request = new ConteudoRequest("Spring Boot", "Introdução ao Spring Boot", null, null);

        when(repository.findByTituloAndTextoHash(anyString(), anyString()))
                .thenReturn(Optional.empty());

        var classificacao = new ClassificacaoResponse("Backend", 0.89, List.of("Java", "Spring Boot"));
        when(classifier.classificar(anyString())).thenReturn(classificacao);

        when(repository.save(any(ConteudoEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var resultado = conteudoService.processar(request);

        assertThat(resultado.categoria()).isEqualTo("Backend");
        assertThat(resultado.probabilidade()).isEqualTo(0.89);
        verify(classifier).classificar(request.texto());
        verify(repository).save(any(ConteudoEntity.class));
    }

    @Test
    void deveBuscarPorCategoriaQuandoCategoriaExiste() {
        var pageable = Pageable.unpaged();
        var entity = new ConteudoEntity("Titulo", "Texto");
        entity.setCategoria("Backend");

        when(repository.existsByCategoriaContainingIgnoreCase("Backend")).thenReturn(true);
        when(repository.findByCategoriaContainingIgnoreCase(eq("Backend"), eq(pageable)))
                .thenReturn(new PageImpl<>(List.of(entity)));

        var resultado = conteudoService.buscarPorCategoria("Backend", pageable);

        assertThat(resultado.getContent()).hasSize(1);
    }

    @Test
    void deveLancarExcecaoQuandoCategoriaNaoExiste() {
        var pageable = Pageable.unpaged();

        when(repository.existsByCategoriaContainingIgnoreCase("Inexistente")).thenReturn(false);

        assertThatThrownBy(() -> conteudoService.buscarPorCategoria("Inexistente", pageable))
                .isInstanceOf(ConteudoNaoEncontradoException.class);

        verify(repository, never()).findByCategoriaContainingIgnoreCase(any(), any());
    }

    @Test
    void deveBuscarRelacionadosQuandoIdExiste() {
        var pageable = Pageable.unpaged();
        var entity = new ConteudoEntity("Titulo", "Texto");
        entity.setCategoria("Backend");

        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(repository.findByCategoriaAndIdNot(eq("Backend"), eq(1L), eq(pageable)))
                .thenReturn(new PageImpl<>(List.of()));

        var resultado = conteudoService.buscarRelacionados(1L, pageable);

        assertThat(resultado).isNotNull();
        verify(repository).findByCategoriaAndIdNot("Backend", 1L, pageable);
    }

    @Test
    void deveLancarExcecaoQuandoIdNaoExisteParaBuscarRelacionados() {
        var pageable = Pageable.unpaged();

        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> conteudoService.buscarRelacionados(99L, pageable))
                .isInstanceOf(ConteudoNaoEncontradoException.class);
    }
}