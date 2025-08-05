package br.com.neocamp.partidas_futebol.service;

import br.com.neocamp.partidas_futebol.dto.clubeDto.ClubeRequestDto;
import br.com.neocamp.partidas_futebol.dto.clubeDto.ClubeResponseDto;
import br.com.neocamp.partidas_futebol.entity.Clube;
import br.com.neocamp.partidas_futebol.repository.ClubeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
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


@ExtendWith(MockitoExtension.class)
public class ClubeServiceTest {

    
    @Mock
    private ClubeRepository clubeRepository;

    
    @InjectMocks
    private ClubeService clubeService;


    ClubeRequestDto clubeDto = new ClubeRequestDto();



    @Test
    public void testarCadastrarClubeComDadosInvalidos() {
        clubeDto.setNome(null);
        clubeDto.setSiglaEstado("SP");
        clubeDto.setDataCriacao(LocalDate.of(1914, 8, 26));
        clubeDto.setAtivo(true);
        assertThrows(ResponseStatusException.class, () -> clubeService.cadastrarClube(clubeDto));

        clubeDto.setNome("");
        assertThrows(ResponseStatusException.class, () -> clubeService.cadastrarClube(clubeDto));

        clubeDto.setNome(" ");
        assertThrows(ResponseStatusException.class, () -> clubeService.cadastrarClube(clubeDto));

        clubeDto.setNome("A");
        assertThrows(ResponseStatusException.class, () -> clubeService.cadastrarClube(clubeDto));

        clubeDto.setNome("Palmeiras");
        clubeDto.setSiglaEstado(null);
        assertThrows(ResponseStatusException.class, () -> clubeService.cadastrarClube(clubeDto));

        clubeDto.setSiglaEstado("");
        assertThrows(ResponseStatusException.class, () -> clubeService.cadastrarClube(clubeDto));

        clubeDto.setSiglaEstado(" ");
        assertThrows(ResponseStatusException.class, () -> clubeService.cadastrarClube(clubeDto));

        clubeDto.setSiglaEstado("XX");
        assertThrows(ResponseStatusException.class, () -> clubeService.cadastrarClube(clubeDto));

        clubeDto.setSiglaEstado("SP");
        clubeDto.setDataCriacao(null);
        assertThrows(ResponseStatusException.class, () -> clubeService.cadastrarClube(clubeDto));

        clubeDto.setDataCriacao(LocalDate.now().plusDays(1));
        assertThrows(ResponseStatusException.class, () -> clubeService.cadastrarClube(clubeDto));

        clubeDto.setDataCriacao(LocalDate.of(1914, 8, 26));
        clubeDto.setAtivo(null);
        assertThrows(ResponseStatusException.class, () -> clubeService.cadastrarClube(clubeDto));
    }



    @Test
    public void testarCadastrarClubeComDadosValidos() {
        clubeDto.setNome("Palmeiras");
        clubeDto.setSiglaEstado("SP");
        clubeDto.setDataCriacao(LocalDate.of(1914, 8, 26));
        clubeDto.setAtivo(true);

        when(clubeRepository.findByNomeAndSiglaEstado(anyString(), anyString())).thenReturn(Optional.empty());

        Clube clubeSalvo = new Clube("Palmeiras", "SP", LocalDate.of(1914, 8, 26), true);
        clubeSalvo.setId(1L);
        when(clubeRepository.save(any(Clube.class))).thenReturn(clubeSalvo);

        ClubeResponseDto response = clubeService.cadastrarClube(clubeDto);

        assertNotNull(response);
        assertEquals("Palmeiras", response.getNome());
        assertEquals("SP", response.getSiglaEstado());
        assertEquals(LocalDate.of(1914, 8, 26), response.getDataCriacao());
        assertTrue(response.getAtivo());
    }



