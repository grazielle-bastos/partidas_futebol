package br.com.neocamp.partidas_futebol.service;

import br.com.neocamp.partidas_futebol.dto.estadioDto.EstadioRequestDto;
import br.com.neocamp.partidas_futebol.dto.estadioDto.EstadioResponseDto;
import br.com.neocamp.partidas_futebol.entity.Estadio;
import br.com.neocamp.partidas_futebol.repository.EstadioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;


@Service
public class EstadioService {

    
    private final EstadioRepository estadioRepository;

    
    @Autowired
    public EstadioService(EstadioRepository estadioRepository) {
        this.estadioRepository = estadioRepository;
    }

    
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

    
    public EstadioResponseDto buscarPorId(Long id) {

        Optional<Estadio> estadioOptional = estadioRepository.findById(id);

        if (estadioOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Estádio não encontrado");
        }

        return toResponseDto(estadioOptional.get());
    }

    
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

    
    public Page<EstadioResponseDto> listarEstadios(String nome, Pageable pageable) {

        Page<Estadio> estadioPage;

        if (nome != null) {
            estadioPage = estadioRepository.findByNomeContainingIgnoreCase(nome.trim(), pageable);
        } else {
            estadioPage = estadioRepository.findAll(pageable);
        }

        return estadioPage.map(this::toResponseDto);

    }

    
    private EstadioResponseDto toResponseDto(Estadio estadio) {

        return new EstadioResponseDto(
                estadio.getId(),
                estadio.getNome()
        );
    }

}
