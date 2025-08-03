package br.com.neocamp.partidas_futebol.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class ClubeRequestDto {

    
    private Long id;

    
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, message = "Nome deve ter no mínimo 2 letras")
    private String nome;

    
    @NotBlank(message = "Sigla do estado é obrigatória")
    @Pattern(regexp = "AC|AL|AP|AM|BA|CE|DF|ES|GO|MA|MT|MS|MG|PA|PB|PR|PE|PI|RJ|RN|RS|RO|RR|SC|SP|SE|TO", message = "Sigla do estado inválida")
    private String siglaEstado;

    
    @NotNull(message = "Data de criação é obrigatória")
    @PastOrPresent(message = "Data de criação não pode ser futura")
    private LocalDate dataCriacao;

    
    private Boolean ativo;

    
    public ClubeRequestDto() {}

    
    public ClubeRequestDto(Long id, String nome, String siglaEstado, LocalDate dataCriacao, Boolean ativo) {
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
