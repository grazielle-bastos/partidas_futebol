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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.data.jpa.domain.AbstractPersistable_.id;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.web.servlet.function.RequestPredicates.contentType;
import static org.springframework.web.servlet.function.RequestPredicates.param;

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

    @Test
    public void testarCadastrarEstadioComNomeNulo() throws Exception {

        estadioRequestDto.setNome(null);

        String estadioRequestJson = objectMapper.writeValueAsString(estadioRequestDto);

        ResultActions response = mockMvc.perform(post("/estadio")
                .contentType("application/json")
                .content(estadioRequestJson));

        response.andExpect(status().isBadRequest())
                .andDo(print());

    }

    //TODO: Implementar outros testes para o método cadastrarEstadio, se necessário.
    // Exemplo: testar retorno 400 BAD REQUEST com nome vazio, nome muito curto (menor do que 3 letras), e retorno 409 CONFLICT se o estádio já existir.

    @Test
    public void testarBuscarEstadioPorIdComSucesso() throws Exception {

        estadioResponseDto.setId(1L);

        when(estadioService.buscarPorId(1L))
                .thenReturn(estadioResponseDto);

        ResultActions response = mockMvc.perform(get("/estadio/1")
                .contentType("application/json"));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andDo(print());

    }

    @Test
    public void testarBuscarEstadioPorIdInexistente() throws Exception {

        Long estadioId = 99L;

        Mockito.when(estadioService.buscarPorId(estadioId))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Estádio não encontrado"));

        ResultActions response = mockMvc.perform(get("/estadio/{id}", id)
            .contentType("application/json"));

        response.andExpect(status().isNotFound())
                .andDo(print());

    }

    @Test
    public void testarAtualizarEstadioComSucesso() throws Exception {

        Long id = 1L;
        String nome = "MorumBIS";

        estadioRequestDto.setId(id);
        estadioRequestDto.setNome(nome);

        estadioResponseDto.setId(2L);
        estadioResponseDto.setNome("MorumBIS");

        Mockito.when(estadioService.atualizarPorId(Mockito.eq(id), Mockito.any(EstadioRequestDto.class)))
                .thenReturn(estadioResponseDto);

        String estadioRequestJson = objectMapper.writeValueAsString(estadioRequestDto);

        ResultActions response = mockMvc.perform(put("/estadio/{id}", id)
                .contentType("application/json")
                .content(estadioRequestJson)
        );

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.nome").value("MorumBIS"))
                .andDo(print());


    }

    @Test
    public void testarAtualizarEstadioComIdInexistente() throws Exception {

        Long id = 99L;
        String nome = "Al";

        estadioRequestDto.setId(id);
        estadioRequestDto.setNome(nome);

        Mockito.when(estadioService.atualizarPorId(Mockito.eq(id), Mockito.any(EstadioRequestDto.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Estádio não encontrado"));

        String estadioRequestJson = objectMapper.writeValueAsString(estadioRequestDto);

        ResultActions response = mockMvc.perform(put("/estadio/{id}", id)
                .contentType("application/json")
                .content(estadioRequestJson)
        );

        response.andExpect(status().isNotFound())
                .andDo(print());

    }

    @Test
    public void testarListarEstadiosComPaginacaoEFiltrosOpcionais() throws Exception {

        EstadioResponseDto estadioDto1 = new EstadioResponseDto();
        estadioDto1.setId(1L);
        estadioDto1.setNome("Allianz Parque");

        EstadioResponseDto estadioDto2 = new EstadioResponseDto();
        estadioDto2.setId(2L);
        estadioDto2.setNome("Maracanã");

        EstadioResponseDto estadioDto3 = new EstadioResponseDto();
        estadioDto3.setId(3L);
        estadioDto3.setNome("Mineirão");

        List<EstadioResponseDto> estadios = List.of(estadioDto1, estadioDto2, estadioDto3);
        Page<EstadioResponseDto> estadiosPage = new PageImpl<>(estadios);

        when(estadioService.listarEstadios(Mockito.anyString(), Mockito.any()))
                .thenReturn(estadiosPage);

        ResultActions response = mockMvc.perform(get("/estadio/lista")
                .param("nome", "All")
                .param("page", "0")
                .param("size", "10")
                .param("sort", "nome,asc")
                .contentType("application/json"));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].nome").value("Allianz Parque"))
                .andExpect(jsonPath("$.content[1].id").value(2))
                .andExpect(jsonPath("$.content[1].nome").value("Maracanã"))
                .andExpect(jsonPath("$.content[2].id").value(3))
                .andExpect(jsonPath("$.content[2].nome").value("Mineirão"))
                .andDo(print());

    }

    //TODO: Implementar teste para listar estádios sem paginação e lista vazia, se necessário.

}
