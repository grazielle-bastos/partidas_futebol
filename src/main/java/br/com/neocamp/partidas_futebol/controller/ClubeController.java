package br.com.neocamp.partidas_futebol.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    public void getCadastrarClubes() {

    }

    @PostMapping
    public ResponseEntity<Clube> cadastrarClubes(@RequestBody Clube clube) {
        Clube clubeSalvo = clubeService.salvar(clube);
        return ResponseEntity.status(HttpStatus.CREATED).body(clubeSalvo);
    }

}
