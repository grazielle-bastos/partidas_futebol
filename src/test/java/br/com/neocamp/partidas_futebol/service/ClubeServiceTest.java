package br.com.neocamp.partidas_futebol.service;

import br.com.neocamp.partidas_futebol.dto.ClubeRequestDto;
import br.com.neocamp.partidas_futebol.dto.ClubeResponseDto;
import br.com.neocamp.partidas_futebol.entity.Clube;
import br.com.neocamp.partidas_futebol.repository.ClubeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.ArgumentMatchers.anyBoolean;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Classe de testes unitários para o {@link ClubeService}.
 *
 * <p>
 * Utiliza o framework Mockito para simular (mockar) as dependências do serviço, permitindo testar apenas a lógica da camada de serviço,
 * sem acessar o banco de dados real. Os testes aqui garantem que as regras de negócio do ClubeService funcionam corretamente em diferentes cenários.
 * </p>
 *
 * <b>Didática:</b>
 * <ul>
 *   <li><b>Objetivo:</b> Validar o comportamento dos métodos do ClubeService de forma isolada.</li>
 *   <li><b>Ferramentas:</b> Usa JUnit 5 para estrutura dos testes e Mockito para simulação das dependências.</li>
 *   <li><b>Benefício:</b> Testes rápidos, confiáveis e sem efeitos colaterais externos.</li>
 * </ul>
 *
 * @testcontexto Testes unitários focados na lógica de negócio do serviço de clubes.
 * @mockedby Mockito (com @Mock e @InjectMocks)
 */
@ExtendWith(MockitoExtension.class)
public class ClubeServiceTest {

    /**
     * Mock do ClubeRepository utilizado nos testes unitários.
     *
     * <p>
     * Este mock é criado automaticamente pelo Mockito, graças à anotação {@code @Mock}.
     * Ele simula o comportamento do repositório de clubes, permitindo controlar as respostas dos métodos
     * sem acessar o banco de dados real.
     * </p>
     *
     * <b>Didática:</b>
     * <ul>
     *   <li><b>Isolamento:</b> Garante que os testes do service não dependam do banco de dados.</li>
     *   <li><b>Controle:</b> Permite definir exatamente o que cada método do repository deve retornar em cada cenário de teste.</li>
     *   <li><b>Prática:</b> Essencial para testar apenas a lógica da camada de serviço, simulando as dependências externas.</li>
     * </ul>
     *
     * @mockedby Mockito (usando @Mock)
     * @contexto Usado para simular o acesso ao banco de dados nos testes do ClubeService.
     */
    @Mock
    private ClubeRepository clubeRepository;

    /**
     * Instância do ClubeService utilizada nos testes unitários.
     *
     * <p>
     * Esta instância é criada e gerenciada automaticamente pelo Mockito, graças à anotação {@code @InjectMocks}.
     * O Mockito injeta (automaticamente) os mocks das dependências necessárias (como o ClubeRepository) dentro do service,
     * permitindo que o comportamento do service seja testado isoladamente, sem acessar o banco de dados real.
     * </p>
     *
     * <b>Didática:</b>
     * <ul>
     *   <li><b>Injeção de dependência:</b> O Mockito cria o objeto ClubeService e injeta os mocks das dependências nele.</li>
     *   <li><b>Objetivo:</b> Garantir que o teste foque apenas na lógica do service, simulando o comportamento das dependências.</li>
     *   <li><b>Benefício:</b> Testes mais rápidos, previsíveis e sem efeitos colaterais externos.</li>
     * </ul>
     *
     * @mockedby Mockito (usando @InjectMocks)
     * @contexto Usado para testar os métodos do ClubeService com dependências simuladas.
     */
    @InjectMocks
    private ClubeService clubeService;

    /**
     * Objeto DTO utilizado para simular os dados de entrada de um clube no teste.
     *
     * <p>
     * Este objeto será preenchido com os dados necessários para testar o fluxo de cadastro de clube,
     * representando o que seria enviado pelo cliente na requisição da API.
     * </p>
     *
     * @arrange Etapa de preparação dos dados de entrada para o teste (Arrange).
     */
    ClubeRequestDto clubeDto = new ClubeRequestDto();

