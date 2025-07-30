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

/**
 * Serviço responsável por implementar as regras de negócio relacionadas à entidade Clube.
 *
 * @requisito Centralizar validações, regras e fluxos de operações de clubes (cadastro, busca, edição, inativação, listagem).
 * @fluxo Recebe dados do controller, valida, aplica regras e interage com o repositório. Retorna DTOs para o controller.
 * @implementacao Utiliza ClubeRepository para persistência, separa lógica de negócio da camada de apresentação.
 *
 * <b>Didática:</b>
 * <ul>
 *   <li>Facilita manutenção e testes isolados das regras de negócio.</li>
 *   <li>Evita lógica complexa no controller, promovendo arquitetura limpa.</li>
 * </ul>
 *
 * Camada de serviço da entidade Clube.
 * @see br.com.neocamp.partidas_futebol.repository.ClubeRepository
 * @see br.com.neocamp.partidas_futebol.dto.ClubeRequestDto
 * @see br.com.neocamp.partidas_futebol.dto.ClubeResponseDto
 */
@Service
public class ClubeService {

    /**
     * Construtor padrão do repositório de clubes.
     *
     * @requisito Permitir injeção automática do Spring Data JPA.
     * @fluxo O Spring instancia e injeta o repositório nas classes de serviço.
     * @implementacao Não requer implementação manual, pois é gerenciado pelo framework.
     */
    private final ClubeRepository clubeRepository;

    /**
     * Construtor padrão do repositório de clubes.
     *
     * @requisito Permitir injeção automática do Spring Data JPA.
     * @fluxo O Spring instancia e injeta o repositório nas classes de serviço.
     * @implementacao Não requer implementação manual, pois é gerenciado pelo framework.
     */
    @Autowired
    public ClubeService(ClubeRepository clubeRepository) {
        this.clubeRepository = clubeRepository;
    }

    /**
     * Cadastra um novo clube no sistema.
     *
     * @requisito Requisito_Funcional-01: Cadastro de clube
     * @fluxo Recebe DTO, valida dados, verifica duplicidade, salva no banco e retorna DTO do clube salvo.
     * @implementacao Valida nome, estado, data e duplicidade. Lança exceções 400 ou 409 conforme regras.
     * @param clubeDto Dados do clube recebidos para cadastro.
     * @return ClubeResponseDto com os dados do clube cadastrado.
     * @throws ResponseStatusException 400 para dados inválidos, 409 para duplicidade.
     */
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

    /**
     * Busca um clube pelo seu ID.
     *
     * @requisito Requisito_Funcional-04: Buscar um clube
     * @fluxo Recebe o ID, consulta o repositório e retorna o DTO do clube. Lança exceção se não encontrar.
     * @implementacao Utiliza Optional do repository. Lança ResponseStatusException 404 se não existir.
     * @param id Identificador único do clube.
     * @return ClubeResponseDto com os dados do clube encontrado.
     * @throws ResponseStatusException 404 se o clube não existir.
     */
    public ClubeResponseDto buscarPorId(Long id) {

        Optional<Clube> clubeOptional = clubeRepository.findById(id);

        if (clubeOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Clube não encontrado");
        }
        return toResponseDto(clubeOptional.get());
    }

    /**
     * Atualiza os dados de um clube existente pelo seu ID.
     *
     * @requisito Requisito_Funcional-02: Editar um clube
     * @fluxo Recebe o ID e os dados atualizados, valida, verifica conflitos e salva no banco. Retorna o clube atualizado.
     * @implementacao Valida nome, estado, data e duplicidade antes de atualizar. Lança exceções para dados inválidos, conflitos ou clube inexistente.
     * @param id Identificador único do clube a ser atualizado.
     * @param clubeAtualizado DTO com os dados atualizados do clube.
     * @return ClubeResponseDto com os dados do clube atualizado.
     * @throws ResponseStatusException 400 para dados inválidos, 409 para conflitos, 404 se o clube não existir.
     */
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

