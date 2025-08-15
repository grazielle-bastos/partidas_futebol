package br.com.neocamp.partidas_futebol.service;

import br.com.neocamp.partidas_futebol.dto.partidaDto.PartidaRequestDto;
import br.com.neocamp.partidas_futebol.dto.partidaDto.PartidaResponseDto;
import br.com.neocamp.partidas_futebol.entity.Clube;
import br.com.neocamp.partidas_futebol.entity.Estadio;
import br.com.neocamp.partidas_futebol.entity.Partida;
import br.com.neocamp.partidas_futebol.exceptions.EntityBadRequestException;
import br.com.neocamp.partidas_futebol.exceptions.EntityConflictException;
import br.com.neocamp.partidas_futebol.repository.ClubeRepository;
import br.com.neocamp.partidas_futebol.repository.EstadioRepository;
import br.com.neocamp.partidas_futebol.repository.PartidaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;

@Service
public class PartidaService {

    private final PartidaRepository partidaRepository;
    private final ClubeRepository clubeRepository;
    private final EstadioRepository estadioRepository;

    @Autowired
    public PartidaService(PartidaRepository partidaRepository, ClubeRepository clubeRepository, EstadioRepository estadioRepository) {
        this.partidaRepository = partidaRepository;
        this.clubeRepository = clubeRepository;
        this.estadioRepository = estadioRepository;
    }

    public PartidaResponseDto cadastrarPartida(PartidaRequestDto partidaRequestDto) {

        Clube clubeMandante = clubeRepository.findById(partidaRequestDto.getClubeMandanteId())
                .orElseThrow(() -> new EntityNotFoundException("Clube mandante não encontrado"));
        Clube clubeVisitante = clubeRepository.findById(partidaRequestDto.getClubeVisitanteId())
                .orElseThrow(() -> new EntityNotFoundException("Clube visitante não encontrado"));
        Estadio estadio = buscarEstadio(partidaRequestDto.getEstadioId());
        LocalDateTime dataCriacaoMandante = clubeMandante.getDataCriacao().atStartOfDay();
        LocalDateTime dataCriacaoVisitante = clubeVisitante.getDataCriacao().atStartOfDay();
        LocalDateTime dataHora = partidaRequestDto.getDataHora();

        validarPartida(partidaRequestDto, dataCriacaoMandante, dataCriacaoVisitante, clubeMandante, clubeVisitante, estadio, dataHora);

        Partida partida = new Partida();
        partida.setClubeMandante(clubeMandante);
        partida.setClubeVisitante(clubeVisitante);
        partida.setClubeMandanteGols(partidaRequestDto.getClubeMandanteGols());
        partida.setClubeVisitanteGols(partidaRequestDto.getClubeVisitanteGols());
        partida.setEstadio(estadio);
        partida.setDataHora(partidaRequestDto.getDataHora());

        return toResponseDto(partidaRepository.save(partida));
    }

    private void validarCamposObrigatorios(PartidaRequestDto partidaRequestDto) {
        if (partidaRequestDto.getClubeMandanteId() == null ||
                partidaRequestDto.getClubeVisitanteId() == null ||
                partidaRequestDto.getClubeMandanteGols() == null ||
                partidaRequestDto.getClubeVisitanteGols() == null ||
                partidaRequestDto.getEstadioId() == null ||
                partidaRequestDto.getDataHora() == null) {
            throw new EntityBadRequestException("Todos os campos são obrigatórios e não podem ser vazios");
        }
    }

    private void validarClubesOponentesDiferentes(PartidaRequestDto partidaRequestDto) {
        if (partidaRequestDto.getClubeMandanteId().equals(partidaRequestDto.getClubeVisitanteId())) {
            throw new EntityBadRequestException("Clube mandante e visitante não podem ser o mesmo");
        }
    }

    private Estadio buscarEstadio(Long id) {
        return estadioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Estádio não encontrado"));
    }

    private void validarGolsNaoNegativos(PartidaRequestDto partidaRequestDto) {
        if (partidaRequestDto.getClubeMandanteGols() < 0 || partidaRequestDto.getClubeVisitanteGols() < 0) {
            throw new EntityBadRequestException("Gols não podem ser negativos");
        }
    }

    private void validarDataHoraAnteriorDataCriacaoClubes(PartidaRequestDto partidaRequestDto, LocalDateTime dataCriacaoMandante, LocalDateTime dataCriacaoVisitante) {
        if (partidaRequestDto.getDataHora().isBefore(dataCriacaoMandante) || partidaRequestDto.getDataHora().isBefore(dataCriacaoVisitante)) {
            throw new EntityConflictException ("Data e hora da partida não podem ser anteriores à data de criação dos clubes envolvidos");
        }
    }

