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
        if (clubeOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Clube não encontrado");
        }
        return toResponseDto(clubeOptional.get());
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

        // TODO: Validar se a nova data de criação é posterior à data de alguma partida já registrada para este clube (requisito de cenários de exceção do projeto - 409 Conflict)

        // Validação de duplicidade: impede atualizar para um nome/estado que já existe em outro clube (ID diferente), mas permite atualizar o próprio clube mesmo que o nome/estado não mudem.
        Optional<Clube> clubeExistente = clubeRepository.findByNomeAndSiglaEstado(clubeAtualizado.getNome().trim(), clubeAtualizado.getSiglaEstado().trim().toUpperCase());
        if (clubeExistente.isPresent() && !clubeExistente.get().getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um clube com o mesmo nome no mesmo estado");
        }
        // Atualiza os campos do clube
        clube.setNome(clubeAtualizado.getNome());
        clube.setSiglaEstado(clubeAtualizado.getSiglaEstado());
        clube.setDataCriacao(clubeAtualizado.getDataCriacao());
        clube.setAtivo(clubeAtualizado.getAtivo());
        // Salva o clube atualizado
        Clube clubeSalvo = clubeRepository.save(clube);
        // Retorna o clube atualizado convertido para DTO
        return toResponseDto(clubeSalvo);
    }

    public void inativarClubePorId(Long id) {
        // Busca o clube pelo ID usando o repository
        Optional<Clube> clubeOptional = clubeRepository.findById(id);
        if (clubeOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Clube não encontrado");
        }
        // Se encontrar, inativa o clube
        Clube clube = clubeOptional.get();
        clube.setAtivo(false);
        clubeRepository.save(clube);
    }

    // Metodo para listar clubes de acordo com os filtros sem paginação
    public List<ClubeResponseDto> listarClubes(String nome, String siglaEstado, Boolean ativo) {
        // Verifica se foi informado algum filtro e chama o repository correspondente
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
            // Se não foi informado nenhum filtro, retorna todos os clubes
            clubes = clubeRepository.findAll();
        }

        // Converte a lista de clubes para lista de DTOs de resposta e retorna
        List<ClubeResponseDto> listarClubesDto = new ArrayList<>();
        // Itera sobre a lista de clubes e chama o metodo auxiliar toResponseDto para cada clube
        for (Clube clube : clubes) {
            // Adiciona o clube convertido para DTO na lista de DTOs de resposta
            listarClubesDto.add(toResponseDto(clube));
        }
        return listarClubesDto;
    }

    // Metodo para listar clubes de acordo com os filtros e com paginação
    public Page<ClubeResponseDto> listarClubes(String nome, String siglaEstado, Boolean ativo, Pageable pageable) {
        // Verifica se foi informado algum filtro de paginação e chama o repository correspondente
        Page<Clube> clubesPage = clubeRepository.buscarClubesPorPaginacao(nome, siglaEstado, ativo, pageable);
        // Itera sobre a lista de clubes e chama o metodo auxiliar toResponseDto para converter cada clube da lista para DTO de resposta, e retorna
        return clubesPage.map(this::toResponseDto);
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