    /**
     * Inativa (soft delete) um clube pelo seu ID.
     *
     * @requisito Requisito_Funcional-03: Inativar um clube
     * @fluxo Recebe o ID, verifica se o clube existe, marca como inativo e salva no banco.
     * @implementacao Não exclui do banco, apenas altera o campo ativo para false. Lança exceção se o clube não existir.
     * @param id Identificador único do clube a ser inativado.
     * @throws ResponseStatusException 404 se o clube não existir.
     */
    public void inativarClubePorId(Long id) {

        Optional<Clube> clubeOptional = clubeRepository.findById(id);
        if (clubeOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Clube não encontrado");
        }

        Clube clube = clubeOptional.get();
        clube.setAtivo(false);
        clubeRepository.save(clube);
    }

    /**
     * Lista clubes com filtros opcionais (nome, estado, ativo).
     *
     * @requisito Requisito_Funcional-05: Listar clubes
     * @fluxo Recebe filtros opcionais, busca no banco e retorna lista de clubes.
     * @implementacao Utiliza métodos do repository para diferentes combinações de filtros. Retorna DTOs.
     * @param nome (opcional) Filtro pelo nome do clube.
     * @param siglaEstado (opcional) Filtro pela sigla do estado.
     * @param ativo (opcional) Filtro pela situação do clube.
     * @return Lista de ClubeResponseDto com os clubes encontrados.
     */
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

    /**
     * Lista clubes com filtros opcionais e suporte à paginação.
     *
     * @requisito Requisito_Funcional-05: Listar clubes (paginado)
     * @fluxo Recebe filtros e parâmetros de paginação, busca no banco e retorna página de clubes.
     * @implementacao Utiliza consulta customizada do repository para paginação e filtros. Retorna DTOs.
     * @param nome (opcional) Filtro pelo nome do clube.
     * @param siglaEstado (opcional) Filtro pela sigla do estado.
     * @param ativo (opcional) Filtro pela situação do clube.
     * @param pageable Parâmetros de paginação e ordenação.
     * @return Página de ClubeResponseDto com os clubes encontrados.
     */
    public Page<ClubeResponseDto> listarClubes(String nome, String siglaEstado, Boolean ativo, Pageable pageable) {

        Page<Clube> clubesPage = clubeRepository.buscarClubesPorPaginacao(nome, siglaEstado, ativo, pageable);

        return clubesPage.map(this::toResponseDto);
    }

    /**
     * Converte uma entidade Clube para o DTO de resposta.
     *
     * @requisito Encapsular os dados do clube para resposta da API.
     * @fluxo Recebe a entidade, mapeia os campos e retorna o DTO.
     * @implementacao Cria e retorna um ClubeResponseDto com os dados da entidade Clube.
     * @param clube Entidade Clube a ser convertida.
     * @return ClubeResponseDto com os dados do clube.
     */
    private ClubeResponseDto toResponseDto(Clube clube) {
        return new ClubeResponseDto(
                clube.getId(),
                clube.getNome(),
                clube.getSiglaEstado(),
                clube.getDataCriacao(),
                clube.getAtivo()
        );
    }

    /**
     * Valida se a sigla do estado informada existe no enum EstadosBrasil.
     *
     * @requisito Garantir que apenas siglas válidas sejam aceitas nas operações de clube.
     * @fluxo Recebe a sigla, verifica no enum e lança exceção se for inválida.
     * @implementacao Utiliza o enum EstadosBrasil para validação. Lança exceção 400 para sigla inválida.
     * @param siglaEstado Sigla do estado brasileiro a ser validada.
     * @throws ResponseStatusException 400 se a sigla for inválida.
     */
    private void validarSiglaEstado(String siglaEstado) {
        try {
            EstadosBrasil.valueOf(siglaEstado.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sigla do estado inválida: deve ser uma sigla válida de um estado do Brasil");
        }
    }

}