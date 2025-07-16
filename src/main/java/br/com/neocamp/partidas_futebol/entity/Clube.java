package br.com.neocamp.partidas_futebol.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

import java.time.LocalDate;

// Entidade Clube, com seus atributos e anotações para mapeamento do JPA
@Entity
@Table(name="clube")
public class Clube {

    // Atributo id com anotações para mapeamento do JPA
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(name = "sigla_estado", nullable = false, length = 2)
    private String siglaEstado;

    @Column(name = "data_criacao", nullable = false)
    private LocalDate dataCriacao;

    @Column(nullable = false)
    private Boolean ativo;

    // Construtor vazio da entidade Clube
    public Clube(){}

    // Construtor com todos os atributos da entidade Clube
    public Clube(String nome, String siglaEstado, LocalDate dataCriacao, Boolean ativo) {
        this.nome = nome;
        this.siglaEstado = siglaEstado;
        this.dataCriacao = dataCriacao;
        this.ativo = ativo;
    }

    // Construtor com todos os atributos da entidade Clube, incluindo o id
    public Clube(Long id, String nome, String siglaEstado, LocalDate dataCriacao, Boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.siglaEstado = siglaEstado;
        this.dataCriacao = dataCriacao;
        this.ativo = ativo;
    }

    // Getters e setters dos atributos da entidade Clube
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSiglaEstado() {
        return siglaEstado;
    }

    public void setSiglaEstado(String siglaEstado) {
        this.siglaEstado = siglaEstado;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

}
