package br.com.neocamp.partidas_futebol.service;

import br.com.neocamp.partidas_futebol.dto.EstadioRequestDto;
import br.com.neocamp.partidas_futebol.dto.EstadioResponseDto;
import br.com.neocamp.partidas_futebol.entity.Estadio;
import br.com.neocamp.partidas_futebol.repository.EstadioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Teste unitário para a classe EstadioService.
 * Utiliza Mockito para simular o comportamento do repositório de estádio.
 *
 * @requisito Testar as regras de negócio da camada de serviço EstadioService.
 * @fluxo Verifica se as operações de CRUD e outras lógicas estão funcionando corretamente.
 * @implementacao Utiliza Mockito para criar um mock do EstadioRepository e testar o EstadioService isoladamente.
 */
@ExtendWith(SpringExtension.class)
public class EstadioServiceTest {

    /**
     * Mock do repositório de estádio.
     * Utilizado para simular o comportamento do repositório durante os testes.
     */
    @Mock
    private EstadioRepository estadioRepository;

    /**
     * Instância do serviço de estádio que será testada.
     * A anotação @InjectMocks injeta o mock do repositório no serviço.
     */
    @InjectMocks
    private EstadioService estadioService;

    /**
     * Instância de EstadioRequestDto utilizada nos testes.
     * Pode ser usada para criar ou atualizar dados de estádio durante os testes.
     * Representa a requisição de dados para criação ou atualização de um estádio, enviado pelo cliente na requisição HTTP da API.
     */
    EstadioRequestDto estadioRequestDto = new EstadioRequestDto();

    /**
     * Testa o método cadastrar do EstadioService para o caso em que os dados do clube são válidos.
     *
     * <p>
     * Cenário: O usuário tenta cadastrar um estádio com um nome válido.
     * </p>
     *
     *<b>Etapas do teste:</b>
     * <ul>
     *     <li><b>Arrange:</b> Prepara os dados de entrada e configura o comportamento dos mocks.</li>
     *     <li><b>Act:</b> Chama o método cadastrarEstadio com o DTO de estádio.</li>
     *     <li><b>Assert:</b>
     *         Verifica se o estádio foi cadastrado corretamente, sem lançar exceções.
     *         O teste deve passar se o método cadastrarEstadio não lançar nenhuma exceção e
     *         o estádio for salvo corretamente no repositório.
     *     </li>
     * </ul>
     *
     * @cenário O usuário cadastra um estádio com nome válido e não duplicado.
     * @resultado O estádio é cadastrado com sucesso e retornado como EstadioResponseDto.
     */
    @Test
    public void testarCadastrarEstadioComDadosValidos() {

        estadioRequestDto.setNome("Neo Química Arena");

        when(estadioRepository.findByNome(anyString())).thenReturn(Optional.empty());

        Estadio estadioCadastrado = new Estadio("Neo Química Arena");

        when(estadioRepository.save(any((Estadio.class)))).thenReturn(estadioCadastrado);

        EstadioResponseDto estadioResponseDto = estadioService.cadastrarEstadio(estadioRequestDto);

        assertNotNull(estadioResponseDto);
        assertEquals("Neo Química Arena", estadioResponseDto.getNome());
    }

    /**
     * Testa o método cadastrar do EstadioService para o caso de duplicidade de nome.
     *
     * <p>
     * Cenário: O usuário tenta cadastrar um estádio com um nome que já existe no banco de dados.
     * </p>
     *
     *<b>Etapas do teste:</b>
     * <ul>
     *     <li><b>Arrange:</b> Configura o DTO com um nome duplicado, e configura o comportamento dos mocks</li>
     *     <li><b>Act & Assert:</b> Chama o método cadastrarEstadio e espera que a exceção ResponseStatusException seja lançada.</li>
     * </ul>
     *
     * @cenário O usuário tenta cadastrar um estádio com duplicidade de nome.
     * @resultado O método lança uma ResponseStatusException com status 409 CONFLICT.
     */
    @Test
    public void testarCadastrarEstadioComDuplicidadeDeNome() {

        estadioRequestDto.setNome("Neo Química Arena");
        when(estadioRepository.findByNome(anyString()))
            .thenReturn(Optional.of(new Estadio("Neo Química Arena")));

        ResponseStatusException ex = assertThrows(
            ResponseStatusException.class,
            () -> estadioService.cadastrarEstadio(estadioRequestDto)
        );

        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
        assertEquals("Já existe um estádio com o mesmo nome", ex.getReason());

    }

