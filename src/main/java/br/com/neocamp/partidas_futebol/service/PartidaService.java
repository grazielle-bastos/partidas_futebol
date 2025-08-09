package br.com.neocamp.partidas_futebol.service;

import br.com.neocamp.partidas_futebol.dto.partidaDto.PartidaRequestDto;
import br.com.neocamp.partidas_futebol.dto.partidaDto.PartidaResponseDto;
import br.com.neocamp.partidas_futebol.entity.Partida;
import br.com.neocamp.partidas_futebol.repository.ClubeRepository;
import br.com.neocamp.partidas_futebol.repository.EstadioRepository;
import br.com.neocamp.partidas_futebol.repository.PartidaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PartidaService {

    private final PartidaRepository partidaRepository;

    @Autowired
    private final ClubeRepository clubeRepository;

    @Autowired
    private final EstadioRepository estadioRepository;

    @Autowired
    public PartidaService(PartidaRepository partidaRepository, ClubeRepository clubeRepository, EstadioRepository estadioRepository) {
        this.partidaRepository = partidaRepository;
        this.clubeRepository = clubeRepository;
        this.estadioRepository = estadioRepository;
    }

    public PartidaResponseDto cadastrarPartida(PartidaRequestDto partidaRequestDto) {

        if (partidaRequestDto.getClubeMandanteId() == null ||
                partidaRequestDto.getClubeVisitanteId() == null ||
                partidaRequestDto.getClubeMandanteGols() == null ||
                partidaRequestDto.getClubeVisitanteGols() == null ||
                partidaRequestDto.getEstadioId() == null ||
                partidaRequestDto.getDataHora() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Todos os campos são obrigatórios e não podem ser vazios");
        }

        if (partidaRequestDto.getClubeMandanteId().equals(partidaRequestDto.getClubeVisitanteId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Clube mandante e visitante não podem ser o mesmo");
        }

        var clubeMandanteOptional = clubeRepository.findById(partidaRequestDto.getClubeMandanteId());
        if (clubeMandanteOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Clube mandante não encontrado");
        }
        var clubeMandante = clubeMandanteOptional.get();
        String nomeMandante = clubeMandante.getNome();

        var clubeVisitanteOptional = clubeRepository.findById(partidaRequestDto.getClubeVisitanteId());
        if (clubeVisitanteOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Clube visitante não encontrado");
        }
        var clubeVisitante = clubeVisitanteOptional.get();
        String nomeVisitante = clubeVisitante.getNome();

        var estadioOptional = estadioRepository.findById(partidaRequestDto.getEstadioId());
        if (estadioOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Estádio não encontrado");
        }
        var estadio = estadioOptional.get();

        if (partidaRequestDto.getClubeMandanteGols() < 0 || partidaRequestDto.getClubeVisitanteGols() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Gols não podem ser negativos");
        }

        if (partidaRequestDto.getDataHora().isBefore(java.time.LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data e hora da partida não podem ser no passado");
        }

    // TODO: Pendências de implementação no método cadastrarPartida:
        // - Validar se a data/hora da partida não é anterior à data de criação dos clubes envolvidos (409 CONFLICT)
        // - Impedir cadastro de partida com clube inativo (409 CONFLICT)
        // - Verificar se algum clube já possui outra partida em menos de 48h (409 CONFLICT)
        // - Garantir que o estádio não tenha outra partida no mesmo dia (409 CONFLICT)
        // Consulte requisitos funcionais 6 e 7 no projeto para detalhes.
        // Dica: Use métodos auxiliares para cada validação e trate as exceções conforme o padrão do projeto.

        Partida partida = new Partida(

                clubeMandante,
                clubeVisitante,
                partidaRequestDto.getClubeMandanteGols(),
                partidaRequestDto.getClubeVisitanteGols(),
                estadio,
                partidaRequestDto.getDataHora()
        );

        return toResponseDto(partidaRepository.save(partida));
    }



    private PartidaResponseDto toResponseDto(Partida partida) {

        return new PartidaResponseDto(

                partida.getPartidaId(),
                partida.getClubeMandante().getId(),
                partida.getClubeMandante().getNome(),
                partida.getClubeVisitante().getId(),
                partida.getClubeVisitante().getNome(),
                partida.getClubeMandanteGols(),
                partida.getClubeVisitanteGols(),
                partida.getEstadio().getId(),
                partida.getEstadio().getNome(),
                partida.getDataHora()
        );
    }

}