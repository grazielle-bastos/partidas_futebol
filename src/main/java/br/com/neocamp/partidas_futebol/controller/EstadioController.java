package br.com.neocamp.partidas_futebol.controller;

import br.com.neocamp.partidas_futebol.dto.EstadioRequestDto;
import br.com.neocamp.partidas_futebol.dto.EstadioResponseDto;
import br.com.neocamp.partidas_futebol.service.EstadioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        EstadioResponseDto estadioSalvo = estadioService.salvar(estadioRequestDto);
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

}

