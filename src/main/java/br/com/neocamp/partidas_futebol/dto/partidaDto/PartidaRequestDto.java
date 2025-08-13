package br.com.neocamp.partidas_futebol.dto.partidaDto;

import java.time.LocalDateTime;

public class PartidaRequestDto {

    private Long partidaId;

    private Long clubeMandanteId;

    private Long clubeVisitanteId;

    private Integer clubeMandanteGols;

    private Integer clubeVisitanteGols;

    private Long estadioId;

    private LocalDateTime dataHora;


    public PartidaRequestDto() {}

    public PartidaRequestDto(Long partidaId, Long clubeMandanteId, Long clubeVisitanteId, Integer clubeMandanteGols, Integer clubeVisitanteGols, Long estadioId, LocalDateTime dataHora) {
        this.partidaId = partidaId;
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
