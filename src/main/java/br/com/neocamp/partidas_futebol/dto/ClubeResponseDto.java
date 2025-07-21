package br.com.neocamp.partidas_futebol.dto;

import java.time.LocalDate;

// DTO de resposta de clube contém apenas os atributos que serão retornados na resposta da API (Ex: GET - Saída de dados)
public class ClubeResponseDto {

    // Atributos, construtores, getters e setters do DTO de resposta do clube, necessários para o Spring fazer a conversão do objeto para JSON na resposta da API

    // Atributos do DTO de resposta de clube
    private Long id;
    private String nome;
    private String siglaEstado;
    private LocalDate dataCriacao;
    private Boolean ativo;

    // Construtor vazio do DTO de resposta de clube
    public ClubeResponseDto() {}

    // Construtor com todos os atributos do DTO de resposta de clube
    public ClubeResponseDto(Long id, String nome, String siglaEstado, LocalDate dataCriacao, Boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.siglaEstado = siglaEstado;
        this.dataCriacao = dataCriacao;
        this.ativo = ativo;
    }

    // Getters e setters dos atributos do DTO de resposta de clube
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

