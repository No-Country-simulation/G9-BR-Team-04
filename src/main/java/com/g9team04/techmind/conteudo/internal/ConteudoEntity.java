// conteudo/internal/ConteudoEntity.java
package com.g9team04.techmind.conteudo.internal;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Entity
@Table(name = "tb_conteudo")
public class ConteudoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String texto;

    private String categoria;

    private Double probabilidade;

    @Convert(converter = StringListConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<String> informacoesAdicionais;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm;

    @PrePersist
    protected void onCreate() {
        this.criadoEm = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
    }

    protected ConteudoEntity() {}

    public ConteudoEntity(String titulo, String texto) {
        this.titulo = titulo;
        this.texto = texto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public Double getProbabilidade() { return probabilidade; }
    public void setProbabilidade(Double probabilidade) { this.probabilidade = probabilidade; }
    public List<String> getInformacoesAdicionais() { return informacoesAdicionais; }
    public void setInformacoesAdicionais(List<String> informacoesAdicionais) { this.informacoesAdicionais = informacoesAdicionais; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
}