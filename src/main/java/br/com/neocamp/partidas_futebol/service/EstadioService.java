package br.com.neocamp.partidas_futebol.service;

import br.com.neocamp.partidas_futebol.dto.EstadioRequestDto;
import br.com.neocamp.partidas_futebol.dto.EstadioResponseDto;
import br.com.neocamp.partidas_futebol.entity.Estadio;
import br.com.neocamp.partidas_futebol.repository.EstadioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Serviço responsável por implementar as regras de negócio relacionadas à entidade Estádio.
 *
 * @requisito Centralizar validações, regras e fluxos de operações de estádio (cadastro, busca, edição, inativação, listagem).
 * @fluxo Recebe dados do controller, valida, aplica regras e interage com o repositório. Retorna DTOs para o controller.
 * @implementacao Utiliza o EstadioRepository para persistência, separa lógica de negócio da camada de apresentação.
 *
 * <b>Didática:</b>
 * <ul>
 *     <li>Facilita manutenção e testes isolados das regras de negócio.</li>
 *     <li>Centraliza a lógica de validação e manipulação de dados de estádio.</li>
 * </ul>
 *
 * Camada de serviço da entidade Clube.
 * @see br.com.neocamp.partidas_futebol.repository.EstadioRepository
 * @see br.com.neocamp.partidas_futebol.dto.EstadioRequestDto
 * @see br.com.neocamp.partidas_futebol.dto.EstadioResponseDto
 */
@Service
public class EstadioService {

    /**
     * Construtor padrão do repositório de estádio.
     *
     * @requisito Permitir injeção automática do Spring Data JPA para operações de CRUD.
     * @fluxo O Spring instancia e injeta o repositório nas classes de serviço que o utilizam.
     * @implementacao Utiliza a anotação @Autowired para injeção de dependência do repositório de estádio. Não requer implementação manual de métodos CRUD, pois o Spring Data JPA já fornece uma implementação padrão.
      */
    private final EstadioRepository estadioRepository;

    /**
     * Construtor padrão do repositório de estádio.
     *
     * @requisito Permitir injeção automática do Spring Data JPA para operações de CRUD.
     * @fluxo O Spring instancia e injeta o repositório nas classes de serviço que o utilizam.
     * @implementacao Utiliza a anotação @Autowired para injeção de dependência do repositório de estádio. Não requer implementação manual de métodos CRUD, pois o Spring Data JPA já fornece uma implementação padrão.
     */
    @Autowired
    public EstadioService(EstadioRepository estadioRepository) {
        // Atribui o repository de estádio recebido no construtor ao atributo do service
        this.estadioRepository = estadioRepository;
    }

    /**
     * Metodo para cadastrar um novo estádio.
     *
     * @requisito Requisito_Funcional-11: Cadastro de estádio
     * @fluxo Recebe os dados do estádio via DTO, valida, verifica duplicidade, salva no banco e retorna o estádio salvo.
     * @implementacao Valida os dados do DTO, verifica se já existe um estádio com o mesmo nome, cria uma nova entidade de estádio, salva no repositório e converte para DTO de resposta. Lança exceções apropriadas em caso de erros de validação ou duplicidade.
     * @param estadioDto dados do estádio recebidos no corpo da requisição como DTO, para cadastro de um novo estádio.
     * @return EstadioResponseDto DTO com os dados do estádio cadastrado e status 201 CREATED.
     * @throws ResponseStatusException 400 BAD REQUEST para tentativa de cadastro sem os dados mínimos ou com nome inválido, 409 CONFLICT se já existir um estádio com o mesmo nome.
     */
    public EstadioResponseDto cadastrarEstadio(EstadioRequestDto estadioDto) {

        if (estadioDto.getNome() == null || estadioDto.getNome().trim().length() < 3) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome inválido: deve ter no mínimo 3 caracteres");
        }
        if (estadioRepository.findByNome(estadioDto.getNome().trim()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um estádio com o mesmo nome");
        }

            Estadio estadio = new Estadio(
                    estadioDto.getNome().trim()
                    );

            return toResponseDto(estadioRepository.save(estadio));
    }