    private void validarClubesInativos(Clube mandante, Clube visitante) {
        if (!mandante.getAtivo() || !visitante.getAtivo()) {
            throw new EntityConflictException("Não é possível realizar operações com clube inativo");
        }
    }

    private void validarIntervaloDePartidas(Clube mandante, Clube visitante, LocalDateTime dataHoraNovaPartida) {
        List<Partida> partidasMandante = partidaRepository.findByClubeMandanteId(mandante.getId());
        List<Partida> partidasVisitante = partidaRepository.findByClubeVisitanteId(visitante.getId());

        for (Partida partida : partidasMandante) {
            long diff = Math.abs(java.time.Duration.between(partida.getDataHora(), dataHoraNovaPartida).toHours());
            if (diff < 48) {
                throw new EntityConflictException("Clube mandante já possui outra partida em menos de 48 horas");
            }
        }

        for (Partida partida : partidasVisitante) {
            long diff = Math.abs(java.time.Duration.between(partida.getDataHora(), dataHoraNovaPartida).toHours());
            if (diff < 48) {
                throw new EntityConflictException("Clube visitante já possui outra partida em menos de 48 horas");
            }
        }

    }

    private void validarPartidaComEstadioDisponivel(Estadio estadio, LocalDateTime dataHoraNovaPartida) {
        List<Partida> partidasEstadio = estadio.getPartidas();
        for (Partida partida : partidasEstadio) {
            if (partida.getDataHora().toLocalDate().equals(dataHoraNovaPartida.toLocalDate())) {
                throw new EntityConflictException("Estádio já possui outra partida no mesmo dia");
            }
        }
    }


    public PartidaResponseDto buscarPartidaPorId(Long id) {
        Partida partida = partidaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Partida não encontrada"));

        return toResponseDto(partida);
    }


    public Page<PartidaResponseDto> listarPartidas(
            Long clubeMandanteId,
            Long clubeVisitanteId,
            Long estadioId,
            Pageable pageable) {

        validarExistenciaDasEntidadesParaFiltrosDePartida(clubeMandanteId, clubeVisitanteId, estadioId);

        Page<Partida> partidas = aplicarFiltrosPartida(clubeMandanteId, clubeVisitanteId, estadioId, pageable);

        return partidas.map(this::toResponseDto);
    }

    private void validarExistenciaDasEntidadesParaFiltrosDePartida(
            Long clubeMandanteId,
            Long clubeVisitanteId,
            Long estadioId) {
        if (clubeMandanteId != null) {
            validarExistenciaClubeMandante(clubeMandanteId);
        }

        if (clubeVisitanteId != null) {
            validarExistenciaClubeVisitante(clubeVisitanteId);
        }

        if (estadioId != null) {
            validarExistenciaEstadio(estadioId);
        }
    }

    private Page<Partida> aplicarFiltrosPartida(Long clubeMandanteId, Long clubeVisitanteId, Long estadioId, Pageable pageable) {
        if (clubeMandanteId != null && clubeVisitanteId != null && estadioId != null) {
            return buscarPorTodosFiltros(clubeMandanteId, clubeVisitanteId, estadioId, pageable);
        }
        else if (clubeMandanteId != null && estadioId != null) {
            return buscarPorFiltroMandanteEEstadio(clubeMandanteId, estadioId, pageable);
        }
        else if (clubeVisitanteId != null && estadioId != null) {
            return buscarPorFiltroVisitanteEEstadio(clubeVisitanteId, estadioId, pageable);
        }
        else if (clubeMandanteId != null) {
            return buscarPorFiltroMandante(clubeMandanteId, pageable);
        }
        else if (clubeVisitanteId != null) {
            return buscarPorFiltroVisitante(clubeVisitanteId, pageable);
        }
        else if (estadioId != null) {
            return buscarPorFiltroEstadio(estadioId, pageable);
        }
        else {
            return buscarTodasPartidas(pageable);
        }
    }

    private Page<Partida> buscarPorTodosFiltros(Long clubeMandanteId, Long clubeVisitanteId, Long estadioId, Pageable pageable) {
        return partidaRepository.findByClubeMandanteIdAndClubeVisitanteIdAndEstadioId(clubeMandanteId, clubeVisitanteId, estadioId, pageable);
    }

