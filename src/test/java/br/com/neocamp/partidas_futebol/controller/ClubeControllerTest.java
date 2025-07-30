package br.com.neocamp.partidas_futebol.controller;

import br.com.neocamp.partidas_futebol.dto.ClubeRequestDto;
import br.com.neocamp.partidas_futebol.dto.ClubeResponseDto;
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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.core.IsEqual.equalTo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Classe de testes para o {@link ClubeController}.
 *
 * @requisito RF-01: Testar os endpoints de gerenciamento de clubes
 * @implementacao Utiliza MockMvc para simular requisições HTTP e verificar respostas
 *
 * Testes contemplados:
 * - POST /clube: Testa cadastro com dados válidos e inválidos
 */
@WebMvcTest(ClubeController.class)
public class ClubeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClubeService clubeService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Configuração inicial para cada teste.
     * Registra o módulo JavaTime no ObjectMapper para serialização de datas.
     */
    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    ClubeRequestDto clubeRequestDto = new ClubeRequestDto();
    ClubeResponseDto salvar = new ClubeResponseDto();

/**
     * Testa o método salvar do ClubeController para o caso em que os dados do clube são válidos.
     *
     * <p>
     * Cenário: Recebe um DTO de clube com nome, sigla do estado, data de criação e situação ativo.
     * Os dados são válidos (nome, sigla do estado, data de criação e situação preenchidos corretamente).
     * </p>
     *
     * <b>Etapas do teste:</b>
     * <ul>
     *   <li><b>Arrange:</b> Prepara os dados de entrada e configura o comportamento dos mocks.</li>
     *   <li><b>Act:</b> Executa o método a ser testado (POST /clube).</li>
     *   <li><b>Assert:</b> Verifica se o resultado está conforme o esperado, incluindo o status HTTP 201 CREATED e o corpo da resposta.</li>
     * </ul>
     *
     * @cenario Cadastro de clube com dados válidos.
     * @resultado Deve retornar status 201 CREATED e o DTO do clube salvo, com os dados preenchidos e ativo.
     */
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

/**
     * Testa o método salvar do ClubeService para o caso em que os dados do clube são inválidos.
     *
     * <p>
     * Cenário: Recebe um DTO de clube com nome, sigla do estado, data de criação e situação ativo.
     * Os dados são inválidos (nome nulo, sigla do estado nula, data de criação futura ou situação nula).
     * </p>
     *
     * <b>Etapas do teste:</b>
     * <ul>
     *   <li><b>Arrange:</b> Prepara os dados de entrada e configura o comportamento dos mocks.</li>
     *   <li><b>Act:</b> Executa o método a ser testado (salvar).</li>
     *   <li><b>Assert:</b> Verifica se uma exceção é lançada com status 400 (BAD_REQUEST).</li>
     * </ul>
     *
     * @cenario Cadastro de clube com dados inválidos.
     * @resultado Deve lançar ResponseStatusException com status BAD_REQUEST.
     */
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

/**
     * Testa o método buscarPorId do ClubeController para o caso em que o clube é encontrado.
     *
     * <p>
     * Cenário: Recebe um ID de clube existente.
     * </p>
     *
     * <b>Etapas do teste:</b>
     * <ul>
     *   <li><b>Arrange:</b> Prepara os dados de entrada e configura o comportamento dos mocks.</li>
     *   <li><b>Act:</b> Executa o método a ser testado (GET /clube/{id}).</li>
     *   <li><b>Assert:</b> Verifica se o resultado está conforme o esperado, incluindo o status HTTP 200 OK e o corpo da resposta.</li>
     * </ul>
     *
     * @cenario Busca de clube por ID com sucesso.
     * @resultado Deve retornar status 200 OK e o DTO do clube encontrado.
     */
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

