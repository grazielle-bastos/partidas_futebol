package br.com.neocamp.partidas_futebol.controller;

import br.com.neocamp.partidas_futebol.dto.EstadioRequestDto;
import br.com.neocamp.partidas_futebol.dto.EstadioResponseDto;
import br.com.neocamp.partidas_futebol.service.EstadioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
// Define o prefixo da URL para acessar os endpoints deste controller
@RequestMapping("/estadio")
public class EstadioController {

    // Injeção de dependência do service de estádio no controller
    // O atributo é final para garantir que ele não será alterado após a injeção de dependência
    private final EstadioService estadioService;

    // Construtor com injeção de dependência do service de estádio no controller
    // O construtor é anotado com @Autowired para indicar que a injeção de dependência deve ser feita automaticamente pelo Spring
    @Autowired
    public EstadioController (EstadioService estadioService) {
        // Atribui o service de estádio recebido no construtor ao atributo do controller
        this.estadioService = estadioService;
    }

    // Endpoint para cadastrar um estádio
    @PostMapping
    // Define o tipo de retorno como ResponseEntity<EstadioResponseDto> para retornar o estádio (DTO) com status da resposta
    public ResponseEntity<EstadioResponseDto> cadastrarEstadio(@RequestBody EstadioRequestDto estadioRequestDto) {
        // Recebe o DTO do estádio pelo corpo da requisição
        EstadioResponseDto estadioSalvo = estadioService.salvar(estadioRequestDto);
        // Retorna o estádio salvo (DTO) com status 201 CREATED
        return ResponseEntity.status(HttpStatus.CREATED).body(estadioSalvo);
        }

    // Endpoint para buscar um estádio pelo ID
    @GetMapping ("/{id}")
    // Define o tipo de retorno como ResponseEntity<EstadioResponseDto> para retornar o estádio (DTO) com status da resposta
    public ResponseEntity<EstadioResponseDto> buscarEstadioPorId(@PathVariable Long id) {
        // Recebe o ID do estádio pela URL
        // Chama o service para buscar o estádio pelo ID
        EstadioResponseDto estadio = estadioService.buscarPorId(id);
        // Retorna o estádio encontrado (DTO) com status 200 OK
        // O metodo body() é usado para definir o corpo da resposta, que é o estádio encontrado (DTO)
        // O metodo status() é usado para definir o status da resposta, que é 200 OK
        // O metodo ResponseEntity é usado para criar uma resposta HTTP com corpo e status personalizados
        return ResponseEntity.status(HttpStatus.OK).body(estadio);
    }

}

