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
import static org.mockito.ArgumentMatchers.anyBoolean;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

// Anotação para indicar que a classe é um teste de unidade com Mockito
@ExtendWith(MockitoExtension.class)
public class ClubeServiceTest {

    // Anotação para indicar que o mock será injetado automaticamente pelo Mockito, para simular o comportamento do repository
    // O repository é uma dependência do service, por isso é necessário mocká-lo
    // Isso é uma injeção de dependência do repository
    @Mock
    private ClubeRepository clubeRepository;

    // Anotação para indicar que o service será injetado automaticamente pelo Mockito, para simular o comportamento do service com o repository mockado
    // Isso é uma injeção de dependência do service com o repository mockado
    // O service é a classe que será testada, por isso é necessário mockar o repository e injetá-lo no service para simular o comportamento do repository no teste
    @InjectMocks
    private ClubeService clubeService;

    // Cria um objeto ClubeRequestDto com nome preenchido para ser usado nos testes
    ClubeRequestDto clubeDto = new ClubeRequestDto();

    // Teste um dos metodos salvar do service (cenário princial: caminho feliz)
    @Test
    public void testarSalvarClubeSemDuplicidadeDeNomeESiglaEstado() {
        // Arrange - Prepara o objeto que será salvo no metodo salvar (Configura o mock e cria um objeto ClubeRequestDto com nome preenchido)
        clubeDto.setNome("Palmeiras");
        clubeDto.setSiglaEstado("SP");
        clubeDto.setDataCriacao(LocalDate.of(1914, 8, 26));
        clubeDto.setAtivo(true);

        // Act - Passa os parâmetros do objeto que será salvo no metodo salvar (Chama o metodo salvar do service com o objeto ClubeRequestDto)
        Clube clubeSalvo = new Clube("Palmeiras", "SP", LocalDate.of(1914, 8, 26), true);
        clubeSalvo.setId(1L);
        when(clubeRepository.findByNomeAndSiglaEstado(anyString(), anyString())).thenReturn(Optional.empty());
        when(clubeRepository.save(any(Clube.class))).thenReturn(clubeSalvo);
        ClubeResponseDto response = clubeService.salvar(clubeDto);


        // Assert - Verifica se o objeto foi salvo corretamente com as validações de negócio
        assertNotNull(response);
        assertEquals("Palmeiras", response.getNome());
        assertEquals("SP", response.getSiglaEstado());
        assertEquals(LocalDate.of(1914, 8, 26), response.getDataCriacao());
        assertTrue(response.getAtivo());
    }

}