    /**
     * Testa o método salvar do ClubeService para o caso em que os dados do clube são inválidos.
     *
     * <p>
     * Cenário: Recebe um DTO de clube com nome, sigla do estado, data de criação e situação ativo.
     * Os dados são inválidos (nome nulo, sigla do estado nula, data de criação futura ou situação nula).
     * </p>
     *
     * <b>Etapas do teste:</b>
     * <ul>
     *   <li><b>Arrange:</b> Prepara os dados de entrada e configura o comportamento dos mocks.</li>
     *   <li><b>Act:</b> Executa o método a ser testado (salvar).</li>
     *   <li><b>Assert:</b> Verifica se uma exceção é lançada com status 400 (BAD_REQUEST).</li>
     * </ul>
     *
     * @cenario Cadastro de clube com dados inválidos.
     * @resultado Deve lançar ResponseStatusException com status BAD_REQUEST.
     */
    @Test
    public void testarSalvarClubeComDadosInvalidos() {
        clubeDto.setNome(null);
        clubeDto.setSiglaEstado("SP");
        clubeDto.setDataCriacao(LocalDate.of(1914, 8, 26));
        clubeDto.setAtivo(true);
        assertThrows(ResponseStatusException.class, () -> clubeService.salvar(clubeDto));

        clubeDto.setNome("");
        assertThrows(ResponseStatusException.class, () -> clubeService.salvar(clubeDto));

        clubeDto.setNome(" ");
        assertThrows(ResponseStatusException.class, () -> clubeService.salvar(clubeDto));

        clubeDto.setNome("A");
        assertThrows(ResponseStatusException.class, () -> clubeService.salvar(clubeDto));

        clubeDto.setNome("Palmeiras");
        clubeDto.setSiglaEstado(null);
        assertThrows(ResponseStatusException.class, () -> clubeService.salvar(clubeDto));

        clubeDto.setSiglaEstado("");
        assertThrows(ResponseStatusException.class, () -> clubeService.salvar(clubeDto));

        clubeDto.setSiglaEstado(" ");
        assertThrows(ResponseStatusException.class, () -> clubeService.salvar(clubeDto));

        clubeDto.setSiglaEstado("XX");
        assertThrows(ResponseStatusException.class, () -> clubeService.salvar(clubeDto));

        clubeDto.setSiglaEstado("SP");
        clubeDto.setDataCriacao(null);
        assertThrows(ResponseStatusException.class, () -> clubeService.salvar(clubeDto));

        clubeDto.setDataCriacao(LocalDate.now().plusDays(1));
        assertThrows(ResponseStatusException.class, () -> clubeService.salvar(clubeDto));

        clubeDto.setDataCriacao(LocalDate.of(1914, 8, 26));
        clubeDto.setAtivo(null);
        assertThrows(ResponseStatusException.class, () -> clubeService.salvar(clubeDto));
    }

    /**
     * Testa o método salvar do ClubeService para o caso em que os dados do clube são válidos.
     *
     * <p>
     * Cenário: Recebe um DTO de clube com nome, sigla do estado, data de criação e situação ativo.
     * Os dados são válidos (nome, sigla do estado, data de criação e situação preenchidos corretamente).
     * </p>
     *
     * <b>Etapas do teste:</b>
     * <ul>
     *   <li><b>Arrange:</b> Prepara os dados de entrada e configura o comportamento dos mocks.</li>
     *   <li><b>Act:</b> Executa o método a ser testado (salvar).</li>
     *   <li><b>Assert:</b> Verifica se o resultado está conforme o esperado.</li>
     * </ul>
     *
     * @cenario Cadastro de clube com dados válidos.
     * @resultado Deve retornar o DTO do clube salvo, com os dados preenchidos e ativo.
     */
    @Test
    public void testarSalvarClubeComDadosValidos() {
        clubeDto.setNome("Palmeiras");
        clubeDto.setSiglaEstado("SP");
        clubeDto.setDataCriacao(LocalDate.of(1914, 8, 26));
        clubeDto.setAtivo(true);

        when(clubeRepository.findByNomeAndSiglaEstado(anyString(), anyString())).thenReturn(Optional.empty());

        Clube clubeSalvo = new Clube("Palmeiras", "SP", LocalDate.of(1914, 8, 26), true);
        clubeSalvo.setId(1L);
        when(clubeRepository.save(any(Clube.class))).thenReturn(clubeSalvo);

        ClubeResponseDto response = clubeService.salvar(clubeDto);

        assertNotNull(response);
        assertEquals("Palmeiras", response.getNome());
        assertEquals("SP", response.getSiglaEstado());
        assertEquals(LocalDate.of(1914, 8, 26), response.getDataCriacao());
        assertTrue(response.getAtivo());
    }

