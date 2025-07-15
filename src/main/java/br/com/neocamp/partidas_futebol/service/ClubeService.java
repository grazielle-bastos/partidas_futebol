package br.com.neocamp.partidas_futebol.service;

import br.com.neocamp.partidas_futebol.dto.ClubeResponseDto;
import br.com.neocamp.partidas_futebol.dto.ClubeRequestDto;
import br.com.neocamp.partidas_futebol.entity.Clube;
import br.com.neocamp.partidas_futebol.enums.EstadosBrasil;
import br.com.neocamp.partidas_futebol.repository.ClubeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClubeService {

    // Injeção de dependência do repository
    private final ClubeRepository clubeRepository;

    @Autowired
    public ClubeService(ClubeRepository clubeRepository) {
        this.clubeRepository = clubeRepository;
    }

    public ClubeResponseDto salvar(ClubeRequestDto clubeDto) {
        // Validações de negócio
        if (clubeDto.getNome() == null || clubeDto.getNome().trim().isEmpty() || clubeDto.getSiglaEstado() == null || clubeDto.getSiglaEstado().trim().isEmpty() || clubeDto.getDataCriacao() == null  || clubeDto.getAtivo() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Todos os campos são obrigatórios e não podem ser vazios");
        }
        if (clubeDto.getNome().trim().length() < 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome inválido: deve ter no mínimo 2 caracteres");
        }
        // Chamada do metodo auxiliar que valida a sigla do estado usando o enum EstadosBrasil
        validarSiglaEstado(clubeDto.getSiglaEstado());
        // Verifica se a data de criação é no futuro
        if (clubeDto.getDataCriacao().isAfter(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data de criação não pode ser no futuro");
        }
        //Verifica duplicidade de clube por nome no mesmo estado
        Optional<Clube> clubeExistente = clubeRepository.findByNomeAndSiglaEstado(clubeDto.getNome().trim(), clubeDto.getSiglaEstado().trim().toUpperCase());
        if(clubeExistente.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um clube com o mesmo nome no mesmo estado");
        }
        // Cria um novo clube com os dados do DTO
        Clube clube = new Clube(
                clubeDto.getNome(),
                clubeDto.getSiglaEstado().trim().toUpperCase(),
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
        // Verifica se o clube foi encontrado
        if (clubeOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Clube não encontrado");
        }
        // Converte o DTO para entidade e atualiza os campos do clube encontrado
        Clube clube = clubeOptional.get();
        // Validações de negócio para os campos que podem ser atualizados
        if (clubeAtualizado.getNome() == null || clubeAtualizado.getNome().trim().length() < 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome inválido: deve ter no mínimo 2 caracteres");
        }
        // Chamada do metodo auxiliar que valida a sigla do estado usando o enum EstadosBrasil
        validarSiglaEstado(clubeAtualizado.getSiglaEstado());
        // Verifica se a data de criação é no futuro
        if (clubeAtualizado.getDataCriacao() == null || clubeAtualizado.getDataCriacao().isAfter(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Data de criação inválida: não pode ser no futuro");
        }


        // Se encontrar, atualiza os dados do clube
        if (clubeOptional.isPresent()) {
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

    // Metodo auxiliar para validar a sigla do estado
    private void validarSiglaEstado(String siglaEstado) {
        try {
            EstadosBrasil.valueOf(siglaEstado.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sigla do estado inválida: deve ser uma sigla válida de um estado do Brasil");
        }
    }

}