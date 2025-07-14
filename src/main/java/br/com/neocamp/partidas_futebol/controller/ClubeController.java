package br.com.neocamp.partidas_futebol.controller;

import br.com.neocamp.partidas_futebol.dto.ClubeRequestDto;
import br.com.neocamp.partidas_futebol.dto.ClubeResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.neocamp.partidas_futebol.entity.Clube;
import br.com.neocamp.partidas_futebol.service.ClubeService;

import java.util.List;

@RestController
@RequestMapping("/clube")
public class ClubeController {

    private final ClubeService clubeService;

    @Autowired
    public ClubeController(ClubeService clubeService) {
        this.clubeService = clubeService;
    }

    @GetMapping()
    public void getCadastrarClubes() {}

    @PostMapping
    public ResponseEntity<ClubeResponseDto> cadastrarClubes(@RequestBody ClubeRequestDto clubeRequestDto) {
        // Recebe o DTO do clube pelo corpo da requisição
        ClubeResponseDto clubeSalvo = clubeService.salvar(clubeRequestDto);
        // Retorna o clube salvo (DTO) com status 201 CREATED
        return ResponseEntity.status(HttpStatus.CREATED).body(clubeSalvo);
    }

    // Endpoint para buscar um clube pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<ClubeResponseDto> buscarClubePorId(@PathVariable Long id) {
        // Recebe o ID do clube pela URL
        // Chama o service para buscar o clube pelo ID
        ClubeResponseDto clube = clubeService.buscarPorId(id);
        // Retorna o clube encontrado (DTO) com status 200 OK
        return ResponseEntity.status(HttpStatus.OK).body(clube);
    }

    //Endpoint para atualizar um clube
    @PutMapping("/{id}")
    public ResponseEntity<ClubeResponseDto> atualizarClube(@PathVariable Long id, @RequestBody ClubeRequestDto clubeAtualizado) {
        // Recebe o ID do clube pela URL, e o clube atualizado pelo corpo da requisição
        // Chama o service para buscar o clube pelo ID e atualizar os dados
        ClubeResponseDto clube = clubeService.atualizarPorId(id, clubeAtualizado);
        // Retorna o clube atualizado (DTO) com status 200 OK
        return ResponseEntity.status(HttpStatus.OK).body(clube);
    }

    //Endpoint para inativar um clube
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativarClube(@PathVariable Long id) {
        // Recebe o ID do clube pela URL
        // Chama o service para inativar o clube pelo ID
        clubeService.inativarClubePorId(id);
        // Retorna status 204 NO CONTENT (não retorna o clube, pois ele foi inativado e não deve ser mais exibido)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    //Endpoint para listar clubes ativos
    @GetMapping("/ativos")
    public ResponseEntity<List<ClubeResponseDto>> listarClubesAtivos() {
        // Chama o service, e recebe a lista de clubes ativos
        List<ClubeResponseDto> clubesAtivos = clubeService.listarClubesAtivos();
        // Retorna a lista de clubes ativos com status 200 OK
        return ResponseEntity.status(HttpStatus.OK).body(clubesAtivos);
    }

}

