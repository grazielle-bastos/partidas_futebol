package br.com.neocamp.partidas_futebol.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name="clube")
public class Clube {

    
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "clubeMandante")
    private List<Partida> partidasMandante = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "clubeVisitante")
    private List<Partida> partidasVisitante = new ArrayList<>();


    public Clube(){}

    
    public Clube(String nome, String siglaEstado, LocalDate dataCriacao, Boolean ativo) {
        this.nome = nome;
        this.siglaEstado = siglaEstado;
        this.dataCriacao = dataCriacao;
        this.ativo = ativo;
    }

    
    public Clube(Long id, String nome, String siglaEstado, LocalDate dataCriacao, Boolean ativo) {
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

    public List<Partida> getPartidasMandante() {
        return partidasMandante;
    }

    public void setPartidasMandante(List<Partida> partidas) {
        this.partidasMandante = partidasMandante;
    }

    public List<Partida> getPartidasVisitante() {
        return partidasVisitante;
    }

    public void setPartidasVisitante(List<Partida> partidas) {
        this.partidasVisitante = partidasVisitante;
    }

}
