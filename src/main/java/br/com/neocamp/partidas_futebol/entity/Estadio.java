package br.com.neocamp.partidas_futebol.entity;

import jakarta.persistence.*;

// Entidade Estadio, com seus atributos e anotações para mapeamento do JPA
// A anotação @Entity indica que essa classe é uma entidade do banco de dados
// A anotação @Table(name = "estadio") indica o nome da tabela no banco de dados
@Entity
@Table(name = "estadio")
public class Estadio {

    // Atributo id com anotação @Id para indicar que é a chave primária da tabela
    // A anotação @GeneratedValue(strategy = GenerationType.IDENTITY) indica que o valor do id será gerado automaticamente pelo banco de dados
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Atributo nome com anotação @Column para indicar o nome da coluna no banco de dados e outras configurações
    // A anotação @Column(nullable = false, length = 100, unique = true) indica que o campo não pode ser nulo, o tamanho do campo é 100 caracteres e o campo é único no banco de dados
    @Column(nullable = false, length = 100, unique = true)
    private String nome;

    //Construtor vazio da entidade Estadio
    public Estadio() {}

    //Construtor com todos os atributos da entidade Estadio
    public Estadio(String nome) {
        this.nome = nome;
    }

    //Construtor com todos os atributos da entidade Estadio, incluindo o id
    public Estadio(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    // Getters e setters dos atributos da entidade Estadio
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
