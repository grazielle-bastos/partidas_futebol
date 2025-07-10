package br.com.neocamp.partidas_futebol.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.neocamp.partidas_futebol.entity.Clube;
import br.com.neocamp.partidas_futebol.service.ClubeService;

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
    public ResponseEntity<Clube> cadastrarClubes(@RequestBody Clube clube) {
        Clube clubeSalvo = clubeService.salvar(clube);
        return ResponseEntity.status(HttpStatus.CREATED).body(clubeSalvo);
    }

    // Endpoint para buscar um clube pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<Clube> buscarClubePorId(@PathVariable Long id) {
        // Recebe o ID do clube pela URL
        // Chama o service para buscar o clube pelo ID
        Clube clube = clubeService.buscarPorId(id);
        // Retorna o clube encontrado com status 200 OK
        return ResponseEntity.ok(clube);
    }

}

