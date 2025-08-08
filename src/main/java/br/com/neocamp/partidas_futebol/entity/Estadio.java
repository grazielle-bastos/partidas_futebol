package br.com.neocamp.partidas_futebol.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "estadio",
       uniqueConstraints = @UniqueConstraint(columnNames = "nome"))
public class Estadio {

    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100, unique = true)
    private String nome;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "estadio")
    private List<Partida> partidas = new ArrayList<>();


    public Estadio() {}

    public Estadio(String nome) {
        this.nome = nome;
    }


    public Estadio(Long id, String nome) {
        this.id = id;
        this.nome = nome;
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


    public List<Partida> getPartidas(){
        return partidas;
    }

    public void setPartidas(List<Partida> partidas) {
        this.partidas = partidas;
    }
}
