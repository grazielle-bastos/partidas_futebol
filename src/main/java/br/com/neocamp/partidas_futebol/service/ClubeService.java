package br.com.neocamp.partidas_futebol.service;

import br.com.neocamp.partidas_futebol.dto.ClubeResponseDto;
import br.com.neocamp.partidas_futebol.dto.ClubeRequestDto;
import br.com.neocamp.partidas_futebol.entity.Clube;
import br.com.neocamp.partidas_futebol.repository.ClubeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClubeService {

    private final ClubeRepository clubeRepository;

    @Autowired
    public ClubeService(ClubeRepository clubeRepository) {
        this.clubeRepository = clubeRepository;
    }

    public ClubeResponseDto salvar(ClubeRequestDto clubeDto) {
        // Aqui podem ser feitas validações ou regras de negócio
        Clube clube = new Clube(
                clubeDto.getNome(),
                clubeDto.getSiglaEstado(),
                clubeDto.getDataCriacao(),
                clubeDto.getAtivo()
        );
        return toResponseDto(clubeRepository.save(clube));
    }

    public ClubeResponseDto buscarPorId(Long id) {
        // Busca o clube pelo ID usando o repository
        Optional<Clube> clubeOptional = clubeRepository.findById(id);
        // Se encontrar, retorna o clube
        if (clubeOptional.isPresent()) {
            return toResponseDto(clubeOptional.get());
        } else {
            // Se não encontrar, lança exceção
            throw new RuntimeException("Clube não encontrado");
        }
    }

    public ClubeResponseDto atualizarPorId(Long id, ClubeRequestDto clubeAtualizado) {
        // Busca o clube pelo ID usando o repository
        Optional<Clube> clubeOptional = clubeRepository.findById(id);
        // Se encontrar, atualiza os dados do clube
        if (clubeOptional.isPresent()) {
            Clube clube = clubeOptional.get();
            // Atualiza os campos do clube
            clube.setNome(clubeAtualizado.getNome());
            clube.setSiglaEstado(clubeAtualizado.getSiglaEstado());
            clube.setDataCriacao(clubeAtualizado.getDataCriacao());
            clube.setAtivo(clubeAtualizado.getAtivo());
            // Salva o clube atualizado
            Clube clubeSalvo = clubeRepository.save(clube);
            // Retorna o clube atualizado convertido para DTO
            return toResponseDto(clubeSalvo);
        } else {
            throw new RuntimeException("Clube não encontrado");
        }
    }

    public void inativarClubePorId(Long id) {
        // Busca o clube pelo ID usando o repository
        Optional<Clube> clubeOptional = clubeRepository.findById(id);
        // Se encontrar, inativa o clube
        if (clubeOptional.isPresent()) {
            Clube clube = clubeOptional.get();
            clube.setAtivo(false);
            clubeRepository.save(clube);
        } else {
            throw new RuntimeException("Clube não encontrado");
        }
    }

    public List<ClubeResponseDto> listarClubesAtivos() {
        // Chama o metodo do repository, e recebe uma lista de clubes com o campo ativo = true
        List<Clube> clubesAtivos = clubeRepository.buscarClubesAtivos();
        // Cria uma lista de clubes DTO vazia, para adicionar os clubes convertidos
        List<ClubeResponseDto> clubesDto = new ArrayList<>();
        // Para cada clube na lista de clubes ativos, converte para DTO e adiciona na lista de clubes DTO, usando o metodo auxiliar toResponseDto
        for (Clube clube : clubesAtivos) {
            clubesDto.add(toResponseDto(clube));
        }
        return clubesDto;
    }

    // Metodo auxiliar, converte Clube para ClubeResponseDto
    private ClubeResponseDto toResponseDto(Clube clube) {
        return new ClubeResponseDto(
                clube.getId(),
                clube.getNome(),
                clube.getSiglaEstado(),
                clube.getDataCriacao(),
                clube.getAtivo()
        );
    }

}