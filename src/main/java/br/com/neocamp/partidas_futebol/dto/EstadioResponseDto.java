package br.com.neocamp.partidas_futebol.dto;

// DTO de resposta de estádio contém apenas os atributos que serão retornados na resposta da API (Ex: GET - Saída de dados)
public class EstadioResponseDto {

    // Atributos, construtores, getters e setters do DTO de resposta de estádio, necessários para o Spring fazer a conversão do objeto para JSON na resposta da API

    // Atributos do DTO de resposta de estádio
    private Long id;
    private String nome;

    // Construtor vazio do DTO de resposta de estádio
    public EstadioResponseDto(){}

    // Construtor com todos os atributos do DTO de resposta de estádio
    public EstadioResponseDto(Long id, String nome){
        this.id = id;
        this.nome = nome;
    }

    // Getters e setters dos atributos do DTO de resposta de estádio
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


}
