/**package br.com.neocamp.partidas_futebol.controller;

import br.com.neocamp.partidas_futebol.dto.partidaDto.PartidaRequestDto;
import br.com.neocamp.partidas_futebol.dto.partidaDto.PartidaResponseDto;
import br.com.neocamp.partidas_futebol.service.PartidaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/partida")
public class PartidaController {

    private final PartidaService partidaService;

    public PartidaController(PartidaService partidaService) {
        this.partidaService = partidaService;
    }

    @PostMapping
    public ResponseEntity<PartidaResponseDto> cadastrarPartida(@Valid @RequestBody PartidaRequestDto partidaRequestDto) {

        PartidaResponseDto partidaSalva = partidaService.cadastrarPartida(partidaRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(partidaSalva);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PartidaResponseDto> buscarPartidaPorId(@PathVariable Long id) {

        PartidaResponseDto partida = partidaService.buscarPorId(id);

        return ResponseEntity.status(HttpStatus.OK).body(partida);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PartidaResponseDto> atualizarPartida(@PathVariable Long id, @RequestBody PartidaRequestDto partidaAtualizada) {

        PartidaResponseDto partida = partidaService.atualizarPorId(id, partidaAtualizada);

        return ResponseEntity.status(HttpStatus.OK).body(partida);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPartida (@PathVariable Long id) {

        partidaService.deletarPartidaPorId(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/lista")
    public ResponseEntity<Page<PartidaResponseDto>> listarPartidas(
            @RequestParam(required = false) String clubeMandante,
            @RequestParam(required = false) String clubeVisitante,
            @RequestParam(required = false) String estadio,
            @PageableDefault(size = 10) Pageable pageable
    ) {

        Page<PartidaResponseDto> partidas = partidaService.listarPartidas(clubeMandante, clubeVisitante, estadio, pageable);

        return ResponseEntity.status(HttpStatus.OK)
                .body(partidas);
    }



}*/