    /**
     * Testa o método cadastrar do EstadioService para o caso de nome inválido.
     *
     * <p>
     * Cenário: O usuário tenta cadastrar um estádio com um nome inválido (menor que 3 caracteres).
     * </p>
     *
     *<b>Etapas do teste:</b>
     * <ul>
     *     <li><b>Arrange:</b> Configura o DTO com um nome inválido.</li>
     *     <li><b>Act & Assert:</b> Chama o método cadastrarEstadio e espera que a exceção ResponseStatusException seja lançada.</li>
     * </ul>
     *
     * @cenário O usuário tenta cadastrar um estádio com nome inválido.
     * @resultado O método lança uma ResponseStatusException com status 400 BAD REQUEST.
     */
    @Test
    public void testarCadastrarEstadioComNomeInvalido() {

        estadioRequestDto.setNome("X ");

        ResponseStatusException ex = assertThrows(
            ResponseStatusException.class,
            () -> estadioService.cadastrarEstadio(estadioRequestDto)
        );

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertEquals("Nome inválido: deve ter no mínimo 3 caracteres", ex.getReason());

    }

    /**
     * Testa o método cadastrar do EstadioService para o caso de nome nulo.
     *
     * <p>
     * Cenário: O usuário tenta cadastrar um estádio com o nome nulo.
     * </p>
     *
     *<b>Etapas do teste:</b>
     * <ul>
     *     <li><b>Arrange:</b> Configura o DTO com nome nulo.</li>
     *     <li><b>Act & Assert:</b> Chama o método cadastrarEstadio e espera que a exceção ResponseStatusException seja lançada.</li>
     * </ul>
     *
     * @cenário O usuário tenta cadastrar um estádio com nome nulo.
     * @resultado O método lança uma ResponseStatusException com status 400 BAD REQUEST.
     */
    @Test
    public void testarCadastrarEstadioComNomeNulo() {

        estadioRequestDto.setNome(null);

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> estadioService.cadastrarEstadio(estadioRequestDto)
        );

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertEquals("Nome inválido: deve ter no mínimo 3 caracteres", ex.getReason());

    }

    /**
     * Testa o método buscarPorId do EstadioService para o caso de sucesso.
     *
     * <p>
     * Cenário: O usuário busca um estádio pelo ID e o estádio existe no banco de dados, este método deve retornar os dados DTO do estádio encontrado.
     * </p>
     *
     *<b>Etapas do teste:</b>
     * <ul>
     *     <li><b>Arrange:</b> Configura o mock do repositório para retornar um estádio com o ID especificado.</li>
     *     <li><b>Act:</b> Chama o método buscarPorId com o ID do estádio.</li>
     *     <li><b>Assert:</b> Verifica se o DTO do estádio retornado tem os dados esperados.</li>
     * </ul>
     *
     * @cenário O usuário busca um estádio pelo ID e o estádio existe.
     * @resultado O método retorna um EstadioResponseDto com os dados corretos do estádio.
     */
    @Test
    public void testarBuscarEstadioPorIdExistente() {

        Long estadioId = 1L;
        Estadio estadio = new Estadio("Neo Química Arena");

        estadio.setId(estadioId);

        when(estadioRepository.findById(estadioId)).thenReturn(Optional.of(estadio));

        EstadioResponseDto estadioResponseDto = estadioService.buscarPorId(estadioId);

        assertNotNull(estadioResponseDto);
        assertEquals(estadioId, estadioResponseDto.getId());
        assertEquals("Neo Química Arena", estadioResponseDto.getNome());

    }

    /**
     * Testa o método buscarPorId do EstadioService para o caso em que o estádio não existe.
     *
     * <p>
     * Cenário: O usuário busca um estádio pelo ID e o estádio não existe no banco de dados, este método deve lançar uma exceção ResponseStatusException com status 404 NOT FOUND.
     * </p>
     *
     *<b>Etapas do teste:</b>
     * <ul>
     *     <li><b>Arrange:</b> Configura o mock do repositório para retornar um Optional vazio.</li>
     *     <li><b>Act:</b> Chama o método buscarPorId com um ID inexistente.</li>
     *     <li><b>Assert:</b> Verifica se a exceção ResponseStatusException é lançada com o status e mensagem corretos.</li>
     * </ul>
     *
     * @cenário O usuário busca um estádio pelo ID e o estádio não existe.
     * @resultado O método lança uma ResponseStatusException com status 404 NOT FOUND.
     */
    @Test
    public void testarBuscarEstadioPorIdInexistente() {

        Long estadioId = 99L;

        when(estadioRepository.findById(estadioId)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class, () -> estadioService.buscarPorId(estadioId)
        );

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertEquals("Estádio não encontrado", ex.getReason());

    }

    /**
     * Testa o método atualizarPorId do EstadioService para o caso de sucesso.
     *
     * <p>
     * Cenário: O usuário tenta atualizar um estádio existente com nome válido.
     * O método deve retornar os dados atualizados do estádio como EstadioResponseDto.
     * </p>
     *
     *<b>Etapas do teste:</b>
     * <ul>
     *     <li><b>Arrange:</b> Configura o mock do repositório para retornar o estádio existente e simula a atualização.</li>
     *     <li><b>Act:</b> Chama o método atualizarPorId com o ID do estádio e o DTO atualizado.</li>
     *     <li><b>Assert:</b> Verifica se o DTO do estádio atualizado foi retornado corretamente.</li>
     * </ul>
     *
     * @cenário O usuário atualiza um estádio existente com nome válido.
     * @resultado O método retorna um EstadioResponseDto com o nome atualizado do estádio.
     */
    @Test
    public void testarAtualizarEstadioComNomeValido() {

        Long estadioId = 1L;

        Estadio estadioExistente = new Estadio("Neo Química Arena");
        estadioExistente.setId(estadioId);

        EstadioRequestDto estadioAtualizado = new EstadioRequestDto();
        estadioAtualizado.setId(estadioId);
        estadioAtualizado.setNome("Allianz Parque");

        when(estadioRepository.findById(estadioId)).thenReturn(Optional.of(estadioExistente));
        when(estadioRepository.findByNome("Neo Química Arena")).thenReturn(Optional.empty());

        Estadio estadioSalvo = new Estadio("Allianz Parque");
        estadioSalvo.setId(estadioId);
        when(estadioRepository.save(any(Estadio.class))).thenReturn(estadioSalvo);

        EstadioResponseDto estadioResponseDto = estadioService.atualizarPorId(estadioId, estadioAtualizado);

        assertNotNull(estadioResponseDto);
        assertEquals(estadioId, estadioResponseDto.getId());
        assertEquals("Allianz Parque", estadioResponseDto.getNome());
    }

    /**
     * Testa o método atualizarPorId do EstadioService para o caso de nome inválido.
     *
     * <p>
     *     Cenário: O usuário tenta atualizar um estádio com um nome inválido (menor que 3 caracteres).
     * </p>
     *
     * <b>Etapas do teste:</b>
     * <ul>
     *     <li><b>Arrange:</b> Configura o mock do repositório para retornar o estádio existente.</li>
     *     <li><b>Act:</b> Chama o método atualizarPorId com o ID do estádio e o DTO atualizado com nome inválido.</li>
     *     <li><b>Assert:</b> Verifica se a exceção ResponseStatusException é lançada com o status e mensagem corretos.</li>
     * </ul>
     * @cenário O usuário tenta atualizar um estádio com nome inválido.
     * @resultado O método lança uma ResponseStatusException com status 400 BAD REQUEST.
     */
    @Test
    public void testarAtualizarEstadioComNomeInvalido() {

        Long estadioId = 1L;

        EstadioRequestDto estadioAtualizado = new EstadioRequestDto();

        estadioAtualizado.setId(estadioId);
        estadioAtualizado.setNome("X ");

        when(estadioRepository.findById(estadioId)).thenReturn(Optional.of(new Estadio("Neo Química Arena")));

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> estadioService.atualizarPorId(estadioId, estadioAtualizado)
        );

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertEquals("Nome inválido: deve ter no mínimo 3 caracteres", ex.getReason());
    }

    /**
     * Testa o método atualizarPorId do EstadioService para o caso de nome nulo.
     *
     * <p>
     * Cenário: O usuário tenta atualizar um estádio com o nome nulo.
     * </p>
     *
     *<b>Etapas do teste:</b>
     * <ul>
     *     <li><b>Arrange:</b> Configura o mock do repositório para retornar o estádio existente.</li>
     *     <li><b>Act:</b> Chama o método atualizarPorId com o ID do estádio e o DTO atualizado com nome nulo.</li>
     *     <li><b>Assert:</b> Verifica se a exceção ResponseStatusException é lançada com o status e mensagem corretos.</li>
     * </ul>
     *
     * @cenário O usuário tenta atualizar um estádio com nome nulo.
     * @resultado O método lança uma ResponseStatusException com status 400 BAD REQUEST.
     */
    @Test
    public void testarAtualizarEstadioComNomeNulo() {

        Long id = 1L;
        EstadioRequestDto estadioAtualizado = new EstadioRequestDto();

        estadioAtualizado.setId(id);
        estadioAtualizado.setNome(null);

        when(estadioRepository.findById(id)).thenReturn(Optional.of(new Estadio("Neo Química Arena")));

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> estadioService.atualizarPorId(id, estadioAtualizado)
        );

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertEquals("Nome inválido: deve ter no mínimo 3 caracteres", ex.getReason());

    }
    /**
     * Testa o método atualizarPorId do EstadioService para o caso de duplicidade de nome.
     *
     * <p>
     * Cenário: O usuário tenta atualizar um estádio com um nome que já existe no banco de dados.
     * </p>
     *
     *<b>Etapas do teste:</b>
     * <ul>
     *     <li><b>Arrange:</b> Configura o mock do repositório para retornar o estádio existente e simula a duplicidade.</li>
     *     <li><b>Act:</b> Chama o método atualizarPorId com o ID do estádio e o DTO atualizado com nome duplicado.</li>
     *     <li><b>Assert:</b> Verifica se a exceção ResponseStatusException é lançada com o status e mensagem corretos.</li>
     * </ul>
     *
     * @cenário O usuário tenta atualizar um estádio com duplicidade de nome.
     * @resultado O método lança uma ResponseStatusException com status 409 CONFLICT.
     */
    @Test
    public void testarAtualizarEstadioComDuplicidadeDeNome() {

            Long estadioExistenteId = 1L;
            Long estadioDuplicadoId = 2L;

            Estadio nomeEstadioExistente = new Estadio("Allianz Parque");
            nomeEstadioExistente.setId(estadioExistenteId);
            Estadio nomeEstadioDuplicado = new Estadio("Neo Química Arena");
            nomeEstadioDuplicado.setId(estadioDuplicadoId);

            EstadioRequestDto estadioAtualizadoDto = new EstadioRequestDto();
            estadioAtualizadoDto.setId(estadioExistenteId);
            estadioAtualizadoDto.setNome("Neo Química Arena");

            when(estadioRepository.findById(estadioExistenteId)).thenReturn(Optional.of(nomeEstadioExistente));
            when(estadioRepository.findByNome("Neo Química Arena")).thenReturn(Optional.of(nomeEstadioDuplicado));

            ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> estadioService.atualizarPorId(estadioExistenteId, estadioAtualizadoDto)
            );

            assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
            assertEquals("Já existe um estádio com o mesmo nome", ex.getReason());

    }
    /**
     * Testa o método atualizarPorId do EstadioService para o caso de sucesso ao atualizar com nome e ID iguais.
     *
     * <p>
     * Cenário: O usuário tenta atualizar um estádio com o mesmo nome e ID, não deve ocorrer erro de duplicidade.
     * </p>
     *
     *<b>Etapas do teste:</b>
     * <ul>
     *     <li><b>Arrange:</b> Configura o mock do repositório para retornar o estádio existente.</li>
     *     <li><b>Act:</b> Chama o método atualizarPorId com o ID e nome iguais.</li>
     *     <li><b>Assert:</b> Verifica se o DTO do estádio atualizado foi retornado corretamente.</li>
     * </ul>
     *
     * @cenário O usuário atualiza um estádio com nome e ID iguais.
     * @resultado O método retorna um EstadioResponseDto com os dados do estádio atualizado.
     */
    @Test
    public void testarAtualizarEstadioComNomeEIdIgual() {

        Long estadioId = 1L;

        Estadio estadioExistente = new Estadio("Allianz Parque");
        estadioExistente.setId(estadioId);

        EstadioRequestDto estadioAtualizadoDto = new EstadioRequestDto();
        estadioAtualizadoDto.setId(estadioId);
        estadioAtualizadoDto.setNome("Allianz Parque");

        when(estadioRepository.findById(estadioId)).thenReturn(Optional.of(estadioExistente));
        when(estadioRepository.findByNome("Allianz Parque")).thenReturn(Optional.of(estadioExistente));
        when(estadioRepository.save(estadioExistente))
                .thenReturn(estadioExistente);

        EstadioResponseDto estadioSalvoDto = estadioService.atualizarPorId(estadioId, estadioAtualizadoDto);

        assertNotNull(estadioSalvoDto);
        assertEquals(estadioId, estadioSalvoDto.getId());
        assertEquals("Allianz Parque", estadioSalvoDto.getNome());

        verify(estadioRepository).save(estadioExistente);

    }

    /**
     * Testa o método atualizarPorId do EstadioService para o caso de ID inexistente.
     *
     * <p>
     * Cenário: O usuário tenta atualizar um estádio com um ID que não existe no banco de dados.
     * </p>
     *
     *<b>Etapas do teste:</b>
     * <ul>
     *     <li><b>Arrange:</b> Configura o mock do repositório para retornar um Optional vazio.</li>
     *     <li><b>Act:</b> Chama o método atualizarPorId com um ID inexistente.</li>
     *     <li><b>Assert:</b> Verifica se a exceção ResponseStatusException é lançada com o status e mensagem corretos.</li>
     * </ul>
     *
     * @cenário O usuário tenta atualizar um estádio com ID inexistente.
     * @resultado O método lança uma ResponseStatusException com status 404 NOT FOUND.
     */
    @Test
    public void testarAtualizarEstadioComIdInexistente() {

        Long estadioId = 99L;

        EstadioRequestDto estadioAtualizado = new EstadioRequestDto();
        estadioAtualizado.setId(estadioId);
        estadioAtualizado.setNome("Allianz Parque");

        when(estadioRepository.findById(estadioId)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> estadioService.atualizarPorId(estadioId, estadioAtualizado)
        );

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertEquals("Estádio não encontrado", ex.getReason());

    }
    /**
     * Testa o método listarEstadios do EstadioService sem filtro de nome.
     *
     * <p>
     * Cenário: O usuário lista todos os estádios cadastrados sem aplicar filtro de nome.
     * </p>
     *
     *<b>Etapas do teste:</b>
     * <ul>
     *     <li><b>Arrange:</b> Configura o mock do repositório para retornar uma página de estádios.</li>
     *     <li><b>Act:</b> Chama o método listarEstadios com nome nulo e parâmetros de paginação.</li>
     *     <li><b>Assert:</b> Verifica se a lista de estádios retornada contém os dados esperados.</li>
     * </ul>
     *
     * @cenário O usuário lista todos os estádios sem filtro de nome.
     * @resultado O método retorna uma página de EstadioResponseDto com os dados dos estádios encontrados.
     */
    @Test
    public void testarListarTodosOsEstadiosSemOFiltroDeNome() {

        Pageable pageable = PageRequest.of(0, 3, Sort.by("nome").ascending());

        Estadio estadio1 = new Estadio("Neo Química Arena");
        estadio1.setId(1L);
        Estadio estadio2 = new Estadio("Allianz Parque");
        estadio2.setId(2L);
        Estadio estadio3 = new Estadio("Maracanã");
        estadio3.setId(3L);

        Page<Estadio> estadioPage =
                new PageImpl<>(List.of(estadio1, estadio2, estadio3), pageable, 2);

        when(estadioRepository.findAll(pageable)).thenReturn(estadioPage);

        Page<EstadioResponseDto> estadioResponsePageDto =
                estadioService.listarEstadios(null, pageable);

        assertEquals(3, estadioResponsePageDto.getTotalElements());
        assertEquals("Neo Química Arena", estadioResponsePageDto.getContent().get(0).getNome());
        assertEquals("Allianz Parque", estadioResponsePageDto.getContent().get(1).getNome());
        assertEquals("Maracanã", estadioResponsePageDto.getContent().get(2).getNome());

        verify(estadioRepository).findAll(pageable);

    }

    /**
     * Testa o método listarEstadios do EstadioService com filtro de nome.
     *
     * <p>
     * Cenário: O usuário lista os estádios filtrando pelo nome.
     * </p>
     *
     *<b>Etapas do teste:</b>
     * <ul>
     *     <li><b>Arrange:</b> Configura o mock do repositório para retornar uma página de estádios filtrados pelo nome.</li>
     *     <li><b>Act:</b> Chama o método listarEstadios com um filtro de nome e parâmetros de paginação.</li>
     *     <li><b>Assert:</b> Verifica se a lista de estádios retornada contém os dados esperados.</li>
     * </ul>
     *
     * @cenário O usuário lista os estádios filtrando pelo nome.
     * @resultado O método retorna uma página de EstadioResponseDto com os dados dos estádios filtrados.
     */
    @Test
    public void testarListarEstadiosComFiltroDeNome() {

        String filtroNome = "Parque";
        Pageable pageable = PageRequest.of(0, 5);

        Estadio estadio1 = new Estadio("Allianz Parque");
        estadio1.setId(1L);

        Page<Estadio> estadioPage = new PageImpl<>(
                List.of(estadio1), pageable, 1
        );

        when(estadioRepository.findByNomeContainingIgnoreCase(filtroNome, pageable))
                .thenReturn(estadioPage);

        Page<EstadioResponseDto> estadioResponsePageDto = estadioService.listarEstadios(filtroNome, pageable);

        assertEquals(1, estadioResponsePageDto.getTotalElements());
        assertEquals("Allianz Parque", estadioResponsePageDto.getContent().get(0).getNome());

        verify(estadioRepository).findByNomeContainingIgnoreCase(filtroNome, pageable);
    }

    /**
     * Testa o método listarEstadios do EstadioService quando não há estádios cadastrados.
     *
     * <p>
     * Cenário: O usuário tenta listar os estádios, mas não há nenhum cadastrado.
     * </p>
     *
     *<b>Etapas do teste:</b>
     * <ul>
     *     <li><b>Arrange:</b> Configura o mock do repositório para retornar uma página vazia.</li>
     *     <li><b>Act:</b> Chama o método listarEstadios com nome nulo e parâmetros de paginação.</li>
     *     <li><b>Assert:</b> Verifica se a página retornada está vazia.</li>
     * </ul>
     *
     * @cenário O usuário tenta listar os estádios, mas não há nenhum cadastrado.
     * @resultado O método retorna uma página vazia de EstadioResponseDto.
     */
    @Test
    public void testarListarComPaginaVaziaDeEstadios() {

        Pageable pageable = PageRequest.of(0, 10);
        when(estadioRepository.findAll(pageable))
                .thenReturn(Page.empty(pageable));

        Page<EstadioResponseDto> estadioResponsePageDto = estadioService.listarEstadios(null, pageable);

        assertTrue(estadioResponsePageDto.isEmpty());

        verify(estadioRepository).findAll(pageable);

    }
}
