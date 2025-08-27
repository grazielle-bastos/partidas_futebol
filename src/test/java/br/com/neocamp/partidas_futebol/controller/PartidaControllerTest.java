package br.com.neocamp.partidas_futebol.controller;

import br.com.neocamp.partidas_futebol.dto.partidaDto.PartidaResponseDto;
import br.com.neocamp.partidas_futebol.service.PartidaService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PartidaController.class)
class PartidaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PartidaService partidaService;

    private PartidaResponseDto partidaResponse;

    @BeforeEach
    void setUp() {
        partidaResponse = new PartidaResponseDto(
                1L,
                2L,
                "Corinthians",
                1L,
                "Palmeiras",
                1,
                2,
                1L,
                "Neo Química Arena",
                LocalDateTime.of(2025, 1, 10, 15, 0)
        );
    }

    @Test
    void testarBuscarPartidaPorId() throws Exception {
        when(partidaService.buscarPartidaPorId(1L)).thenReturn(partidaResponse);

        mockMvc.perform(get("/partida/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.partidaId").value(1L))
                .andExpect(jsonPath("$.clubeMandanteGols").value(1))
                .andExpect(jsonPath("$.clubeVisitanteGols").value(2))
                .andExpect(jsonPath("$.clubeMandanteNome").value("Corinthians"))
                .andDo(print());

    }

    @Test
    void testarBuscarPartidaPorId_PartidaNaoEncontrada() throws Exception {
        when(partidaService.buscarPartidaPorId(999L))
                .thenThrow(new EntityNotFoundException("Partida não encontrada"));

        mockMvc.perform(get("/partida/999"))
                .andExpect(status().isNotFound())
        .andDo(print());
    }

}
