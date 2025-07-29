package br.com.neocamp.partidas_futebol.controller;

import br.com.neocamp.partidas_futebol.dto.ClubeRequestDto;
import br.com.neocamp.partidas_futebol.dto.ClubeResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.neocamp.partidas_futebol.service.ClubeService;

import java.util.List;

/**
 * Controller responsável pelos endpoints de operações com clubes de futebol.
 *
 * @requisito Gerencia cadastro, edição, inativação, busca e listagem de clubes.
 * @fluxo Recebe requisições HTTP, valida dados e delega regras de negócio ao service.
 * @implementacao Utiliza o ClubeService para executar as operações principais.
 *
 * Endpoints disponíveis:
 * - POST /clube: Cadastrar clube
 * - GET /clube/{id}: Buscar clube por ID
 * - PUT /clube/{id}: Atualizar clube
 * - DELETE /clube/{id}: Inativar clube (soft delete)
 * - GET /clube/lista: Listar clubes com filtros e paginação
 */
@RestController
@RequestMapping("/clube")
public class ClubeController {

    /**
     * Serviço responsável por executar as regras de negócio relacionadas a clubes.
     * Injetado automaticamente pelo Spring para ser utilizado nos endpoints do controller.
     */
    private final ClubeService clubeService;

    /**
     * Construtor do controller de clubes.
     *
     * @param clubeService Serviço de clubes injetado automaticamente pelo Spring.
     * @implementacao Utiliza injeção de dependência para garantir que o controller tenha acesso às regras de negócio dos clubes.
     */
    @Autowired
    public ClubeController(ClubeService clubeService) {
        this.clubeService = clubeService;
    }

    /**
     * Cadastra um novo clube no sistema.
     *
     * @requisito Requisito_Funcional-01: Cadastro de clube
     * @fluxo Recebe os dados do clube via DTO, valida, salva no banco e retorna o clube salvo.
     * @implementacao Utiliza ClubeService para lógica de negócio e ResponseEntity para resposta HTTP.
     * @param clubeRequestDto Dados do clube recebidos no corpo da requisição.
     * @return ClubeResponseDto com os dados do clube cadastrado e status 201 CREATED.
     */
    @Operation(
            summary = "Cadastrar clube",
            description = "Endpoint para cadastrar um novo clube no sistema.")
    @PostMapping
    public ResponseEntity<ClubeResponseDto> cadastrarClubes(@Valid @RequestBody ClubeRequestDto clubeRequestDto) {
        ClubeResponseDto clubeSalvo = clubeService.salvar(clubeRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(clubeSalvo);
    }

    /**
     * Busca um clube pelo seu ID.
     *
     * @requisito Requisito_Funcional-04: Buscar um clube
     * @fluxo Recebe o ID do clube pela URL, chama o service para buscar o clube e retorna o resultado.
     * @implementacao Utiliza ClubeService para a lógica de busca e ResponseEntity para resposta HTTP.
     * @param id Identificador único do clube a ser buscado.
     * @return ClubeResponseDto com os dados do clube encontrado e status 200 OK.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClubeResponseDto> buscarClubePorId(@PathVariable Long id) {
        ClubeResponseDto clube = clubeService.buscarPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(clube);
    }

    /**
     * Atualiza os dados de um clube existente.
     *
     * @requisito Requisito_Funcional-02: Editar um clube
     * @fluxo Recebe o ID do clube pela URL e os dados atualizados via DTO, valida, atualiza no banco e retorna o clube atualizado.
     * @implementacao Utiliza ClubeService para lógica de atualização e ResponseEntity para resposta HTTP.
     * @param id Identificador único do clube a ser atualizado.
     * @param clubeAtualizado Dados atualizados do clube recebidos no corpo da requisição.
     * @return ClubeResponseDto com os dados do clube atualizado e status 200 OK.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ClubeResponseDto> atualizarClube(@PathVariable Long id, @RequestBody ClubeRequestDto clubeAtualizado) {
        ClubeResponseDto clube = clubeService.atualizarPorId(id, clubeAtualizado);
        return ResponseEntity.status(HttpStatus.OK).body(clube);
    }

    /**
     * Inativa (soft delete) um clube existente no sistema.
     *
     * @requisito Requisito_Funcional-03: Inativar um clube
     * @fluxo Recebe o ID do clube pela URL, chama o service para inativar (não excluir) o clube e retorna status 204 NO CONTENT.
     * @implementacao Utiliza ClubeService para lógica de inativação e ResponseEntity para resposta HTTP sem corpo.
     * @param id Identificador único do clube a ser inativado.
     * @return ResponseEntity sem corpo e status 204 NO CONTENT.
     */    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativarClube(@PathVariable Long id) {
        clubeService.inativarClubePorId(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

/**
 * Lista clubes com filtros opcionais e suporte à paginação.
 *
 * @requisito Requisito_Funcional-05: Listar clubes
 * @fluxo Recebe filtros opcionais (nome, sigla do estado, ativo) e parâmetros de paginação, chama o service para buscar os clubes e retorna a lista ou página de resultados.
 * @implementacao Utiliza ClubeService para lógica de busca, suporta tanto listagem completa quanto paginada, e ResponseEntity para resposta HTTP.
 * <b>Incremento:</b> O método retorna uma lista completa se o pageSize for Integer.MAX_VALUE, ou uma página paginada caso contrário.
 * @param nome (opcional) Filtro pelo nome do clube.
 * @param siglaEstado (opcional) Filtro pela sigla do estado do clube.
 * @param ativo (opcional) Filtro pela situação do clube (ativo/inativo).
 * @param pageable Parâmetros de paginação e ordenação.
 * @return Lista ou página de ClubeResponseDto com status 200 OK.
 */
    @GetMapping("/lista")
    public ResponseEntity<?> listarClubes(
            @RequestParam(required = false)
            String nome,
            @RequestParam(required = false) String siglaEstado,
            @RequestParam(required = false) Boolean ativo,
            @PageableDefault(size = 10) Pageable pageable
    ) {
            Page<ClubeResponseDto> clubes = clubeService.listarClubes(nome, siglaEstado, ativo, pageable);

            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(clubes);
        }

    /**
     * Lista clubes sem suporte à paginação.
     *
     * @requisito Requisito_Funcional-05: Listar clubes
     * @fluxo Recebe filtros opcionais (nome, sigla do estado, ativo), chama o service para buscar os clubes e retorna a lista de resultados.
     * @implementacao Utiliza ClubeService para lógica de busca e ResponseEntity para resposta HTTP.
     * @param nome (opcional) Filtro pelo nome do clube.
     * @param siglaEstado (opcional) Filtro pela sigla do estado do clube.
     * @param ativo (opcional) Filtro pela situação do clube (ativo/inativo).
     * @return Lista de ClubeResponseDto com status 200 OK.
     */
    @GetMapping("/lista-sem-paginacao")
    public ResponseEntity<List<ClubeResponseDto>> listarClubesSemPaginacao(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String siglaEstado,
            @RequestParam(required = false) Boolean ativo
    ) {
        List<ClubeResponseDto> clubes = clubeService.listarClubes(nome, siglaEstado, ativo);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(clubes);
    }

}
