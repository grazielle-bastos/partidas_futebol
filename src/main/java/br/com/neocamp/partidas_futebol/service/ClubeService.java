package br.com.neocamp.partidas_futebol.service;

import br.com.neocamp.partidas_futebol.entity.Clube;
import br.com.neocamp.partidas_futebol.repository.ClubeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClubeService {

    private final ClubeRepository clubeRepository;

    @Autowired
    public ClubeService(ClubeRepository clubeRepository) {
        this.clubeRepository = clubeRepository;
    }

    public Clube salvar(Clube clube) {
        // Aqui podem ser feitas validações ou regras de negócio
        return clubeRepository.save(clube);
    }

    public Clube buscarPorId(Long id) {
        // Busca o clube pelo ID usando o repository
        Optional<Clube> clubeOptional = clubeRepository.findById(id);
        // Se encontrar, retorna o clube
        if (clubeOptional.isPresent()) {
            return clubeOptional.get();
        } else {
            // Se não encontrar, lança exceção
            throw new RuntimeException("Clube não encontrado");
        }
    }
}