    private Page<Partida> buscarPorFiltroMandanteEEstadio(Long mandanteId, Long estadioId, Pageable pageable) {
        return partidaRepository.findByClubeMandanteIdAndEstadioId(mandanteId, estadioId, pageable);
    }

    private Page<Partida> buscarPorFiltroVisitanteEEstadio(Long visitanteId, Long estadioId, Pageable pageable) {
        return partidaRepository.findByClubeVisitanteIdAndEstadioId(visitanteId, estadioId, pageable);
    }

    private Page<Partida> buscarPorFiltroMandante(Long mandanteId, Pageable pageable) {
        return partidaRepository.findByClubeMandanteId(mandanteId, pageable);
    }

    private Page<Partida> buscarPorFiltroVisitante(Long visitanteId, Pageable pageable) {
        return partidaRepository.findByClubeVisitanteId(visitanteId, pageable);
    }

    private Page<Partida> buscarPorFiltroEstadio(Long estadioId, Pageable pageable) {
        return partidaRepository.findByEstadioId(estadioId, pageable);
    }

    private Page<Partida> buscarTodasPartidas(Pageable pageable) {
        return partidaRepository.findAll(pageable);
    }

    private void validarExistenciaClubeMandante(Long clubeMandante) {
        if (!clubeRepository.existsById(clubeMandante)) {
            throw new EntityNotFoundException("Clube mandante não encontrado");
        }
    }

    private void validarExistenciaClubeVisitante(Long clubeVisitante) {
        if (!clubeRepository.existsById(clubeVisitante)) {
            throw new EntityNotFoundException("Clube visitante não encontrado");
        }
    }

    private void validarExistenciaEstadio(Long estadioId) {
        if (!estadioRepository.existsById(estadioId)) {
            throw new EntityNotFoundException("Estádio não encontrado");
        }
    }


    public PartidaResponseDto atualizarPartidaPorId(Long id, PartidaRequestDto partidaRequestDto) {

        Partida partidaExistente = partidaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Partida não encontrada"));

        Clube clubeMandante = clubeRepository.findById(partidaRequestDto.getClubeMandanteId())
                .orElseThrow(() -> new EntityNotFoundException("Clube mandante não encontrado"));
        Clube clubeVisitante = clubeRepository.findById(partidaRequestDto.getClubeVisitanteId())
                .orElseThrow(() -> new EntityNotFoundException("Clube visitante não encontrado"));
        Estadio estadio = buscarEstadio(partidaRequestDto.getEstadioId());
        LocalDateTime dataCriacaoMandante = clubeMandante.getDataCriacao().atStartOfDay();
        LocalDateTime dataCriacaoVisitante = clubeVisitante.getDataCriacao().atStartOfDay();
        LocalDateTime dataHora = partidaRequestDto.getDataHora();

        validarPartida(partidaRequestDto, dataCriacaoMandante, dataCriacaoVisitante, clubeMandante, clubeVisitante, estadio, dataHora);

        partidaExistente.setClubeMandante(clubeMandante);
        partidaExistente.setClubeVisitante(clubeVisitante);
        partidaExistente.setClubeMandanteGols(partidaRequestDto.getClubeMandanteGols());
        partidaExistente.setClubeVisitanteGols(partidaRequestDto.getClubeVisitanteGols());
        partidaExistente.setEstadio(estadio);
        partidaExistente.setDataHora(partidaRequestDto.getDataHora());

        return toResponseDto(partidaRepository.save(partidaExistente));

    }


    public void deletarPartidaPorId(Long id) {
        Partida partida = partidaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Partida não encontrada"));

        partidaRepository.delete(partida);
    }


    private void validarPartida(PartidaRequestDto partidaRequestDto, LocalDateTime dataCriacaoMandante, LocalDateTime dataCriacaoVisitante, Clube clubeMandante, Clube clubeVisitante, Estadio estadio, LocalDateTime dataHora) {
        validarCamposObrigatorios(partidaRequestDto);
        validarClubesOponentesDiferentes(partidaRequestDto);
        validarGolsNaoNegativos(partidaRequestDto);
        validarDataHoraAnteriorDataCriacaoClubes(partidaRequestDto, dataCriacaoMandante, dataCriacaoVisitante);
        validarClubesInativos(clubeMandante, clubeVisitante);
        validarIntervaloDePartidas(clubeMandante, clubeVisitante, dataHora);
        validarPartidaComEstadioDisponivel(estadio, dataHora);
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