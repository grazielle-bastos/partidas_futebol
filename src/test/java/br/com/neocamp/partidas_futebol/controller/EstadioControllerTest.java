package br.com.neocamp.partidas_futebol.controller;

import br.com.neocamp.partidas_futebol.dto.EstadioRequestDto;
import br.com.neocamp.partidas_futebol.dto.EstadioResponseDto;
import br.com.neocamp.partidas_futebol.service.EstadioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EstadioController.class)
public class EstadioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EstadioService estadioService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper.findAndRegisterModules();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    EstadioRequestDto estadioRequestDto = new EstadioRequestDto();
    EstadioResponseDto estadioResponseDto = new EstadioResponseDto();

    @Test
    public void testarCadastrarEstadioComSucesso() throws Exception {

        estadioRequestDto.setNome("Allianz Parque");

        estadioResponseDto.setId(1L);
        estadioResponseDto.setNome("Allianz Parque");

        Mockito.when(estadioService.cadastrarEstadio(Mockito.any(EstadioRequestDto.class)))
                .thenReturn(estadioResponseDto);

        String estadioRequestJson = objectMapper.writeValueAsString(estadioRequestDto);

        ResultActions response = mockMvc.perform(post("/estadio")
                .contentType("application/json")
                .content(estadioRequestJson));

        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Allianz Parque"))
                .andDo(print());

    }



}
