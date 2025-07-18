package br.com.neocamp.partidas_futebol.dto;

import java.time.LocalDate;

// DTO de requisição de clube
public class ClubeRequestDto {

    // Atributos do DTO de requisição de clube
    private Long id;
    private String nome;
    private String siglaEstado;
    private LocalDate dataCriacao;
    private Boolean ativo;

    // Construtor vazio do DTO de requisição de clube
    // Necessário para o Spring fazer a conversão do JSON da requisição para o objeto DTO
    public ClubeRequestDto() {}

    // Construtor com todos os atributos do DTO de requisição de clube
    public ClubeRequestDto(Long id, String nome, String siglaEstado, LocalDate dataCriacao, Boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.siglaEstado = siglaEstado;
        this.dataCriacao = dataCriacao;
        this.ativo = ativo;
    }

    // Getters e setters dos atributos do DTO de requisição de clube
    // Necessários para o Spring fazer a conversão do JSON da requisição para o objeto DTO
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
