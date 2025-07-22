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

}
