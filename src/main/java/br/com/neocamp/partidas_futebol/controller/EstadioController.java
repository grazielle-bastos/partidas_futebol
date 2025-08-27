package br.com.neocamp.partidas_futebol.controller;

import br.com.neocamp.partidas_futebol.dto.estadioDto.EstadioRequestDto;
import br.com.neocamp.partidas_futebol.dto.estadioDto.EstadioResponseDto;
import br.com.neocamp.partidas_futebol.service.EstadioService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/estadio")
public class EstadioController {

    
    private final EstadioService estadioService;


    @Autowired
    public EstadioController (EstadioService estadioService) {

        this.estadioService = estadioService;
    }


    @PostMapping
    public ResponseEntity<EstadioResponseDto> cadastrarEstadio(@Valid @RequestBody EstadioRequestDto estadioRequestDto) {
        EstadioResponseDto estadioSalvo = estadioService.cadastrarEstadio(estadioRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(estadioSalvo);
        }

    
    @GetMapping ("/{id}")
    public ResponseEntity<EstadioResponseDto> buscarEstadioPorId(@PathVariable Long id) {

        EstadioResponseDto estadio = estadioService.buscarPorId(id);

        return ResponseEntity.status(HttpStatus.OK).body(estadio);
    }

    
    @PutMapping("/{id}")
    public ResponseEntity<EstadioResponseDto> atualizarEstadio(@PathVariable Long id, @RequestBody EstadioRequestDto estadioAtualizado) {
        EstadioResponseDto estadio = estadioService.atualizarPorId(id, estadioAtualizado);
        return ResponseEntity.status(HttpStatus.OK).body(estadio);
    }

    
    @GetMapping("/lista")
    public ResponseEntity<Page<EstadioResponseDto>> listarEstadios(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false, defaultValue = "10") int page,
            @RequestParam(required = false, defaultValue = "0") int size,
            @RequestParam(required = false, defaultValue = "id") String sortBy
            //@PageableDefault(size = 10, sort = "id, asc") Pageable pageable
    )
    {

        Page<EstadioResponseDto> estadioPage = estadioService.listarEstadios(nome, PageRequest.of(page, size, Sort.Direction.ASC, sortBy));

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(estadioPage);
    }

}

