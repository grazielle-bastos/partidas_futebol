package br.com.neocamp.partidas_futebol.service;

import br.com.neocamp.partidas_futebol.dto.EstadioRequestDto;
import br.com.neocamp.partidas_futebol.dto.EstadioResponseDto;
import br.com.neocamp.partidas_futebol.entity.Estadio;
import br.com.neocamp.partidas_futebol.repository.EstadioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
    public void testarCadastrarEstadioComSucesso() {

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

    //TODO CRIAR TESTES PARA OS DEMAIS MÉTODOS DO ESTADIOSERVICE
    // Exemplo: CadastrarEstadio (Condição Null para total cobertura com retorno 400 BAD REQUEST), buscarPorId, atualizarPorId, listarEstadios, etc


}
