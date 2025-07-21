package br.com.neocamp.partidas_futebol.dto;

// DTO de requisição de estádio contém apenas os atributos que serão recebidos na requisição da API (Ex: POST, PUT, DELETE - Entrada de dados)
public class EstadioRequestDto {

    // Atributos, construtores, getters e setters do DTO de requisição de estádio, necessários para o Spring fazer a conversão do JSON para objeto na requisição da API

    // Atributos do DTO de requisição de estádio
    private Long id;
    private String nome;

    // Construtor vazio do DTO de requisição de estádio
    public EstadioRequestDto(){}

    // Construtor com todos os atributos do DTO de requisição de estádio
    public EstadioRequestDto(Long id, String nome){
        this.id = id;
        this.nome = nome;
    }

    // Getters e setters dos atributos do DTO de requisição de estádio
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
