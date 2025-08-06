package br.com.neocamp.partidas_futebol.dto.partidaDto;

import java.time.LocalDateTime;

public class PartidaResponseDto {

    private Long partidaId;

    private Long clubeMandanteId;

    private String clubeMandanteNome;

    private Long clubeVisitanteId;

    private String clubeVisitanteNome;

    private Integer clubeMandanteGols;

    private Integer clubeVisitanteGols;

    private Long estadioId;

    private String estadioNome;

    private LocalDateTime dataHora;


    public PartidaResponseDto() {}

    public PartidaResponseDto(Long partidaId, Long clubeMandanteId, String clubeMandanteNome, Long clubeVisitanteId, String clubeVisitanteNome, Integer clubeMandanteGols, Integer clubeVisitanteGols, Long estadioId, String estadioNome, LocalDateTime dataHora) {
        this.partidaId = partidaId;
        this.clubeMandanteId = clubeMandanteId;
        this.clubeMandanteNome = clubeMandanteNome;
        this.clubeVisitanteId = clubeVisitanteId;
        this.clubeVisitanteNome = clubeVisitanteNome;
        this.clubeMandanteGols = clubeMandanteGols;
        this.clubeVisitanteGols = clubeVisitanteGols;
        this.estadioId = estadioId;
        this.estadioNome = estadioNome;
        this.dataHora = dataHora;
    }


    public Long getPartidaId() {
        return partidaId;
    }

    public void setPartidaId(Long partidaId) {
        this.partidaId = partidaId;
    }

    public Long getClubeMandanteId() {
        return clubeMandanteId;
    }

    public void setClubeMandanteId(Long clubeMandanteId) {
        this.clubeMandanteId = clubeMandanteId;
    }

    public String getClubeMandanteNome() {
        return clubeMandanteNome;
    }

    public void setClubeMandanteNome(String clubeMandanteNome) {
        this.clubeMandanteNome = clubeMandanteNome;
    }

    public Long getClubeVisitanteId() {
        return clubeVisitanteId;
    }

    public void setClubeVisitanteId(Long clubeVisitanteId) {
        this.clubeVisitanteId = clubeVisitanteId;
    }

    public String getClubeVisitanteNome() {
        return clubeVisitanteNome;
    }

    public void setClubeVisitanteNome(String clubeVisitanteNome) {
        this.clubeVisitanteNome = clubeVisitanteNome;
    }

    public Integer getClubeMandanteGols() {
        return clubeMandanteGols;
    }

    public void setClubeMandanteGols(Integer golsMandanteClube) {
        this.clubeMandanteGols = golsMandanteClube;
    }

    public Integer getClubeVisitanteGols() {
        return clubeVisitanteGols;
    }

    public void setClubeVisitanteGols(Integer golsVisitanteClube) {
        this.clubeVisitanteGols = golsVisitanteClube;
    }

    public Long getEstadioId() {
        return estadioId;
    }

    public void setEstadioId(Long estadioId) {
        this.estadioId = estadioId;
    }

    public String getEstadioNome() {
        return estadioNome;
    }

    public void setEstadioNome(String estadioNome) {
        this.estadioNome = estadioNome;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

}
