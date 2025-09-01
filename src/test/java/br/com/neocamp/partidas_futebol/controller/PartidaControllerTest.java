package br.com.neocamp.partidas_futebol.controller;

import br.com.neocamp.partidas_futebol.dto.partidaDto.PartidaRequestDto;
import br.com.neocamp.partidas_futebol.dto.partidaDto.PartidaResponseDto;
import br.com.neocamp.partidas_futebol.service.PartidaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.mockito.ArgumentMatchers.any;
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

    private PartidaRequestDto partidaRequestDto;
    private PartidaResponseDto partidaResponse;

    @BeforeEach
    void setUp() {
        partidaResponse = new PartidaResponseDto(
                1L,
                1L,
                "Corinthians",
                2L,
                "Palmeiras",
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
    void testarBuscarPartidaPorId() throws Exception {
        when(partidaService.buscarPartidaPorId(1L)).thenReturn(partidaResponse);

        mockMvc.perform(get("/partida/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.partidaId").value(1L))
                .andExpect(jsonPath("$.clubeMandanteGols").value(2))
                .andExpect(jsonPath("$.clubeVisitanteGols").value(1))
                .andExpect(jsonPath("$.clubeMandanteNome").value("Corinthians"))
                .andDo(print());

        verify(partidaService).buscarPartidaPorId(1L);
    }

    @Test
    void testarBuscarPartidaPorId_PartidaNaoEncontrada() throws Exception {
        when(partidaService.buscarPartidaPorId(999L))
                .thenThrow(new EntityNotFoundException("Partida não encontrada"));

        mockMvc.perform(get("/partida/999"))
                .andExpect(status().isNotFound())
                .andDo(print());

        verify(partidaService).buscarPartidaPorId(999L);
    }

    @Test
    void testarCadastrarPartida_Sucesso() throws  Exception {
        when(partidaService.cadastrarPartida(any(PartidaRequestDto.class)))
                .thenReturn(partidaResponse);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String jsonRequest = objectMapper.writeValueAsString(partidaRequestDto);

        mockMvc.perform(post("/partida")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.clubeMandanteNome").value("Corinthians"))
                .andExpect(jsonPath("$.clubeVisitanteNome").value("Palmeiras"))
                .andDo(print());

                verify(partidaService).cadastrarPartida(any(PartidaRequestDto.class));

    }

}
