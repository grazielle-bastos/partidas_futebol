package br.com.neocamp.partidas_futebol.controller;

import br.com.neocamp.partidas_futebol.dto.ClubeRequestDto;
import br.com.neocamp.partidas_futebol.dto.ClubeResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.neocamp.partidas_futebol.service.ClubeService;

import java.util.List;

@RestController
// Define o prefixo da URL para acessar os endpoints deste controller
@RequestMapping("/clube")
public class ClubeController {

    // Injeção de dependência do service de clube no controller
    private final ClubeService clubeService;

    // Construtor com injeção de dependência do service de clube no controller
    // O construtor é anotado com @Autowired para indicar que a injeção de dependência deve ser feita automaticamente pelo Spring
    @Autowired
    public ClubeController(ClubeService clubeService) {
        // Atribui o service de clube recebido no construtor ao atributo do controller
        this.clubeService = clubeService;
    }

    // Endpoint para cadastrar um clube
    @PostMapping
    // Define o tipo de retorno como ResponseEntity<ClubeResponseDto> para retornar o clube (DTO) com status da resposta
    public ResponseEntity<ClubeResponseDto> cadastrarClubes(@RequestBody ClubeRequestDto clubeRequestDto) {
        // Recebe o DTO do clube pelo corpo da requisição
        ClubeResponseDto clubeSalvo = clubeService.salvar(clubeRequestDto);
        // Retorna o clube salvo (DTO) com status 201 CREATED
        return ResponseEntity.status(HttpStatus.CREATED).body(clubeSalvo);
    }

    // Endpoint para buscar um clube pelo ID
    @GetMapping("/{id}")
    // Define o tipo de retorno como ResponseEntity<ClubeResponseDto> para retornar o clube (DTO) com status da resposta
    public ResponseEntity<ClubeResponseDto> buscarClubePorId(@PathVariable Long id) {
        // Recebe o ID do clube pela URL
        // Chama o service para buscar o clube pelo ID
        ClubeResponseDto clube = clubeService.buscarPorId(id);
        // Retorna o clube encontrado (DTO) com status 200 OK
        return ResponseEntity.status(HttpStatus.OK).body(clube);
    }

    //Endpoint para atualizar um clube
    @PutMapping("/{id}")
    // Define o tipo de retorno como ResponseEntity<ClubeResponseDto> para retornar o clube (DTO) com status da resposta
    public ResponseEntity<ClubeResponseDto> atualizarClube(@PathVariable Long id, @RequestBody ClubeRequestDto clubeAtualizado) {
        // Recebe o ID do clube pela URL, e o clube atualizado pelo corpo da requisição
        // Chama o service para buscar o clube pelo ID e atualizar os dados
        ClubeResponseDto clube = clubeService.atualizarPorId(id, clubeAtualizado);
        // Retorna o clube atualizado (DTO) com status 200 OK
        return ResponseEntity.status(HttpStatus.OK).body(clube);
    }

    //Endpoint para inativar um clube
    @DeleteMapping("/{id}")
    // Define o tipo de retorno como ResponseEntity<Void> para retornar apenas o status da resposta (sem corpo)
    public ResponseEntity<Void> inativarClube(@PathVariable Long id) {
        // Recebe o ID do clube pela URL
        // Chama o service para inativar o clube pelo ID
        clubeService.inativarClubePorId(id);
        // Retorna status 204 NO CONTENT (não retorna o clube, pois ele foi inativado e não deve ser mais exibido)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    //Endpoint para listar clubes
    @GetMapping("/lista")
    // Recebe os parâmetros de filtro (nome, sigla do estado, ativo) como parâmetros da URL
    public ResponseEntity<?> listarClubes(
            @RequestParam(required = false)
            String nome,
            @RequestParam(required = false) String siglaEstado,
            @RequestParam(required = false) Boolean ativo,
            Pageable pageable
    ) {
        // Verifica se foi informado algum filtro de paginação
        // Se a página for 0 e o tamanho for o máximo, chama o service para listar todos os clubes sem paginação
        if (pageable.getPageSize() == Integer.MAX_VALUE) {
            // Chama o service para listar os clubes de acordo com os filtros
            List<ClubeResponseDto> clubes = clubeService.listarClubes(nome, siglaEstado, ativo);
            // Retorna a lista de clubes encontrados (DTO) com status 200 OK
            return ResponseEntity.status(HttpStatus.OK).body(clubes);
        } else {
            // Chama o service para listar os clubes de acordo com os filtros e com paginação
            Page<ClubeResponseDto> clubes = clubeService.listarClubes(nome, siglaEstado, ativo, pageable);
            // Retorna a lista de clubes encontrados (DTO) com status 200 OK
            return ResponseEntity.status(HttpStatus.OK).body(clubes);
        }
    }

}
