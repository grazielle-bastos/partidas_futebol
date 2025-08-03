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


@ExtendWith(SpringExtension.class)
public class EstadioServiceTest {

    
    @Mock
    private EstadioRepository estadioRepository;

    
    @InjectMocks
    private EstadioService estadioService;

    
    EstadioRequestDto estadioRequestDto = new EstadioRequestDto();

    
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