    /**
     * Testa o método salvar do ClubeService para o caso em que já existe um clube com o mesmo nome e sigla de estado.
     *
     * <p>
     * Cenário: Recebe um DTO de clube com nome, sigla do estado, data de criação e situação ativo.
     * Já existe um clube com o mesmo nome e sigla no banco.
     * </p>
     *
     * <b>Etapas do teste:</b>
     * <ul>
     *   <li><b>Arrange:</b> Prepara os dados de entrada e configura o comportamento dos mocks.</li>
     *   <li><b>Act:</b> Executa o método a ser testado (salvar).</li>
     *   <li><b>Assert:</b> Verifica se uma exceção é lançada com status 409 (CONFLICT).</li>
     * </ul>
     *
     * @cenario Cadastro de clube com duplicidade de nome e sigla.
     * @resultado Deve lançar ResponseStatusException com status CONFLICT.
     */
    @Test
    public void testarSalvarClubeComDuplicidadeDeNomeESiglaEstado() {
        clubeDto.setNome("Palmeiras");
        clubeDto.setSiglaEstado("SP");
        clubeDto.setDataCriacao(LocalDate.of(1914, 8, 26));
        clubeDto.setAtivo(true);

        Clube clubeExistente = new Clube("Palmeiras", "SP", LocalDate.of(1914, 8, 26), true);
        clubeExistente.setId(1L);

        when(clubeRepository.findByNomeAndSiglaEstado(anyString(), anyString())).thenReturn(Optional.of(clubeExistente));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> clubeService.salvar(clubeDto));
        assertEquals (HttpStatus.CONFLICT, ex.getStatusCode());
    }

    /**
     * Testa o método salvar do ClubeService para cadastro de clube sem duplicidade de nome e sigla de estado.
     *
     * <p>
     * Cenário: Recebe um DTO de clube com nome, sigla do estado, data de criação e situação ativo.
     * Não existe clube com o mesmo nome e sigla no banco.
     * </p>
     *
     * <b>Etapas do teste:</b>
     * <ul>
     *   <li><b>Arrange:</b> Prepara os dados de entrada e configura o comportamento dos mocks.</li>
     *   <li><b>Act:</b> Executa o método a ser testado (salvar).</li>
     *   <li><b>Assert:</b> Verifica se o resultado está conforme o esperado.</li>
     * </ul>
     *
     * @cenario Cadastro de clube novo, sem duplicidade.
     * @resultado Clube salvo corretamente, com os dados preenchidos e ativo.
     */
    @Test
    public void testarSalvarClubeSemDuplicidadeDeNomeESiglaEstado() {
        clubeDto.setNome("Palmeiras");
        clubeDto.setSiglaEstado("SP");
        clubeDto.setDataCriacao(LocalDate.of(1914, 8, 26));
        clubeDto.setAtivo(true);

        Clube clubeSalvo = new Clube("Palmeiras", "SP", LocalDate.of(1914, 8, 26), true);
        clubeSalvo.setId(1L);
        when(clubeRepository.findByNomeAndSiglaEstado(anyString(), anyString())).thenReturn(Optional.empty());
        when(clubeRepository.save(any(Clube.class))).thenReturn(clubeSalvo);
        ClubeResponseDto response = clubeService.salvar(clubeDto);

        assertNotNull(response);
        assertEquals("Palmeiras", response.getNome());
        assertEquals("SP", response.getSiglaEstado());
        assertEquals(LocalDate.of(1914, 8, 26), response.getDataCriacao());
        assertTrue(response.getAtivo());
    }

    /**
     * Testa o método buscarPorId do ClubeService para o caso em que o clube NÃO existe.
     *
     * <p>
     * Cenário: Dado um ID inexistente, o método deve lançar uma exceção 404 (NOT FOUND).
     * </p>
     *
     * <b>Etapas do teste:</b>
     * <ul>
     *   <li><b>Arrange:</b> Configura o mock do repositório para retornar vazio (Optional.empty()).</li>
     *   <li><b>Act & Assert:</b> Chama o método buscarPorId e verifica se lança ResponseStatusException com status 404.</li>
     * </ul>
     *
     * @cenario Busca de clube inexistente.
     * @resultado Deve lançar ResponseStatusException com status NOT_FOUND.
     */
    @Test
    public void testarBuscarClubePorIdInexistente() {
        Long id = 99L;
        when(clubeRepository.findById(id)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> clubeService.buscarPorId(id));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

    /**
     * Testa o método buscarPorId do ClubeService para o caso em que o clube existe.
     *
     * <p>
     * Cenário: Dado um ID válido, o método deve retornar o DTO do clube correspondente.
     * </p>
     *
     * <b>Etapas:</b>
     * <ul>
     *   <li>Arrange: Prepara o mock do repositório para retornar um clube.</li>
     *   <li>Act: Chama o método buscarPorId.</li>
     *   <li>Assert: Verifica se o DTO retornado tem os dados esperados.</li>
     * </ul>
     *
     * @cenario Busca de clube existente.
     * @resultado Deve retornar o DTO do clube.
     */
    @Test
    public void testarBuscarClubePorIdExistente() {
        Long id = 1L;
        Clube clube = new Clube("Palmeiras", "SP", LocalDate.of(1914, 8, 26), true);
        clube.setId(id);
        when(clubeRepository.findById(id)).thenReturn(Optional.of(clube));

        ClubeResponseDto response = clubeService.buscarPorId(id);

        assertNotNull(response);
        assertEquals(id, response.getId());
        assertEquals("Palmeiras", response.getNome());
    }

    /**
     * Testa o método atualizarPorId do ClubeService para o caso em que os dados do clube são inválidos.
     *
     * <p>
     * Cenário: Recebe um DTO de clube com nome, sigla do estado, e data de criação.
     * Os dados são inválidos (nome nulo, sigla do estado nula, ou data de criação futura).
     * </p>
     *
     * <b>Etapas do teste:</b>
     * <ul>
     *   <li><b>Arrange:</b> Prepara os dados de entrada e configura o comportamento dos mocks.</li>
     *   <li><b>Act:</b> Executa o método a ser testado (atualizarPorId).</li>
     *   <li><b>Assert:</b> Verifica se uma exceção é lançada com status 400 (BAD_REQUEST).</li>
     * </ul>
     *
     * @cenario Atualização de clube com dados inválidos.
     * @resultado Deve lançar ResponseStatusException com status BAD_REQUEST.
     */
    @Test
    public void testarAtualizarClubeComDadosInvalidos() {
        Long id = 1L;
        Clube clubeExistente = new Clube("Palmeiras", "SP", LocalDate.of(1914, 8, 26), true);
        clubeExistente.setId(id);

        when(clubeRepository.findById(id)).thenReturn(Optional.of(clubeExistente));

        ClubeRequestDto clubeDto = new ClubeRequestDto();

        clubeDto.setNome("A");
        clubeDto.setSiglaEstado("SP");
        clubeDto.setDataCriacao(LocalDate.of(1914, 8, 26));
        clubeDto.setAtivo(true);
        assertThrows(ResponseStatusException.class, () -> clubeService.atualizarPorId(id, clubeDto));

        clubeDto.setNome("Corinthians");
        clubeDto.setSiglaEstado("XX");
        assertThrows(ResponseStatusException.class, () -> clubeService.atualizarPorId(id, clubeDto));

        clubeDto.setSiglaEstado("SP");
        clubeDto.setDataCriacao(LocalDate.now().plusDays(1));
        assertThrows(ResponseStatusException.class, () -> clubeService.atualizarPorId(id, clubeDto));

    }

    /**
     * Testa o método atualizarPorId do ClubeService para o caso em que os dados do clube são válidos.
     *
     * <p>
     * Cenário: Recebe um DTO de clube com nome, sigla do estado, data de criação e situação ativo.
     * Os dados são válidos (nome, sigla do estado, data de criação e situação preenchidos corretamente).
     * </p>
     *
     * <b>Etapas do teste:</b>
     * <ul>
     *   <li><b>Arrange:</b> Prepara os dados de entrada e configura o comportamento dos mocks.</li>
     *   <li><b>Act:</b> Executa o método a ser testado (atualizarPorId).</li>
     *   <li><b>Assert:</b> Verifica se o resultado está conforme o esperado.</li>
     * </ul>
     *
     * @cenario Atualização de clube com dados válidos.
     * @resultado Deve retornar o DTO do clube atualizado, com os dados preenchidos e ativo.
     */
    @Test
    public void testarAtualizarClubeComDadosValidos() {
        Long id = 1L;
        Clube clubeExistente = new Clube("Palmeiras", "SP", LocalDate.of(1914, 8, 26), true);
        clubeExistente.setId(id);

        when(clubeRepository.findById(id)).thenReturn(Optional.of(clubeExistente));

        ClubeRequestDto clubeDto = new ClubeRequestDto();
        clubeDto.setNome("Corinthians");
        clubeDto.setSiglaEstado("SP");
        clubeDto.setDataCriacao(LocalDate.of(1910, 9, 1));
        clubeDto.setAtivo(true);

        when(clubeRepository.findByNomeAndSiglaEstado("Corinthians", "SP")).thenReturn(Optional.empty());

        Clube clubeAtualizado = new Clube("Corinthians", "SP", LocalDate.of(1910, 9, 1), true);
        clubeAtualizado.setId(id);
        when(clubeRepository.save(any(Clube.class))).thenReturn(clubeAtualizado);

        ClubeResponseDto response = clubeService.atualizarPorId(id, clubeDto);

        assertNotNull(response);
        assertEquals("Corinthians", response.getNome());
        assertEquals("SP", response.getSiglaEstado());
        assertEquals(LocalDate.of(1910, 9, 1), response.getDataCriacao());
        assertTrue(response.getAtivo());
    }

    // TODO: Testar cenários de data de criação posterior à data de alguma partida do clube (requisito 409 CONFLICT)

}