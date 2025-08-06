package br.com.neocamp.partidas_futebol.dto.clubeDto;

import java.time.LocalDate;


public class ClubeResponseDto {

    
    private Long id;

    
    private String nome;

    
    private String siglaEstado;

    
    private LocalDate dataCriacao;

    
    private Boolean ativo;

    
    public ClubeResponseDto() {}

    
    public ClubeResponseDto(Long id, String nome, String siglaEstado, LocalDate dataCriacao, Boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.siglaEstado = siglaEstado;
        this.dataCriacao = dataCriacao;
        this.ativo = ativo;
    }

    
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

