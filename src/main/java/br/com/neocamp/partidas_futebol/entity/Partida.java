package br.com.neocamp.partidas_futebol.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "partida")
public class Partida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long partidaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clube_mandante_id", referencedColumnName = "id", nullable = false)
    private Clube clubeMandante;

    @ManyToOne
    @JoinColumn(name = "clube_visitante_id", referencedColumnName = "id", nullable = false)
    private Clube clubeVisitante;

    @Column(name = "clube_mandante_gols", nullable = true)
    private Integer clubeMandanteGols;

    @Column(name = "clube_visitante_gols", nullable = true)
    private Integer clubeVisitanteGols;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estadio_id", referencedColumnName = "id", nullable = false)
    private Estadio estadio;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;


    public Partida() {}

    public Partida(Long partidaId, Clube clubeMandante, Clube clubeVisitante, Integer clubeMandanteGols, Integer clubeVisitanteGols, Estadio estadio, LocalDateTime dataHora) {
        this.partidaId = partidaId;
        this.clubeMandante = clubeMandante;
        this.clubeVisitante = clubeVisitante;
        this.clubeMandanteGols = clubeMandanteGols;
        this.clubeVisitanteGols = clubeVisitanteGols;
        this.estadio = estadio;
        this.dataHora = dataHora;
    }


    public Long getPartidaId() {
        return partidaId;
    }

    public void setPartidaId(Long partidaId) {
        this.partidaId = partidaId;
    }

    public Clube getClubeMandante() {
        return clubeMandante;
    }

    public void setClubeMandante(Clube clubeMandante) {
        this.clubeMandante = clubeMandante;
    }

    public Clube getClubeVisitante() {
        return clubeVisitante;
    }

    public void setClubeVisitante(Clube clubeVisitante) {
        this.clubeVisitante = clubeVisitante;
    }

    public Integer getClubeMandanteGols() {
        return clubeMandanteGols;
    }

    public void setClubeMandanteGols(Integer clubeMandanteGols) {
        this.clubeMandanteGols = clubeMandanteGols;
    }

    public Integer getClubeVisitanteGols() {
        return clubeVisitanteGols;
    }

    public void setClubeVisitanteGols(Integer clubeVisitanteGols) {
        this.clubeVisitanteGols = clubeVisitanteGols;
    }

    public Estadio getEstadio() {
        return estadio;
    }

    public void setEstadio(Estadio estadio) {
        this.estadio = estadio;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }
}
