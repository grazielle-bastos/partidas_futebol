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


@RestController
@RequestMapping("/clube")
public class ClubeController {

    
    private final ClubeService clubeService;

    
    @Autowired
    public ClubeController(ClubeService clubeService) {
        this.clubeService = clubeService;
    }

    
    @Operation(
            summary = "Cadastrar clube",
            description = "Endpoint para cadastrar um novo clube no sistema.")
    @PostMapping
    public ResponseEntity<ClubeResponseDto> cadastrarClubes(@Valid @RequestBody ClubeRequestDto clubeRequestDto) {
        ClubeResponseDto clubeSalvo = clubeService.cadastrarClube(clubeRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(clubeSalvo);
    }

    
    @GetMapping("/{id}")
    public ResponseEntity<ClubeResponseDto> buscarClubePorId(@PathVariable Long id) {
        ClubeResponseDto clube = clubeService.buscarPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(clube);
    }

    
    @PutMapping("/{id}")
    public ResponseEntity<ClubeResponseDto> atualizarClube(@PathVariable Long id, @RequestBody ClubeRequestDto clubeAtualizado) {
        ClubeResponseDto clube = clubeService.atualizarPorId(id, clubeAtualizado);
        return ResponseEntity.status(HttpStatus.OK).body(clube);
    }

        @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativarClube(@PathVariable Long id) {
        clubeService.inativarClubePorId(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


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
