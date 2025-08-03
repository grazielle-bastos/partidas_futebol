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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
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

    
    public ClubeResponseDto cadastrarClube(ClubeRequestDto clubeDto) {
        if (clubeDto.getNome() == null || clubeDto.getNome().trim().isEmpty() || clubeDto.getSiglaEstado() == null || clubeDto.getSiglaEstado().trim().isEmpty() || clubeDto.getDataCriacao() == null  || clubeDto.getAtivo() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Todos os campos são obrigatórios e não podem ser vazios");
        }
        if (clubeDto.getNome().trim().length() < 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome inválido: deve ter no mínimo 2 caracteres");
        }

        validarSiglaEstado(clubeDto.getSiglaEstado());

        if (clubeDto.getDataCriacao().isAfter(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data de criação não pode ser no futuro");
        }

        Optional<Clube> clubeExistente = clubeRepository.findByNomeAndSiglaEstado(clubeDto.getNome().trim(), clubeDto.getSiglaEstado().trim().toUpperCase());
        if(clubeExistente.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um clube com o mesmo nome no mesmo estado");
        }

        Clube clube = new Clube(
                clubeDto.getNome().trim(),
                clubeDto.getSiglaEstado().trim().toUpperCase(),
                clubeDto.getDataCriacao(),
                clubeDto.getAtivo()
        );
        return toResponseDto(clubeRepository.save(clube));
    }

    
    public ClubeResponseDto buscarPorId(Long id) {

        Optional<Clube> clubeOptional = clubeRepository.findById(id);

        if (clubeOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Clube não encontrado");
        }
        return toResponseDto(clubeOptional.get());
    }

    
    public ClubeResponseDto atualizarPorId(Long id, ClubeRequestDto clubeAtualizado) {

        Optional<Clube> clubeOptional = clubeRepository.findById(id);

        if (clubeOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Clube não encontrado");
        }

        Clube clube = clubeOptional.get();

        if (clubeAtualizado.getNome() == null || clubeAtualizado.getNome().trim().length() < 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome inválido: deve ter no mínimo 2 caracteres");
        }

        validarSiglaEstado(clubeAtualizado.getSiglaEstado());

        if (clubeAtualizado.getDataCriacao() == null || clubeAtualizado.getDataCriacao().isAfter(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Data de criação inválida: não pode ser no futuro");
        }

        // TODO: Validar se a nova data de criação é posterior à data de alguma partida já registrada para este clube (requisito de cenários de exceção do projeto - 409 Conflict)


        Optional<Clube> clubeExistente = clubeRepository.findByNomeAndSiglaEstado(clubeAtualizado.getNome().trim(), clubeAtualizado.getSiglaEstado().trim().toUpperCase());
        if (clubeExistente.isPresent() && !clubeExistente.get().getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um clube com o mesmo nome no mesmo estado");
        }

        clube.setNome(clubeAtualizado.getNome());
        clube.setSiglaEstado(clubeAtualizado.getSiglaEstado());
        clube.setDataCriacao(clubeAtualizado.getDataCriacao());
        clube.setAtivo(clubeAtualizado.getAtivo());

        Clube clubeSalvo = clubeRepository.save(clube);

        return toResponseDto(clubeSalvo);
    }

    
    public void inativarClubePorId(Long id) {

        Optional<Clube> clubeOptional = clubeRepository.findById(id);
        if (clubeOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Clube não encontrado");
        }

        Clube clube = clubeOptional.get();
        clube.setAtivo(false);
        clubeRepository.save(clube);
    }

    
    public List<ClubeResponseDto> listarClubes(String nome, String siglaEstado, Boolean ativo) {

        List<Clube> clubes;

        if (nome != null && siglaEstado != null && ativo != null) {
            clubes = clubeRepository.findByNomeContainingIgnoreCaseAndSiglaEstadoAndAtivo(nome, siglaEstado, ativo);
        } else if (nome != null && siglaEstado != null) {
            clubes = clubeRepository.findByNomeContainingIgnoreCaseAndSiglaEstado(nome, siglaEstado);
        } else if (nome != null && ativo != null) {
            clubes = clubeRepository.findByNomeContainingIgnoreCaseAndAtivo(nome, ativo);
        } else if (siglaEstado != null && ativo != null) {
            clubes = clubeRepository.findBySiglaEstadoAndAtivo(siglaEstado, ativo);
        } else if (nome != null) {
            clubes = clubeRepository.findByNomeContainingIgnoreCase(nome);
        } else if (siglaEstado != null) {
            clubes = clubeRepository.findBySiglaEstado(siglaEstado);
        } else if (ativo != null) {
            clubes = clubeRepository.findByAtivo(ativo);
        } else {
            clubes = clubeRepository.findAll();
        }

        List<ClubeResponseDto> listarClubesDto = new ArrayList<>();

        for (Clube clube : clubes) {

            listarClubesDto.add(toResponseDto(clube));
        }
        return listarClubesDto;
    }

    
    public Page<ClubeResponseDto> listarClubes(String nome, String siglaEstado, Boolean ativo, Pageable pageable) {

        Page<Clube> clubesPage = clubeRepository.buscarClubesPorPaginacao(nome, siglaEstado, ativo, pageable);

        return clubesPage.map(this::toResponseDto);
    }

    
    private ClubeResponseDto toResponseDto(Clube clube) {
        return new ClubeResponseDto(
                clube.getId(),
                clube.getNome(),
                clube.getSiglaEstado(),
                clube.getDataCriacao(),
                clube.getAtivo()
        );
    }

    
    private void validarSiglaEstado(String siglaEstado) {
        try {
            EstadosBrasil.valueOf(siglaEstado.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sigla do estado inválida: deve ser uma sigla válida de um estado do Brasil");
        }
    }

}