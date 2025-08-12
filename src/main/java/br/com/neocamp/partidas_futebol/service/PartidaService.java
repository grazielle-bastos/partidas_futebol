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

    /**public PartidaResponseDto cadastrarPartida(PartidaRequestDto partidaRequestDto) {

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
    }**/

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
        Clube mandante = buscarClube(clubeMandanteId, "mandante");
        Clube visitante = buscarClube(clubeVisitanteId, "visitante");
        Estadio estadio = buscarEstadio(estadioId);
        validarGolsNaoNegativos(clubeMandanteGols, clubeVisitanteGols);
        validarDataHoraFutura(dataHora);
        LocalDateTime dataCriacaoMandante = mandante.getDataCriacao().atStartOfDay();
        LocalDateTime dataCriacaoVisitante = visitante.getDataCriacao().atStartOfDay();
        validarDataHoraAnteriorDataCriacaoClubes(dataHora, dataCriacaoMandante, dataCriacaoVisitante);
        validarClubesInativos(mandante, visitante);
        validarIntervaloDePartidas(mandante, visitante, novaDataHora);
        validarPartidaComEstadioDisponivel(estadio, dataHora);

        Partida partida = new Partida();
        partida.setClubeMandante(mandante);
        partida.setClubeVisitante(visitante);
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