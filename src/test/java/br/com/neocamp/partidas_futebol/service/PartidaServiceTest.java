package br.com.neocamp.partidas_futebol.service;

import br.com.neocamp.partidas_futebol.dto.partidaDto.PartidaResponseDto;
import br.com.neocamp.partidas_futebol.entity.Clube;
import br.com.neocamp.partidas_futebol.entity.Estadio;
import br.com.neocamp.partidas_futebol.entity.Partida;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PartidaServiceTest {

    @Mock
    private PartidaRepository partidaRepository;

    @InjectMocks
    private PartidaService partidaService;

    private Partida partida;
    private Clube clubeMandante;
    private Clube clubeVisitante;
    private Estadio estadio;

    @BeforeEach
    void setUp() {
        clubeMandante = new Clube(1L, "São Paulo Futebol Clube", "SP", LocalDate.of(1930, 1, 25), true);

        clubeVisitante = new Clube(2L, "Corinthians", "SP", LocalDate.of(1910, 9, 1), true);

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

    }

    @Test
    void testarBuscarPartidaPorId() {
        when(partidaRepository.findById(1L)).thenReturn(Optional.of(partida));

        PartidaResponseDto resultadoRequest = partidaService.buscarPartidaPorId(1L);

        assertNotNull(resultadoRequest);
        assertEquals(1L, resultadoRequest.getPartidaId());
        assertEquals(1L, resultadoRequest.getClubeMandanteId());
        assertEquals("São Paulo Futebol Clube", resultadoRequest.getClubeMandanteNome());
        assertEquals(2L, resultadoRequest.getClubeVisitanteId());
        assertEquals("Corinthians", resultadoRequest.getClubeVisitanteNome());
        assertEquals(2, resultadoRequest.getClubeMandanteGols());
        assertEquals(1, resultadoRequest.getClubeVisitanteGols());
        assertEquals(1L, resultadoRequest.getEstadioId());
        assertEquals("Neo Química Arena", resultadoRequest.getEstadioNome());
        assertEquals(LocalDateTime.of(2025, 1, 10, 15, 0), resultadoRequest.getDataHora());

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

}
