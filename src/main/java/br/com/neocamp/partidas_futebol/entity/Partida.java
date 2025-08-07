package br.com.neocamp.partidas_futebol.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "partida")
public class Partida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long partidaId;

    @ManyToOne
    @JoinColumn(name = "clube_mandante_id", referencedColumnName = "id", nullable = false)
    private Clube clubeMandante;
    @Column(name = "clube_mandante_id", nullable = false)
    private Long clubeMandanteId;

    @Column(name = "clube_visitante_id", nullable = false)
    private Long clubeVisitanteId;

    @Column(name = "clube_mandante_gols", nullable = true)
    private Integer clubeMandanteGols;

    @Column(name = "clube_visitante_gols", nullable = true)
    private Integer clubeVisitanteGols;

    @Column(name = "estadio_id", nullable = false)
    private Long estadioId;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;


    public Partida() {}

    public Partida(Long partidaId, Clube clubeMandante, Long clubeMandanteId, Long clubeVisitanteId, Integer clubeMandanteGols, Integer clubeVisitanteGols, Long estadioId, LocalDateTime dataHora) {
        this.partidaId = partidaId;
        this.clubeMandante = clubeMandante;
        this.clubeMandanteId = clubeMandanteId;
        this.clubeVisitanteId = clubeVisitanteId;
        this.clubeMandanteGols = clubeMandanteGols;
        this.clubeVisitanteGols = clubeVisitanteGols;
        this.estadioId = estadioId;
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

    public Long getClubeMandanteId() {
        return clubeMandanteId;
    }

    public void setClubeMandanteId(Long clubeMandanteId) {
        this.clubeMandanteId = clubeMandanteId;
    }

    public Long getClubeVisitanteId() {
        return clubeVisitanteId;
    }

    public void setClubeVisitanteId(Long clubeVisitanteId) {
        this.clubeVisitanteId = clubeVisitanteId;
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

    public Long getEstadioId() {
        return estadioId;
    }

    public void setEstadioId(Long estadioId) {
        this.estadioId = estadioId;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }
}