    @Test
    public void testarCadastrarClubeComDuplicidadeDeNomeESiglaEstado() {
        clubeDto.setNome("Palmeiras");
        clubeDto.setSiglaEstado("SP");
        clubeDto.setDataCriacao(LocalDate.of(1914, 8, 26));
        clubeDto.setAtivo(true);

        Clube clubeExistente = new Clube("Palmeiras", "SP", LocalDate.of(1914, 8, 26), true);
        clubeExistente.setId(1L);

        when(clubeRepository.findByNomeAndSiglaEstado(anyString(), anyString())).thenReturn(Optional.of(clubeExistente));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> clubeService.cadastrarClube(clubeDto));
        assertEquals (HttpStatus.CONFLICT, ex.getStatusCode());
    }



    @Test
    public void testarCadastrarClubeSemDuplicidadeDeNomeESiglaEstado() {
        clubeDto.setNome("Palmeiras");
        clubeDto.setSiglaEstado("SP");
        clubeDto.setDataCriacao(LocalDate.of(1914, 8, 26));
        clubeDto.setAtivo(true);

        Clube clubeSalvo = new Clube("Palmeiras", "SP", LocalDate.of(1914, 8, 26), true);
        clubeSalvo.setId(1L);
        when(clubeRepository.findByNomeAndSiglaEstado(anyString(), anyString())).thenReturn(Optional.empty());
        when(clubeRepository.save(any(Clube.class))).thenReturn(clubeSalvo);
        ClubeResponseDto response = clubeService.cadastrarClube(clubeDto);

        assertNotNull(response);
        assertEquals("Palmeiras", response.getNome());
        assertEquals("SP", response.getSiglaEstado());
        assertEquals(LocalDate.of(1914, 8, 26), response.getDataCriacao());
        assertTrue(response.getAtivo());
    }



    @Test
    public void testarBuscarClubePorIdInexistente() {
        Long id = 99L;
        when(clubeRepository.findById(id)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> clubeService.buscarPorId(id));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }



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



    @Test
    public void testarAtualizarClubeComDadosInvalidos() {
        Long id = 1L;
        Clube clubeExistente = new Clube("Palmeiras", "SP", LocalDate.of(1914, 8, 26), true);
        clubeExistente.setId(id);

        when(clubeRepository.findById(id)).thenReturn(Optional.of(clubeExistente));

        ClubeRequestDto clubeDto = new ClubeRequestDto();

        clubeDto.setNome(null);
        clubeDto.setSiglaEstado("SP");
        clubeDto.setDataCriacao(LocalDate.of(1914, 8, 26));
        clubeDto.setAtivo(true);
        assertThrows(ResponseStatusException.class, () -> clubeService.atualizarPorId(id, clubeDto));

        clubeDto.setNome("A");
        assertThrows(ResponseStatusException.class, () -> clubeService.atualizarPorId(id, clubeDto));

        clubeDto.setNome("Corinthians");
        clubeDto.setSiglaEstado("XX");
        assertThrows(ResponseStatusException.class, () -> clubeService.atualizarPorId(id, clubeDto));

        clubeDto.setSiglaEstado("SP");
        clubeDto.setDataCriacao(null);
        assertThrows(ResponseStatusException.class, () -> clubeService.atualizarPorId(id, clubeDto));

        clubeDto.setDataCriacao(LocalDate.now().plusDays(1));
        assertThrows(ResponseStatusException.class, () -> clubeService.atualizarPorId(id, clubeDto));

    }



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



    @Test
    public void testarAtualizarClubeComDuplicidadeDeNomeESiglaEstadoComInsucesso() {
        Long id = 1L;
        Clube clubeExistente = new Clube("Palmeiras", "SP", LocalDate.of(1914, 8, 26), true);
        clubeExistente.setId(id);

        when(clubeRepository.findById(id)).thenReturn(Optional.of(clubeExistente));

        ClubeRequestDto clubeDto = new ClubeRequestDto();
        clubeDto.setNome("Corinthians");
        clubeDto.setSiglaEstado("SP");
        clubeDto.setDataCriacao(LocalDate.of(1910, 9, 1));
        clubeDto.setAtivo(true);

        Clube clubeDuplicado = new Clube("Corinthians", "SP", LocalDate.of(1910, 9, 1), true);
        clubeDuplicado.setId(2L);
        when(clubeRepository.findByNomeAndSiglaEstado("Corinthians", "SP")).thenReturn(Optional.of(clubeDuplicado));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> clubeService.atualizarPorId(id, clubeDto));
        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());

    }



    @Test
    public void testarAtualizarClubeSemDuplicidadeDeNomeESiglaEstadoComSucesso() {
        Long id = 1L;
        Clube clubeExistente = new Clube("Palmeiras", "SP", LocalDate.of(1914, 8, 26), true);
        clubeExistente.setId(id);

        when(clubeRepository.findById(id)).thenReturn(Optional.of(clubeExistente));

        ClubeRequestDto clubeDto = new ClubeRequestDto();
        clubeDto.setNome("Palmeiras");
        clubeDto.setSiglaEstado("SP");
        clubeDto.setDataCriacao(LocalDate.of(1914, 8, 26));
        clubeDto.setAtivo(true);

        when(clubeRepository.findByNomeAndSiglaEstado("Palmeiras", "SP")).thenReturn(Optional.of(clubeExistente));

        when(clubeRepository.save(any(Clube.class))).thenReturn(clubeExistente);

        ClubeResponseDto response = clubeService.atualizarPorId(id, clubeDto);

        assertNotNull(response);
        assertEquals("Palmeiras", response.getNome());
        assertEquals("SP", response.getSiglaEstado());
        assertEquals(LocalDate.of(1914, 8, 26), response.getDataCriacao());
        assertTrue(response.getAtivo());

    }

    
    @Test
    public void testarAtualizarClubeSemDuplicidadeDeNomeESiglaEstado() {
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



    @Test
    public void testarAtualizarClubeComIdInexistente() {
        Long id = 99L;
        ClubeRequestDto clubeDto = new ClubeRequestDto();
        clubeDto.setNome("Corinthians");
        clubeDto.setSiglaEstado("SP");
        clubeDto.setDataCriacao(LocalDate.of(1910, 9, 1));
        clubeDto.setAtivo(true);

        when(clubeRepository.findById(id)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> clubeService.atualizarPorId(id, clubeDto));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

    
    @Test
    public void testarAtualizarClubeComIdExistente() {
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



    @Test
    public void testarInativarClubeComIdInexistente() {
        Long id = 99L;
        when(clubeRepository.findById(id)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> clubeService.inativarClubePorId(id));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }



    @Test
    public void testarInativarClubeComIdExistente() {
        Long id = 1L;
        Clube clubeExistente = new Clube("Palmeiras", "SP", LocalDate.of(1914, 8, 26), true);
        clubeExistente.setId(id);

        when(clubeRepository.findById(id)).thenReturn(Optional.of(clubeExistente));
        when(clubeRepository.save(any(Clube.class))).thenAnswer(invocation -> invocation.getArgument(0));

        clubeService.inativarClubePorId(id);

        assertFalse(clubeExistente.getAtivo(), "O clube deve ser inativado");
    }



    @Test
    public void testarListarClubesFiltradosSemResultados() {
        when(clubeRepository.findByNomeContainingIgnoreCaseAndSiglaEstadoAndAtivo(anyString(), anyString(), anyBoolean())).thenReturn(List.of());

        List<ClubeResponseDto> resultado = clubeService.listarClubes("Inexistente", "ZZ", false);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        when(clubeRepository.findByNomeContainingIgnoreCaseAndSiglaEstado(anyString(), anyString())).thenReturn(List.of());

        resultado = clubeService.listarClubes("Inexistente", "ZZ", null);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        when(clubeRepository.findByNomeContainingIgnoreCaseAndAtivo(anyString(), anyBoolean())).thenReturn(List.of());

        resultado = clubeService.listarClubes("Inexistente", null, false);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        when(clubeRepository.findBySiglaEstadoAndAtivo(anyString(), anyBoolean())).thenReturn(List.of());
        resultado = clubeService.listarClubes(null, "ZZ", false);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        when(clubeRepository.findByNomeContainingIgnoreCase(anyString())).thenReturn(List.of());
        resultado = clubeService.listarClubes("Inexistente", null, null);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        when(clubeRepository.findBySiglaEstado(anyString())).thenReturn(List.of());
        resultado = clubeService.listarClubes(null, "ZZ", null);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        when(clubeRepository.findByAtivo(anyBoolean())).thenReturn(List.of());
        resultado = clubeService.listarClubes(null, null, true);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        when(clubeRepository.findAll()).thenReturn(List.of());
        resultado = clubeService.listarClubes(null, null, null);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

    }



    @Test
    public void testarListarClubesFiltradosComResultados() {
        Clube clube1 = new Clube("Palmeiras", "SP", LocalDate.of(1914, 8, 26), true);
        clube1.setId(1L);
        Clube clube2 = new Clube("Corinthians", "SP", LocalDate.of(1910, 9,1), true);
        clube2.setId(2L);

        when(clubeRepository.findByNomeContainingIgnoreCaseAndSiglaEstadoAndAtivo("Palmeiras", "SP", true)).thenReturn(List.of(clube1));

        List<ClubeResponseDto> resultado = clubeService.listarClubes("Palmeiras", "SP", true);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Palmeiras", resultado.get(0).getNome());
        assertEquals("SP", resultado.get(0).getSiglaEstado());
        assertTrue(resultado.get(0).getAtivo());

        when(clubeRepository.findByNomeContainingIgnoreCaseAndSiglaEstado("Palmeiras", "SP")).thenReturn(List.of(clube1));
        resultado = clubeService.listarClubes("Palmeiras", "SP", null);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Palmeiras", resultado.get(0).getNome());
        assertEquals("SP", resultado.get(0).getSiglaEstado());
        assertTrue(resultado.get(0).getAtivo());

        when(clubeRepository.findByNomeContainingIgnoreCaseAndAtivo("Palmeiras", true)).thenReturn(List.of(clube1));
        resultado = clubeService.listarClubes("Palmeiras", null, true);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Palmeiras", resultado.get(0).getNome());
        assertEquals("SP", resultado.get(0).getSiglaEstado());
        assertTrue(resultado.get(0).getAtivo());

        when(clubeRepository.findBySiglaEstadoAndAtivo("SP", true)).thenReturn(List.of(clube1, clube2));
        resultado = clubeService.listarClubes(null, "SP", true);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());

        boolean listarClubesEncontrado = false;
        for (ClubeResponseDto c : resultado) {
            if (c.getNome().equals("Palmeiras") && c.getSiglaEstado().equals("SP") && c.getAtivo()) {
                listarClubesEncontrado = true;
                break;
            }
        }
        assertTrue(listarClubesEncontrado);

    }

    
    @Test
    public void testarListarClubesFiltradosComPaginacaoEOrdenacaoComResultado() {
        Clube clube1 = new Clube("Palmeiras", "SP", LocalDate.of(1914, 8, 26), true);
        clube1.setId(1L);
        Clube clube2 = new Clube("Corinthians", "SP", LocalDate.of(1910, 9,1), true);
        clube2.setId(2L);

        List<Clube> listarClubes = List.of(clube2, clube1);
        Page<Clube> clubesPage = new PageImpl<>(listarClubes);

        Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "nome"));

        when(clubeRepository.buscarClubesPorPaginacao(null, "SP", true, pageable)).thenReturn(clubesPage);

        Page<ClubeResponseDto> resultado = clubeService.listarClubes(null, "SP", true, pageable);

        assertNotNull(resultado);
        assertEquals(2, resultado.getContent().size());
        assertEquals("Palmeiras", resultado.getContent().get(1).getNome());
        assertEquals("Corinthians", resultado.getContent().get(0).getNome());
    }

}