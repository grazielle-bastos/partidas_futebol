package br.com.neocamp.partidas_futebol.controller;

import br.com.neocamp.partidas_futebol.dto.clubeDto.ClubeRequestDto;
import br.com.neocamp.partidas_futebol.dto.clubeDto.ClubeResponseDto;
import br.com.neocamp.partidas_futebol.service.ClubeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.core.IsEqual.equalTo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(ClubeController.class)
public class ClubeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClubeService clubeService;

    @Autowired
    private ObjectMapper objectMapper;

    
    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    ClubeRequestDto clubeRequestDto = new ClubeRequestDto();
    ClubeResponseDto salvar = new ClubeResponseDto();


    @Test
    public void testarCadastrarClubesComDadosValidos() throws Exception {
        clubeRequestDto.setNome("Palmeiras");
        clubeRequestDto.setSiglaEstado("SP");
        clubeRequestDto.setDataCriacao(LocalDate.of(1914, 8, 26));
        clubeRequestDto.setAtivo(true);

        salvar.setId(1L);
        salvar.setNome("Palmeiras");
        salvar.setSiglaEstado("SP");
        salvar.setDataCriacao(LocalDate.of(1914, 8, 26));
        salvar.setAtivo(true);

        Mockito.when(clubeService.cadastrarClube(Mockito.any(ClubeRequestDto.class))).thenReturn(salvar);

        String clubeJson = objectMapper.writeValueAsString(clubeRequestDto);
        ResultActions response = mockMvc.perform(post("/clube")
                        .contentType(MediaType.valueOf("application/json"))
                        .content(clubeJson));

        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.nome", equalTo("Palmeiras")))
                .andExpect(jsonPath("$.siglaEstado", equalTo("SP")))
                .andExpect(jsonPath("$.ativo", equalTo(true)))
                .andExpect(jsonPath("$.dataCriacao", equalTo("1914-08-26")))
                .andDo(print());

    }


    @Test
    public void testarSalvarClubesComDadosInvalidos() throws Exception {
        // Nome curto
        clubeRequestDto.setNome("A");
        clubeRequestDto.setSiglaEstado("SP");
        clubeRequestDto.setDataCriacao(LocalDate.of(1914, 8, 26));
        clubeRequestDto.setAtivo(true);

        ResultActions response = mockMvc.perform(post("/clube")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clubeRequestDto)));

        response.andExpect(status().isBadRequest())
                .andDo(print());

        // SiglaEstado inválida
        clubeRequestDto.setNome("Palmeiras");
        clubeRequestDto.setSiglaEstado("XX");
        clubeRequestDto.setDataCriacao(LocalDate.of(1914, 8, 26));
        clubeRequestDto.setAtivo(true);

        response = mockMvc.perform(post("/clube")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clubeRequestDto)));

        response.andExpect(status().isBadRequest())
                .andDo(print());

        // DataCriacao no futuro
        clubeRequestDto.setNome("Palmeiras");
        clubeRequestDto.setSiglaEstado("SP");
        clubeRequestDto.setDataCriacao(LocalDate.now().plusDays(1));
        clubeRequestDto.setAtivo(true);

        response = mockMvc.perform(post("/clube")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clubeRequestDto)));

        response.andExpect(status().isBadRequest())
                .andDo(print());

        //TODO - Adicionar teste para dataCriacao do clube para uma data posterior à alguma partida do clube já registrada

        // Nome duplicado do mesmo estado
        clubeRequestDto.setNome("Palmeiras");
        clubeRequestDto.setSiglaEstado("SP");
        clubeRequestDto.setDataCriacao(LocalDate.of(1914, 8, 26));
        clubeRequestDto.setAtivo(true);

        Mockito.when(clubeService.cadastrarClube(Mockito.any(ClubeRequestDto.class))).thenThrow(new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um clube com o mesmo nome no mesmo estado"));

        response = mockMvc.perform(post("/clube")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clubeRequestDto)));

        response.andExpect(status().isConflict())
                .andDo(print());

    }


    @Test
    public void testarBuscarClubePorIdComSucesso() throws Exception {
        Long id = 1L;
        ClubeResponseDto clube = new ClubeResponseDto();
        clube.setId(id);
        clube.setNome("Palmeiras");
        clube.setSiglaEstado("SP");
        clube.setDataCriacao(LocalDate.of(1914, 8 ,26));
        clube.setAtivo(true);

        Mockito.when(clubeService.buscarPorId(id)).thenReturn(clube);

        ResultActions response = mockMvc.perform(get("/clube/{id}", id)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.nome", equalTo("Palmeiras")))
                .andExpect(jsonPath("$.siglaEstado", equalTo("SP")))
                .andExpect(jsonPath("$.ativo", equalTo(true)))
                .andExpect(jsonPath("$.dataCriacao", equalTo("1914-08-26")))
                .andDo(print());


    }


    @Test
    public void testarBuscarClubePorIdInexistente() throws Exception {
        Long id = 99L;

        Mockito.when(clubeService.buscarPorId(id)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Clube não encontrado"));

        ResultActions response = mockMvc.perform(get("/clube/{id}", id)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isNotFound())
                .andDo(print());
    }


    @Test
    public void testarAtualizarClubeComSucesso() throws Exception {
        Long id = 1L;

        ClubeRequestDto clubeRequestDto = new ClubeRequestDto();
        clubeRequestDto.setNome("Corinthians");
        clubeRequestDto.setSiglaEstado("SP");
        clubeRequestDto.setDataCriacao(LocalDate.of(1910, 9, 1));
        clubeRequestDto.setAtivo(true);

        ClubeResponseDto clubeResponseDto = new ClubeResponseDto();
        clubeResponseDto.setId(id);
        clubeResponseDto.setNome("Corinthians");
        clubeResponseDto.setSiglaEstado("SP");
        clubeResponseDto.setDataCriacao(LocalDate.of(1910, 9, 1));
        clubeResponseDto.setAtivo(true);

        Mockito.when(clubeService.atualizarPorId(Mockito.eq(id), Mockito.any(ClubeRequestDto.class)))
                .thenReturn(clubeResponseDto);

        String clubeJson = objectMapper.writeValueAsString(clubeRequestDto);
        ResultActions response = mockMvc.perform(put("/clube/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(clubeJson)
        );

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.nome", equalTo("Corinthians")))
                .andExpect(jsonPath("$.siglaEstado", equalTo("SP")))
                .andExpect(jsonPath("$.dataCriacao", equalTo("1910-09-01")))
                .andDo(print());

    }


    @Test
    public void testarAtualizarClubeComIdInexistente() throws Exception {
        Long id = 99L;

        ClubeRequestDto clubeRequestDto = new ClubeRequestDto();
        clubeRequestDto.setNome("Corinthians");
        clubeRequestDto.setSiglaEstado("SP");
        clubeRequestDto.setDataCriacao(LocalDate.of(1910, 9, 1));
        clubeRequestDto.setAtivo(true);

        Mockito.when(clubeService.atualizarPorId(Mockito.eq(id), Mockito.any(ClubeRequestDto.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Clube não encontrado"));

        String clubeJson = objectMapper.writeValueAsString(clubeRequestDto);
        ResultActions response = mockMvc.perform(put("/clube/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(clubeJson)
        );

        response.andExpect(status().isNotFound())
                .andDo(print());


    }


    @Test
    public void testarInativarClubeComIdExistente() throws Exception {
        Long id = 1L;

        Mockito.doNothing().when(clubeService).inativarClubePorId(id);

        ResultActions response = mockMvc.perform(delete("/clube/{id}", id))
                .andExpect(status().isNoContent());

        response.andExpect(status().isNoContent())
                .andDo(print());
    }


    @Test
    public void testarInativarClubeComIdInexistente() throws Exception {
        Long id = 99L;

        Mockito.doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Clube não encontrado")).when(clubeService).inativarClubePorId(id);

        ResultActions response = mockMvc.perform(delete("/clube/{id}", id))
                .andExpect(status().isNotFound())
                .andDo(print());
    }


    @Test
    public void testarListarClubesComFiltrosOpcionaisEPaginacao() throws Exception {
        ClubeResponseDto clubeDto1 = new ClubeResponseDto();
        clubeDto1.setId(1L);
        clubeDto1.setNome("Palmeiras");
        clubeDto1.setSiglaEstado("SP");
        clubeDto1.setDataCriacao(LocalDate.of(1914, 8, 26));
        clubeDto1.setAtivo(true);

        ClubeResponseDto clubeDto2 = new ClubeResponseDto();
        clubeDto2.setId(2L);
        clubeDto2.setNome("Corinthians");
        clubeDto2.setSiglaEstado("SP");
        clubeDto2.setDataCriacao(LocalDate.of(1910, 9, 1));
        clubeDto2.setAtivo(true);

        List<ClubeResponseDto> clubes = List.of(clubeDto1, clubeDto2);
        Page<ClubeResponseDto> page = new PageImpl<>(clubes);

        Mockito.when(clubeService.listarClubes(
                Mockito.eq("a"),
                Mockito.eq("SP"),
                Mockito.eq(true),
                Mockito.any(Pageable.class)))
                .thenReturn(page);

        ResultActions response = mockMvc.perform(get("/clube/lista")
                .param("nome", "a")
                .param("siglaEstado", "SP")
                .param("ativo", "true")
                .param("page", "0")
                .param("size", "10")
                .param("sort", "nome,asc")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id", equalTo(1)))
                .andExpect(jsonPath("$.content[0].nome", equalTo("Palmeiras")))
                .andExpect(jsonPath("$.content[1].id", equalTo(2)))
                .andExpect(jsonPath("$.content[1].nome", equalTo("Corinthians")))
                .andDo(print());

    }


    @Test
    public void testarListarClubesComFiltrosOpcionaisSemPaginacao() throws Exception {
        ClubeResponseDto clubeDto1 = new ClubeResponseDto();
        clubeDto1.setId(1L);
        clubeDto1.setNome("Palmeiras");
        clubeDto1.setSiglaEstado("SP");
        clubeDto1.setDataCriacao(LocalDate.of(1914, 8, 26));
        clubeDto1.setAtivo(true);

        ClubeResponseDto clubeDto2 = new ClubeResponseDto();
        clubeDto2.setId(2L);
        clubeDto2.setNome("Corinthians");
        clubeDto2.setSiglaEstado("SP");
        clubeDto2.setDataCriacao(LocalDate.of(1910, 9, 1));
        clubeDto2.setAtivo(true);

        List<ClubeResponseDto> clubesList = List.of(clubeDto1, clubeDto2);

        Mockito.when(clubeService.listarClubes(
                    Mockito.any(),
                    Mockito.any(),
                    Mockito.any()))
                .thenReturn(clubesList);

        ResultActions response = mockMvc.perform(get("/clube/lista-sem-paginacao")
                .param("nome", "a")
                .param("siglaEstado", "SP")
                .param("ativo", "true")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nome").value("Palmeiras"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].nome").value("Corinthians"))
                .andDo(print());
    }


    @Test
    public void testarListarClubesComFiltrosOpcionaisSemPaginacaoVazia() throws Exception {
        Mockito.when(clubeService.listarClubes(
                Mockito.eq("inexistente"),
                Mockito.eq("XX"),
                Mockito.eq(false),
                Mockito.any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of()));

        ResultActions responseEmpty = mockMvc.perform(get("/clube/lista")
            .param("nome", "inexistente")
            .param("siglaEstado", "XX")
            .param("ativo", "false")
            .param("page", "0")
            .param("size", "10")
            .contentType(MediaType.APPLICATION_JSON));

        responseEmpty.andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").isEmpty())
                .andDo(print());
    }


    @Test
    public void testarListarClubesComOrdenacao() throws Exception {
        List<ClubeResponseDto> clubesOrdenados = List.of(
                new ClubeResponseDto(1L, "Palmeiras", "SP", LocalDate.of(1914, 8, 26), true),
                new ClubeResponseDto(2L, "Corinthians", "SP", LocalDate.of(1910, 9, 1), true)
        );

        Mockito.when(clubeService.listarClubes(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(new PageImpl<>(clubesOrdenados));

        ResultActions response = mockMvc.perform(get("/clube/lista")
                .param("sort", "nome,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nome").value("Palmeiras"))
                .andExpect(jsonPath("$.content[1].nome").value("Corinthians"))
                .andDo(print());

    }
}