    /**
     * Busca um estádio pelo seu ID.
     *
     * @requisito Requisito_Funcional-13: Busca de estádio por ID
     * @fluxo Recebe o ID do estádio, busca no repositório e retorna os dados do estádio encontrado. Lança exceção se não encontrado.
     * @implementacao Utiliza o repositório para buscar o estádio pelo ID, converte a entidade para DTO de resposta e trata exceções, caso o estádio não seja encontrado.
     * @param id ID do estádio a ser buscado.
     * @return EstadioResponseDto com os dados do estádio encontrado.
     * @throws ResponseStatusException 404 NOT FOUND se o estádio com o ID fornecido não existir.
     */
    public EstadioResponseDto buscarPorId(Long id) {

        Optional<Estadio> estadioOptional = estadioRepository.findById(id);

        if (estadioOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Estádio não encontrado");
        }

        return toResponseDto(estadioOptional.get());
    }

    /**
     * Atualiza os dados de um estádio existente pelo seu ID.
     *
     * @requisito Requisito_Funcional-12: Edição de estádio
     * @fluxo Recebe o ID do estádio e os novos dados via DTO, valida, verifica duplicidade, atualiza no repositório e retorna o estádio atualizado.
     * @implementacao Valida os dados do DTO, verifica se já existe um estádio com o mesmo nome, atualiza a entidade de estádio existente, salva no repositório e converte para DTO de resposta. Lança exceções apropriadas em caso de erros de validação ou duplicidade.
     * @param id ID do estádio a ser atualizado.
     * @param estadioAtualizado dados do estádio recebidos no corpo da requisição como DTO, para atualização dos dados do estádio.
     * @return EstadioResponseDto DTO com os dados do estádio atualizado e status 200 OK.
     * @throws ResponseStatusException 400 BAD REQUEST para tentativa de atualização sem os dados mínimos ou com nome inválido, 404 NOT FOUND se o estádio com o ID fornecido não existir, 409 CONFLICT se já existir um estádio com o mesmo nome.
     */
    public EstadioResponseDto atualizarPorId(Long id, EstadioRequestDto estadioAtualizado) {

        Optional<Estadio> estadioOptional = estadioRepository.findById(id);

        if(estadioOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Estádio não encontrado");
        }

        Estadio estadio = estadioOptional.get();

        if (estadioAtualizado.getNome() == null || estadioAtualizado.getNome().trim().length() < 3) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome inválido: deve ter no mínimo 3 caracteres");
        }

        Optional<Estadio> estadioExistente = estadioRepository
                .findByNome(estadioAtualizado.getNome().trim());

        if (estadioExistente.isPresent() && !estadioExistente.get().getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um estádio com o mesmo nome");
        }

        estadio.setNome(estadioAtualizado.getNome());

        Estadio estadioSalvo = estadioRepository.save(estadio);

        return toResponseDto(estadioSalvo);

    }

    /**
     * Lista todos os estádios ou filtra por nome, paginando os resultados.
     *
     * @requisito Requisito_Funcional-14: Listagem de estádios
     * @fluxo Recebe um nome opcional para filtro e parâmetros de paginação, busca os estádios no repositório e retorna uma lista paginada de DTOs de resposta.
     * @implementacao Utiliza o repositório para buscar os estádios com base no nome (se fornecido) e na paginação, converte as entidades para DTOs de resposta e retorna a lista paginada.
     * @param nome Nome do estádio para filtro (opcional).
     * @param pageable Parâmetros de paginação (página, tamanho, ordenação).
     * @return Page<EstadioResponseDto> com os dados dos estádios encontrados.
     */
    public Page<EstadioResponseDto> listarEstadios(String nome, Pageable pageable) {

        Page<Estadio> estadioPage;

        if (nome != null) {
            estadioPage = estadioRepository.findByNomeContainingIgnoreCase(nome.trim(), pageable);
        } else {
            estadioPage = estadioRepository.findAll(pageable);
        }

        return estadioPage.map(this::toResponseDto);

    }

    /**
     * Converte uma entidade Estadio em um DTO de resposta EstadioResponseDto.
     *
     * @requisito Encapsular os dados da entidade Estadio em um DTO para retorno ao cliente.
     * @fluxo Recebe uma entidade Estadio, extrai os dados necessários e cria um DTO de resposta.
     * @implementacao Cria e retorna um EstadioResponseDto com os dados da entidade Estadio.
     * @param estadio Entidade Estadio a ser convertida em DTO de resposta.
     * @return EstadioResponseDto com os dados do estádio.
     */
    private EstadioResponseDto toResponseDto(Estadio estadio) {

        return new EstadioResponseDto(
                estadio.getId(),
                estadio.getNome()
        );
    }

}
