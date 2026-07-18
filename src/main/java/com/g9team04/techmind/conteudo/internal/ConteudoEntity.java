// conteudo/internal/ConteudoEntity.java
package com.g9team04.techmind.conteudo.internal;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_conteudo")
public class ConteudoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;


    @Column(name = "texto", columnDefinition = "CLOB", nullable = false)
    private String texto;

    @Column(name = "texto_hash", nullable = false, length = 64)
    private String textoHash;

    private String categoria;

    private Double probabilidade;

    @Convert(converter = StringListConverter.class)
    @Column(name = "Informacoes_adicionais")
    private List<String> informacoesAdicionais =  new ArrayList<>();

    @Column(name = "criado_em")
    private LocalDateTime criadoEm;

    @PrePersist
    protected void onCreate() {
        this.criadoEm = LocalDateTime.now(ZoneId.of("UTC"));
    }

    protected ConteudoEntity() {}

    public ConteudoEntity(String titulo, String texto) {
        this.titulo = titulo;
        this.texto = texto;
        this.textoHash = HashUtils.sha256(texto);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getTextoHash() { return textoHash; }
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