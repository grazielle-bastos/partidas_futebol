package br.com.neocamp.partidas_futebol.controller;

import br.com.neocamp.partidas_futebol.dto.EstadioRequestDto;
import br.com.neocamp.partidas_futebol.dto.EstadioResponseDto;
import br.com.neocamp.partidas_futebol.service.EstadioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller responsável pelos endpoints de operações com estádios de futebol.
 *
 * @requisito Gerencia cadastro, edição, inativação, busca e listagem de estádios.
 * @fluxo Recebe requisições HTTP, valida dados e delega regras de negócio ao service.
 * @implementacao Utiliza o EstadioService para executar as operações principais.
 *
 * Endpoints disponíveis:
 * - POST /estadio: Cadastrar estádio
 * - GET /estadio/{id}: Buscar estádio por ID
 * - PUT /estadio/{id}: Atualizar estádio
 * - DELETE /estadio/{id}: Inativar estádio (soft delete)
 * - GET /estadio/lista: Listar estádios com filtros e paginação
 */
@RestController
@RequestMapping("/estadio")
public class EstadioController {

    /**
     * Serviço responsável por executar as regras de negócio relacionadas a estádios.
     * Injetado automaticamente pelo Spring para ser utilizado nos endpoints do controller.
     */
    private final EstadioService estadioService;

/**
     * Construtor do controller de estádios.
     *
     * @param estadioService Serviço de estádios injetado automaticamente pelo Spring.
     * @implementacao Utiliza injeção de dependência para garantir que o controller tenha acesso às regras de negócio dos estádios.
     */
    @Autowired
    public EstadioController (EstadioService estadioService) {
        // Atribui o service de estádio recebido no construtor ao atributo do controller
        this.estadioService = estadioService;
    }

    /**
     * Cadastra um novo estádio no sistema.
     *
     * @requisito Requisito_Funcional-11: Cadastro de estádio
     * @fluxo Recebe os dados do estádio via DTO, valida, salva no banco e retorna o estádio salvo.
     * @implementacao Utiliza EstadioService para lógica de negócio e ResponseEntity para resposta HTTP.
     * @param estadioRequestDto DTO contendo os dados do estádio a ser cadastrado
     * @return EstadioResponseDto/ResponseEntity com o estádio cadastrado (DTO) e status 201 CREATED
     */
    @PostMapping
    public ResponseEntity<EstadioResponseDto> cadastrarEstadio(@RequestBody EstadioRequestDto estadioRequestDto) {
        EstadioResponseDto estadioSalvo = estadioService.cadastrarEstadio(estadioRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(estadioSalvo);
        }

    /**
     * Busca um estádio pelo ID.
     *
     * @requisito Requisito_Funcional-13: Busca de estádio por ID
     * @fluxo Recebe o ID do estádio pela URL, busca no banco e retorna o estádio encontrado.
     * @implementacao Utiliza EstadioService para lógica de negócio e ResponseEntity para resposta HTTP.
     * @param id ID do estádio a ser buscado
     * @return EstadioResponseDto/ResponseEntity com o estádio encontrado (DTO) e status 200 OK
     */
    @GetMapping ("/{id}")
    public ResponseEntity<EstadioResponseDto> buscarEstadioPorId(@PathVariable Long id) {

        EstadioResponseDto estadio = estadioService.buscarPorId(id);

        return ResponseEntity.status(HttpStatus.OK).body(estadio);
    }

    /**
     * Atualiza os dados de um estádio existente.
     *
     * @requisito Requisito_Funcional-12: Editar um estádio
     * @fluxo Recebe o ID do estádio pela URL e os novos dados atualizados via DTO, valida, atualiza no banco e retorna o estádio atualizado.
     * @implementacao Utiliza EstadioService para lógica de negócio e ResponseEntity para resposta HTTP.
     * @param id Identificador único do estádio a ser atualizado
     * @param estadioAtualizado DTO contendo os novos dados do estádio a ser atualizado recebidos no corpo da requisição
     * @return EstadioResponseDto/ResponseEntity com os dados do estádio atualizado (DTO) e status 200 OK
     */
    @PutMapping("/{id}")
    public ResponseEntity<EstadioResponseDto> atualizarEstadio(@PathVariable Long id, @RequestBody EstadioRequestDto estadioAtualizado) {
        EstadioResponseDto estadio = estadioService.atualizarPorId(id, estadioAtualizado);
        return ResponseEntity.status(HttpStatus.OK).body(estadio);
    }

    /**
     * Lista os estádios cadastrados com opção de filtro por nome e com suporte à paginação.
     *
     * @requisito Requisito_Funcional-14: Listagem de estádios
     * @fluxo Recebe o nome do estádio como filtro opcional e parâmetros de pagina para paginação, chama o service para buscar os estádios no repository e retorna uma lista paginada de estádios.
     * @implementacao Utiliza EstadioService para lógica de listagem e ResponseEntity para resposta HTTP.
     * @param nome filtro opcional para buscar estádios por nome.
     * @param pageable parâmetros de paginação, como número da página e tamanho da página.
     * @return ResponseEntity com uma página de EstadioResponseDto contendo os dados dos estádios encontrados e status 200 OK.
     */
    @GetMapping("/lista")
    public ResponseEntity<Page<EstadioResponseDto>> listarEstadios(
            @RequestParam(required = false) String nome,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Page<EstadioResponseDto> estadioPage = estadioService.listarEstadios(nome, pageable);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(estadioPage);
    }

}

