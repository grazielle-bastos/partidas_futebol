package br.com.neocamp.partidas_futebol.service;

import br.com.neocamp.partidas_futebol.dto.partidaDto.PartidaRequestDto;
import br.com.neocamp.partidas_futebol.dto.partidaDto.PartidaResponseDto;
import br.com.neocamp.partidas_futebol.entity.Clube;
import br.com.neocamp.partidas_futebol.entity.Estadio;
import br.com.neocamp.partidas_futebol.entity.Partida;
import br.com.neocamp.partidas_futebol.repository.ClubeRepository;
import br.com.neocamp.partidas_futebol.repository.EstadioRepository;
import br.com.neocamp.partidas_futebol.repository.PartidaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
        Long clubeMandanteId = partidaRequestDto.getClubeMandanteId();
        Long clubeVisitanteId = partidaRequestDto.getClubeVisitanteId();
        Long estadioId = partidaRequestDto.getEstadioId();
        Integer clubeMandanteGols = partidaRequestDto.getClubeMandanteGols();
        Integer clubeVisitanteGols = partidaRequestDto.getClubeVisitanteGols();
        LocalDateTime dataHora = partidaRequestDto.getDataHora();
        LocalDateTime novaDataHora = partidaRequestDto.getDataHora();

        validarCamposObrigatorios(partidaRequestDto);
        validarClubesOponentesDiferentes(clubeMandanteId, clubeVisitanteId);
        Clube clubeMandante = buscarClubeMandante(clubeMandanteId);
        Clube clubeVisitante = buscarClubeVisitante(clubeVisitanteId);
        Estadio estadio = buscarEstadio(estadioId);
        validarGolsNaoNegativos(clubeMandanteGols, clubeVisitanteGols);
        LocalDateTime dataCriacaoMandante = clubeMandante.getDataCriacao().atStartOfDay();
        LocalDateTime dataCriacaoVisitante = clubeVisitante.getDataCriacao().atStartOfDay();
        validarDataHoraAnteriorDataCriacaoClubes(dataHora, dataCriacaoMandante, dataCriacaoVisitante);
        validarDataHoraFutura(dataHora);
        validarClubesInativos(clubeMandante, clubeVisitante);
        validarIntervaloDePartidas(clubeMandante, clubeVisitante, novaDataHora);
        validarPartidaComEstadioDisponivel(estadio, dataHora);

        Partida partida = new Partida();
        partida.setClubeMandante(clubeMandante);
        partida.setClubeVisitante(clubeVisitante);
        partida.setClubeMandanteGols(partidaRequestDto.getClubeMandanteGols());
        partida.setClubeVisitanteGols(partidaRequestDto.getClubeVisitanteGols());
        partida.setEstadio(estadio);
        partida.setDataHora(partidaRequestDto.getDataHora());

        Partida partidaSalva = partidaRepository.save(partida);

        return toResponseDto(partidaSalva);
    }

    private void validarCamposObrigatorios(PartidaRequestDto partidaRequestDto) {
        if (partidaRequestDto.getClubeMandanteId() == null ||
                partidaRequestDto.getClubeVisitanteId() == null ||
                partidaRequestDto.getClubeMandanteGols() == null ||
                partidaRequestDto.getClubeVisitanteGols() == null ||
                partidaRequestDto.getEstadioId() == null ||
                partidaRequestDto.getDataHora() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Todos os campos são obrigatórios e não podem ser vazios");
        }
    }

    private void validarClubesOponentesDiferentes(Long clubeMandanteId, Long clubeVisitanteId) {
        if (clubeMandanteId.equals(clubeVisitanteId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Clube mandante e visitante não podem ser o mesmo");
        }
    }

    private Clube buscarClube(Long id, String tipoClube) {
        Clube clube = clubeRepository.findById(id).orElse(null);
        if (clube == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Clube " + tipoClube + " não encontrado");
        }
        return clube;
    }

    private Clube buscarClubeMandante(Long id) {
        return buscarClube(id, "mandante");
    }

    private Clube buscarClubeVisitante(Long id) {
        return buscarClube(id, "visitante");
    }

    private Estadio buscarEstadio(Long id) {
        Estadio estadio = estadioRepository.findById(id).orElse(null);
        if (estadio == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Estádio não encontrado");
        }
        return estadio;
    }

    private void validarGolsNaoNegativos(Integer clubeMandanteGols, Integer clubeVisitanteGols) {
        if (clubeMandanteGols < 0 || clubeVisitanteGols < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Gols não podem ser negativos");
        }
    }

    private void validarDataHoraFutura(LocalDateTime dataHora) {
        if (dataHora.isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data e hora da partida não podem ser no passado");
        }
    }

    private void validarDataHoraAnteriorDataCriacaoClubes(LocalDateTime dataHora, LocalDateTime dataCriacaoMandante, LocalDateTime dataCriacaoVisitante) {
        if (dataHora.isBefore(dataCriacaoMandante) || dataHora.isBefore(dataCriacaoVisitante)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Data e hora da partida não podem ser anteriores à data de criação dos clubes envolvidos");
        }
    }

    private void validarClubesInativos(Clube mandante, Clube visitante) {
        if (!mandante.getAtivo() || !visitante.getAtivo()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Não é possível cadastrar partida com clube inativo");
        }
    }

    private void validarIntervaloDePartidas(Clube mandante, Clube visitante, LocalDateTime dataHoraNovaPartida) {
        List<Partida> partidasMandante = partidaRepository.findByClubeMandanteId(mandante.getId());
        List<Partida> partidasVisitante = partidaRepository.findByClubeVisitanteId(visitante.getId());

        for (Partida partida : partidasMandante) {
            long diff = Math.abs(java.time.Duration.between(partida.getDataHora(), dataHoraNovaPartida).toHours());
            if (diff < 48) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Clube mandante já possui outra partida em menos de 48 horas");
            }
        }

        for (Partida partida : partidasVisitante) {
            long diff = Math.abs(java.time.Duration.between(partida.getDataHora(), dataHoraNovaPartida).toHours());
            if (diff < 48) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Clube visitante já possui outra partida em menos de 48 horas");
            }
        }

    }

    private void validarPartidaComEstadioDisponivel(Estadio estadio, LocalDateTime dataHoraNovaPartida) {
        List<Partida> partidasEstadio = estadio.getPartidas();
        for (Partida partida : partidasEstadio) {
            if (partida.getDataHora().toLocalDate().equals(dataHoraNovaPartida.toLocalDate())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Estádio já possui outra partida no mesmo dia");
            }
        }
    }


    public PartidaResponseDto buscarPartidaPorId(Long id) {
        Partida partida = partidaRepository.findById(id).orElse(null);

        if (partida == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Partida não encontrada");
        }

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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Clube mandante não encontrado");
        }
    }

    private void validarExistenciaClubeVisitante(Long clubeVisitante) {
        if (!clubeRepository.existsById(clubeVisitante)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Clube visitante não encontrado");
        }
    }

    private void validarExistenciaEstadio(Long estadioId) {
        if (!estadioRepository.existsById(estadioId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Estádio não encontrado");
        }
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