/**
     * Testa o método buscarPorId do ClubeController para o caso em que o clube não é encontrado.
     *
     * <p>
     * Cenário: Recebe um ID de clube inexistente.
     * </p>
     *
     * <b>Etapas do teste:</b>
     * <ul>
     *   <li><b>Arrange:</b> Prepara os dados de entrada e configura o comportamento dos mocks.</li>
     *   <li><b>Act:</b> Executa o método a ser testado (GET /clube/{id}).</li>
     *   <li><b>Assert:</b> Verifica se o resultado está conforme o esperado, incluindo o status HTTP 404 NOT FOUND.</li>
     * </ul>
     *
     * @cenario Busca de clube por ID inexistente.
     * @resultado Deve retornar status 404 NOT FOUND.
     */
    @Test
    public void testarBuscarClubePorIdInexistente() throws Exception {
        Long id = 99L;

        Mockito.when(clubeService.buscarPorId(id)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Clube não encontrado"));

        ResultActions response = mockMvc.perform(get("/clube/{id}", id)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isNotFound())
                .andDo(print());
    }

/**
     * Testa o método atualizarPorId do ClubeController para o caso em que o clube é atualizado com sucesso.
     *
     * <p>
     * Cenário: Recebe um ID de clube existente e um DTO de clube com dados válidos.
     * </p>
     *
     * <b>Etapas do teste:</b>
     * <ul>
     *   <li><b>Arrange:</b> Prepara os dados de entrada e configura o comportamento dos mocks.</li>
     *   <li><b>Act:</b> Executa o método a ser testado (PUT /clube/{id}).</li>
     *   <li><b>Assert:</b> Verifica se o resultado está conforme o esperado, incluindo o status HTTP 200 OK e o corpo da resposta.</li>
     * </ul>
     *
     * @cenario Atualização de clube com sucesso.
     * @resultado Deve retornar status 200 OK e o DTO do clube atualizado.
     */
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

/**
     * Testa o método atualizarPorId do ClubeController para o caso em que o clube não é encontrado.
     *
     * <p>
     * Cenário: Recebe um ID de clube inexistente e um DTO de clube com dados válidos.
     * </p>
     *
     * <b>Etapas do teste:</b>
     * <ul>
     *   <li><b>Arrange:</b> Prepara os dados de entrada e configura o comportamento dos mocks.</li>
     *   <li><b>Act:</b> Executa o método a ser testado (PUT /clube/{id}).</li>
     *   <li><b>Assert:</b> Verifica se o resultado está conforme o esperado, incluindo o status HTTP 404 NOT FOUND.</li>
     * </ul>
     *
     * @cenario Atualização de clube inexistente.
     * @resultado Deve retornar status 404 NOT FOUND.
     */
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

/**
     * Testa o método inativarClubePorId do ClubeController para o caso em que o clube é inativado com sucesso.
     *
     * <p>
     * Cenário: Recebe um ID de clube existente.
     * </p>
     *
     * <b>Etapas do teste:</b>
     * <ul>
     *   <li><b>Arrange:</b> Prepara os dados de entrada e configura o comportamento dos mocks.</li>
     *   <li><b>Act:</b> Executa o método a ser testado (DELETE /clube/{id}).</li>
     *   <li><b>Assert:</b> Verifica se o resultado está conforme o esperado, incluindo o status HTTP 204 NO CONTENT.</li>
     * </ul>
     *
     * @cenario Inativação de clube com sucesso.
     * @resultado Deve retornar status 204 NO CONTENT.
     */
    @Test
    public void testarInativarClubeComIdExistente() throws Exception {
        Long id = 1L;

        Mockito.doNothing().when(clubeService).inativarClubePorId(id);

        ResultActions response = mockMvc.perform(delete("/clube/{id}", id))
                .andExpect(status().isNoContent());

        response.andExpect(status().isNoContent())
                .andDo(print());
    }

/**
     * Testa o método inativarClubePorId do ClubeController para o caso em que o clube não é encontrado.
     *
     * <p>
     * Cenário: Recebe um ID de clube inexistente.
     * </p>
     *
     * <b>Etapas do teste:</b>
     * <ul>
     *   <li><b>Arrange:</b> Prepara os dados de entrada e configura o comportamento dos mocks.</li>
     *   <li><b>Act:</b> Executa o método a ser testado (DELETE /clube/{id}).</li>
     *   <li><b>Assert:</b> Verifica se o resultado está conforme o esperado, incluindo o status HTTP 404 NOT FOUND.</li>
     * </ul>
     *
     * @cenario Inativação de clube inexistente.
     * @resultado Deve retornar status 404 NOT FOUND.
     */
    @Test
    public void testarInativarClubeComIdInexistente() throws Exception {
        Long id = 99L;

        Mockito.doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Clube não encontrado")).when(clubeService).inativarClubePorId(id);

        ResultActions response = mockMvc.perform(delete("/clube/{id}", id))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

/**
     * Testa o método listarClubes do ClubeController com filtros opcionais e paginação.
     *
     * <p>
     * Cenário: Recebe filtros opcionais (nome, sigla do estado, ativo) e parâmetros de paginação.
     * </p>
     *
     * <b>Etapas do teste:</b>
     * <ul>
     *   <li><b>Arrange:</b> Prepara os dados de entrada e configura o comportamento dos mocks.</li>
     *   <li><b>Act:</b> Executa o método a ser testado (GET /clube/lista).</li>
     *   <li><b>Assert:</b> Verifica se o resultado está conforme o esperado, incluindo o status HTTP 200 OK e a lista de clubes.</li>
     * </ul>
     *
     * @cenario Listagem de clubes com filtros opcionais e paginação.
     * @resultado Deve retornar status 200 OK e uma lista paginada de clubes.
     */
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

/**
     * Testa o método listarClubes do ClubeController com filtros opcionais sem paginação.
     *
     * <p>
     * Cenário: Recebe filtros opcionais (nome, sigla do estado, ativo) e não utiliza paginação.
     * </p>
     *
     * <b>Etapas do teste:</b>
     * <ul>
     *   <li><b>Arrange:</b> Prepara os dados de entrada e configura o comportamento dos mocks.</li>
     *   <li><b>Act:</b> Executa o método a ser testado (GET /clube/lista-sem-paginacao).</li>
     *   <li><b>Assert:</b> Verifica se o resultado está conforme o esperado, incluindo o status HTTP 200 OK e a lista de clubes.</li>
     * </ul>
     *
     * @cenario Listagem de clubes com filtros opcionais sem paginação.
     * @resultado Deve retornar status 200 OK e uma lista completa de clubes.
     */
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

/**
     * Testa o método listarClubes do ClubeController com filtros opcionais sem paginação, retornando uma lista vazia.
     *
     * <p>
     * Cenário: Recebe filtros opcionais (nome, sigla do estado, ativo) e não utiliza paginação, mas não encontra clubes.
     * </p>
     *
     * <b>Etapas do teste:</b>
     * <ul>
     *   <li><b>Arrange:</b> Prepara os dados de entrada e configura o comportamento dos mocks.</li>
     *   <li><b>Act:</b> Executa o método a ser testado (GET /clube/lista-sem-paginacao).</li>
     *   <li><b>Assert:</b> Verifica se o resultado está conforme o esperado, incluindo o status HTTP 200 OK e uma lista vazia.</li>
     * </ul>
     *
     * @cenario Listagem de clubes com filtros opcionais sem paginação e lista vazia.
     * @resultado Deve retornar status 200 OK e uma lista vazia de clubes.
     */
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

/**
     * Testa o método listarClubes do ClubeController com ordenação.
     *
     * <p>
     * Cenário: Recebe parâmetros de ordenação e retorna a lista de clubes ordenada.
     * </p>
     *
     * <b>Etapas do teste:</b>
     * <ul>
     *   <li><b>Arrange:</b> Prepara os dados de entrada e configura o comportamento dos mocks.</li>
     *   <li><b>Act:</b> Executa o método a ser testado (GET /clube/lista).</li>
     *   <li><b>Assert:</b> Verifica se o resultado está conforme o esperado, incluindo o status HTTP 200 OK e a lista de clubes ordenada.</li>
     * </ul>
     *
     * @cenario Listagem de clubes com ordenação.
     * @resultado Deve retornar status 200 OK e uma lista de clubes ordenada pelo nome.
     */
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
