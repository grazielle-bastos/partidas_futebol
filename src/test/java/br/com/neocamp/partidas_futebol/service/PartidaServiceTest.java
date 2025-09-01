package br.com.neocamp.partidas_futebol.service;

import br.com.neocamp.partidas_futebol.dto.partidaDto.PartidaRequestDto;
import br.com.neocamp.partidas_futebol.dto.partidaDto.PartidaResponseDto;
import br.com.neocamp.partidas_futebol.entity.Clube;
import br.com.neocamp.partidas_futebol.entity.Estadio;
import br.com.neocamp.partidas_futebol.entity.Partida;
import br.com.neocamp.partidas_futebol.repository.ClubeRepository;
import br.com.neocamp.partidas_futebol.repository.EstadioRepository;
import br.com.neocamp.partidas_futebol.repository.PartidaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PartidaServiceTest {

    @Mock
    private PartidaRepository partidaRepository;

    @Mock
    private ClubeRepository clubeRepository;

    @Mock
    private EstadioRepository estadioRepository;

    @InjectMocks
    private PartidaService partidaService;

    private Partida partida;
    private Clube clubeMandante;
    private Clube clubeVisitante;
    private Estadio estadio;

    private PartidaRequestDto partidaRequestDto;
    private PartidaResponseDto partidaResponse;

    @BeforeEach
    void setUp() {
        clubeMandante = new Clube(1L, "Corinthians", "SP", LocalDate.of(1910, 9, 1), true);

        clubeVisitante = new Clube(2L, "São Paulo Futebol Clube", "SP", LocalDate.of(1930, 1, 25), true);

        estadio = new Estadio(1L, "Neo Química Arena");

        partida = new Partida(
                1L,
                clubeMandante,
                clubeVisitante,
                2,
                1,
                estadio,
                LocalDateTime.of(2025, 1, 10, 15, 0)
        );

        partidaResponse = new PartidaResponseDto(
                1L,
                1L,
                "Corinthians",
                2L,
                "São Paulo Futebol Clube",
                2,
                1,
                1L,
                "Neo Química Arena",
                LocalDateTime.of(2025, 1, 10, 15, 0)
        );

        partidaRequestDto = new PartidaRequestDto();
        partidaRequestDto.setClubeMandanteId(1L);
        partidaRequestDto.setClubeVisitanteId(2L);
        partidaRequestDto.setClubeMandanteGols(2);
        partidaRequestDto.setClubeVisitanteGols(1);
        partidaRequestDto.setEstadioId(1L);
        partidaRequestDto.setDataHora(LocalDateTime.of(2025, 1, 10, 15, 0));

    }

    @Test
    void testarBuscarPartidaPorId() {
        when(partidaRepository.findById(1L)).thenReturn(Optional.of(partida));

        partidaResponse = partidaService.buscarPartidaPorId(1L);

        assertNotNull(partidaResponse);
        assertEquals(1L, partidaResponse.getPartidaId());
        assertEquals(1L, partidaResponse.getClubeMandanteId());
        assertEquals("Corinthians", partidaResponse.getClubeMandanteNome());
        assertEquals(2L, partidaResponse.getClubeVisitanteId());
        assertEquals("São Paulo Futebol Clube", partidaResponse.getClubeVisitanteNome());
        assertEquals(2, partidaResponse.getClubeMandanteGols());
        assertEquals(1, partidaResponse.getClubeVisitanteGols());
        assertEquals(1L, partidaResponse.getEstadioId());
        assertEquals("Neo Química Arena", partidaResponse.getEstadioNome());
        assertEquals(LocalDateTime.of(2025, 1, 10, 15, 0), partidaResponse.getDataHora());

        verify(partidaRepository).findById(1L);
    }

    @Test
    void testarBuscarPartidaPorId_PartidaNaoEncontrada() {
        when(partidaRepository.findById(999L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> partidaService.buscarPartidaPorId(999L)
        );

        assertEquals("Partida não encontrada", exception.getMessage());

        verify(partidaRepository).findById(999L);
    }

    @Test
    void testarCadastrarPartida_Sucesso() {
        when(clubeRepository.findById(1L)).thenReturn(Optional.of(clubeMandante));
        when(clubeRepository.findById(2L)).thenReturn(Optional.of(clubeVisitante));
        when(estadioRepository.findById(1L)).thenReturn(Optional.of(estadio));

        Partida partidaSalva = new Partida();
        partidaSalva.setClubeMandante(clubeMandante);
        partidaSalva.setClubeVisitante(clubeVisitante);
        partidaSalva.setClubeMandanteGols(2);
        partidaSalva.setClubeVisitanteGols(1);
        partidaSalva.setEstadio(estadio);
        partidaSalva.setDataHora(LocalDateTime.of(2025, 1, 10, 15, 0));

        when(partidaRepository.save(any(Partida.class))).thenReturn(partidaSalva);

        partidaResponse = partidaService.cadastrarPartida(partidaRequestDto);

        assertNotNull(partidaResponse);
        assertEquals(1L, partidaResponse.getClubeMandanteId());
        assertEquals("Corinthians", partidaResponse.getClubeMandanteNome());
        assertEquals(2L, partidaResponse.getClubeVisitanteId());
        assertEquals("São Paulo Futebol Clube", partidaResponse.getClubeVisitanteNome());
        assertEquals(2, partidaResponse.getClubeMandanteGols());
        assertEquals(1, partidaResponse.getClubeVisitanteGols());
        assertEquals(1L, partidaResponse.getEstadioId());
        assertEquals("Neo Química Arena", partidaResponse.getEstadioNome());
        assertEquals(LocalDateTime.of(2025, 1, 10, 15, 0), partidaResponse.getDataHora());

        verify(partidaRepository).save(any(Partida.class));
        verify(clubeRepository).findById(1L);
        verify(clubeRepository).findById(2L);
        verify(estadioRepository).findById(1L);

    }

}
